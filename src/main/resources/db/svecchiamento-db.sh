#!/bin/bash
#
# Esegue lo svecchiamento del database di un'installazione GovPay.
#
# Lo svecchiamento e' diviso in sezioni, una per area di dati, e ciascuna e' uno
# script SQL a se' in sql/<dialetto>/svecchiamento/. Questo script ne compone in
# serie quelle richieste, con la retention passata da fuori, e le esegue con il
# client nativo del database.
#
# La divisione in sezioni non e' estetica: ogni installazione ha cose diverse da
# svecchiare, e comporre le sezioni volute e' piu' onesto che avere un unico
# script da commentare a mano.
#
# Uso tipico:
#   ./svecchiamento-db.sh postgresql
#       tutte le sezioni, con la retention di default di ciascuna
#   ./svecchiamento-db.sh postgresql --sezioni eventi,tracciati
#       le sezioni indicate, con la retention di default di ciascuna
#   ./svecchiamento-db.sh postgresql --sezioni eventi,tracciati --retention-eventi 15 --retention-tracciati 60
#       le sezioni indicate, con la retention in giorni indicata per sezione
#
# Eventi e metadati dei batch si cancellano e basta. Tutte le altre sezioni
# copiano prima le righe che cancellano in tabelle di archivio
# <tabella>_AAAAMMGG, una serie al giorno: se quelle di oggi esistono gia', le
# sezioni con archivio vengono saltate. Il suffisso si cambia con
# --suffisso-archivio. Salvarle ed eliminarle e' compito del DBA. Le anagrafiche
# non vengono mai toccate.
#
# ATTENZIONE: cancella dati in modo definitivo, salvo quanto copiato nelle
# tabelle di archivio. Sui metadati Spring Batch non c'e' filtro sullo stato
# delle esecuzioni: un job in corso creato prima della soglia viene cancellato.
#
set -euo pipefail

BASEDIR="$(cd "$(dirname "$0")" && pwd)"       # src/main/resources/db
CORE_SQL_DIR="${BASEDIR}/sql"
REPO_ROOT="$(cd "${BASEDIR}/../../../.." && pwd)"

# Dialetti canonici, secondo src/main/resources/db/README.md
DIALETTI_NOTI=(postgresql oracle mysql sqlserver hsql)

# Sezioni, nell'ordine in cui vanno eseguite. Il giornale viene per primo perche'
# e' la tabella piu' grande: sfoltirlo rende meno costose le DELETE della sezione
# tracciati, che su eventi passano da una sottoquery.
# L'esito non dipende dall'ordine: cio' che viene cancellato e' l'unione dei due
# criteri, gli eventi piu' vecchi della retention e quelli collegati ai tracciati
# scaduti, e l'unione non cambia a seconda di quale si applica prima.
# flussi_rendicontazione precede pendenze_pagate: cancella le pendenze
# rendicontate, e pendenze_pagate si occupa di quelle pagate e mai rendicontate.
# incassi_orfani e documenti_orfani vanno per ultime: cancellano gli incassi e
# i documenti che le sezioni sulle pendenze hanno lasciato senza riferimenti.
SEZIONI_NOTE=(eventi tracciati spring-batch tracciati_notifica_pagamenti audit
              pendenze_scadute_non_pagate pendenze_annullate flussi_rendicontazione
              pendenze_pagate incassi_orfani documenti_orfani)

# Retention minima, in giorni, delle sezioni che cancellano dati contabili,
# rendicontazioni, pendenze pagate e incassi: sotto questa soglia si cancellano
# dati che possono servire ancora, ed e' quindi richiesta una conferma
# esplicita, --force, oppure la simulazione, --dry-run.
RETENTION_MINIMA_GIORNI=730
SEZIONI_RETENTION_MINIMA=(flussi_rendicontazione pendenze_pagate incassi_orfani)

# Le sezioni che archiviano le righe cancellate lo fanno in tabelle
# <tabella>_aaaammgg; aaaammgg e' un segnaposto, che qui diventa la data
# dell'esecuzione. In simulazione diventa sim<data>, e le tabelle vengono
# eliminate alla fine: altrimenti la simulazione lascerebbe tabelle vuote con
# il nome di quelle vere. Una sezione archivia se il suo script contiene il
# segnaposto: l'elenco non e' duplicato qui.
SEGNAPOSTO_ARCHIVIO="aaaammgg"

# Nome del parametro di retention dentro ciascuno script. E' un dettaglio interno
# agli script SQL, e non coincide sempre con il nome della sezione: spring-batch
# non e' un identificatore SQL. Da fuori la retention si indica sempre con il
# nome della sezione, --retention-<sezione>. Vuoto per le sezioni senza
# retention.
function parametro_di() {
  case "$1" in
    tracciati)    echo "tracciati" ;;
    eventi)       echo "eventi" ;;
    spring-batch) echo "batch" ;;
    tracciati_notifica_pagamenti) echo "tnp" ;;
    audit)        echo "audit" ;;
    pendenze_scadute_non_pagate) echo "pendenze" ;;
    pendenze_annullate) echo "annullate" ;;
    flussi_rendicontazione) echo "fr" ;;
    pendenze_pagate) echo "pendenze_pagate" ;;
    incassi_orfani) echo "incassi" ;;
    documenti_orfani) echo "" ;;
  esac
}

# Le sezioni con archivio sono quelle il cui script contiene il segnaposto.
function con_archivio() {   # $1 = sezione
  grep -q "_${SEGNAPOSTO_ARCHIVIO}\b" "${SEZIONI_DIR}/$1.sql"
}

SEZIONI=("${SEZIONI_NOTE[@]}")
declare -A RETENTION=()

SOLO_SQL=false
SUFFISSO_OPZIONE=""
FINESTRA=""
DRY_RUN=false
FORCE=false
SENZA_CONFERMA=false
OUTDIR=""
WORKDIR=""

# Connessione: gli stessi nomi di variabile usati dall'init dei container, cosi'
# un ambiente gia' configurato per quelli vale anche qui.
DB_SERVER="${GOVPAY_DB_SERVER:-}"
DB_HOST=""
DB_PORT=""
DB_NAME="${GOVPAY_DB_NAME:-}"
DB_USER="${GOVPAY_DB_USER:-}"
DB_PASSWORD="${GOVPAY_DB_PASSWORD:-}"
ORACLE_CONN="${GOVPAY_ORACLE_JDBC_URL_TYPE:-servicename}"

