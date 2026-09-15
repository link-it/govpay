#!/bin/bash
#
# Raccoglie lo SQL di un rilascio GovPay in un unico script per dialetto.
#
# Prende i componenti del rilascio con le loro versioni, scarica da ciascuna
# GitHub Release l'asset sql.zip (se presente), e concatena quel contenuto con
# lo SQL di questo progetto in uno script complessivo, pronto da allegare al
# rilascio del core dalla pipeline.
#
# Ordine di concatenazione, che non e' arbitrario:
#   1. il core, perche' definisce le tabelle su cui gli altri poggiano
#      (per esempio govpay-aca-batch crea viste su versamenti)
#   2. le tabelle di Spring Batch, una volta sola
#   3. i componenti satellite, in ordine alfabetico
#
# Uso tipico:
#   ./collect-release-sql.sh --core 3.10.0 \
#       --set aca-batch=1.4.0 --set rt-batch=2.1.0
#
set -euo pipefail

BASEDIR="$(cd "$(dirname "$0")" && pwd)"       # src/main/resources/db
CORE_SQL_DIR="${BASEDIR}/sql"

# ── Componenti del rilascio ──────────────────────────────────────────────────
# Una variabile per coppia progetto/versione. Vuota = componente non incluso in
# questo rilascio. La versione e' il tag della GitHub Release da cui scaricare
# sql.zip.
#
# I valori arrivano da release-components.env, che e' versionato e va aggiornato
# quando si prepara un rilascio. Precedenza: --set, poi l'ambiente, poi il file.
COMPONENTS_ENV="${BASEDIR}/release-components.env"

GOVPAY_CORE_VERSION="${GOVPAY_CORE_VERSION:-}"
GOVPAY_ACA_BATCH_VERSION="${GOVPAY_ACA_BATCH_VERSION:-}"
GOVPAY_CONSOLE_API_VERSION="${GOVPAY_CONSOLE_API_VERSION:-}"
GOVPAY_FDR_BATCH_VERSION="${GOVPAY_FDR_BATCH_VERSION:-}"
GOVPAY_IBAN_BATCH_VERSION="${GOVPAY_IBAN_BATCH_VERSION:-}"
GOVPAY_MAGGIOLI_JPPA_VERSION="${GOVPAY_MAGGIOLI_JPPA_VERSION:-}"
GOVPAY_NOTIFY_BATCH_VERSION="${GOVPAY_NOTIFY_BATCH_VERSION:-}"
GOVPAY_RT_BATCH_VERSION="${GOVPAY_RT_BATCH_VERSION:-}"
GOVPAY_TRACCIATI_BATCH_VERSION="${GOVPAY_TRACCIATI_BATCH_VERSION:-}"

# Nome del componente -> nome della variabile che ne porta la versione.
# L'ordine qui e' quello di concatenazione dei satelliti.
COMPONENTI=(
  "aca-batch:GOVPAY_ACA_BATCH_VERSION"
  "console-api:GOVPAY_CONSOLE_API_VERSION"
  "fdr-batch:GOVPAY_FDR_BATCH_VERSION"
  "iban-batch:GOVPAY_IBAN_BATCH_VERSION"
  "maggioli-jppa:GOVPAY_MAGGIOLI_JPPA_VERSION"
  "notify-batch:GOVPAY_NOTIFY_BATCH_VERSION"
  "rt-batch:GOVPAY_RT_BATCH_VERSION"
  "tracciati-batch:GOVPAY_TRACCIATI_BATCH_VERSION"
)

GH_OWNER="${GH_OWNER:-link-it}"

# Immagini docker dei componenti: prefisso del repository e percorso interno
# dove i Dockerfile dei batch copiano lo SQL con COPY sql /opt/sql.
DOCKER="${DOCKER_BIN:-docker}"
DOCKER_DEV_PREFIX="${DOCKER_DEV_PREFIX:-linkitaly}"
DOCKER_SQL_PATH="${DOCKER_SQL_PATH:-/opt/sql}"

# Dialetti canonici, secondo src/main/resources/db/README.md
DIALETTI=(postgresql oracle mysql sqlserver hsql)

# File di creazione da includere. I nomi non sono uniformi fra i repository,
# quindi si cerca per funzione e non per nome esatto (vedi README.md).
FILE_CREAZIONE=(create-db.sql create.sql console-api-schema.sql)

