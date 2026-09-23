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
#   ./svecchiamento-db.sh postgresql --host localhost --db govpay --user govpay
#   ./svecchiamento-db.sh postgresql --sezioni eventi --retention-eventi 30 --solo-sql
#
# ATTENZIONE: cancella dati in modo definitivo. Sui metadati Spring Batch non
# c'e' filtro sullo stato delle esecuzioni, quindi va eseguito a batch fermi.
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
SEZIONI_NOTE=(eventi tracciati spring-batch)

# Nome del parametro di retention dentro ciascuno script. Non coincide sempre
# con il nome della sezione: spring-batch non e' un identificatore SQL.
function parametro_di() {
  case "$1" in
    tracciati)    echo "tracciati" ;;
    eventi)       echo "eventi" ;;
    spring-batch) echo "batch" ;;
  esac
}

SEZIONI=("${SEZIONI_NOTE[@]}")
declare -A RETENTION=()

SOLO_SQL=false
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
  --retention-tracciati <giorni>
  --retention-eventi <giorni>
  --retention-batch <giorni>
                       Sovrascrivono il valore scritto nello script della
                       sezione. Senza, vale quello

Connessione (in alternativa alle variabili d'ambiente indicate):
  --host <host>        Host del database            [GOVPAY_DB_SERVER, host[:porta]]
  --port <porta>       Porta                        [GOVPAY_DB_SERVER]
  --db <nome>          Database, o service name su oracle   [GOVPAY_DB_NAME]
  --user <utente>      Utente                       [GOVPAY_DB_USER]
  --password <segreto> Password                     [GOVPAY_DB_PASSWORD]
  --oracle-conn <tipo> servicename | sid           [GOVPAY_ORACLE_JDBC_URL_TYPE]

Esecuzione:
  --solo-sql           Compone lo script e si ferma, senza toccare il database
  -y, --si             Non chiedere conferma prima di eseguire
  --out <dir>          Directory di uscita (default: target/svecchiamento-sql)
  -h, --help           Mostra questo aiuto

Gli script delle sezioni stanno in sql/<dialetto>/svecchiamento/ e sono
eseguibili anche uno per uno con il client del database: questo script serve a
comporli e a passare la retention da fuori, non a nascondere cosa fanno.

Il client usato e' quello nativo del dialetto — psql, sqlplus, mysql, sqlcmd,
SqlTool su hsql — perche' gli script usano i comandi del client per i parametri
e per i messaggi di avanzamento, e nessun client generico li esegue.

ATTENZIONE: la cancellazione e' definitiva. Sui metadati Spring Batch non c'e'
filtro sullo stato: un job in corso la cui esecuzione e' anteriore alla soglia
viene cancellato e Spring Batch ne perde traccia. Eseguire a batch fermi.
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
    --retention-tracciati)   RETENTION[tracciati]="${2:-}"; shift 2 ;;
    --retention-eventi)      RETENTION[eventi]="${2:-}"; shift 2 ;;
    --retention-batch)       RETENTION[batch]="${2:-}"; shift 2 ;;
    --host)                  DB_HOST="${2:-}"; shift 2 ;;
    --port)                  DB_PORT="${2:-}"; shift 2 ;;
    --db)                    DB_NAME="${2:-}"; shift 2 ;;
    --user)                  DB_USER="${2:-}"; shift 2 ;;
    --password)              DB_PASSWORD="${2:-}"; shift 2 ;;
    --oracle-conn)           ORACLE_CONN="${2:-}"; shift 2 ;;
    --solo-sql)              SOLO_SQL=true; shift ;;
    -y|--si)                 SENZA_CONFERMA=true; shift ;;
    --out)                   OUTDIR="${2:-}"; shift 2 ;;
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

for p in "${!RETENTION[@]}"; do
  v="${RETENTION[$p]}"
  [[ "${v}" =~ ^[0-9]+$ ]] || errore "la retention di ${p} deve essere un numero di giorni: '${v}'"
  [[ "${v}" -gt 0 ]]       || errore "la retention di ${p} deve essere maggiore di zero"
