#!/bin/bash
#
# Esegue lo svecchiamento completo del database di un'installazione GovPay.
#
# Compone in un unico script, per il dialetto indicato, tutte le parti dello
# svecchiamento e poi lo esegue con il client nativo del database:
#
#   1. le tabelle applicative del core, da sql/<dialetto>/svecchiamento.sql
#      (tracciati con operazioni ed eventi collegati, e il giornale degli
#      eventi per eta');
#   2. i metadati di Spring Batch, dagli script di govpay-common, che sono la
#      sorgente unica per tutti i batch e non sono duplicati qui;
#   3. lo svecchiamento dei componenti aggiuntivi del rilascio, se ne hanno,
#      con --con-componenti.
#
# E' il corrispettivo, per lo svecchiamento, di quello che collect-release-sql.sh
# fa per l'installazione: un solo script per un solo database, perche' core,
# metadati batch e componenti stanno tutti nella stessa base dati.
#
# Uso tipico:
#   ./svecchiamento-db.sh postgresql --host localhost --db govpay --user govpay
#   ./svecchiamento-db.sh postgresql --solo-sql --retention-eventi 30
#
# ATTENZIONE: cancella dati in modo definitivo. Sui metadati Spring Batch non
# c'e' filtro sullo stato delle esecuzioni, quindi va eseguito a batch fermi.
#
set -euo pipefail

BASEDIR="$(cd "$(dirname "$0")" && pwd)"       # src/main/resources/db
CORE_SQL_DIR="${BASEDIR}/sql"
RACCOGLITORE="${BASEDIR}/collect-release-sql.sh"
REPO_ROOT="$(cd "${BASEDIR}/../../../.." && pwd)"

# Dialetti canonici, secondo src/main/resources/db/README.md, e gli alias che
# si incontrano negli altri repository e nelle variabili dei container.
DIALETTI_NOTI=(postgresql oracle mysql sqlserver hsql)

# Retention di default della parte batch. Per il core il default e' quello
# scritto in sql/<dialetto>/svecchiamento.sql e non viene toccato se non si
# passa l'opzione: la sorgente di verita' resta un file solo.
RET_BATCH_DEFAULT=90

RET_TRACCIATI=""
RET_EVENTI=""
RET_BATCH=""
CUTOFF_BATCH=""

CON_CORE=true
CON_BATCH=true
CON_COMPONENTI=false
SOLO_SQL=false
SENZA_CONFERMA=false

OUTDIR=""
WORKDIR=""
SQL_BATCH_DIR="${GOVPAY_COMMON_SQL_DIR:-}"
VERSIONE_CORE=""

# Connessione: gli stessi nomi di variabile usati dall'init dei container e da
# spring-batch-cleanup.sh di govpay-common, cosi' un ambiente gia' configurato
# per quelli vale anche qui.
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