# Schema dei metadati Spring Batch. Due forme, perche' i componenti sono passati
# a prenderlo da spring-batch-core ma le immagini in circolazione possono essere
# ancora quelle di prima:
#   upstream  sql/spring-batch/schema-<vendor>.sql, estratto verbatim dal jar dal
#             profilo maven "dist" e depositato in /opt/sql/spring-batch
#   legacy    sql/<dialetto>/tabelle_batch-create.sql, la copia che i repository
#             mantenevano a mano
# I nomi dei vendor sono quelli di upstream, quindi hsqldb e non hsql: la
# normalizzazione la fa gia' candidati_dialetto.
DIR_SPRING_BATCH="spring-batch"
FILE_SPRING_BATCH_LEGACY="tabelle_batch-create.sql"

# File che registra, nello sql.zip dei componenti passati al profilo "dist", le
# versioni di progetto, spring-batch e govpay-common a bordo dell'immagine.
FILE_VERSIONE="VERSION"

# File da NON includere mai in uno script di installazione: distruggono dati o
# servono ad altro. Elencati per essere segnalati, non ignorati in silenzio.
# Gli ultimi due arrivano da upstream dentro sql/spring-batch, perche' il profilo
# "dist" estrae schema-*.sql e migration/** senza distinguere: schema-drop-*.sql
# cancella le tabelle dei metadati, l'albero migration/ serve a chi aggiorna il
# framework su un database esistente, non a installarne uno nuovo.
FILE_ESCLUSI=(delete.sql delete-db.sql drop.sql drop-db.sql tabelle_batch-drop.sql
              spring-batch-cleanup.sql spring-batch-6.0-migration.sql utils.sql
              'schema-drop-<vendor>.sql' 'migration/**')

MODE=install
OUTDIR=""
WORKDIR=""
DOWNLOAD=true
DRYRUN=false

function usage() {
cat <<EOHELP
Usage: $(basename "$0") --core <versione> [--set <componente>=<versione>]... [opzioni]

Componenti riconosciuti:
$(for c in "${COMPONENTI[@]}"; do echo "  ${c%%:*}"; done)

Obbligatori:
  --core <v>         Versione di questo progetto. In modalita' upgrade deve
                     esistere sql/<dialetto>/patch/<v>.sql

Opzioni:
  --set <n>=<v>      Versione di un componente satellite. Ripetibile.
                     <v> puo' essere: il tag di un rilascio; branch:<nome>
                     per lo stato corrente di un branch; image:<rif> per
                     leggere lo SQL da un'immagine docker, dove <rif> e' un
                     riferimento completo oppure il solo tag, espanso in
                     <prefisso>/govpay-<nome>-dev:<tag>
  --mode <m>         install (default) usa la baseline gov_pay.sql del core,
                     upgrade usa la patch della versione indicata con --core,
                     both produce entrambi, componenti emette solo lo SQL dei
                     componenti senza il core. Serve dove il core e' gia'
                     applicato da altri, per esempio dall'installer
  --out <dir>        Directory di uscita (default: target/release-sql)
  --dialects <l>     Dialetti da produrre, separati da virgola
                     (default: tutti e cinque)
  --work <dir>       Directory di lavoro per gli scaricamenti
                     (default: una temporanea, rimossa all'uscita)
  --components <f>   File con le versioni dei componenti
                     (default: release-components.env accanto a questo script)
  --no-download      Non scarica: usa quanto gia' presente in --work. Serve a
                     rieseguire la composizione senza ripetere i download
  --dry-run          Elenca cosa userebbe, senza scrivere gli script
  -h, --help         Mostra questo aiuto

Variabili d'ambiente:
  GOVPAY_<NOME>_VERSION  alternativa a --set (es. GOVPAY_ACA_BATCH_VERSION)
  GH_OWNER               organizzazione GitHub (default: ${GH_OWNER})
  GH_TOKEN               facoltativo, solo per alzare il limite di richieste
                         dell'API GitHub: i repository sono pubblici
  DOCKER_BIN             comando docker (es. "sudo docker")
  DOCKER_DEV_PREFIX      prefisso delle immagini dev (default: ${DOCKER_DEV_PREFIX})
  DOCKER_SQL_PATH        percorso dello SQL nell'immagine (default: ${DOCKER_SQL_PATH})
EOHELP
}

function errore() { echo "Errore: $*" >&2; exit 1; }
function nota()   { echo "  $*"; }

# ── Argomenti ────────────────────────────────────────────────────────────────
while [[ $# -gt 0 ]]; do
  case "$1" in
    --core)        GOVPAY_CORE_VERSION="${2:-}"; shift 2 ;;
    --mode)        MODE="${2:-}"; shift 2 ;;
    --dialects)    IFS=',' read -r -a DIALETTI <<< "${2:-}"; shift 2 ;;
    --out)         OUTDIR="${2:-}"; shift 2 ;;
    --work)        WORKDIR="${2:-}"; shift 2 ;;
    --components)  COMPONENTS_ENV="${2:-}"; shift 2 ;;
    --no-download) DOWNLOAD=false; shift ;;
    --dry-run)     DRYRUN=true; shift ;;
    -h|--help)     usage; exit 0 ;;
    --set)
      spec="${2:-}"; shift 2
      [[ "${spec}" == *=* ]] || errore "--set vuole <componente>=<versione>, ricevuto '${spec}'"
      nome="${spec%%=*}"; ver="${spec#*=}"
      trovato=false
      for c in "${COMPONENTI[@]}"; do
        if [[ "${c%%:*}" == "${nome}" ]]; then
          printf -v "${c#*:}" '%s' "${ver}"; trovato=true; break
        fi
      done
      [[ "${trovato}" == true ]] || errore "componente sconosciuto: ${nome}"
      ;;
    *) errore "opzione sconosciuta: $1" ;;
  esac
