#!/bin/bash
#
# Compone la patch completa per aggiornare un'installazione GovPay da una
# versione a un'altra, in un unico script per il dialetto indicato.
#
# Le patch del core sono nominate con la versione a cui portano, quindi
# aggiornare da X ad A significa applicare, in ordine di versione, tutte quelle
# v con X < v <= A. Lo script le seleziona e le concatena, e aggiunge le patch
# dei componenti aggiuntivi del rilascio quando ne hanno.
#
# Uso tipico:
#   ./build-upgrade-sql.sh postgresql 3.8.2 3.10.0
#
set -euo pipefail

BASEDIR="$(cd "$(dirname "$0")" && pwd)"       # src/main/resources/db
CORE_SQL_DIR="${BASEDIR}/sql"
RACCOGLITORE="${BASEDIR}/collect-release-sql.sh"

# Dialetti canonici, secondo src/main/resources/db/README.md
DIALETTI_NOTI=(postgresql oracle mysql sqlserver hsql)

# Una patch del core e' un file il cui nome e' una versione: cifre separate da
# punti, con l'eventuale suffisso di release candidate. Il filtro serve perche'
# nella stessa directory convivono file che versioni non sono, per esempio
# 3.8_aca.sql su oracle, che e' una patch di un componente depositata li'.
REGEX_VERSIONE='^[0-9]+(\.[0-9]+)*(-rc[0-9]+)?$'

OUTDIR=""
WORKDIR=""
CON_COMPONENTI=true
DRYRUN=false

# Migrazione dei metadati di Spring Batch: fuori per default, perche' riguarda
# solo le installazioni che quei metadati li avevano gia' persistiti nella forma
# di una versione precedente del framework.
VERSIONE_MIGRAZIONE=""
SQL_BATCH_DIR="${GOVPAY_SPRING_BATCH_SQL_DIR:-}"
MIGRAZIONE_FILE=""
MIGRAZIONE_ORIGINE=""

function usage() {
cat <<EOHELP
Usage: $(basename "$0") <tipoDB> <versioneDA> <versioneA> [opzioni]

Argomenti:
  tipoDB        Dialetto del database: $(IFS='|'; echo "${DIALETTI_NOTI[*]}")
  versioneDA    Versione attualmente installata. Le sue patch NON vengono
                incluse: si parte da quelle successive
  versioneA     Versione di destinazione, inclusa

Opzioni:
  --out <dir>          Directory di uscita (default: target/upgrade-sql)
  --work <dir>         Directory di lavoro per lo SQL dei componenti
                       (default: una temporanea, rimossa all'uscita)
  --senza-componenti   Considera solo le patch del core
  --con-migrazione-batch <v>
                       Include anche la migrazione dei metadati di Spring Batch
                       alla versione <v> del framework, per esempio 6.0
  --sql-batch <dir>    Directory sql/spring-batch da cui prendere la migrazione
                       [GOVPAY_SPRING_BATCH_SQL_DIR]
  --dry-run            Elenca le patch che includerebbe, senza scrivere
  -h, --help           Mostra questo aiuto

La migrazione dei metadati di Spring Batch NON e' inclusa per default, e non lo
e' nemmeno con --con-componenti: gli script di upstream trasformano tabelle
BATCH_* che esistono gia' nella forma di una versione precedente del framework, e
su un'installazione che non le ha, o le ha gia' nella forma nuova, falliscono.
Va chiesta esplicitamente, sapendo da quale forma si parte.

Per il dialetto mysql viene usato migration-mysql.sql anche su MariaDB, e non il
migration-mariadb.sql che upstream spedisce: quello usa RENAME SEQUENCE, che in
MariaDB non esiste. Verificato su MariaDB 11.8.

Le patch dei componenti aggiuntivi sono cercate in <dialetto>/patch/ dentro lo
SQL che accompagna ciascun componente del rilascio, letto dalle stesse sorgenti
usate da collect-release-sql.sh e con le versioni dichiarate in
release-components.env. Un componente senza patch non e' un errore.

NON vengono mai incluse le patch sotto patch/clienti/, che sono specifiche di
singole installazioni, ne' le directory di patch cumulative preconfezionate
come patch/3.8.2_to_3.9.2/, che sono un'alternativa a questo script e non un
suo ingrediente.
EOHELP
}

function errore() { echo "Errore: $*" >&2; exit 1; }
function nota()   { echo "  $*"; }

# ── Argomenti ────────────────────────────────────────────────────────────────
[[ $# -ge 1 ]] || { usage >&2; exit 1; }
case "${1:-}" in -h|--help) usage; exit 0 ;; esac
[[ $# -ge 3 ]] || { usage >&2; errore "servono tre argomenti: tipoDB, versioneDA, versioneA"; }

TIPO_DB="$1"; VERSIONE_DA="$2"; VERSIONE_A="$3"; shift 3

while [[ $# -gt 0 ]]; do
  case "$1" in
    --out)               OUTDIR="${2:-}"; shift 2 ;;
    --work)              WORKDIR="${2:-}"; shift 2 ;;
    --senza-componenti)  CON_COMPONENTI=false; shift ;;
    --con-migrazione-batch) VERSIONE_MIGRAZIONE="${2:-}"; shift 2 ;;
    --sql-batch)         SQL_BATCH_DIR="${2:-}"; shift 2 ;;
    --dry-run)           DRYRUN=true; shift ;;
    -h|--help)           usage; exit 0 ;;
    *) errore "opzione sconosciuta: $1" ;;
  esac