function usage() {
cat <<EOHELP
Usage: $(basename "$0") <tipoDB> [opzioni]

Argomenti:
  tipoDB        Dialetto del database: $(IFS='|'; echo "${DIALETTI_NOTI[*]}")
                Sono accettati anche gli alias hsqldb (= hsql) e mariadb (= mysql)

Sezioni (default: tutte, nell'ordine $(IFS=', '; echo "${SEZIONI_NOTE[*]}")):
  --sezioni <lista>    Sezioni da eseguire, separate da virgola. L'ordine e'
                       sempre quello sopra, indipendentemente da come si scrivono
  --retention-<sezione> <giorni>
                       Con il nome della sezione come in --sezioni, per tutte
                       tranne documenti_orfani, che non ha retention.
                       Retention in giorni della sezione, che deve essere tra
                       quelle eseguite. Sovrascrive il valore scritto nello
                       script della sezione; senza, vale quello

Connessione (in alternativa alle variabili d'ambiente indicate):
  --host <host>        Host del database            [GOVPAY_DB_SERVER, host[:porta]]
  --port <porta>       Porta                        [GOVPAY_DB_SERVER]
  --db <nome>          Database, o service name su oracle   [GOVPAY_DB_NAME]
  --user <utente>      Utente                       [GOVPAY_DB_USER]
  --password <segreto> Password                     [GOVPAY_DB_PASSWORD]
  --oracle-conn <tipo> servicename | sid           [GOVPAY_ORACLE_JDBC_URL_TYPE]

Esecuzione:
  --solo-sql           Compone lo script e si ferma, senza toccare il database
  --dry-run            Simula l'esecuzione: esegue le sezioni sul database e
                       chiude ognuna con ROLLBACK invece di COMMIT. Riporta le
                       righe che ogni DELETE cancellerebbe e non modifica nulla.
                       Non chiede conferma. Come nell'esecuzione vera, le righe
                       interessate restano bloccate finche' la sezione e' aperta
  --force              Obbligatoria per eseguire davvero flussi_rendicontazione,
                       pendenze_pagate e incassi_orfani con una retention
                       inferiore a ${RETENTION_MINIMA_GIORNI} giorni.
                       Senza --force, o --dry-run, lo script si rifiuta di
                       procedere, anche con --solo-sql: lo script composto
                       cancellerebbe quei dati
  --finestra <giorni>  Svecchia a passate, dai dati piu' vecchi: la prima passata
                       cancella solo la fascia di <giorni> giorni piu' vecchia di
                       ogni sezione, e ogni passata successiva la successiva, fino
                       alla retention. Ogni passata e' una transazione per
                       sezione, piu' piccola di quella unica. Con --dry-run
                       simula solo la prima passata e stima la durata totale.
                       Non si combina con --solo-sql: per trovare i dati piu'
                       vecchi serve il database
  --suffisso-archivio <suffisso>
                       Suffisso delle tabelle di archivio, al posto della data
                       di oggi (AAAAMMGG): lettere, cifre e '_'. Vale anche per
                       il controllo dell'archivio gia' presente: se esiste una
                       tabella di archivio con questo suffisso, le sezioni con
                       archivio vengono saltate
  -y, --si             Non chiedere conferma prima di eseguire
  --out <dir>          Directory di uscita (default: target/svecchiamento-sql del
                       repository; fuori dal repository, svecchiamento-sql/
                       accanto a questo script)
  -h, --help           Mostra questo aiuto

Archivio: tutte le sezioni tranne eventi e spring-batch copiano le righe che
cancellano in tabelle <tabella>_AAAAMMGG, con la data dell'esecuzione, create e
riempite nella stessa esecuzione della cancellazione. Il backup e' giornaliero:
se nel database esiste gia' anche una sola tabella di archivio con il suffisso
di questa esecuzione, le sezioni con archivio vengono saltate. Il suffisso si
cambia con --suffisso-archivio. Salvare ed eliminare le tabelle di
archivio e' compito del DBA. In simulazione le tabelle si chiamano
<tabella>_simAAAAMMGG e vengono eliminate alla fine.

Documentazione completa, con requisiti ed esempi: README-svecchiamento.md,
accanto a questo script.

Gli script delle sezioni stanno in sql/<dialetto>/svecchiamento/ e sono
eseguibili anche uno per uno con il client del database: questo script serve a
comporli e a passare la retention da fuori, non a nascondere cosa fanno.

Il client usato e' quello nativo del dialetto — psql, sqlplus, mysql, sqlcmd,
SqlTool su hsql — perche' gli script usano i comandi del client per i parametri
e per i messaggi di avanzamento, e nessun client generico li esegue.

ATTENZIONE: la cancellazione e' definitiva, salvo quanto copiato nelle tabelle
di archivio. Sui metadati Spring Batch non c'e' filtro sullo stato: un job in
corso la cui esecuzione e' anteriore alla soglia viene cancellato e Spring Batch
ne perde traccia.
EOHELP
}

function errore() { echo "Errore: $*" >&2; exit 1; }
function nota()   { echo "  $*"; }

# ── Argomenti ────────────────────────────────────────────────────────────────
[[ $# -ge 1 ]] || { usage >&2; exit 1; }
case "${1:-}" in -h|--help) usage; exit 0 ;; esac

TIPO_DB="$1"; shift

while [[ $# -gt 0 ]]; do
  case "$1" in
    --sezioni)               SEZIONI_RICHIESTE="${2:-}"; shift 2 ;;
    --retention-*)
      # La sezione e' quello che segue --retention-: l'opzione si ricava dal
      # nome, e resta allineata anche quando si aggiunge una sezione.
      sez="${1#--retention-}"
      nota_sez=false
      for n in "${SEZIONI_NOTE[@]}"; do [[ "${n}" == "${sez}" ]] && nota_sez=true; done
      [[ "${nota_sez}" == true ]] \
        || errore "opzione sconosciuta: $1. Le retention sono: $(for n in "${SEZIONI_NOTE[@]}"; do printf '%s' "--retention-${n} "; done)"
      [[ "${sez}" != "documenti_orfani" ]] \
        || errore "la sezione documenti_orfani non ha retention: un documento orfano non ha una data"
      RETENTION[${sez}]="${2:-}"; shift 2 ;;
    --host)                  DB_HOST="${2:-}"; shift 2 ;;
    --port)                  DB_PORT="${2:-}"; shift 2 ;;
    --db)                    DB_NAME="${2:-}"; shift 2 ;;
    --user)                  DB_USER="${2:-}"; shift 2 ;;
    --password)              DB_PASSWORD="${2:-}"; shift 2 ;;
    --oracle-conn)           ORACLE_CONN="${2:-}"; shift 2 ;;
    --solo-sql)              SOLO_SQL=true; shift ;;
    --dry-run)               DRY_RUN=true; shift ;;
    --force)                 FORCE=true; shift ;;
    -y|--si)                 SENZA_CONFERMA=true; shift ;;
    --out)                   OUTDIR="${2:-}"; shift 2 ;;
    --finestra)              FINESTRA="${2:-}"; shift 2 ;;
    --suffisso-archivio)     SUFFISSO_OPZIONE="${2:-}"; [[ -n "${SUFFISSO_OPZIONE}" ]] || errore "--suffisso-archivio richiede un valore"; shift 2 ;;
    -h|--help)               usage; exit 0 ;;
    *) errore "opzione sconosciuta: $1" ;;
  esac
done

# Alias: hsqldb e mariadb sono i nomi usati negli altri repository e nelle
# variabili GOVPAY_DB_TYPE dei container, e arrivano quindi anche qui.
case "${TIPO_DB}" in
  hsqldb)  nota "dialetto hsqldb ricondotto a hsql";   TIPO_DB=hsql ;;
  mariadb) nota "dialetto mariadb ricondotto a mysql"; TIPO_DB=mysql ;;
esac

trovato=false
for d in "${DIALETTI_NOTI[@]}"; do [[ "${d}" == "${TIPO_DB}" ]] && trovato=true; done
[[ "${trovato}" == true ]] || errore "dialetto sconosciuto: ${TIPO_DB}. Ammessi: ${DIALETTI_NOTI[*]}"

SEZIONI_DIR="${CORE_SQL_DIR}/${TIPO_DB}/svecchiamento"
[[ -d "${SEZIONI_DIR}" ]] || errore "directory delle sezioni non trovata: ${SEZIONI_DIR#${REPO_ROOT}/}"

# Selezione delle sezioni. L'ordine resta quello di SEZIONI_NOTE: sceglierlo
# dalla riga di comando sarebbe un modo di sbagliarlo.
if [[ -n "${SEZIONI_RICHIESTE:-}" ]]; then
  IFS=',' read -r -a richieste <<< "${SEZIONI_RICHIESTE}"
  for s in "${richieste[@]}"; do
    noto=false
    for n in "${SEZIONI_NOTE[@]}"; do [[ "${n}" == "${s}" ]] && noto=true; done
    [[ "${noto}" == true ]] || errore "sezione sconosciuta: ${s}. Ammesse: ${SEZIONI_NOTE[*]}"
  done
  SEZIONI=()
  for n in "${SEZIONI_NOTE[@]}"; do
    for s in "${richieste[@]}"; do
      [[ "${n}" == "${s}" ]] && { SEZIONI+=("${n}"); break; }
    done
  done