done

# Il file usa la forma ${VAR:-valore}, quindi non sovrascrive cio' che e' gia'
# valorizzato: ne' l'ambiente ne' le opzioni --set gia' applicate sopra.
if [[ -n "${COMPONENTS_ENV}" && -f "${COMPONENTS_ENV}" ]]; then
  # shellcheck source=/dev/null
  source "${COMPONENTS_ENV}"
  echo "Composizione letta da ${COMPONENTS_ENV#${BASEDIR}/}"
elif [[ -n "${COMPONENTS_ENV}" && "${COMPONENTS_ENV}" != "${BASEDIR}/release-components.env" ]]; then
  errore "file dei componenti non trovato: ${COMPONENTS_ENV}"
fi

[[ -n "${GOVPAY_CORE_VERSION}" ]] || { usage >&2; errore "--core non indicata."; }
case "${MODE}" in install|upgrade|both|componenti) ;; *) errore "--mode deve valere install, upgrade, both o componenti" ;; esac
[[ -d "${CORE_SQL_DIR}" ]] || errore "SQL del core non trovato: ${CORE_SQL_DIR}"

OUTDIR="${OUTDIR:-$(cd "${BASEDIR}/../../../.." && pwd)/target/release-sql}"

if [[ -z "${WORKDIR}" ]]; then
  WORKDIR="$(mktemp -d)"
  trap 'rm -rf "${WORKDIR}"' EXIT
fi
mkdir -p "${WORKDIR}"

# ── Componenti effettivamente in rilascio ────────────────────────────────────
IN_RILASCIO=()
for c in "${COMPONENTI[@]}"; do
  nome="${c%%:*}"; var="${c#*:}"; ver="${!var}"
  [[ -n "${ver}" ]] && IN_RILASCIO+=("${nome}:${ver}")
done

echo "=============================================="
echo "Raccolta SQL del rilascio GovPay"
echo "  core:        ${GOVPAY_CORE_VERSION}"
echo "  modalita':   ${MODE}"
echo "  componenti:  ${#IN_RILASCIO[@]}"
for e in "${IN_RILASCIO[@]}"; do echo "               ${e%%:*} ${e#*:}"; done
echo "  uscita:      ${OUTDIR}"
echo "=============================================="

# ── Scaricamento degli sql.zip ───────────────────────────────────────────────
# "se presente": un componente senza asset sql.zip non e' un errore, non tutti
# ne hanno uno. Viene solo annotato, per non far sparire l'informazione.
SENZA_SQL=()
DA_BRANCH=()
DA_IMMAGINE=()

# I repository GovPay sono pubblici, quindi gli asset dei rilasci si scaricano
# in anonimo con curl: non serve ne' gh ne' un token. GH_TOKEN, se presente
# nell'ambiente, viene usato soltanto per alzare il limite di richieste
# dell'API, che in anonimo e' di 60 all'ora per indirizzo IP.
function curl_gh() {
  if [[ -n "${GH_TOKEN:-}" ]]; then
    curl -fsSL -H "Authorization: Bearer ${GH_TOKEN}" "$@"
  else
    curl -fsSL "$@"
  fi
}