done

trovato=false
for d in "${DIALETTI_NOTI[@]}"; do [[ "${d}" == "${TIPO_DB}" ]] && trovato=true; done
[[ "${trovato}" == true ]] || errore "dialetto sconosciuto: ${TIPO_DB}. Ammessi: ${DIALETTI_NOTI[*]}"

PATCH_DIR="${CORE_SQL_DIR}/${TIPO_DB}/patch"
[[ -d "${PATCH_DIR}" ]] || errore "directory delle patch non trovata: ${PATCH_DIR#${BASEDIR}/}"

OUTDIR="${OUTDIR:-$(cd "${BASEDIR}/../../../.." && pwd)/target/upgrade-sql}"

if [[ -z "${WORKDIR}" ]]; then
  WORKDIR="$(mktemp -d)"
  # La pulizia non deve decidere l'esito dello script: e' l'ultimo comando
  # eseguito, e un suo fallimento diventerebbe il codice di uscita.
  trap 'rm -rf "${WORKDIR}" 2>/dev/null || true' EXIT
fi
mkdir -p "${WORKDIR}"

# ── Confronto di versioni ────────────────────────────────────────────────────
# sort -V ordina correttamente le forme in uso, compresi 3.9 < 3.9.2 < 3.10.0 e
# 3.1-rc1 < 3.1.1, dove un ordinamento lessicografico sbaglierebbe.
function ver_lt() {   # vero se $1 < $2
  [[ "$1" != "$2" ]] && [[ "$(printf '%s\n%s\n' "$1" "$2" | sort -V | head -1)" == "$1" ]]
}

function in_intervallo() {   # vero se versioneDA < $1 <= versioneA
  ver_lt "${VERSIONE_DA}" "$1" && ! ver_lt "${VERSIONE_A}" "$1"
}

# ── Patch del core nell'intervallo ───────────────────────────────────────────
VERSIONI_NOTE=()
SCARTATE=()
while read -r f; do
  v="$(basename "${f}" .sql)"
  if [[ "${v}" =~ ${REGEX_VERSIONE} ]]; then
    VERSIONI_NOTE+=("${v}")
  else
    SCARTATE+=("${v}.sql")
  fi
done < <(find "${PATCH_DIR}" -maxdepth 1 -name '*.sql' | sort)