Connessione (in alternativa alle variabili d'ambiente indicate):
  --host <host>        Host del database            [GOVPAY_DB_SERVER, host[:porta]]
  --port <porta>       Porta                        [GOVPAY_DB_SERVER]
  --db <nome>          Database, o service name su oracle   [GOVPAY_DB_NAME]
  --user <utente>      Utente                       [GOVPAY_DB_USER]
  --password <segreto> Password                     [GOVPAY_DB_PASSWORD]
  --oracle-conn <tipo> servicename | sid           [GOVPAY_ORACLE_JDBC_URL_TYPE]

Retention:
  --retention-tracciati <giorni>  Tracciati. Default: il valore scritto in
                                  sql/<dialetto>/svecchiamento.sql
  --retention-eventi <giorni>     Giornale degli eventi. Default: come sopra
  --retention-batch <giorni>      Metadati Spring Batch (default: ${RET_BATCH_DEFAULT})
  --cutoff-batch <data>           Data di taglio esplicita per i metadati batch,
                                  nella forma YYYY-MM-DD oppure
                                  "YYYY-MM-DD HH:MM:SS". Alternativa a
                                  --retention-batch

Parti da includere:
  --senza-core         Non svecchiare le tabelle applicative del core
  --senza-batch        Non svecchiare i metadati di Spring Batch
  --con-componenti     Cerca anche lo svecchiamento dei componenti del rilascio,
                       leggendo il loro SQL con collect-release-sql.sh

Esecuzione:
  --solo-sql           Compone lo script e si ferma, senza toccare il database
  -y, --si             Non chiedere conferma prima di eseguire
  --out <dir>          Directory di uscita (default: target/svecchiamento-sql)
  --work <dir>         Directory di lavoro per lo SQL dei componenti
  --sql-batch <dir>    Directory degli script di svecchiamento dei metadati
                       Spring Batch                 [GOVPAY_COMMON_SQL_DIR]
  --versione-core <v>  Versione del core da dichiarare al raccoglitore dello SQL
                       dei componenti (default: quella del pom di questo albero)
  -h, --help           Mostra questo aiuto

Gli script dei metadati Spring Batch non sono copiati in questo repository: la
sorgente unica e' govpay-common, da cui il profilo dist dei batch li porta in
sql/cleanup dentro sql.zip e quindi in /opt/sql/cleanup nelle immagini. Sono
cercati, in ordine: --sql-batch, lo SQL dei componenti quando si usa
--con-componenti, una copia di lavoro di govpay-common accanto a questa del
core, /opt/sql/cleanup.

Il client usato e' quello nativo del dialetto — psql, sqlplus, mysql, sqlcmd,
SqlTool su hsql — perche' gli script del core usano i comandi del client per i
parametri e per i messaggi di avanzamento, e nessun client generico li esegue.

ATTENZIONE: la cancellazione e' definitiva. Sui metadati Spring Batch non c'e'
filtro sullo stato: un job in corso la cui esecuzione e' anteriore alla data di
taglio viene cancellato e Spring Batch ne perde traccia. Eseguire a batch fermi.
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
    --host)                  DB_HOST="${2:-}"; shift 2 ;;
    --port)                  DB_PORT="${2:-}"; shift 2 ;;
    --db)                    DB_NAME="${2:-}"; shift 2 ;;
    --user)                  DB_USER="${2:-}"; shift 2 ;;
    --password)              DB_PASSWORD="${2:-}"; shift 2 ;;
    --oracle-conn)           ORACLE_CONN="${2:-}"; shift 2 ;;
    --retention-tracciati)   RET_TRACCIATI="${2:-}"; shift 2 ;;
    --retention-eventi)      RET_EVENTI="${2:-}"; shift 2 ;;
    --retention-batch)       RET_BATCH="${2:-}"; shift 2 ;;
    --cutoff-batch)          CUTOFF_BATCH="${2:-}"; shift 2 ;;
    --senza-core)            CON_CORE=false; shift ;;
    --senza-batch)           CON_BATCH=false; shift ;;
    --con-componenti)        CON_COMPONENTI=true; shift ;;
    --solo-sql)              SOLO_SQL=true; shift ;;
    -y|--si)                 SENZA_CONFERMA=true; shift ;;
    --out)                   OUTDIR="${2:-}"; shift 2 ;;
    --work)                  WORKDIR="${2:-}"; shift 2 ;;
    --sql-batch)             SQL_BATCH_DIR="${2:-}"; shift 2 ;;
    --versione-core)         VERSIONE_CORE="${2:-}"; shift 2 ;;
    -h|--help)               usage; exit 0 ;;
    *) errore "opzione sconosciuta: $1" ;;
  esac
done

# Alias: hsqldb e mariadb sono i nomi usati negli altri repository e nelle
# variabili GOVPAY_DB_TYPE dei container, e arrivano quindi anche qui.
case "${TIPO_DB}" in
  hsqldb)  nota "dialetto hsqldb ricondotto a hsql";  TIPO_DB=hsql ;;
  mariadb) nota "dialetto mariadb ricondotto a mysql"; TIPO_DB=mysql ;;
esac

trovato=false
for d in "${DIALETTI_NOTI[@]}"; do [[ "${d}" == "${TIPO_DB}" ]] && trovato=true; done
[[ "${trovato}" == true ]] || errore "dialetto sconosciuto: ${TIPO_DB}. Ammessi: ${DIALETTI_NOTI[*]}"