fi
[[ ${#SEZIONI[@]} -gt 0 ]] || errore "nessuna sezione da eseguire"

for s in "${SEZIONI[@]}"; do
  [[ -f "${SEZIONI_DIR}/${s}.sql" ]] \
    || errore "la sezione ${s} non esiste per ${TIPO_DB}: manca ${SEZIONI_DIR#${REPO_ROOT}/}/${s}.sql"
done

if [[ -n "${FINESTRA}" ]]; then
  [[ "${FINESTRA}" =~ ^[0-9]+$ && "${FINESTRA}" -gt 0 ]] \
    || errore "--finestra deve essere un numero di giorni maggiore di zero: '${FINESTRA}'"
  [[ "${SOLO_SQL}" != true ]] \
    || errore "--finestra non si combina con --solo-sql: per trovare i dati piu' vecchi serve il database"
fi

for p in "${!RETENTION[@]}"; do
  v="${RETENTION[$p]}"
  eseguita=false
  for s in "${SEZIONI[@]}"; do [[ "${s}" == "${p}" ]] && eseguita=true; done
  [[ "${eseguita}" == true ]] \
    || errore "--retention-${p} indicata, ma la sezione non e' tra quelle da eseguire: aggiungerla a --sezioni"
  [[ "${v}" =~ ^[0-9]+$ ]] || errore "la retention di ${p} deve essere un numero di giorni: '${v}'"
  [[ "${v}" -gt 0 ]]       || errore "la retention di ${p} deve essere maggiore di zero"
done

# Suffisso delle tabelle di archivio: la data dell'esecuzione, uguale per tutte
# le sezioni, o il valore di --suffisso-archivio. Il backup e' uno per suffisso:
# se le tabelle con quel suffisso esistono gia', le sezioni con archivio vengono
# saltate (vedi Archivio gia' presente, sotto).
ARCHIVIO="${SUFFISSO_OPZIONE:-$(date '+%Y%m%d')}"
[[ "${ARCHIVIO}" =~ ^[A-Za-z0-9_]+$ ]] \
  || errore "--suffisso-archivio: ammessi solo lettere, cifre e '_', perche' finisce nel nome delle tabelle: '${ARCHIVIO}'"
# Il nome piu' lungo, tabella d'origine piu' suffisso, deve stare nel limite del
# dialetto. Su Oracle il limite e' 30 caratteri fino alla 12.1: si resta su
# quello, perche' qui la versione non si conosce.
case "${TIPO_DB}" in
  postgresql) LIMITE_NOMI=63 ;;
  mysql)      LIMITE_NOMI=64 ;;
  oracle)     LIMITE_NOMI=30 ;;
  *)          LIMITE_NOMI=128 ;;
esac
if [[ "${DRY_RUN}" == true ]]; then
  SUFFISSO_ARCHIVIO="sim${ARCHIVIO}"
else
  SUFFISSO_ARCHIVIO="${ARCHIVIO}"
fi
PIU_LUNGA="$(grep -ohE "\b[A-Za-z_]+_${SEGNAPOSTO_ARCHIVIO}\b" "${SEZIONI_DIR}"/*.sql 2>/dev/null \
             | sed "s/_${SEGNAPOSTO_ARCHIVIO}\$//" | awk '{ print length, $0 }' | sort -rn | head -1 | cut -d' ' -f2)"
if [[ -n "${PIU_LUNGA}" ]]; then
  lunghezza=$(( ${#PIU_LUNGA} + 1 + ${#SUFFISSO_ARCHIVIO} ))
  [[ "${lunghezza}" -le "${LIMITE_NOMI}" ]] \
    || errore "--suffisso-archivio: ${PIU_LUNGA}_${SUFFISSO_ARCHIVIO} ha ${lunghezza} caratteri, oltre il limite di ${LIMITE_NOMI} per ${TIPO_DB}: usare un suffisso piu' corto"
fi

# Nel repository lo script composto va in target/, come gli altri prodotti della
# build. Copiato da solo su un server, con le sue sezioni, il repository non c'e'
# e REPO_ROOT punterebbe a una directory a caso, magari la radice: il default
# diventa allora una directory accanto allo script.
if [[ -f "${REPO_ROOT}/pom.xml" && -d "${REPO_ROOT}/src/main/resources/db" ]]; then
  OUTDIR="${OUTDIR:-${REPO_ROOT}/target/svecchiamento-sql}"
else
  OUTDIR="${OUTDIR:-${BASEDIR}/svecchiamento-sql}"
fi
WORKDIR="$(mktemp -d)"
# La pulizia non deve decidere l'esito dello script: e' l'ultimo comando
# eseguito, e un suo fallimento diventerebbe il codice di uscita.
trap 'rm -rf "${WORKDIR}" 2>/dev/null || true' EXIT

# ── Retention ────────────────────────────────────────────────────────────────
# Il default di ogni sezione sta nel suo script e non e' duplicato qui: senza
# l'opzione lo script passa invariato, e ci limitiamo a leggere il valore per
# poterlo riportare. Con l'opzione, la riga del parametro viene sostituita
# nell'idioma del dialetto e il risultato viene verificato: uno script di sezione
# che cambiasse idioma deve far fallire questo, non farlo ricadere in silenzio
# sul valore di default.
function retention_dal_file() {   # $1 = file, $2 = nome parametro
  local n=""
  if [[ "${TIPO_DB}" == "hsql" ]]; then
    n="$(grep -m1 -oE 'CURRENT_(DATE|TIMESTAMP) - [0-9]+ DAY' "$1" 2>/dev/null | grep -oE '[0-9]+' || true)"
  else
    # Ancorata alla riga della dichiarazione: il nome del parametro compare
    # anche nei commenti di testa, dove di cifre non ce ne sono.
    n="$(grep -m1 -E "^(\\\\set|DEFINE|SET|DECLARE)[[:space:]]*@?retention_$2([[:space:]]|=)" "$1" 2>/dev/null \
         | grep -oE '[0-9]+' | head -1 || true)"
  fi
  echo "${n:-?}"
}

function riga_retention() {   # $1 = nome parametro, $2 = giorni
  case "${TIPO_DB}" in
    postgresql) printf '%s\n' "\\set retention_$1 '\\'$2 days\\''" ;;
    oracle)     printf '%s\n' "DEFINE retention_$1 = $2;" ;;
    mysql)      printf '%s\n' "SET @retention_$1 = $2;" ;;
    sqlserver)  printf '%s\n' "DECLARE @retention_$1 INT = $2;" ;;
  esac
}

