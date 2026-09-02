#!/bin/bash
#
# Costruisce, e opzionalmente pubblica, le immagini Docker di GovPay partendo
# dall'installer prodotto dal build Maven (src/main/resources/setup/target).
#
# E' il punto di ingresso usato dalla pipeline:
#   stage docker-dev  ->  --set dev      : solo l'immagine postgres, in govpay-dev
#   stage docker      ->  --set release  : postgres, oracle, mariadb, mysql e senza
#                                          db, in govpay
#
# I due insiemi vanno in repository distinti, quindi i tag non possono collidere e
# la versione compare nel tag cosi' com'e'. I suffissi per database sono quelli
# gia' in uso su Docker Hub:
#   senza db    <repo>:<versione>
#   postgresql  <repo>:<versione>_postgres
#   oracle      <repo>:<versione>_oracle
#   mariadb     <repo>:<versione>_mariadb
#   mysql       <repo>:<versione>_mysql
#
# Il login al registry NON e' fatto qui: lo esegue il chiamante, cosi' le
# credenziali restano fuori dallo script e dai log della pipeline.
#
set -euo pipefail

# build_image.sh usa percorsi relativi al contesto di build, quindi va eseguito
# con la directory corrente qui dentro. Lo spostamento avviene pero' solo dopo
# aver risolto il percorso dell'installer, che il chiamante indica rispetto alla
# propria directory: la pipeline lo passa relativo alla radice del workspace.
BASEDIR="$(cd "$(dirname "$0")" && pwd)"

DOCKER="${DOCKER_BIN:-docker}"

VERSION=
INSTALLER=
IMAGE_SET=
IMAGE_BASE=
PUSH=false
LATEST=false
DRYRUN=false

function usage() {
cat <<EOHELP
Usage: $(basename "$0") --version <versione> --installer <file.tgz> --set dev|release [opzioni]

Obbligatori:
  --version <v>      Versione del prodotto, usata per il tag e per i path interni
                     all'immagine (es. 3.10.0-SNAPSHOT oppure 3.10.0)
  --installer <f>    Installer binario da cui costruire le immagini. Il nome del
                     file deve essere govpay-installer-<versione>.tgz
  --set dev|release  Insieme di immagini da produrre:
                       dev     -> postgresql
                       release -> postgresql, oracle, mariadb, mysql, senza db

Opzioni:
  --image-base <r>   Repository delle immagini. Se non indicato dipende
                     dall'insieme: <prefisso>/govpay-dev per dev,
                     <prefisso>/govpay per release, con prefisso preso da
                     REGISTRY_PREFIX (default: linkitaly)
  --push             Pubblica le immagini prodotte. Il login al registry deve
                     essere gia' stato eseguito dal chiamante
  --latest           Aggiunge il tag :latest all'immagine senza db (solo release)
  --dry-run          Stampa i comandi senza eseguirli
  -h, --help         Mostra questo aiuto

Variabili d'ambiente:
  DOCKER_BIN         Comando docker da usare (es. "sudo docker")
  REGISTRY_PREFIX    Prefisso del repository, usato se --image-base non e' indicato
EOHELP
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --version)    VERSION="${2:-}"; shift 2 ;;
    --installer)  INSTALLER="${2:-}"; shift 2 ;;
    --set)        IMAGE_SET="${2:-}"; shift 2 ;;
    --image-base) IMAGE_BASE="${2:-}"; shift 2 ;;
    --push)       PUSH=true; shift ;;
    --latest)     LATEST=true; shift ;;
    --dry-run)    DRYRUN=true; shift ;;
    -h|--help)    usage; exit 0 ;;
    *)            echo "Opzione sconosciuta: $1" >&2; usage >&2; exit 1 ;;
  esac
done

[[ -z "${VERSION}" ]]   && { echo "Errore: --version non indicata." >&2; exit 1; }
[[ -z "${INSTALLER}" ]] && { echo "Errore: --installer non indicato." >&2; exit 1; }