[[ "${CON_CORE}" == true || "${CON_BATCH}" == true || "${CON_COMPONENTI}" == true ]] \
  || errore "--senza-core e --senza-batch insieme non lasciano nulla da fare"

# Il nome del dialetto negli script dei metadati batch di govpay-common segue
# la nomenclatura di quel repository, dove hsql si chiama hsqldb.
VENDOR_BATCH="${TIPO_DB}"
[[ "${TIPO_DB}" == "hsql" ]] && VENDOR_BATCH="hsqldb"

CORE_SQL="${CORE_SQL_DIR}/${TIPO_DB}/svecchiamento.sql"
if [[ "${CON_CORE}" == true ]]; then
  [[ -f "${CORE_SQL}" ]] || errore "script del core non trovato: ${CORE_SQL#${REPO_ROOT}/}"
fi

OUTDIR="${OUTDIR:-${REPO_ROOT}/target/svecchiamento-sql}"

if [[ -z "${WORKDIR}" ]]; then
  WORKDIR="$(mktemp -d)"
  # La pulizia non deve decidere l'esito dello script: e' l'ultimo comando
  # eseguito, e un suo fallimento diventerebbe il codice di uscita.
  trap 'rm -rf "${WORKDIR}" 2>/dev/null || true' EXIT
fi
mkdir -p "${WORKDIR}"

# ── Retention e data di taglio ───────────────────────────────────────────────
function valida_giorni() {
  [[ "$2" =~ ^[0-9]+$ ]] || errore "$1 deve essere un numero di giorni: '$2'"
  [[ "$2" -gt 0 ]]       || errore "$1 deve essere maggiore di zero"
}
[[ -n "${RET_TRACCIATI}" ]] && valida_giorni "--retention-tracciati" "${RET_TRACCIATI}"
[[ -n "${RET_EVENTI}" ]]    && valida_giorni "--retention-eventi" "${RET_EVENTI}"
[[ -n "${RET_BATCH}" ]]     && valida_giorni "--retention-batch" "${RET_BATCH}"

if [[ -n "${CUTOFF_BATCH}" && -n "${RET_BATCH}" ]]; then
  errore "--cutoff-batch e --retention-batch sono alternativi"
fi