function sezione_con_retention() {   # $1 = sezione; scrive su stdout
  local sez="$1"
  local par; par="$(parametro_di "${sez}")"
  local src="${SEZIONI_DIR}/${sez}.sql"
  local v="${RETENTION[${sez}]:-}"
  local tmp="${WORKDIR}/_${sez}.sql"
  cp "${src}" "${tmp}"

  if [[ -n "${v}" ]]; then
    if [[ "${TIPO_DB}" == "hsql" ]]; then
      # Su hsql i giorni sono letterali dentro le DELETE: HSQLDB non ha
      # variabili negli script.
      sed -E -i "s/(CURRENT_(DATE|TIMESTAMP)) - [0-9]+ DAY/\\1 - ${v} DAY/g" "${tmp}"
      grep -q "${v} DAY" "${tmp}" \
        || errore "sostituzione della retention di ${sez} non riuscita su ${src#${REPO_ROOT}/}: lo script e' cambiato, aggiornare $(basename "$0")"
      # anche il valore citato nel commento di testa, per non lasciarlo mentire
      sed -E -i "s/(la retention e' il letterale)/\\1/; s/(nelle DELETE qui sotto, )[0-9]+( giorni)/\\1${v}\\2/" "${tmp}"
    else
      local espressione="^(\\\\set|DEFINE|SET|DECLARE)[[:space:]]*@?retention_${par}([[:space:]]|=)"
      # Il controllo e' che la riga da sostituire ci sia: se lo script di sezione
      # cambiasse nome o idioma del parametro, cancellare zero righe e mettere la
      # nostra in testa lascerebbe uno script che si esegue e ignora l'opzione.
      grep -qE "${espressione}" "${tmp}" \
        || errore "in ${src#${REPO_ROOT}/} non c'e' la riga del parametro retention_${par}: lo script e' cambiato, aggiornare $(basename "$0")"
      sed -i -E "/${espressione}/d" "${tmp}"
      { riga_retention "${par}" "${v}"; cat "${tmp}"; } > "${tmp}.new" && mv "${tmp}.new" "${tmp}"
      grep -qE "retention_${par}[^0-9]+${v}([^0-9]|\$)" "${tmp}" \
        || errore "sostituzione della retention di ${sez} non riuscita su ${src#${REPO_ROOT}/}"
    fi
  fi
  if grep -q "_${SEGNAPOSTO_ARCHIVIO}\b" "${tmp}"; then
    sed -i "s/_${SEGNAPOSTO_ARCHIVIO}\b/_${SUFFISSO_ARCHIVIO}/g" "${tmp}"
    ! grep -q "_${SEGNAPOSTO_ARCHIVIO}" "${tmp}" \
      || errore "sostituzione del suffisso delle tabelle di archivio non riuscita in ${src#${REPO_ROOT}/}"
  fi
  [[ "${DRY_RUN}" == true ]] && simulazione "${sez}" "${tmp}"
  # Nelle passate intermedie di --finestra il VACUUM di PostgreSQL ripasserebbe
  # le tabelle intere a ogni passata: lo fa solo l'ultima.
  if [[ "${SENZA_VACUUM:-false}" == true ]]; then
    sed -i -E "/^VACUUM|^\\\\echo 'VACUUM|^-- VACUUM/d" "${tmp}"
  fi
  cat "${tmp}"
}

# ── Simulazione ──────────────────────────────────────────────────────────────
# Il dry-run esegue le stesse DELETE, con gli stessi vincoli e le stesse chiavi
# esterne, e chiude la sezione con ROLLBACK. Ogni script di sezione finisce con
# un solo COMMIT, ed e' quello che viene sostituito: se non ce n'e' esattamente
# uno, la composizione si ferma, perche' una simulazione che committa e' il solo
# errore che qui non ci si puo' permettere.
function simulazione() {   # $1 = sezione, $2 = file da modificare
  local sez="$1" tmp="$2"
  local src="${SEZIONI_DIR}/${sez}.sql"
  local commit_re='^COMMIT( TRANSACTION)?;[[:space:]]*$'
  local n; n="$(grep -cE "${commit_re}" "${tmp}" || true)"
  [[ "${n}" == "1" ]] \
    || errore "--dry-run: in ${src#${REPO_ROOT}/} ci sono ${n} COMMIT invece di uno solo in chiusura: lo script e' cambiato, aggiornare $(basename "$0")"
  sed -i -E 's/^COMMIT( TRANSACTION)?;[[:space:]]*$/ROLLBACK\1;/' "${tmp}"
  ! grep -qiE '^[[:space:]]*COMMIT' "${tmp}" \
    || errore "--dry-run: in ${src#${REPO_ROOT}/} resta un COMMIT dopo la sostituzione"

  case "${TIPO_DB}" in
    postgresql)
      # VACUUM non ha senso dopo un ROLLBACK: non c'e' nulla da recuperare.
      if grep -qE '^VACUUM' "${tmp}"; then
        sed -i -E "/^VACUUM|^\\\\echo 'VACUUM|^-- VACUUM/d" "${tmp}"
        echo "\\echo 'Simulazione: VACUUM ANALYZE non eseguito'" >> "${tmp}"
      fi ;;
    mysql)
      # Il client mysql non riporta le righe cancellate.
      conta_righe_dopo_delete "${tmp}" "SELECT ROW_COUNT() AS righe_cancellate;" ;;
    hsql)
      # SqlTool non riporta le righe cancellate. E con autocommit attivo ogni
      # DELETE sarebbe confermata subito, e il ROLLBACK finale non annullerebbe
      # nulla: SqlTool parte con autocommit disattivato, ma un rcfile puo'
      # cambiarlo, e qui non ci si affida al default.
      conta_righe_dopo_delete "${tmp}" "SELECT 'righe cancellate: ' || DIAGNOSTICS(ROW_COUNT) FROM (VALUES(0));"
      { echo "SET AUTOCOMMIT FALSE;"; cat "${tmp}"; } > "${tmp}.new" && mv "${tmp}.new" "${tmp}" ;;
  esac
}

# Aggiunge un'istruzione dopo ogni DELETE, anche quelle scritte su piu' righe.
function conta_righe_dopo_delete() {   # $1 = file, $2 = istruzione
  awk -v istr="$2" '
    /^[[:space:]]*DELETE[[:space:]]/ { in_del = 1 }
    { print }
    in_del && /;[[:space:]]*$/ { print istr; in_del = 0 }
  ' "$1" > "$1.new" && mv "$1.new" "$1"
}

# ── Retention minima ─────────────────────────────────────────────────────────
# Dati contabili piu' recenti di RETENTION_MINIMA_GIORNI si
# cancellano solo chiedendolo esplicitamente con --force. La simulazione non
# cancella nulla e passa sempre. Il controllo e' sulla retention effettiva:
# quella dell'opzione o, senza, quella scritta nello script della sezione, che
# potrebbe essere stata abbassata a mano.
for s in "${SEZIONI[@]}"; do
  soggetta=false
  for m in "${SEZIONI_RETENTION_MINIMA[@]}"; do [[ "${s}" == "${m}" ]] && soggetta=true; done
  [[ "${soggetta}" == true ]] || continue
  giorni="${RETENTION[${s}]:-$(retention_dal_file "${SEZIONI_DIR}/${s}.sql" "$(parametro_di "${s}")")}"
  # Una retention illeggibile non si puo' confrontare: meglio fermarsi.
  [[ "${giorni}" =~ ^[0-9]+$ ]] \
    || errore "retention di ${s} non leggibile da ${SEZIONI_DIR#${REPO_ROOT}/}/${s}.sql: lo script e' cambiato, aggiornare $(basename "$0")"
  [[ "${giorni}" -lt "${RETENTION_MINIMA_GIORNI}" ]] || continue
  if [[ "${DRY_RUN}" == true ]]; then
    nota "${s}: retention di ${giorni} giorni, inferiore a ${RETENTION_MINIMA_GIORNI}; ammessa perche' e' una simulazione"
  elif [[ "${FORCE}" == true ]]; then
    nota "${s}: retention di ${giorni} giorni, inferiore a ${RETENTION_MINIMA_GIORNI}; ammessa con --force"
  else
    errore "${s}: retention di ${giorni} giorni, inferiore al minimo di ${RETENTION_MINIMA_GIORNI}. Cancellerebbe dati contabili recenti: aggiungere --dry-run per simulare, o --force per cancellare davvero"
  fi
done