done

OUTDIR="${OUTDIR:-${REPO_ROOT}/target/svecchiamento-sql}"
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
  local v="${RETENTION[${par}]:-}"
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
  cat "${tmp}"
}

# ── Composizione ─────────────────────────────────────────────────────────────
mkdir -p "${OUTDIR}"
OUT="${OUTDIR}/govpay-svecchiamento-${TIPO_DB}.sql"

declare -A RETENTION_USATA=()
for s in "${SEZIONI[@]}"; do
  par="$(parametro_di "${s}")"
  if [[ -n "${RETENTION[${par}]:-}" ]]; then
    RETENTION_USATA[${s}]="${RETENTION[${par}]}"
  else
    RETENTION_USATA[${s}]="$(retention_dal_file "${SEZIONI_DIR}/${s}.sql" "${par}") (dal file)"
  fi
done

{
  echo "-- Svecchiamento del database GovPay"
  echo "-- Dialetto: ${TIPO_DB}"
  echo "--"
  echo "-- Generato da src/main/resources/db/$(basename "$0") il $(date '+%Y-%m-%d %H:%M:%S')."
  echo "-- Non modificare a mano: rigenerare indicando le stesse sezioni."
  echo "--"
  echo "-- Sezioni incluse, nell'ordine di esecuzione:"
  for s in "${SEZIONI[@]}"; do
    echo "--   ${s}, retention ${RETENTION_USATA[${s}]} giorni, da sql/${TIPO_DB}/svecchiamento/${s}.sql"
  done
  if [[ ${#SEZIONI[@]} -lt ${#SEZIONI_NOTE[@]} ]]; then
    echo "--"
    echo "-- Sezioni disponibili e non incluse:"
    for n in "${SEZIONI_NOTE[@]}"; do
      inclusa=false
      for s in "${SEZIONI[@]}"; do [[ "${n}" == "${s}" ]] && inclusa=true; done
      [[ "${inclusa}" == false ]] && echo "--   ${n}"
    done
  fi
  echo "--"
  echo "-- ATTENZIONE: la cancellazione e' definitiva. Ogni sezione e' una transazione"
  echo "-- a se': se una fallisce, quelle completate prima restano applicate."
  echo ""
  # sqlplus esce 0 anche dopo un errore, se non gli si dice altrimenti, e senza
  # EXIT finale resta in attesa di input.
  [[ "${TIPO_DB}" == "oracle" ]] && echo "WHENEVER SQLERROR EXIT SQL.SQLCODE"
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

[[ "${TIPO_DB}" == "oracle" ]] && { echo "" >> "${OUT}"; echo "EXIT;" >> "${OUT}"; }

echo "=============================================="
echo "Svecchiamento del database GovPay"
echo "  dialetto: ${TIPO_DB}"
for s in "${SEZIONI[@]}"; do
  printf '  sezione:  %-13s retention %s giorni\n' "${s}" "${RETENTION_USATA[${s}]}"
done
echo "  script:   ${OUT}"
echo "            $(wc -l < "${OUT}") righe"
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
    # con la password dentro. Il WHENEVER SQLERROR copre anche il CONNECT.
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
      org.hsqldb.cmdline.SqlTool --rcFile="${RC}" svecchiamento_db "${OUT}"
    ;;
esac
}

# Ogni sezione e' una transazione a se': se una fallisce, quelle prima di essa
# sono committate. E' la ragione per cui l'esito va detto a chiare lettere
# invece di lasciare l'ultima riga al client.
ESITO=0
esegui || ESITO=$?

if [[ "${ESITO}" -ne 0 ]]; then
  echo >&2
  echo "==============================================" >&2
  echo "Svecchiamento FALLITO (codice ${ESITO})" >&2
  echo "  script: ${OUT}" >&2
  echo "  Ogni sezione e' una transazione a se': quelle completate prima" >&2
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