if [[ "${CON_BATCH}" == true ]]; then
  if [[ -z "${CUTOFF_BATCH}" ]]; then
    RET_BATCH="${RET_BATCH:-${RET_BATCH_DEFAULT}}"
    CUTOFF_BATCH="$(date -d "${RET_BATCH} days ago" '+%Y-%m-%d 00:00:00')"
  else
    if [[ "${CUTOFF_BATCH}" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
      CUTOFF_BATCH="${CUTOFF_BATCH} 00:00:00"
    elif ! [[ "${CUTOFF_BATCH}" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}$ ]]; then
      errore "formato di --cutoff-batch non valido: '${CUTOFF_BATCH}'. Attesi YYYY-MM-DD oppure 'YYYY-MM-DD HH:MM:SS'"
    fi
    date -d "${CUTOFF_BATCH}" >/dev/null 2>&1 || errore "data inesistente: '${CUTOFF_BATCH}'"
  fi
fi

# ── Script dei metadati Spring Batch ─────────────────────────────────────────
# Non sono copiati in questo repository: la sorgente unica e' govpay-common.
# Si cercano dove possono trovarsi, dalla piu' fedele al rilascio alla piu'
# comoda in sviluppo, e la provenienza finisce nell'intestazione dello script.
BATCH_SQL=""
BATCH_ORIGINE=""

function cerca_batch_in() {   # $1 directory, $2 descrizione
  local f="$1/spring-batch-cleanup-${VENDOR_BATCH}.sql"
  if [[ -f "${f}" ]]; then
    BATCH_SQL="${f}"
    BATCH_ORIGINE="$2"
    return 0
  fi
  return 1
}

# ── SQL dei componenti ──────────────────────────────────────────────────────
# Come per le patch di aggiornamento, lo SQL dei componenti si ottiene dal
# raccoglitore, che sa leggerlo dalle tre sorgenti secondo release-components.env:
# qui serve solo che popoli la directory di lavoro.
COMP_SVECCHIA=()
COMP_SENZA=()
RACCOLTA_FATTA=false
if [[ "${CON_COMPONENTI}" == true ]]; then
  echo
  echo "-- Svecchiamento dei componenti aggiuntivi"
  # Il raccoglitore vuole sapere di quale rilascio del core si tratta: in
  # modalita' componenti la usa solo per intestazioni e nomi dei file, ma e'
  # obbligatoria. Si prende dal pom di questo albero, che e' la risposta giusta
  # quando lo script viene invocato dal repository.
  if [[ -z "${VERSIONE_CORE}" && -f "${REPO_ROOT}/pom.xml" ]]; then
    VERSIONE_CORE="$(grep -m1 '<version>' "${REPO_ROOT}/pom.xml" \
                     | sed -E 's;.*<version>(.*)</version>.*;\1;' || true)"
  fi
  if [[ ! -f "${RACCOGLITORE}" ]]; then
    nota "collect-release-sql.sh non trovato: componenti non considerati"
  elif [[ -z "${VERSIONE_CORE}" ]]; then
    nota "versione del core non determinabile: indicarla con --versione-core"
  elif ! bash "${RACCOGLITORE}" --core "${VERSIONE_CORE}" --mode componenti \
         --dialects "${TIPO_DB}" --work "${WORKDIR}" \
         --out "${WORKDIR}/_scarto" >"${WORKDIR}/_raccoglitore.log" 2>&1; then
    # Non fatale: core e metadati batch restano l'esito essenziale, e il motivo
    # del fallimento (docker assente, immagine non disponibile, rete) e' nel log.
    nota "lettura dello SQL dei componenti non riuscita, componenti non considerati"
    nota "dettaglio in ${WORKDIR}/_raccoglitore.log"
  else
    RACCOLTA_FATTA=true
    while read -r base; do
      nome="$(basename "$(dirname "${base}")")"
      [[ "${nome}" == "_scarto" ]] && continue
      sf=""
      for d in "${TIPO_DB}" hsqldb mariadb; do
        [[ -f "${base}/${d}/svecchiamento.sql" ]] && { sf="${base}/${d}/svecchiamento.sql"; break; }
      done
      if [[ -z "${sf}" ]]; then
        COMP_SENZA+=("${nome}")
        continue
      fi
      COMP_SVECCHIA+=("${nome}|${sf}")
      nota "govpay-${nome}: svecchiamento presente"
    done < <(find "${WORKDIR}" -maxdepth 2 -type d -name sql | sort)
    if [[ ${#COMP_SENZA[@]} -gt 0 ]]; then
      nota "senza script di svecchiamento: $(IFS=', '; echo "${COMP_SENZA[*]}")"
    fi
  fi
fi

if [[ "${CON_BATCH}" == true ]]; then
  if [[ -n "${SQL_BATCH_DIR}" ]]; then
    cerca_batch_in "${SQL_BATCH_DIR}" "indicata con --sql-batch" \
      || errore "in ${SQL_BATCH_DIR} non c'e' spring-batch-cleanup-${VENDOR_BATCH}.sql"
  fi
  # Dallo SQL dei componenti: e' la copia che accompagna i binari del rilascio,
  # quindi la piu' fedele. Il profilo dist la porta in sql/cleanup.
  if [[ -z "${BATCH_SQL}" && "${RACCOLTA_FATTA}" == true ]]; then
    while read -r d; do
      cerca_batch_in "${d}" "SQL del componente govpay-$(basename "$(dirname "$(dirname "${d}")")")" && break
    done < <(find "${WORKDIR}" -maxdepth 3 -type d -name cleanup | sort)
  fi
  # Copia di lavoro di govpay-common accanto a questa del core.
  if [[ -z "${BATCH_SQL}" ]]; then
    cerca_batch_in "$(dirname "${REPO_ROOT}")/govpay-common/src/main/resources/sql/cleanup" \
                   "copia di lavoro di govpay-common (non e' una versione rilasciata)" || true
  fi
  # Layout delle immagini dei batch, e di un sql.zip scompattato.
  if [[ -z "${BATCH_SQL}" ]]; then
    cerca_batch_in "/opt/sql/cleanup" "/opt/sql/cleanup dell'installazione" || true
  fi
  if [[ -z "${BATCH_SQL}" ]]; then
    echo >&2
    echo "Errore: script di svecchiamento dei metadati Spring Batch non trovato per ${VENDOR_BATCH}." >&2
    echo "Cercato spring-batch-cleanup-${VENDOR_BATCH}.sql in:" >&2
    echo "  - la directory indicata con --sql-batch o GOVPAY_COMMON_SQL_DIR" >&2
    echo "  - lo SQL dei componenti, con --con-componenti" >&2
    echo "  - $(dirname "${REPO_ROOT}")/govpay-common/src/main/resources/sql/cleanup" >&2
    echo "  - /opt/sql/cleanup" >&2
    echo "Con --senza-batch si svecchia il solo core." >&2
    exit 1
  fi
fi

# ── Retention del core ───────────────────────────────────────────────────────
# I default stanno negli script del core e non vengono duplicati qui: se non si
# passa l'opzione, l'unica cosa che facciamo e' leggere il valore per poterlo
# riportare. Se si passa, la riga del parametro viene sostituita con la nostra,
# nell'idioma del dialetto, e il risultato viene verificato: uno script del core
# che cambiasse idioma deve far fallire questo, non farlo ricadere in silenzio
# sul valore di default.
function retention_dal_file() {   # $1 = tracciati|eventi
  local n=""
  if [[ "${TIPO_DB}" == "hsql" ]]; then
    if [[ "$1" == "tracciati" ]]; then
      n="$(grep -m1 'data_completamento < CURRENT_DATE' "${CORE_SQL}" 2>/dev/null \
           | grep -oE 'CURRENT_DATE - [0-9]+ DAY' | grep -oE '[0-9]+' || true)"
    else
      n="$(grep -m1 '^DELETE FROM eventi WHERE data <' "${CORE_SQL}" 2>/dev/null \
           | grep -oE 'CURRENT_DATE - [0-9]+ DAY' | grep -oE '[0-9]+' || true)"
    fi
  else
    n="$(grep -m1 "retention_$1" "${CORE_SQL}" 2>/dev/null | grep -oE '[0-9]+' | head -1 || true)"
  fi
  echo "${n:-?}"
}

function riga_retention() {   # $1 = tracciati|eventi, $2 = giorni
  case "${TIPO_DB}" in
    postgresql) printf '%s\n' "\\set retention_$1 '\\'$2 days\\''" ;;
    oracle)     printf '%s\n' "DEFINE retention_$1 = $2;" ;;
    mysql)      printf '%s\n' "SET @retention_$1 = $2;" ;;
    sqlserver)  printf '%s\n' "DECLARE @retention_$1 INT = $2;" ;;
  esac
}