# ── Connessione ──────────────────────────────────────────────────────────────
# I parametri di connessione servono per eseguire e per controllare l'archivio di
# oggi: con --solo-sql non servono, e lo script si compone anche dove non si ha
# accesso al database.
if [[ "${SOLO_SQL}" != true ]]; then
  if [[ -n "${DB_SERVER}" && -z "${DB_HOST}" ]]; then
    IFS=':' read -r DB_HOST DB_PORT_ENV <<< "${DB_SERVER}"
    [[ -z "${DB_PORT}" && -n "${DB_PORT_ENV:-}" ]] && DB_PORT="${DB_PORT_ENV}"
  fi

  for coppia in "DB_HOST:--host" "DB_NAME:--db" "DB_USER:--user"; do
    var="${coppia%%:*}"; opt="${coppia#*:}"
    [[ -n "${!var}" ]] || errore "parametro di connessione mancante: ${opt} (oppure la variabile d'ambiente corrispondente)"
  done

  if [[ -z "${DB_PORT}" ]]; then
    case "${TIPO_DB}" in
      postgresql) DB_PORT=5432 ;;
      mysql)      DB_PORT=3306 ;;
      oracle)     DB_PORT=1521 ;;
      sqlserver)  DB_PORT=1433 ;;
      hsql)       DB_PORT=9001 ;;
    esac
  fi
fi

function richiedi_client() {
  command -v "$1" >/dev/null 2>&1 || errore "client $1 non trovato: serve per eseguire lo svecchiamento su ${TIPO_DB}. Con --solo-sql lo script viene composto senza eseguirlo"
}

function esegui() {   # $1 = script da eseguire (default: lo script composto)
local script="${1:-${OUT}}"
case "${TIPO_DB}" in
  postgresql)
    richiedi_client psql
    # ON_ERROR_STOP e' necessario: senza, psql esce 0 anche dopo un errore.
    PGPASSWORD="${DB_PASSWORD}" psql -v ON_ERROR_STOP=1 \
      -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" -f "${script}"
    ;;
  mysql)
    richiedi_client mysql
    # MYSQL_PWD evita la password nella riga di comando, visibile in ps.
    MYSQL_PWD="${DB_PASSWORD}" mysql -h "${DB_HOST}" -P "${DB_PORT}" \
      -u "${DB_USER}" -D "${DB_NAME}" < "${script}"
    ;;
  oracle)
    richiedi_client sqlplus
    if [[ "${ORACLE_CONN}" == "sid" ]]; then
      DESCRITTORE="${DB_HOST}:${DB_PORT}:${DB_NAME}"
    else
      DESCRITTORE="//${DB_HOST}:${DB_PORT}/${DB_NAME}"
    fi
    # CONNECT arriva da stdin e non dalla riga di comando, che ps mostrerebbe
    # con la password dentro. Il WHENEVER SQLERROR copre anche il CONNECT.
    sqlplus -S -L /nolog <<EOSQLPLUS
WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK
CONNECT ${DB_USER}/${DB_PASSWORD}@${DESCRITTORE}
@${script}
EXIT ROLLBACK;
EOSQLPLUS
    ;;
  sqlserver)
    richiedi_client sqlcmd
    # -b: uscita non nulla al primo errore.
    SQLCMDPASSWORD="${DB_PASSWORD}" sqlcmd -b \
      -S "${DB_HOST},${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" -i "${script}"
    ;;
  hsql)
    richiedi_client java
    # Su hsql il client e' SqlTool, come nell'init dei container: non c'e' un
    # client a riga di comando nativo, e gli script non usano comandi di client.
    SQLTOOL_JAR="${GOVPAY_SQLTOOL_JAR:-/opt/hsqldb-${HSQLDB_FULLVERSION:-2.7.4}/hsqldb/lib/sqltool.jar}"
    [[ -f "${SQLTOOL_JAR}" ]] || errore "sqltool.jar non trovato in ${SQLTOOL_JAR}: indicarlo con GOVPAY_SQLTOOL_JAR"
    RC="${WORKDIR}/_sqltool.rc"
    ( umask 077; cat > "${RC}" <<EORC
urlid svecchiamento_db
url jdbc:hsqldb:hsql://${DB_HOST}:${DB_PORT}/${DB_NAME}
username ${DB_USER}
password ${DB_PASSWORD}
driver ${GOVPAY_DS_DRIVER_CLASS:-org.hsqldb.jdbc.JDBCDriver}
transiso TRANSACTION_READ_COMMITTED
charset UTF-8
EORC
    )
    java -Dfile.encoding=UTF-8 \
      -cp "${GOVPAY_DS_JDBC_LIBS:-/opt/jdbc-drivers}/*:${SQLTOOL_JAR}" \
      org.hsqldb.cmdline.SqlTool --rcFile="${RC}" svecchiamento_db "${script}"
    ;;
esac
}


# ── Archivio gia' presente ───────────────────────────────────────────────────
# Il backup e' uno per suffisso, di default uno al giorno: se nel database c'e'
# gia' anche una sola tabella di archivio con il suffisso di questa esecuzione,
# lo svecchiamento con archivio e' gia' stato fatto, e le sezioni con archivio
# vengono saltate. Le altre, eventi e
# metadati dei batch, girano comunque: cancellano per data, e rieseguirle non
# fa danni. Il controllo vale anche per la simulazione, che deve mostrare cio'
# che farebbe l'esecuzione vera. Con --solo-sql non si puo' fare: lo script
# composto contiene tutte le sezioni richieste.
SEZIONI_ARCHIVIO=()
for s in "${SEZIONI[@]}"; do con_archivio "${s}" && SEZIONI_ARCHIVIO+=("${s}"); done

function tabelle_di_archivio() {   # nomi base, da tutte le sezioni con archivio
  for n in "${SEZIONI_NOTE[@]}"; do
    [[ -f "${SEZIONI_DIR}/${n}.sql" ]] || continue
    grep -oE "\b[A-Za-z_]+_${SEGNAPOSTO_ARCHIVIO}\b" "${SEZIONI_DIR}/${n}.sql" || true
  done | sed "s/_${SEGNAPOSTO_ARCHIVIO}\$//" | sort -u
}

function query_archivio_di_oggi() {   # scrive su stdout lo script del controllo
  local nomi
  nomi="$(tabelle_di_archivio | sed "s/.*/'&_${ARCHIVIO}'/" | tr '[:upper:]' '[:lower:]' | paste -sd, -)"
  case "${TIPO_DB}" in
    postgresql)
      echo "\\pset tuples_only on"
      echo "\\pset format unaligned"
      echo "SELECT 'ARCHIVIO_PRESENTI ' || count(*) FROM information_schema.tables WHERE table_schema = current_schema() AND lower(table_name) IN (${nomi});" ;;
    mysql)
      echo "SELECT CONCAT('ARCHIVIO_PRESENTI ', COUNT(*)) AS n FROM information_schema.tables WHERE table_schema = DATABASE() AND LOWER(table_name) IN (${nomi});" ;;
    oracle)
      echo "SET HEADING OFF FEEDBACK OFF PAGESIZE 0"
      echo "SELECT 'ARCHIVIO_PRESENTI ' || COUNT(*) FROM user_tables WHERE LOWER(table_name) IN (${nomi});" ;;
    sqlserver)
      echo "SET NOCOUNT ON;"
      echo "SELECT 'ARCHIVIO_PRESENTI ' + CAST(COUNT(*) AS VARCHAR(10)) FROM sys.tables WHERE LOWER(name) IN (${nomi});" ;;
    hsql)
      echo "SELECT 'ARCHIVIO_PRESENTI ' || COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE LOWER(TABLE_NAME) IN (${nomi});" ;;
  esac
}