# Le immagini di sviluppo vanno in un repository dedicato, separato da quello
# delle release: sono due cataloghi distinti e i tag non possono collidere,
# quindi la versione finisce nel tag cosi' com'e', senza marcatori aggiuntivi.
# Il default dipende dall'insieme, cosi' un'invocazione manuale che dimentica
# --image-base non pubblica per sbaglio le immagini di sviluppo fra le release.
case "${IMAGE_SET}" in
  dev)     DBS=(postgresql)
           IMAGE_BASE="${IMAGE_BASE:-${REGISTRY_PREFIX:-linkitaly}/govpay-dev}" ;;
  release) DBS=(postgresql oracle mariadb mysql '')
           IMAGE_BASE="${IMAGE_BASE:-${REGISTRY_PREFIX:-linkitaly}/govpay}" ;;
  *)       echo "Errore: --set deve valere dev oppure release (valore: '${IMAGE_SET}')." >&2; exit 1 ;;
esac

if [[ ! -f "${INSTALLER}" ]]; then
  echo "Errore: installer non trovato: ${INSTALLER}" >&2
  exit 1
fi

# Il Dockerfile.daFile estrae l'archivio per nome (ADD govpay-installer-${GOVPAY_FULLVERSION}.tgz),
# quindi un nome file non allineato alla versione fallirebbe dentro il build docker
# con un errore poco leggibile. Meglio intercettarlo qui.
ATTESO="govpay-installer-${VERSION}.tgz"
if [[ "$(basename "${INSTALLER}")" != "${ATTESO}" ]]; then
  echo "Errore: il nome dell'installer non corrisponde alla versione richiesta." >&2
  echo "        atteso:  ${ATTESO}" >&2
  echo "        trovato: $(basename "${INSTALLER}")" >&2
  exit 1
fi

if [[ "${LATEST}" == true && "${IMAGE_SET}" != release ]]; then
  echo "Errore: --latest e' applicabile solo a --set release, perche' il tag :latest" >&2
  echo "        viene derivato dall'immagine senza db, che l'insieme dev non produce." >&2
  exit 1
fi

# Percorso assoluto, risolto finche' la directory corrente e' ancora quella del
# chiamante, e solo dopo lo spostamento nel contesto di build.
INSTALLER="$(cd "$(dirname "${INSTALLER}")" && pwd)/$(basename "${INSTALLER}")"

cd "${BASEDIR}"

function tagFor() {
  local db="${1:-}"
  case "${db:-hsql}" in
    hsql)       echo "${IMAGE_BASE}:${VERSION}" ;;
    postgresql) echo "${IMAGE_BASE}:${VERSION}_postgres" ;;
    *)          echo "${IMAGE_BASE}:${VERSION}_${db}" ;;
  esac
}

function run() {
  if [[ "${DRYRUN}" == true ]]; then
    echo "[dry-run] $*"
  else
    "$@"
  fi
}

echo "=============================================="
echo "Build immagini GovPay"
echo "  versione:  ${VERSION}"
echo "  insieme:   ${IMAGE_SET}"
echo "  installer: ${INSTALLER}"
echo "  registry:  ${IMAGE_BASE}"
echo "  push:      ${PUSH}"
echo "  latest:    ${LATEST}"
echo "=============================================="

IMMAGINI=()
for db in "${DBS[@]}"; do
  TAG="$(tagFor "${db}")"
  echo "--- ${TAG} (database: ${db:-senza db})"
  OPTS=(-t "${TAG}" -v "${VERSION}" -l "${INSTALLER}")
  [[ -n "${db}" ]] && OPTS+=(-d "${db}")
  run ./build_image.sh "${OPTS[@]}"
  IMMAGINI+=("${TAG}")
done

if [[ "${LATEST}" == true ]]; then
  echo "--- tag ${IMAGE_BASE}:latest"
  run ${DOCKER} tag "${IMAGE_BASE}:${VERSION}" "${IMAGE_BASE}:latest"
  IMMAGINI+=("${IMAGE_BASE}:latest")
fi

if [[ "${PUSH}" == true ]]; then
  for img in "${IMMAGINI[@]}"; do
    echo "--- push ${img}"
    run ${DOCKER} push "${img}"
  done
fi

echo "=============================================="
echo "Immagini prodotte:"
for img in "${IMMAGINI[@]}"; do
  echo "  ${img}"
done
echo "=============================================="