function core_con_retention() {   # scrive su stdout lo script del core
  local tmp="${WORKDIR}/_core.sql"
  cp "${CORE_SQL}" "${tmp}"

  if [[ "${TIPO_DB}" == "hsql" ]]; then
    # Su hsql i giorni sono letterali dentro le DELETE: HSQLDB non ha variabili
    # negli script. Le righe dei tracciati si riconoscono da data_completamento,
    # quella del giornale dalla DELETE su eventi per data.
    [[ -n "${RET_TRACCIATI}" ]] && sed -E -i \
      "/data_completamento/ s/CURRENT_DATE - [0-9]+ DAY/CURRENT_DATE - ${RET_TRACCIATI} DAY/g" "${tmp}"
    [[ -n "${RET_EVENTI}" ]] && sed -E -i \
      "/^DELETE FROM eventi WHERE data </ s/CURRENT_DATE - [0-9]+ DAY/CURRENT_DATE - ${RET_EVENTI} DAY/" "${tmp}"
  else
    # Le righe del parametro sono rimosse e riscritte in testa: su sqlserver un
    # secondo DECLARE della stessa variabile sarebbe un errore, quindi non basta
    # aggiungere l'assegnamento.
    local intestazione="${WORKDIR}/_core_par.sql"
    : > "${intestazione}"
    for p in tracciati eventi; do
      local v=""
      [[ "${p}" == "tracciati" ]] && v="${RET_TRACCIATI}" || v="${RET_EVENTI}"
      [[ -z "${v}" ]] && continue
      # Lo spazio fra la parola chiave e il nome c'e' su tutti i dialetti tranne
      # mysql e sqlserver, dove il nome e' prefissato da @: l'espressione copre
      # le quattro forme senza una riga per dialetto.
      local espressione="^(\\\\set|DEFINE|SET|DECLARE)[[:space:]]*@?retention_${p}([[:space:]]|=)"
      # Il controllo e' che la riga da sostituire ci sia: se lo script del core
      # cambiasse nome o idioma del parametro, cancellare zero righe e mettere
      # la nostra in testa lascerebbe uno script che si esegue e ignora
      # l'opzione, cioe' il caso peggiore.
      grep -qE "${espressione}" "${tmp}" \
        || errore "in ${CORE_SQL#${REPO_ROOT}/} non c'e' la riga del parametro retention_${p}: lo script del core e' cambiato, aggiornare $(basename "$0")"
      sed -i -E "/${espressione}/d" "${tmp}"
      riga_retention "${p}" "${v}" >> "${intestazione}"
    done
    if [[ -s "${intestazione}" ]]; then
      cat "${intestazione}" "${tmp}" > "${tmp}.new" && mv "${tmp}.new" "${tmp}"
    fi
  fi

  # Verifica della sostituzione: il valore chiesto deve comparire.
  for p in tracciati eventi; do
    local v=""
    [[ "${p}" == "tracciati" ]] && v="${RET_TRACCIATI}" || v="${RET_EVENTI}"
    [[ -z "${v}" ]] && continue
    if [[ "${TIPO_DB}" == "hsql" ]]; then
      grep -q "CURRENT_DATE - ${v} DAY" "${tmp}" \
        || errore "sostituzione della retention ${p} non riuscita su ${CORE_SQL#${REPO_ROOT}/}: lo script del core e' cambiato, aggiornare $(basename "$0")"
    else
      grep -qE "retention_${p}[^0-9]+${v}([^0-9]|\$)" "${tmp}" \
        || errore "sostituzione della retention ${p} non riuscita su ${CORE_SQL#${REPO_ROOT}/}: lo script del core e' cambiato, aggiornare $(basename "$0")"
    fi
  done

  cat "${tmp}"
}