if [[ ${#SEZIONI_ARCHIVIO[@]} -gt 0 ]]; then
  if [[ "${SOLO_SQL}" == true ]]; then
    nota "--solo-sql: non verificato se l'archivio *_${ARCHIVIO} esiste gia'"
  else
    CONTROLLO="${WORKDIR}/_archivio_di_oggi.sql"
    query_archivio_di_oggi > "${CONTROLLO}"
    esito_controllo="$(esegui "${CONTROLLO}" 2>&1)" \
      || errore "controllo dell'archivio gia' presente non riuscito: ${esito_controllo}"
    presenti="$(grep -oE 'ARCHIVIO_PRESENTI [0-9]+' <<< "${esito_controllo}" | tail -1 | grep -oE '[0-9]+' || true)"
    # Senza risposta non si sa se l'archivio c'e' gia': meglio fermarsi che
    # rifare un backup gia' fatto, o saltarne uno da fare.
    [[ -n "${presenti}" ]] || errore "controllo dell'archivio gia' presente senza risposta: ${esito_controllo}"
    if [[ "${presenti}" -gt 0 ]]; then
      nota "l'archivio *_${ARCHIVIO} esiste gia' (${presenti} tabelle): sezioni con archivio saltate: ${SEZIONI_ARCHIVIO[*]}"
      RESTANTI=()
      for s in "${SEZIONI[@]}"; do con_archivio "${s}" || RESTANTI+=("${s}"); done
      if [[ ${#RESTANTI[@]} -eq 0 ]]; then
        echo
        echo "Nessuna sezione da eseguire: lo svecchiamento con archivio *_${ARCHIVIO} e' gia' stato fatto."
        exit 0
      fi
      SEZIONI=("${RESTANTI[@]}")
    fi
  fi
fi

# ── Composizione ─────────────────────────────────────────────────────────────
mkdir -p "${OUTDIR}"
if [[ "${DRY_RUN}" == true ]]; then
  OUT="${OUTDIR}/govpay-svecchiamento-${TIPO_DB}-dry-run.sql"
else
  OUT="${OUTDIR}/govpay-svecchiamento-${TIPO_DB}.sql"
fi

function elimina_archivio() {   # scrive su stdout
  echo ""
  echo "-- ============================================================"
  echo "-- Simulazione: eliminazione delle tabelle di archivio, vuote"
  echo "-- ============================================================"
  echo ""
  for t in "${ARCHIVIO_TABELLE[@]}"; do
    case "${TIPO_DB}" in
      postgresql|mysql) echo "DROP TABLE IF EXISTS ${t};" ;;
      hsql)             echo "DROP TABLE ${t} IF EXISTS;" ;;
      sqlserver)        echo "IF OBJECT_ID('${t}', 'U') IS NOT NULL DROP TABLE ${t};" ;;
      oracle)           echo "BEGIN EXECUTE IMMEDIATE 'DROP TABLE ${t}'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;"
                        echo "/" ;;
    esac
  done
  [[ "${TIPO_DB}" == "sqlserver" ]] && echo "GO"
  [[ "${TIPO_DB}" == "hsql" ]] && echo "COMMIT;"
  return 0
}

# Compone in OUT le sezioni di SEZIONI con le retention di RETENTION. Imposta
# RETENTION_USATA e ARCHIVIO_TABELLE. Va chiamata senza $(...): imposta variabili
# globali.
function componi() {
  declare -gA RETENTION_USATA=()
  local s par
  for s in "${SEZIONI[@]}"; do
    par="$(parametro_di "${s}")"
    if [[ -n "${RETENTION[${s}]:-}" ]]; then
      RETENTION_USATA[${s}]="retention ${RETENTION[${s}]} giorni"
    elif [[ -z "${par}" ]]; then
      RETENTION_USATA[${s}]="senza retention"
    else
      RETENTION_USATA[${s}]="retention $(retention_dal_file "${SEZIONI_DIR}/${s}.sql" "${par}") giorni (dal file)"
    fi
  done

  {
    echo "-- Svecchiamento del database GovPay"
    echo "-- Dialetto: ${TIPO_DB}"
    echo "--"
    echo "-- Generato da src/main/resources/db/$(basename "$0") il $(date '+%Y-%m-%d %H:%M:%S')."
    echo "-- Non modificare a mano: rigenerare indicando le stesse sezioni."
    [[ -n "${PASSATA_DESCRIZIONE:-}" ]] && { echo "--"; echo "-- ${PASSATA_DESCRIZIONE}"; }
    echo "--"
    echo "-- Sezioni incluse, nell'ordine di esecuzione:"
    for s in "${SEZIONI[@]}"; do
      echo "--   ${s}, ${RETENTION_USATA[${s}]}, da sql/${TIPO_DB}/svecchiamento/${s}.sql"
    done
    if [[ ${#SEZIONI[@]} -lt ${#SEZIONI_NOTE[@]} ]]; then
      echo "--"
      echo "-- Sezioni disponibili e non incluse:"
      local n inclusa
      for n in "${SEZIONI_NOTE[@]}"; do
        inclusa=false
        for s in "${SEZIONI[@]}"; do [[ "${n}" == "${s}" ]] && inclusa=true; done
        [[ "${inclusa}" == false ]] && echo "--   ${n}"
      done
    fi
    echo "--"
    if [[ "${DRY_RUN}" == true ]]; then
      echo "-- SIMULAZIONE (--dry-run): ogni sezione termina con ROLLBACK invece di"
      echo "-- COMMIT, e il database non viene modificato."
    else
      echo "-- ATTENZIONE: la cancellazione e' definitiva. Ogni sezione e' una transazione"
      echo "-- a se': se una fallisce, quelle completate prima restano applicate."
    fi
    echo ""
    # sqlplus esce 0 anche dopo un errore, se non gli si dice altrimenti, e senza
    # EXIT finale resta in attesa di input. ROLLBACK perche' l'EXIT di sqlplus,
    # per default, fa COMMIT: un errore a meta' sezione confermerebbe le DELETE
    # gia' eseguite, e in simulazione sarebbe una cancellazione vera.
    if [[ "${TIPO_DB}" == "oracle" ]]; then
      echo "WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK"
      [[ "${DRY_RUN}" == true ]] && echo "SET FEEDBACK ON"
    fi
    echo ""
  } > "${OUT}"

  for s in "${SEZIONI[@]}"; do
    {
      echo ""
      echo "-- ============================================================"
      echo "-- Sezione: ${s}"
      echo "-- ============================================================"
      echo ""
      sezione_con_retention "${s}"
      echo ""
      # Su sqlserver ogni sezione dichiara le proprie variabili, e due DECLARE
      # della stessa variabile nello stesso batch sarebbero un errore: GO chiude
      # il batch e ne apre un altro.
      [[ "${TIPO_DB}" == "sqlserver" ]] && echo "GO"
    } >> "${OUT}"
  done

  # In simulazione le tabelle di archivio, create fuori dalle transazioni
  # annullate, restano vuote: vanno eliminate. I nomi si ricavano dallo script
  # composto.
  ARCHIVIO_TABELLE=()
  local t
  while IFS= read -r t; do [[ -n "${t}" ]] && ARCHIVIO_TABELLE+=("${t}"); done \
    < <(grep -oE "\b[A-Za-z_]+_${SUFFISSO_ARCHIVIO}\b" "${OUT}" | sort -u)
  if [[ "${DRY_RUN}" == true && ${#ARCHIVIO_TABELLE[@]} -gt 0 ]]; then
    elimina_archivio >> "${OUT}"
  fi

  [[ "${TIPO_DB}" == "oracle" ]] && { echo "" >> "${OUT}"; echo "EXIT ROLLBACK;" >> "${OUT}"; }
  return 0
}

# ── Passate (--finestra) ─────────────────────────────────────────────────────
# Con --finestra lo svecchiamento procede dai dati piu' vecchi. Per ogni sezione
# con retention si trova l'eta', in giorni, del dato piu' vecchio; poi, partendo
# dall'eta' massima, la soglia scende di FINESTRA giorni a passata, e ogni sezione
# usa come retention il massimo fra la soglia e la propria retention finale. Una
# sezione entra in una passata solo se ha dati piu' vecchi di quella soglia. Una
# passata finale, con tutte le sezioni e le loro retention, chiude il lavoro.
#
# L'eta' si ricava qui da tabella e colonna di ogni sezione: e' una
# duplicazione, ma serve solo a scegliere le passate. Se fosse imprecisa,
# cambierebbe il numero di passate, non cio' che viene cancellato: ogni passata
# usa il criterio completo della sezione.
function eta_sorgente() {   # $1 = sezione -> "tabella|colonna|condizione"
  case "$1" in
    eventi)                       echo "eventi|data|" ;;
    tracciati)                    echo "tracciati|data_completamento|" ;;
    spring-batch)                 echo "BATCH_JOB_EXECUTION|COALESCE(END_TIME, START_TIME, CREATE_TIME)|" ;;
    tracciati_notifica_pagamenti) echo "trac_notif_pag|data_creazione|" ;;
    audit)                        echo "gp_audit|data|" ;;
    pendenze_scadute_non_pagate)  echo "versamenti|data_scadenza|stato_versamento = 'NON_ESEGUITO'" ;;
    pendenze_annullate)           echo "versamenti|data_ora_ultimo_aggiornamento|stato_versamento = 'ANNULLATO'" ;;
    flussi_rendicontazione)       echo "fr|data_ora_flusso|" ;;
    pendenze_pagate)              echo "pagamenti|data_pagamento|" ;;
    incassi_orfani)               echo "incassi|data_ora_incasso|" ;;
  esac
}