[[ ${#VERSIONI_NOTE[@]} -gt 0 ]] || errore "nessuna patch di versione in ${PATCH_DIR#${BASEDIR}/}"

# Ordinate per versione: e' l'ordine in cui vanno applicate.
mapfile -t VERSIONI_ORDINATE < <(printf '%s\n' "${VERSIONI_NOTE[@]}" | sort -V)

DA_APPLICARE=()
for v in "${VERSIONI_ORDINATE[@]}"; do
  in_intervallo "${v}" && DA_APPLICARE+=("${v}")
done

echo "=============================================="
echo "Patch di aggiornamento GovPay"
echo "  dialetto:    ${TIPO_DB}"
echo "  da versione: ${VERSIONE_DA}"
echo "  a versione:  ${VERSIONE_A}"
echo "  uscita:      ${OUTDIR}"
echo "=============================================="

# Controlli sugli estremi, prima di comporre: un intervallo vuoto o rovesciato
# e' quasi sempre un errore di invocazione, e proseguire produrrebbe uno script
# vuoto che sembra valido.
ver_lt "${VERSIONE_DA}" "${VERSIONE_A}" \
  || errore "versioneDA (${VERSIONE_DA}) deve precedere versioneA (${VERSIONE_A})"

esiste_a=false
for v in "${VERSIONI_ORDINATE[@]}"; do [[ "${v}" == "${VERSIONE_A}" ]] && esiste_a=true; done
[[ "${esiste_a}" == true ]] \
  || errore "per la versione di destinazione ${VERSIONE_A} non esiste ${PATCH_DIR#${BASEDIR}/}/${VERSIONE_A}.sql. Versioni disponibili: ${VERSIONI_ORDINATE[*]}"

esiste_da=false
for v in "${VERSIONI_ORDINATE[@]}"; do [[ "${v}" == "${VERSIONE_DA}" ]] && esiste_da=true; done
if [[ "${esiste_da}" != true ]]; then
  echo
  echo "  ATTENZIONE: per la versione di partenza ${VERSIONE_DA} non esiste una patch in questo dialetto." >&2
  echo "  L'intervallo e' stato comunque calcolato per confronto di versione, ma verificare che" >&2
  echo "  l'installazione sia effettivamente a quella versione." >&2
fi

[[ ${#DA_APPLICARE[@]} -gt 0 ]] \
  || errore "nessuna patch da applicare fra ${VERSIONE_DA} e ${VERSIONE_A} per ${TIPO_DB}"

echo
echo "-- Patch del core, nell'ordine di applicazione"
for v in "${DA_APPLICARE[@]}"; do nota "${v}.sql"; done
if [[ ${#SCARTATE[@]} -gt 0 ]]; then
  echo
  echo "-- File nella directory delle patch il cui nome non e' una versione, non inclusi"
  for f in "${SCARTATE[@]}"; do nota "${f}"; done
fi

# ── SQL dei componenti ───────────────────────────────────────────────────────
# Lo SQL dei componenti si ottiene dal raccoglitore, che sa leggerlo dalle tre
# sorgenti (rilascio, immagine, branch) secondo release-components.env: qui
# serve solo che popoli la directory di lavoro, lo script che compone lo butta.
# Serve sia per le patch dei componenti sia per la migrazione dei metadati
# batch, che upstream spedisce dentro lo stesso sql.zip: viene invocato una volta
# sola, e non viene invocato affatto se non serve a nessuna delle due.
RACCOLTA_OK=false
SERVE_RACCOLTA=false
[[ "${CON_COMPONENTI}" == true ]] && SERVE_RACCOLTA=true
[[ -n "${VERSIONE_MIGRAZIONE}" && -z "${SQL_BATCH_DIR}" ]] && SERVE_RACCOLTA=true

if [[ "${SERVE_RACCOLTA}" == true ]]; then
  if [[ ! -f "${RACCOGLITORE}" ]]; then
    echo
    nota "collect-release-sql.sh non trovato: SQL dei componenti non disponibile"
  elif ! bash "${RACCOGLITORE}" --core "${VERSIONE_A}" --mode componenti \
         --dialects "${TIPO_DB}" --work "${WORKDIR}" \
         --out "${WORKDIR}/_scarto" >"${WORKDIR}/_raccoglitore.log" 2>&1; then
    # Non fatale: le patch del core restano l'esito essenziale, e il motivo del
    # fallimento (docker assente, immagine non disponibile, rete) e' nel log.
    echo
    nota "lettura dello SQL dei componenti non riuscita"
    nota "dettaglio in ${WORKDIR}/_raccoglitore.log"
  else
    RACCOLTA_OK=true
  fi
fi

# ── Patch dei componenti ─────────────────────────────────────────────────────
COMP_PATCH=()
COMP_SENZA=()
if [[ "${CON_COMPONENTI}" == true ]]; then
  echo
  echo "-- Patch dei componenti aggiuntivi"
  if [[ "${RACCOLTA_OK}" != true ]]; then
    nota "componenti non considerati"
  else
    while read -r base; do
      nome="$(basename "$(dirname "${base}")")"
      [[ "${nome}" == "_scarto" ]] && continue
      cpd=""
      for d in "${TIPO_DB}" hsqldb mariadb; do
        [[ -d "${base}/${d}/patch" ]] && { cpd="${base}/${d}/patch"; break; }
      done
      if [[ -z "${cpd}" ]]; then
        COMP_SENZA+=("${nome}")
        continue
      fi
      trovate=0
      while read -r pf; do
        pv="$(basename "${pf}" .sql)"
        if [[ "${pv}" =~ ${REGEX_VERSIONE} ]] && in_intervallo "${pv}"; then
          COMP_PATCH+=("${nome}|${pv}|${pf}")
          trovate=$(( trovate + 1 ))
        fi
      done < <(find "${cpd}" -maxdepth 1 -name '*.sql' | sort)
      nota "govpay-${nome}: ${trovate} patch nell'intervallo"
    done < <(find "${WORKDIR}" -maxdepth 2 -type d -name sql | sort)
    if [[ ${#COMP_SENZA[@]} -gt 0 ]]; then
      nota "senza directory di patch: $(IFS=', '; echo "${COMP_SENZA[*]}")"
    fi
  fi
else
  echo
  nota "componenti esclusi da --senza-componenti"
fi

# ── Migrazione dei metadati di Spring Batch ──────────────────────────────────
# Gli script sono di upstream, estratti da spring-batch-core dal profilo dist e
# spediti in sql/spring-batch/migration/<versione>/migration-<vendor>.sql dentro
# sql.zip, quindi in /opt/sql nelle immagini dei batch. Qui non sono duplicati:
# si cercano dove arrivano.
# Il nome del vendor e' quello di upstream, che per hsql e' hsqldb; per mysql
# upstream distingue mysql da mariadb con due script diversi e incompatibili,
# perche' su MySQL la sequence e' emulata con una tabella e su MariaDB e' una
# sequence vera, quindi la scelta del motore non e' ricavabile dal dialetto.
if [[ -n "${VERSIONE_MIGRAZIONE}" ]]; then
  echo
  echo "-- Migrazione dei metadati di Spring Batch alla ${VERSIONE_MIGRAZIONE}"

  # Upstream spedisce due script distinti per mysql e mariadb, ma quello mariadb
  # usa RENAME SEQUENCE, che in MariaDB non esiste: provato su MariaDB 11.8, dove
  # e' un errore di sintassi. Il file mysql invece funziona su entrambi i motori,
  # perche' RENAME TABLE rinomina anche una sequence MariaDB conservandola come
  # sequence. Per il dialetto mysql si usa quindi sempre migration-mysql.sql.
  VENDOR_UPSTREAM="${TIPO_DB}"
  case "${TIPO_DB}" in
    hsql)  VENDOR_UPSTREAM=hsqldb ;;
    mysql) nota "su MariaDB si usa lo stesso migration-mysql.sql: il migration-mariadb.sql di upstream usa RENAME SEQUENCE, che MariaDB non ha" ;;
  esac

  RELATIVO="migration/${VERSIONE_MIGRAZIONE}/migration-${VENDOR_UPSTREAM}.sql"
  DIR_CERCATE=()

  function cerca_migrazione() {   # $1 directory sql/spring-batch, $2 descrizione
    DIR_CERCATE+=("$1")
    if [[ -f "$1/${RELATIVO}" ]]; then
      MIGRAZIONE_FILE="$1/${RELATIVO}"
      MIGRAZIONE_ORIGINE="$2"
      return 0
    fi
    return 1
  }

  if [[ -n "${SQL_BATCH_DIR}" ]]; then
    cerca_migrazione "${SQL_BATCH_DIR}" "indicata con --sql-batch" || true
  fi
  if [[ -z "${MIGRAZIONE_FILE}" && "${RACCOLTA_OK}" == true ]]; then
    while read -r d; do
      cerca_migrazione "${d}" "SQL del componente govpay-$(basename "$(dirname "$(dirname "${d}")")")" && break
    done < <(find "${WORKDIR}" -maxdepth 3 -type d -name 'spring-batch' | sort)
  fi
  if [[ -z "${MIGRAZIONE_FILE}" ]]; then
    cerca_migrazione "/opt/sql/spring-batch" "/opt/sql dell'installazione" || true
  fi

  if [[ -z "${MIGRAZIONE_FILE}" ]]; then
    echo >&2
    echo "Errore: ${RELATIVO} non trovato." >&2
    if [[ ${#DIR_CERCATE[@]} -gt 0 ]]; then
      echo "Cercato in:" >&2
      for d in "${DIR_CERCATE[@]}"; do echo "  ${d}" >&2; done
      # Elencare le versioni presenti e' piu' utile del solo fallimento: sono
      # quelle che upstream spedisce nella spring-batch.version di questo bom.
      disponibili=""
      for d in "${DIR_CERCATE[@]}"; do
        [[ -d "${d}/migration" ]] || continue
        disponibili="${disponibili} $(find "${d}/migration" -maxdepth 1 -mindepth 1 -type d -printf '%f ' 2>/dev/null || true)"
      done
      [[ -n "${disponibili// /}" ]] && echo "Versioni disponibili:${disponibili}" >&2
      vendor_presenti="$(find "${DIR_CERCATE[@]}" -path "*/migration/${VERSIONE_MIGRAZIONE}/*" -name 'migration-*.sql' -printf '%f ' 2>/dev/null || true)"
      [[ -n "${vendor_presenti// /}" ]] && echo "Per la ${VERSIONE_MIGRAZIONE} ci sono: ${vendor_presenti}" >&2
    fi
    echo "Con --sql-batch si indica una directory sql/spring-batch; senza" >&2
    echo "--con-migrazione-batch la migrazione non viene cercata." >&2
    exit 1
  fi
  nota "${RELATIVO}, da ${MIGRAZIONE_ORIGINE}"
  nota "ATTENZIONE: da applicare solo se le tabelle BATCH_* sono nella forma anteriore alla ${VERSIONE_MIGRAZIONE}"
fi

# ── Composizione ─────────────────────────────────────────────────────────────
OUT="${OUTDIR}/govpay-upgrade-${VERSIONE_DA}-a-${VERSIONE_A}-${TIPO_DB}.sql"

if [[ "${DRYRUN}" == true ]]; then
  echo
  echo "=============================================="
  echo "dry-run: nessuno script scritto. Avrebbe prodotto:"
  echo "  ${OUT}"
  echo "=============================================="
  exit 0
fi

mkdir -p "${OUTDIR}"
{
  echo "-- Patch di aggiornamento GovPay da ${VERSIONE_DA} a ${VERSIONE_A}"
  echo "-- Dialetto: ${TIPO_DB}"
  echo "--"
  echo "-- Generato da src/main/resources/db/build-upgrade-sql.sh."
  echo "-- Non modificare a mano: rigenerare indicando le due versioni."
  echo "--"
  echo "-- Da applicare a un'installazione che si trova alla versione ${VERSIONE_DA}."
  echo "-- Le patch sono nell'ordine di applicazione e non vanno riordinate."
  echo "--"
  echo "-- Patch del core incluse:"
  for v in "${DA_APPLICARE[@]}"; do echo "--   ${v}.sql"; done
  if [[ ${#COMP_PATCH[@]} -gt 0 ]]; then
    echo "--"
    echo "-- Patch dei componenti incluse:"
    for e in "${COMP_PATCH[@]}"; do echo "--   govpay-${e%%|*} ${e#*|}" | sed 's/|.*$//'; done
  fi
  if [[ -n "${MIGRAZIONE_FILE}" ]]; then
    echo "--"
    echo "-- Migrazione dei metadati di Spring Batch alla ${VERSIONE_MIGRAZIONE} inclusa in coda:"
    echo "--   ${RELATIVO}, da ${MIGRAZIONE_ORIGINE}"
    echo "--   Script di upstream, non nostro: non e' idempotente e va applicato solo a"
    echo "--   un'installazione le cui tabelle BATCH_* sono nella forma anteriore alla"
    echo "--   ${VERSIONE_MIGRAZIONE}. Su tabelle gia' migrate, o assenti, fallisce."
  fi
  if [[ ${#SCARTATE[@]} -gt 0 ]]; then
    echo "--"
    echo "-- Nella directory delle patch del core ci sono file il cui nome non e' una"
    echo "-- versione, che non sono stati inclusi perche' non collocabili nell'intervallo:"
    for f in "${SCARTATE[@]}"; do echo "--   ${f}"; done
  fi
  echo ""
} > "${OUT}"

function sezione() {
  echo ""
  echo "-- ============================================================"
  echo "-- $*"
  echo "-- ============================================================"
  echo ""
}

for v in "${DA_APPLICARE[@]}"; do
  { sezione "govpay (core) — patch ${v}"; cat "${PATCH_DIR}/${v}.sql"; echo; } >> "${OUT}"
done

# Le patch dei componenti vanno dopo quelle del core, che possono creare o
# modificare le strutture su cui poggiano, e fra loro in ordine di versione.
if [[ ${#COMP_PATCH[@]} -gt 0 ]]; then
  while read -r e; do
    nome="${e%%|*}"; resto="${e#*|}"; pv="${resto%%|*}"; pf="${resto#*|}"
    { sezione "govpay-${nome} — patch ${pv}"; cat "${pf}"; echo; } >> "${OUT}"
  done < <(printf '%s\n' "${COMP_PATCH[@]}" | sort -t'|' -k2,2V)
fi

# La migrazione dei metadati batch va per ultima, e separata: non e' una nostra
# patch ma uno script di upstream, tocca solo tabelle BATCH_* e ha una condizione
# di applicabilita' diversa da tutto il resto.
if [[ -n "${MIGRAZIONE_FILE}" ]]; then
  {
    sezione "Spring Batch ${VERSIONE_MIGRAZIONE} — migrazione dei metadati (${VENDOR_UPSTREAM})"
    echo "-- Da ${RELATIVO}, ${MIGRAZIONE_ORIGINE}."
    echo "-- Script di upstream, riportato senza modifiche."
    echo "--"
    echo "-- Applicare SOLO a un'installazione le cui tabelle BATCH_* sono nella forma"
    echo "-- anteriore alla ${VERSIONE_MIGRAZIONE}. Su tabelle gia' migrate, o su"
    echo "-- un'installazione che non ha mai persistito i metadati, fallisce."
    echo ""
    cat "${MIGRAZIONE_FILE}"
    echo
  } >> "${OUT}"
fi

echo
echo "=============================================="
echo "Script prodotto:"
echo "  ${OUT}"
echo "  $(wc -l < "${OUT}") righe, $(( ${#DA_APPLICARE[@]} + ${#COMP_PATCH[@]} )) patch"
[[ -n "${MIGRAZIONE_FILE}" ]] && echo "  piu' la migrazione dei metadati batch alla ${VERSIONE_MIGRAZIONE}"
echo "==============================================" 