RET_TRACCIATI_USATA="${RET_TRACCIATI}"
RET_EVENTI_USATA="${RET_EVENTI}"
if [[ "${CON_CORE}" == true ]]; then
  [[ -z "${RET_TRACCIATI_USATA}" ]] && RET_TRACCIATI_USATA="$(retention_dal_file tracciati) (dal file)"
  [[ -z "${RET_EVENTI_USATA}" ]]    && RET_EVENTI_USATA="$(retention_dal_file eventi) (dal file)"
fi

# ── Composizione ─────────────────────────────────────────────────────────────
mkdir -p "${OUTDIR}"
OUT="${OUTDIR}/govpay-svecchiamento-${TIPO_DB}.sql"

function sezione() {
  echo ""
  echo "-- ============================================================"
  echo "-- $*"
  echo "-- ============================================================"
  echo ""
}

{
  echo "-- Svecchiamento del database GovPay"
  echo "-- Dialetto: ${TIPO_DB}"
  echo "--"
  echo "-- Generato da src/main/resources/db/$(basename "$0") il $(date '+%Y-%m-%d %H:%M:%S')."
  echo "-- Non modificare a mano: rigenerare indicando gli stessi parametri."
  echo "--"
  echo "-- Parti incluse:"
  if [[ "${CON_CORE}" == true ]]; then
    echo "--   tabelle applicative del core, da sql/${TIPO_DB}/svecchiamento.sql"
    echo "--     retention tracciati: ${RET_TRACCIATI_USATA} giorni"
    echo "--     retention eventi:    ${RET_EVENTI_USATA} giorni"
  fi
  if [[ "${CON_BATCH}" == true ]]; then
    echo "--   metadati Spring Batch, da ${BATCH_ORIGINE}"
    echo "--     data di taglio: ${CUTOFF_BATCH}${RET_BATCH:+ (${RET_BATCH} giorni)}"
  fi
  for e in "${COMP_SVECCHIA[@]:-}"; do
    [[ -z "${e}" ]] && continue
    echo "--   componente govpay-${e%%|*}"
  done
  echo "--"
  echo "-- ATTENZIONE: la cancellazione e' definitiva. Sui metadati Spring Batch non"
  echo "-- c'e' filtro sullo stato delle esecuzioni: eseguire a batch fermi."
  echo ""
  # sqlplus esce 0 anche dopo un errore, se non gli si dice altrimenti, e senza
  # EXIT finale resta in attesa di input.
  [[ "${TIPO_DB}" == "oracle" ]] && echo "WHENEVER SQLERROR EXIT SQL.SQLCODE"
  echo ""
} > "${OUT}"