function query_eta() {   # $@ = sezioni; scrive su stdout lo script della query
  local s sorgente tabella colonna cond eta where
  case "${TIPO_DB}" in
    postgresql) echo "\\pset tuples_only on"; echo "\\pset format unaligned" ;;
    oracle)     echo "SET HEADING OFF FEEDBACK OFF PAGESIZE 0" ;;
    sqlserver)  echo "SET NOCOUNT ON;" ;;
  esac
  for s in "$@"; do
    sorgente="$(eta_sorgente "${s}")"
    [[ -n "${sorgente}" ]] || continue
    IFS='|' read -r tabella colonna cond <<< "${sorgente}"
    where=""; [[ -n "${cond}" ]] && where=" WHERE ${cond}"
    case "${TIPO_DB}" in
      postgresql) echo "SELECT 'ETA ${s} ' || COALESCE(CURRENT_DATE - MIN(${colonna})::date, 0) FROM ${tabella}${where};" ;;
      mysql)      echo "SELECT CONCAT('ETA ${s} ', COALESCE(DATEDIFF(CURRENT_DATE, MIN(${colonna})), 0)) AS eta FROM ${tabella}${where};" ;;
      oracle)     echo "SELECT 'ETA ${s} ' || COALESCE(TRUNC(CURRENT_DATE) - TRUNC(MIN(CAST(${colonna} AS DATE))), 0) FROM ${tabella}${where};" ;;
      sqlserver)  echo "SELECT 'ETA ${s} ' + CAST(COALESCE(DATEDIFF(DAY, MIN(${colonna}), GETDATE()), 0) AS VARCHAR(10)) FROM ${tabella}${where};" ;;
      hsql)       echo "SELECT 'ETA ${s} ' || COALESCE(DATEDIFF('day', MIN(${colonna}), CURRENT_DATE), 0) FROM ${tabella}${where};" ;;
    esac
  done
}

# Ogni passata e' una stringa "sezione=retention ..."; retention vuota vuol dire
# quella finale della sezione.
PASSATE=()
if [[ -n "${FINESTRA}" ]]; then
  SEZIONI_TUTTE=("${SEZIONI[@]}")
  declare -A RETENTION_FINALE=() ETA=()
  for s in "${SEZIONI_TUTTE[@]}"; do
    par="$(parametro_di "${s}")"
    [[ -n "${par}" ]] || continue
    RETENTION_FINALE[${s}]="${RETENTION[${s}]:-$(retention_dal_file "${SEZIONI_DIR}/${s}.sql" "${par}")}"
  done
  echo "-- Ricerca dei dati piu' vecchi per sezione..."
  QUERY_ETA="${WORKDIR}/_eta.sql"
  query_eta "${!RETENTION_FINALE[@]}" > "${QUERY_ETA}"
  esito_eta="$(esegui "${QUERY_ETA}" 2>&1)" || errore "ricerca dei dati piu' vecchi non riuscita: ${esito_eta}"
  while read -r _ s n; do ETA[${s}]="${n}"; done < <(grep -oE '^ETA [a-z_-]+ -?[0-9]+' <<< "${esito_eta}")
  eta_massima=0
  for s in "${!RETENTION_FINALE[@]}"; do
    [[ -n "${ETA[${s}]:-}" ]] || errore "eta' dei dati di ${s} non trovata nella risposta: ${esito_eta}"
    [[ "${ETA[${s}]}" -gt "${eta_massima}" ]] && eta_massima="${ETA[${s}]}"
    nota "${s}: dato piu' vecchio di ${ETA[${s}]} giorni, retention ${RETENTION_FINALE[${s}]}"
  done
  # La soglia scende di FINESTRA giorni a passata, finche' resta sopra la piu'
  # bassa delle retention finali: sotto, ci pensa la passata finale.
  minima=""
  for s in "${!RETENTION_FINALE[@]}"; do
    [[ -z "${minima}" || "${RETENTION_FINALE[${s}]}" -lt "${minima}" ]] && minima="${RETENTION_FINALE[${s}]}"
  done
  soglia=$(( eta_massima - FINESTRA ))
  while [[ -n "${minima}" && "${soglia}" -gt "${minima}" ]]; do
    passata=""
    for s in "${SEZIONI_TUTTE[@]}"; do
      [[ -n "${RETENTION_FINALE[${s}]:-}" ]] || continue
      # la sezione entra se la soglia e' sopra la sua retention finale e se ha
      # dati piu' vecchi della soglia
      if [[ "${soglia}" -gt "${RETENTION_FINALE[${s}]}" && "${ETA[${s}]}" -gt "${soglia}" ]]; then
        passata+="${s}=${soglia} "
      fi
    done
    [[ -n "${passata}" ]] && PASSATE+=("${passata% }")
    soglia=$(( soglia - FINESTRA ))
  done
  finale=""
  for s in "${SEZIONI_TUTTE[@]}"; do finale+="${s}= "; done
  PASSATE+=("${finale% }")
  declare -A RETENTION_UTENTE=()
  for s in "${!RETENTION[@]}"; do RETENTION_UTENTE[${s}]="${RETENTION[${s}]}"; done
fi

# Imposta SEZIONI e RETENTION per la passata indicata.
function applica_passata() {   # $1 = passata "sezione=retention ..."
  local tok s v
  SEZIONI=()
  RETENTION=()
  for s in "${!RETENTION_UTENTE[@]}"; do RETENTION[${s}]="${RETENTION_UTENTE[${s}]}"; done
  for tok in $1; do
    s="${tok%%=*}"; v="${tok#*=}"
    SEZIONI+=("${s}")
    [[ -n "${v}" ]] && RETENTION[${s}]="${v}"
  done
  return 0
}