if [[ "${DOWNLOAD}" == true && ${#IN_RILASCIO[@]} -gt 0 ]]; then
  # curl serve solo se qualche componente arriva da un rilascio o da un branch:
  # chi legge tutto dalle immagini non ha bisogno di rete verso GitHub.
  SERVE_CURL=false
  for e in "${IN_RILASCIO[@]}"; do
    [[ "${e#*:}" == image:* ]] || SERVE_CURL=true
  done
  if [[ "${SERVE_CURL}" == true ]]; then
    command -v curl >/dev/null || errore "curl non trovato: serve per scaricare gli asset (oppure --no-download)"
  fi
  command -v unzip >/dev/null || errore "unzip non trovato: serve per estrarre gli asset"
  command -v tar   >/dev/null || errore "tar non trovato: serve per estrarre gli archivi di branch"
  echo
  echo "-- Scaricamento asset sql.zip"
  for e in "${IN_RILASCIO[@]}"; do
    nome="${e%%:*}"; ver="${e#*:}"
    repo="govpay-${nome}"
    dest="${WORKDIR}/${nome}"
    mkdir -p "${dest}"
    # In sviluppo i rilasci dei componenti non esistono ancora, quindi la
    # versione puo' essere anche un branch, con la forma branch:<nome>: si
    # scarica l'archivio del branch e si legge lo SQL dall'albero sorgente.
    # E' l'unico modo per avere lo stato corrente di un componente non ancora
    # rilasciato, e non richiede token perche' i repository sono pubblici.
    # Terza forma: image:<riferimento>. Sull'agent Jenkins le immagini dei
    # componenti sono gia' presenti, e i Dockerfile dei batch copiano lo SQL
    # nell'immagine con COPY sql /opt/sql. E' la sorgente piu' fedele per lo
    # sviluppo: lo SQL e' quello che accompagna il binario in esecuzione, e la
    # versione e' esplicita nel tag, non lo stato mutevole di un branch.
    # Un riferimento senza / viene espanso come <prefisso>/govpay-<nome>-dev:<tag>.
    if [[ "${ver}" == image:* ]]; then
      img="${ver#image:}"
      [[ "${img}" == */* ]] || img="${DOCKER_DEV_PREFIX}/govpay-${nome}-dev:${img}"
      cid="$(${DOCKER} create "${img}" 2>/dev/null || true)"
      [[ -n "${cid}" ]] || errore "immagine non disponibile: ${img}"
      ${DOCKER} cp "${cid}:${DOCKER_SQL_PATH}" "${dest}/sql" >/dev/null 2>&1 || true
      ${DOCKER} rm -f "${cid}" >/dev/null 2>&1 || true
      if [[ -z "$(find "${dest}/sql" -name '*.sql' -print -quit 2>/dev/null)" ]]; then
        rm -rf "${dest}/sql"
        SENZA_SQL+=("${repo} ${img} (immagine senza SQL in ${DOCKER_SQL_PATH})")
        nota "${repo}: ${img} non contiene SQL in ${DOCKER_SQL_PATH}, componente saltato"
      else
        nota "${repo}: SQL letto da ${img}"
        DA_IMMAGINE+=("${repo} ${img}")
      fi
      continue
    fi

    if [[ "${ver}" == branch:* ]]; then
      ref="${ver#branch:}"
      if ! curl_gh -o /dev/null "https://api.github.com/repos/${GH_OWNER}/${repo}/branches/${ref}" 2>/dev/null; then
        errore "branch non trovato: ${GH_OWNER}/${repo} ${ref}. Verificare il valore in $(basename "${COMPONENTS_ENV}")."
      fi
      if ! curl_gh -o "${dest}/archivio.tar.gz" \
             "https://github.com/${GH_OWNER}/${repo}/archive/refs/heads/${ref}.tar.gz" 2>/dev/null; then
        errore "archivio del branch non scaricabile: ${GH_OWNER}/${repo} ${ref}"
      fi
      tar xzf "${dest}/archivio.tar.gz" -C "${dest}" --strip-components=1
      nota "${repo} branch ${ref}: archivio scaricato"
      DA_BRANCH+=("${repo} ${ref}")
      continue
    fi

    # Due casi da non confondere. Rilascio inesistente: e' un errore, tipicamente
    # una versione sbagliata nel file di composizione, e proseguire produrrebbe
    # uno script a cui manca un componente senza che si veda. Rilascio esistente
    # ma senza sql.zip: e' legittimo, non tutti i componenti ne hanno uno.
    if ! curl_gh -o /dev/null "https://api.github.com/repos/${GH_OWNER}/${repo}/releases/tags/${ver}" 2>/dev/null; then
      errore "rilascio non trovato: ${GH_OWNER}/${repo} ${ver}. Verificare la versione in $(basename "${COMPONENTS_ENV}")."
    fi
    if curl_gh -o "${dest}/sql.zip" \
         "https://github.com/${GH_OWNER}/${repo}/releases/download/${ver}/sql.zip" 2>/dev/null; then
      unzip -q -o "${dest}/sql.zip" -d "${dest}"
      nota "${repo} ${ver}: sql.zip scaricato"
    else
      rm -f "${dest}/sql.zip"
      SENZA_SQL+=("${repo} ${ver}")
      nota "${repo} ${ver}: rilascio presente ma senza asset sql.zip, componente saltato"
    fi
  done
fi

# ── Normalizzazione dei nomi dei dialetti ────────────────────────────────────
# I batch usano hsqldb dove il canonico e' hsql, e mariadb condivide gli script
# di mysql. La normalizzazione e' solo in lettura: vedi README.md.
function candidati_dialetto() {
  local dialetto="$1"
  echo "${dialetto}"
  [[ "${dialetto}" == hsql  ]] && echo hsqldb
  [[ "${dialetto}" == mysql ]] && echo mariadb
  return 0
}

# Vero se il componente ha almeno una directory per questo dialetto.
function ha_dialetto() {
  local base="$1" dialetto="$2" d
  while read -r d; do
    [[ -d "${base}/${d}" ]] && return 0
  done < <(candidati_dialetto "${dialetto}")
  return 1
}

# Cerca un file fra le directory candidate del dialetto. Cerca il file e non la
# directory: in almeno un repository esiste una directory hsql accanto a
# hsqldb, e fissare la directory per nome faceva sparire il contenuto vero.
function trova_file() {
  local base="$1" dialetto="$2" nome="$3" d
  while read -r d; do
    [[ -f "${base}/${d}/${nome}" ]] && { echo "${base}/${d}/${nome}"; return 0; }
  done < <(candidati_dialetto "${dialetto}")
  return 1
}

# Schema dei metadati Spring Batch di un componente per un dialetto: prima la
# forma upstream, poi la copia legacy, cosi' che un componente che avesse
# entrambe usi quella autorevole. Non restituisce il percorso sullo standard
# output ma lo scrive in SB_FILE, insieme all'origine in SB_ORIGINE: chiamarla
# in una sostituzione di comando ne perderebbe gli effetti, che avvengono in una
# subshell.
SB_FILE=""
SB_ORIGINE=""
function trova_schema_batch() {
  local base="$1" dialetto="$2" d
  SB_FILE=""; SB_ORIGINE=""
  while read -r d; do
    if [[ -f "${base}/${DIR_SPRING_BATCH}/schema-${d}.sql" ]]; then
      SB_FILE="${base}/${DIR_SPRING_BATCH}/schema-${d}.sql"
      SB_ORIGINE="schema-${d}.sql estratto da spring-batch-core"
      return 0
    fi
  done < <(candidati_dialetto "${dialetto}")
  if SB_FILE="$(trova_file "${base}" "${dialetto}" "${FILE_SPRING_BATCH_LEGACY}" 2>/dev/null)"; then
    SB_ORIGINE="${FILE_SPRING_BATCH_LEGACY} mantenuto nel repository"
    return 0
  fi
  SB_FILE=""
  return 1
}

function radice_sql() {
  # Due forme possibili. Da un asset sql.zip: i batch zippano
  # src/main/resources/sql, quindi dentro c'e' sql/<dialetto>, e console-api
  # zippa src/main/resources/db producendo la stessa forma. Da un archivio di
  # branch: l'albero sorgente completo, dove la radice e' piu' in profondita'.
  local dest="$1" d
  for d in sql db/sql src/main/resources/sql src/main/resources/db/sql; do
    [[ -d "${dest}/${d}" ]] && { echo "${dest}/${d}"; return 0; }
  done
  return 1
}

# ── Versioni degli script a bordo dei componenti ─────────────────────────────
# I componenti passati al profilo maven "dist" portano in sql/VERSION le versioni
# di progetto, spring-batch e govpay-common con cui lo SQL e' stato prodotto.
# Quella di spring-batch e' l'informazione che serve qui: con lo schema preso da
# upstream, due componenti hanno lo stesso schema dei metadati se e solo se hanno
# la stessa versione della libreria. Finisce nell'intestazione dello script.
VERSIONI_SQL=()
SB_VERSIONI=()
for e in "${IN_RILASCIO[@]}"; do
  nome="${e%%:*}"
  base="$(radice_sql "${WORKDIR}/${nome}" 2>/dev/null)" || continue
  [[ -f "${base}/${FILE_VERSIONE}" ]] || continue
  # La riga con la versione del progetto stesso e' gia' nell'elenco dei
  # componenti, quindi si tiene solo il resto, su una riga sola.
  riepilogo="$(grep -v '^#' "${base}/${FILE_VERSIONE}" | grep -v '^[[:space:]]*$' \
                 | grep -v "^govpay-${nome}=" | paste -sd, - | sed 's/,/, /g')"
  [[ -n "${riepilogo}" ]] && VERSIONI_SQL+=("govpay-${nome}: ${riepilogo}")
  v="$(sed -n 's/^spring-batch=//p' "${base}/${FILE_VERSIONE}" | head -1)"
  [[ -n "${v}" ]] && SB_VERSIONI+=("${v}")
done

# Piu' di una versione di spring-batch fra i componenti significa schemi dei
# metadati potenzialmente diversi. Va detto prima di comporre, non scoperto
# dall'eventuale divergenza di contenuto piu' sotto.
if [[ ${#SB_VERSIONI[@]} -gt 0 ]]; then
  SB_VERSIONI_UNICHE="$(printf '%s\n' "${SB_VERSIONI[@]}" | sort -u | tr '\n' ' ')"
  if [[ "$(printf '%s\n' "${SB_VERSIONI[@]}" | sort -u | wc -l)" -gt 1 ]]; then
    echo "  ATTENZIONE: i componenti dichiarano versioni diverse di spring-batch: ${SB_VERSIONI_UNICHE}" >&2
  fi
fi

# ── Composizione ─────────────────────────────────────────────────────────────
function intestazione() {
  local dialetto="$1" modo="$2"
  echo "-- Script SQL complessivo del rilascio GovPay ${GOVPAY_CORE_VERSION}"
  echo "-- Dialetto: ${dialetto}    Modalita': ${modo}"
  echo "--"
  echo "-- Generato da src/main/resources/db/collect-release-sql.sh."
  echo "-- Non modificare a mano: rigenerare dai componenti del rilascio."
  echo "--"
  echo "-- Componenti inclusi:"
  if [[ "${modo}" == componenti ]]; then
    echo "--   govpay (core) ${GOVPAY_CORE_VERSION}: NON incluso, va applicato a parte"
  else
    echo "--   govpay (core) ${GOVPAY_CORE_VERSION}"
  fi
  for e in "${IN_RILASCIO[@]}"; do echo "--   govpay-${e%%:*} ${e#*:}"; done
  if [[ ${#VERSIONI_SQL[@]} -gt 0 ]]; then
    echo "--"
    echo "-- Versioni degli script a bordo dei componenti (sql/VERSION):"
    for v in "${VERSIONI_SQL[@]}"; do echo "--   ${v}"; done
  fi
  if [[ ${#DA_IMMAGINE[@]} -gt 0 ]]; then
    echo "--"
    echo "-- Componenti il cui SQL e' stato letto dall'immagine docker:"
    for b in "${DA_IMMAGINE[@]}"; do echo "--   ${b}"; done
  fi
  if [[ ${#DA_BRANCH[@]} -gt 0 ]]; then
    echo "--"
    echo "-- ATTENZIONE: questo script NON e' riproducibile. I componenti seguenti"
    echo "-- sono stati presi dallo stato corrente di un branch e non da un rilascio,"
    echo "-- quindi rigenerandolo domani il contenuto puo' essere diverso:"
    for b in "${DA_BRANCH[@]}"; do echo "--   ${b}"; done
  fi
  if [[ ${#SENZA_SQL[@]} -gt 0 ]]; then
    echo "--"
    echo "-- Componenti del rilascio senza asset sql.zip, quindi non rappresentati qui:"
    for s in "${SENZA_SQL[@]}"; do echo "--   ${s}"; done
  fi
  echo ""
}

function sezione() {
  echo ""
  echo "-- ============================================================"
  echo "-- $*"
  echo "-- ============================================================"
  echo ""
}

function componi() {
  local dialetto="$1" modo="$2" out="$3"
  local core_file

  # In modalita' componenti il core non viene emesso: lo applica chi ha gia'
  # eseguito l'installer, e ripeterlo qui duplicherebbe lo schema.
  if [[ "${modo}" == componenti ]]; then
    intestazione "${dialetto}" "${modo}" > "${out}"
    if [[ ${#IN_RILASCIO[@]} -eq 0 ]]; then
      nota "${dialetto}/${modo}: nessun componente in rilascio, niente da comporre"
      return 1
    fi
  else
    if [[ "${modo}" == install ]]; then
      core_file="${CORE_SQL_DIR}/${dialetto}/gov_pay.sql"
    else
      core_file="${CORE_SQL_DIR}/${dialetto}/patch/${GOVPAY_CORE_VERSION}.sql"
    fi
    if [[ ! -f "${core_file}" ]]; then
      nota "${dialetto}/${modo}: manca ${core_file#${BASEDIR}/}, dialetto saltato"
      return 1
    fi
    {
      intestazione "${dialetto}" "${modo}"
      sezione "govpay (core) ${GOVPAY_CORE_VERSION} — $(basename "${core_file}")"
      cat "${core_file}"
    } > "${out}"
  fi

  # Tabelle di Spring Batch, una volta sola: sono duplicate in ogni batch, ed
  # emetterle una volta per componente farebbe fallire il secondo CREATE TABLE.
  #
  # Non sono pero' tutte uguali. Il confronto e' fatto sul contenuto
  # normalizzato, perche' alcune copie differiscono solo per il newline finale
  # e sarebbe rumore; le differenze che restano sono reali, per esempio la
  # sequenza BATCH_JOB_SEQ rinominata BATCH_JOB_INSTANCE_SEQ da Spring Batch 6.
  # Viene inclusa la variante di maggioranza e le altre sono segnalate: dare a
  # un batch lo schema di un'altra versione del framework non puo' essere
  # una scelta silenziosa.
  local -A sb_conta=() sb_file=() sb_chi=() sb_origine=()
  local hash f base d
  for e in "${IN_RILASCIO[@]}"; do
    nome="${e%%:*}"
    base="$(radice_sql "${WORKDIR}/${nome}" 2>/dev/null)" || continue
    trova_schema_batch "${base}" "${dialetto}" || continue
    f="${SB_FILE}"
    # awk termina sempre la riga in uscita, quindi normalizza anche l'assenza
    # del newline finale, che da sola faceva divergere l'hash di copie identiche.
    hash="$(awk '{ sub(/[[:space:]]+$/, ""); if (length($0)) print }' "${f}" | md5sum | cut -d' ' -f1)"
    sb_conta["${hash}"]=$(( ${sb_conta["${hash}"]:-0} + 1 ))
    sb_file["${hash}"]="${f}"
    sb_chi["${hash}"]="${sb_chi["${hash}"]:-}${sb_chi["${hash}"]:+, }govpay-${nome}"
    sb_origine["${hash}"]="${SB_ORIGINE}"
  done

  if [[ ${#sb_conta[@]} -gt 0 ]]; then
    local vincente="" max=-1 h
    for h in "${!sb_conta[@]}"; do
      if (( sb_conta["${h}"] > max )); then max=${sb_conta["${h}"]}; vincente="${h}"; fi
    done
    { sezione "Tabelle di Spring Batch, condivise — ${sb_origine["${vincente}"]} (${sb_chi["${vincente}"]})"; cat "${sb_file["${vincente}"]}"; echo; } >> "${out}"
    for h in "${!sb_conta[@]}"; do
      [[ "${h}" == "${vincente}" ]] && continue
      echo "  ATTENZIONE ${dialetto}: ${sb_chi["${h}"]} dichiara uno schema dei metadati Spring Batch diverso dalla maggioranza (${sb_origine["${h}"]}); incluso quello di ${sb_chi["${vincente}"]}" >&2
      {
        echo "-- ATTENZIONE: ${sb_chi["${h}"]} dichiara uno schema dei metadati Spring Batch"
        echo "-- diverso da quello incluso sopra (${sb_origine["${h}"]}), e non e' stato"
        echo "-- aggiunto. Verificare la versione di Spring Batch attesa da quei componenti"
        echo "-- prima di applicare."
        # Origini diverse significa che il rilascio mescola componenti passati agli
        # schemi upstream e componenti che portano ancora la copia locale. Non e' una
        # divergenza fra versioni del framework ma fra artefatti: le immagini
        # anteriori al passaggio vanno ricostruite.
        if [[ "${sb_origine["${h}"]}" != "${sb_origine["${vincente}"]}" ]]; then
          echo "-- Le due varianti hanno origine diversa, quindi il rilascio mescola"
          echo "-- componenti che prendono lo schema da spring-batch-core e componenti che"
          echo "-- portano ancora la copia mantenuta a mano: ricostruire le immagini"
          echo "-- anteriori al passaggio agli schemi upstream."
        fi
        echo
      } >> "${out}"
    done
  elif [[ ${#IN_RILASCIO[@]} -gt 0 ]]; then
    # Prima lo schema stava nell'albero sorgente di ogni batch, quindi si trovava
    # sempre. Ora lo produce il profilo "dist" in fase di packaging: un componente
    # preso con branch:<nome> non ce l'ha, perche' nel sorgente non c'e' piu'.
    # Non emettere la sezione in silenzio sarebbe la trappola peggiore: lo script
    # sembrerebbe completo e i batch non troverebbero le tabelle dei metadati.
    echo "  ATTENZIONE ${dialetto}: nessun componente ha fornito lo schema dei metadati Spring Batch; se il rilascio comprende dei batch lo script e' incompleto" >&2
    {
      echo "-- ATTENZIONE: nessuno dei componenti di questo rilascio ha fornito lo schema"
      echo "-- dei metadati Spring Batch per ${dialetto}, quindi qui non c'e'. Se il rilascio"
      echo "-- comprende dei batch, questo script e' incompleto e loro non partiranno."
      echo "-- Lo schema e' prodotto dal profilo maven \"dist\" in fase di packaging e non"
      echo "-- sta piu' nell'albero sorgente: un componente indicato con branch:<nome> non"
      echo "-- lo porta. Usare image:<tag> o il tag di un rilascio."
      echo
    } >> "${out}"
  fi

  # Creazione delle strutture proprie dei componenti
  for e in "${IN_RILASCIO[@]}"; do
    nome="${e%%:*}"; ver="${e#*:}"
    base="$(radice_sql "${WORKDIR}/${nome}" 2>/dev/null)" || continue
    if ! ha_dialetto "${base}" "${dialetto}"; then
      nota "${dialetto}: govpay-${nome} non ha questo dialetto"
      continue
    fi
    local aggiunto=false cf
    for c in "${FILE_CREAZIONE[@]}"; do
      cf="$(trova_file "${base}" "${dialetto}" "${c}" 2>/dev/null)" || continue
      { sezione "govpay-${nome} ${ver} — ${c}"; cat "${cf}"; echo; } >> "${out}"
      aggiunto=true
    done
    if [[ "${aggiunto}" != true ]]; then
      nota "${dialetto}: govpay-${nome} non ha script di creazione propri"
    fi
  done

  return 0
}

mkdir -p "${OUTDIR}"
MODI=()
case "${MODE}" in
  install)    MODI=(install) ;;
  upgrade)    MODI=(upgrade) ;;
  both)       MODI=(install upgrade) ;;
  componenti) MODI=(componenti) ;;
esac

echo
echo "-- Composizione"
PRODOTTI=()
for modo in "${MODI[@]}"; do
  for dialetto in "${DIALETTI[@]}"; do
    out="${OUTDIR}/govpay-${GOVPAY_CORE_VERSION}-${modo}-${dialetto}.sql"
    if [[ "${DRYRUN}" == true ]]; then
      echo "  [dry-run] ${out}"
      continue
    fi
    if componi "${dialetto}" "${modo}" "${out}"; then
      nota "$(basename "${out}") ($(wc -l < "${out}") righe)"
      PRODOTTI+=("${out}")
    else
      rm -f "${out}"
    fi
  done
done

echo
echo "=============================================="
if [[ "${DRYRUN}" == true ]]; then
  echo "dry-run: nessuno script scritto"
else
  # Non produrre nulla e uscire con successo sarebbe una trappola in pipeline:
  # uno stage verde che non ha fatto niente. Il caso tipico e' --mode upgrade
  # con una versione -SNAPSHOT, per cui la patch non esiste ancora.
  if [[ ${#PRODOTTI[@]} -eq 0 ]]; then
    echo "Nessuno script prodotto." >&2
    errore "nessun dialetto ha prodotto uno script per il core ${GOVPAY_CORE_VERSION} in modalita' ${MODE}. In modalita' upgrade deve esistere sql/<dialetto>/patch/${GOVPAY_CORE_VERSION}.sql."
  fi
  echo "Script prodotti in ${OUTDIR}:"
  for p in "${PRODOTTI[@]}"; do echo "  $(basename "${p}")"; done
fi
if [[ ${#SENZA_SQL[@]} -gt 0 ]]; then
  echo
  echo "Componenti del rilascio senza asset sql.zip:"
  for s in "${SENZA_SQL[@]}"; do echo "  ${s}"; done
fi
echo "=============================================="