if [[ "${CON_CORE}" == true ]]; then
  { sezione "govpay (core) — tabelle applicative"; core_con_retention; echo; } >> "${OUT}"
fi

if [[ "${CON_BATCH}" == true ]]; then
  {
    sezione "Spring Batch — metadati delle esecuzioni (taglio: ${CUTOFF_BATCH})"
    # Su hsql non si apre la transazione: HSQLDB non accetta START TRANSACTION
    # nudo, vuole ISOLATION LEVEL o READ WRITE, e SqlTool lavora comunque con
    # autocommit disattivato, quindi il COMMIT in coda basta.
    case "${TIPO_DB}" in
      postgresql|mysql) echo "START TRANSACTION;" ;;
      sqlserver)        echo "BEGIN TRANSACTION;" ;;
    esac
    echo ""
    sed "s/@@CUTOFF@@/${CUTOFF_BATCH}/g" "${BATCH_SQL}"
    echo ""
    case "${TIPO_DB}" in
      sqlserver) echo "COMMIT TRANSACTION;" ;;
      *)         echo "COMMIT;" ;;
    esac
    echo
  } >> "${OUT}"
  # Lo stesso controllo che fa spring-batch-cleanup.sh: una sostituzione non
  # avvenuta produrrebbe uno script che il client rifiuta, o peggio accetta.
  if grep -q '@@CUTOFF@@' "${OUT}"; then
    errore "sostituzione della data di taglio non riuscita su ${BATCH_SQL}"
  fi
fi