if [[ ${#PASSATE[@]} -gt 0 ]]; then
  applica_passata "${PASSATE[0]}"
  [[ ${#PASSATE[@]} -gt 1 ]] && SENZA_VACUUM=true
  PASSATA_DESCRIZIONE="Passata 1 di ${#PASSATE[@]} (--finestra ${FINESTRA})"
fi
componi

echo "=============================================="
if [[ "${DRY_RUN}" == true ]]; then
  echo "Svecchiamento del database GovPay - SIMULAZIONE (--dry-run)"
else
  echo "Svecchiamento del database GovPay"
fi
echo "  dialetto: ${TIPO_DB}"
if [[ ${#PASSATE[@]} -gt 0 ]]; then
  echo "  passate:  ${#PASSATE[@]}, finestra di ${FINESTRA} giorni"
  i=0
  for p in "${PASSATE[@]}"; do
    i=$((i + 1))
    if [[ ${i} -le 3 || ${i} -eq ${#PASSATE[@]} ]]; then
      printf '    %3d: %s\n' "${i}" "$(sed -E 's/=( |$)/=finale\1/g' <<< "${p}")"
    elif [[ ${i} -eq 4 ]]; then
      echo "    ..."
    fi
  done
else
  for s in "${SEZIONI[@]}"; do
    printf '  sezione:  %-28s %s\n' "${s}" "${RETENTION_USATA[${s}]}"
  done
fi
echo "  script:   ${OUT}"
echo "            $(wc -l < "${OUT}") righe"
if [[ ${#ARCHIVIO_TABELLE[@]} -gt 0 ]]; then
  if [[ "${DRY_RUN}" == true ]]; then
    echo "  archivio: tabelle *_${SUFFISSO_ARCHIVIO}, eliminate a fine simulazione"
  else
    echo "  archivio: tabelle *_${SUFFISSO_ARCHIVIO} (${#ARCHIVIO_TABELLE[@]})"
  fi
fi
echo "=============================================="

if [[ "${SOLO_SQL}" == true ]]; then
  echo
  echo "--solo-sql: database non modificato."
  exit 0
fi

# La simulazione non modifica il database: la conferma non serve.
if [[ "${SENZA_CONFERMA}" != true && "${DRY_RUN}" != true ]]; then
  echo
  echo "Verra' eseguito su ${DB_USER}@${DB_HOST}:${DB_PORT}/${DB_NAME} (${TIPO_DB})."
  echo "La cancellazione e' definitiva."
  if [[ ! -t 0 ]]; then
    errore "conferma non possibile senza terminale: usare -y per eseguire, o --solo-sql per comporre soltanto"
  fi
  read -r -p "Procedere? [s/N] " risposta
  case "${risposta}" in s|S|si|SI|Si) ;; *) echo "Annullato: database non modificato."; exit 0 ;; esac
fi

echo
if [[ "${DRY_RUN}" == true ]]; then
  echo "-- Simulazione su ${DB_USER}@${DB_HOST}:${DB_PORT}/${DB_NAME}: ogni sezione termina con ROLLBACK"
else
  echo "-- Esecuzione su ${DB_HOST}:${DB_PORT}/${DB_NAME}"
fi

function fallimento() {   # $1 = esito, $2 = passata (vuoto senza --finestra)
  local esito="$1" passata="$2"
  echo >&2
  echo "==============================================" >&2
  if [[ "${DRY_RUN}" == true ]]; then
    echo "Simulazione FALLITA (codice ${esito})" >&2
    echo "  script: ${OUT}" >&2
    echo "  La sezione in errore e' stata annullata, e quelle prima di essa" >&2
    echo "  erano gia' terminate con ROLLBACK: il database non e' modificato." >&2
    echo "  L'errore si ripresenterebbe nell'esecuzione vera." >&2
    if [[ ${#ARCHIVIO_TABELLE[@]} -gt 0 ]]; then
      echo "  Possono essere rimaste, vuote, tabelle di archivio *_${SUFFISSO_ARCHIVIO}:" >&2
      echo "  le elimina la prossima simulazione andata a buon fine dello stesso giorno." >&2
    fi
  else
    echo "Svecchiamento FALLITO (codice ${esito})" >&2
    echo "  script: ${OUT}" >&2
    [[ -n "${passata}" ]] && echo "  Passata ${passata}: le passate precedenti sono committate." >&2
    echo "  Ogni sezione e' una transazione a se': quelle completate prima" >&2
    echo "  dell'errore sono committate. Corretta la causa si puo' rieseguire," >&2
    echo "  perche' lo svecchiamento cancella per data e non per stato." >&2
    if [[ ${#ARCHIVIO_TABELLE[@]} -gt 0 ]]; then
      echo "  Le tabelle di archivio *_${SUFFISSO_ARCHIVIO} esistono gia': rieseguendo con lo" >&2
      echo "  stesso suffisso le sezioni con archivio verrebbero saltate. Per riprovare il" >&2
      echo "  DBA deve salvare ed eliminare le tabelle *_${SUFFISSO_ARCHIVIO}, oppure si usa" >&2
      echo "  un altro suffisso con --suffisso-archivio." >&2
    fi
  fi
  echo "==============================================" >&2
  exit "${esito}"
}

# Ogni sezione e' una transazione a se': se una fallisce, quelle prima di essa
# sono committate. E' la ragione per cui l'esito va detto a chiare lettere
# invece di lasciare l'ultima riga al client.
ESITO=0
if [[ ${#PASSATE[@]} -gt 0 ]]; then
  echo
  echo "-- Passata 1 di ${#PASSATE[@]}: $(sed -E 's/=( |$)/=finale\1/g' <<< "${PASSATE[0]}")"
fi
inizio=$(date +%s)
esegui || ESITO=$?
[[ "${ESITO}" -eq 0 ]] || fallimento "${ESITO}" "$([[ ${#PASSATE[@]} -gt 0 ]] && echo "1 di ${#PASSATE[@]}")"
durata_prima=$(( $(date +%s) - inizio ))

# Passate successive. In simulazione non si fanno: ogni passata finisce con
# ROLLBACK, e la successiva ritroverebbe gli stessi dati. Si stima la durata.
if [[ ${#PASSATE[@]} -gt 1 && "${DRY_RUN}" != true ]]; then
  for (( i = 1; i < ${#PASSATE[@]}; i++ )); do
    applica_passata "${PASSATE[${i}]}"
    SENZA_VACUUM=false
    [[ $((i + 1)) -lt ${#PASSATE[@]} ]] && SENZA_VACUUM=true
    PASSATA_DESCRIZIONE="Passata $((i + 1)) di ${#PASSATE[@]} (--finestra ${FINESTRA})"
    componi
    echo
    echo "-- Passata $((i + 1)) di ${#PASSATE[@]}: $(sed -E 's/=( |$)/=finale\1/g' <<< "${PASSATE[${i}]}")"
    esegui || fallimento "$?" "$((i + 1)) di ${#PASSATE[@]}"
  done
fi

echo
echo "=============================================="
if [[ "${DRY_RUN}" == true ]]; then
  echo "Simulazione completata: il database non e' stato modificato"
  echo "  Le righe che ogni DELETE cancellerebbe sono riportate qui sopra."
  if [[ ${#PASSATE[@]} -gt 1 ]]; then
    echo "  Simulata solo la passata 1 di ${#PASSATE[@]}, in ${durata_prima} secondi."
    echo "  Stima per tutte le passate: circa $(( durata_prima * ${#PASSATE[@]} )) secondi."
    echo "  E' una stima grossolana: la durata di una passata dipende dai dati della"
    echo "  sua fascia, e la prima, la piu' vecchia, non e' sempre rappresentativa."
  fi
else
  echo "Svecchiamento completato"
  [[ ${#PASSATE[@]} -gt 0 ]] && echo "  passate:         ${#PASSATE[@]}, finestra di ${FINESTRA} giorni"
fi
echo "  script eseguito: ${OUT}"
if [[ ${#ARCHIVIO_TABELLE[@]} -gt 0 && "${DRY_RUN}" != true ]]; then
  echo "  archivio:        tabelle *_${SUFFISSO_ARCHIVIO}, da salvare ed eliminare a cura del DBA"
fi
echo "=============================================="