if [[ ${#COMP_SVECCHIA[@]} -gt 0 ]]; then
  for e in "${COMP_SVECCHIA[@]}"; do
    nome="${e%%|*}"; sf="${e#*|}"
    { sezione "govpay-${nome} — svecchiamento del componente"; cat "${sf}"; echo; } >> "${OUT}"
  done
fi

[[ "${TIPO_DB}" == "oracle" ]] && { echo "EXIT;" >> "${OUT}"; } || true

echo "=============================================="
echo "Svecchiamento del database GovPay"
echo "  dialetto:   ${TIPO_DB}"
[[ "${CON_CORE}" == true ]]  && echo "  core:       tracciati ${RET_TRACCIATI_USATA} giorni, eventi ${RET_EVENTI_USATA} giorni" || true
[[ "${CON_BATCH}" == true ]] && echo "  batch:      taglio ${CUTOFF_BATCH}, da ${BATCH_ORIGINE}" || true
[[ ${#COMP_SVECCHIA[@]} -gt 0 ]] && echo "  componenti: ${#COMP_SVECCHIA[@]}" || true
echo "  script:     ${OUT}"
echo "              $(wc -l < "${OUT}") righe"
echo "=============================================="

if [[ "${SOLO_SQL}" == true ]]; then
  echo
  echo "--solo-sql: database non modificato."
  exit 0
fi

# ── Esecuzione ───────────────────────────────────────────────────────────────
# I parametri di connessione servono solo qui: comporre lo script non ne ha
# bisogno, e --solo-sql funziona anche dove non si ha accesso al database.
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

if [[ "${SENZA_CONFERMA}" != true ]]; then
  echo
  echo "Verra' eseguito su ${DB_USER}@${DB_HOST}:${DB_PORT}/${DB_NAME} (${TIPO_DB})."
  echo "La cancellazione e' definitiva."
  if [[ ! -t 0 ]]; then
    errore "conferma non possibile senza terminale: usare -y per eseguire, o --solo-sql per comporre soltanto"
  fi
  read -r -p "Procedere? [s/N] " risposta
  case "${risposta}" in s|S|si|SI|Si) ;; *) echo "Annullato: database non modificato."; exit 0 ;; esac
fi

function richiedi_client() {
  command -v "$1" >/dev/null 2>&1 || errore "client $1 non trovato: serve per eseguire lo svecchiamento su ${TIPO_DB}. Con --solo-sql lo script viene composto senza eseguirlo"
}

echo
echo "-- Esecuzione su ${DB_HOST}:${DB_PORT}/${DB_NAME}"

function esegui() {
case "${TIPO_DB}" in
  postgresql)
    richiedi_client psql
    # ON_ERROR_STOP e' necessario: senza, psql esce 0 anche dopo un errore.
    PGPASSWORD="${DB_PASSWORD}" psql -v ON_ERROR_STOP=1 \
      -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" -f "${OUT}"
    ;;
  mysql)
    richiedi_client mysql
    # MYSQL_PWD evita la password nella riga di comando, visibile in ps.
    MYSQL_PWD="${DB_PASSWORD}" mysql -h "${DB_HOST}" -P "${DB_PORT}" \
      -u "${DB_USER}" -D "${DB_NAME}" < "${OUT}"
    ;;
  oracle)
    richiedi_client sqlplus
    if [[ "${ORACLE_CONN}" == "sid" ]]; then
      DESCRITTORE="${DB_HOST}:${DB_PORT}:${DB_NAME}"
    else
      DESCRITTORE="//${DB_HOST}:${DB_PORT}/${DB_NAME}"
    fi
    # CONNECT arriva da stdin e non dalla riga di comando, che ps mostrerebbe
    # con la password dentro. Il WHENEVER SQLERROR copre anche il CONNECT: senza,
    # una connessione rifiutata farebbe eseguire lo script senza sessione.
    sqlplus -S -L /nolog <<EOSQLPLUS
WHENEVER SQLERROR EXIT SQL.SQLCODE
CONNECT ${DB_USER}/${DB_PASSWORD}@${DESCRITTORE}
@${OUT}
EXIT;
EOSQLPLUS
    ;;
  sqlserver)
    richiedi_client sqlcmd
    # -b: uscita non nulla al primo errore.
    SQLCMDPASSWORD="${DB_PASSWORD}" sqlcmd -b \
      -S "${DB_HOST},${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" -i "${OUT}"
    ;;
  hsql)
    richiedi_client java
    # Su hsql il client e' SqlTool, come nell'init dei container: non c'e' un
    # client a riga di comando nativo, e lo script del core non usa comandi di
    # client, quindi passa da qui senza adattamenti.
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
      org.hsqldb.cmdline.SqlTool --rcFile="${RC}" svecchiamento_db "${OUT}"
    ;;
esac
}

# Le sezioni dello script sono transazioni distinte, una per parte: se una
# fallisce, quelle prima di essa sono committate. E' la ragione per cui l'esito
# va detto a chiare lettere invece di lasciare l'ultima riga al client.
ESITO=0
esegui || ESITO=$?

if [[ "${ESITO}" -ne 0 ]]; then
  echo >&2
  echo "==============================================" >&2
  echo "Svecchiamento FALLITO (codice ${ESITO})" >&2
  echo "  script: ${OUT}" >&2
  echo "  Le sezioni sono transazioni distinte: quelle completate prima" >&2
  echo "  dell'errore sono committate. Corretta la causa si puo' rieseguire," >&2
  echo "  perche' lo svecchiamento cancella per data e non per stato." >&2
  echo "==============================================" >&2
  exit "${ESITO}"
fi

echo
echo "=============================================="
echo "Svecchiamento completato"
echo "  script eseguito: ${OUT}"
echo "=============================================="
