#!/bin/bash
# Upload SBOM su Dependency-Track sotto un progetto padre gia' esistente.
#
# Adattamento GovPay dello script GovDesk upload-sbom-dependency-track.sh.
# Rispetto all'originale la gerarchia e' collassata: non ci sono i livelli
# root/group/parent da creare, ma un solo progetto padre che DEVE gia'
# esistere (da cui il nome "parent-required"). Nessun livello viene creato.
#
# Struttura attesa in Dependency-Track:
#   <PARENT_PROJECT>              (es. "GovPay-Componente", senza versione - deve gia' esistere)
#   |--- <PROJECT_NAME> <PROJECT_VERSION>
#        (es. "govpay-common 2.0.4", "govpay-console-api main")
#
# Uso:
#   ./upload-sbom-parent-required.sh <API_KEY> <FILE_SBOM> <PARENT_PROJECT>
#
# Nome e versione del progetto foglia non sono argomenti: vengono ricavati dai
# metadati dell'SBOM (metadata.component), e sono sovrascrivibili via ambiente
# per gestire i casi in cui la pipeline vuole imporre un valore diverso
# (es. versione = nome del branch invece della versione Maven -SNAPSHOT).
#
# Variabili d'ambiente opzionali:
#   DTRACK_URL           - URL di Dependency-Track (default: https://lab.link.it/dependency-track)
#   DTRACK_PROJECT_NAME  - forza il nome del progetto foglia (default: da SBOM)
#   DTRACK_VERSION       - forza la versione del progetto foglia (default: da SBOM)
#   DTRACK_LATEST        - se "true" marca la versione come 'latest' (default: false)
#   DTRACK_DEBUG         - se "true" stampa le risposte grezze delle API
#
# Permessi richiesti per l'API key:
#   - VIEW_PORTFOLIO           (per la lookup del progetto padre)
#   - PROJECT_CREATION_UPLOAD  (per creare la versione foglia)
#   - BOM_UPLOAD               (per l'upload dell'SBOM)
#   - PORTFOLIO_MANAGEMENT     (necessario solo se DTRACK_LATEST=true)

set -u

DTRACK_URL="${DTRACK_URL:-https://lab.link.it/dependency-track}"
DTRACK_DEBUG="${DTRACK_DEBUG:-false}"
LATEST="${DTRACK_LATEST:-false}"

if [ $# -lt 3 ]; then
    echo "Uso: $0 <API_KEY> <FILE_SBOM> <PARENT_PROJECT>"
    echo ""
    echo "Esempio:"
    echo "  $0 odt_xxxxx sbom/cyclonedx/bom.cdx.json GovPay-Componente"
    echo ""
    echo "Nome e versione del progetto foglia sono ricavati dai metadati dell'SBOM."
    echo "Per forzarli: DTRACK_PROJECT_NAME=... DTRACK_VERSION=... $0 ..."
    exit 1
fi

API_KEY="$1"
BOM_FILE="$2"
PARENT_NAME="$3"

if [ ! -f "$BOM_FILE" ]; then
    echo "[ERROR] File SBOM non trovato: $BOM_FILE"
    exit 1
fi

# --- Estrazione di nome e versione dai metadati dell'SBOM ---
# I valori passati via ambiente hanno la precedenza.
read_sbom_metadata() {
    BOM_FILE="$BOM_FILE" python3 - <<'PY'
import json, os, sys
try:
    with open(os.environ['BOM_FILE'], encoding='utf-8') as fh:
        data = json.load(fh)
except Exception as exc:
    print(f"__ERROR__{exc}")
    sys.exit(0)
comp = (data.get('metadata') or {}).get('component') or {}
print((comp.get('name') or '').strip())
print((comp.get('version') or '').strip())
PY
}

SBOM_META="$(read_sbom_metadata)"
case "$SBOM_META" in
    __ERROR__*)
        echo "[ERROR] SBOM non leggibile: ${SBOM_META#__ERROR__}"
        exit 1
        ;;
esac
SBOM_NAME="$(echo "$SBOM_META" | sed -n '1p')"
SBOM_VERSION="$(echo "$SBOM_META" | sed -n '2p')"

PROJECT_NAME="${DTRACK_PROJECT_NAME:-$SBOM_NAME}"
PROJECT_VERSION="${DTRACK_VERSION:-$SBOM_VERSION}"

if [ -z "$PROJECT_NAME" ]; then
    echo "[ERROR] Nome del progetto non determinabile: metadata.component.name assente nell'SBOM."
    echo "[HINT]  Impostare DTRACK_PROJECT_NAME per forzarlo."
    exit 1
fi
if [ -z "$PROJECT_VERSION" ]; then
    echo "[ERROR] Versione del progetto non determinabile: metadata.component.version assente nell'SBOM."
    echo "[HINT]  Impostare DTRACK_VERSION per forzarla."
    exit 1
fi

echo "Dependency-Track: $DTRACK_URL"
echo "Gerarchia: $PARENT_NAME / $PROJECT_NAME $PROJECT_VERSION"
echo "SBOM: $BOM_FILE ($(wc -c < "$BOM_FILE") bytes)"
if [ "$LATEST" = "true" ]; then
    echo "Latest: SI (la versione verra' marcata come 'latest')"
fi
echo ""

# URL-encode minimale per i nomi dei progetti (gestisce gli spazi).
urlencode() {
    python3 -c "import urllib.parse,sys; print(urllib.parse.quote(sys.argv[1], safe=''))" "$1"
}

# Stampa la response sull'errore per diagnostica.
debug_response() {
    if [ "$DTRACK_DEBUG" = "true" ]; then
        echo "[DEBUG] Response: $1" >&2
    fi
}

# Cerca nella lista JSON di progetti il primo con un dato nome e nessuna versione.
# Il nome e' passato via ambiente, non interpolato nel sorgente Python, per non
# rompersi su apici o caratteri speciali.
# Riceve nome e path del file JSON. Il nome passa per ambiente, non
# interpolato nel sorgente, per non rompersi su apici o caratteri speciali.
filter_first_by_name_no_version() {
    FILTER_NAME="$1" JSON_FILE="$2" python3 - <<'PY' 2>/dev/null
import json, os
name = os.environ['FILTER_NAME']
try:
    with open(os.environ['JSON_FILE'], encoding='utf-8') as fh:
        data = json.load(fh)
    # L'endpoint puo' restituire un array o un oggetto paginato {items:[...]}
    if isinstance(data, dict) and 'items' in data:
        data = data['items']
    if not isinstance(data, list):
        raise SystemExit
    for p in data:
        if p.get('name') == name and not p.get('version'):
            print(p.get('uuid', ''))
            break
except Exception:
    pass
PY
}

# Elenca i nomi dei progetti senza versione restituiti dalla API, per capire
# cosa vede effettivamente l'API key quando la lookup non trova nulla.
# Riceve il path di un file JSON (non stdin: il programma Python arriva da
# heredoc, quindi stdin non e' disponibile per i dati).
list_candidate_names() {
    JSON_FILE="$1" python3 - <<'PY' 2>/dev/null
import json, os
try:
    with open(os.environ['JSON_FILE'], encoding='utf-8') as fh:
        data = json.load(fh)
    if isinstance(data, dict) and 'items' in data:
        data = data['items']
    if not isinstance(data, list):
        raise SystemExit
    for n in sorted({p.get('name', '') for p in data if not p.get('version')}):
        if n:
            print(f"         - {n}")
except Exception:
    pass
PY
}

# Cerca il progetto padre per nome, fra quelli senza versione.
# A differenza dell'originale controlla lo stato HTTP, per non confondere
# "progetto assente" con "chiave non autorizzata" o "server irraggiungibile".
# NB: assegna le globali LOOKUP_HTTP, LOOKUP_BODY e PARENT_UUID. Va invocata
# direttamente e non in una command substitution, che girerebbe in una subshell
# e perderebbe le assegnazioni.
LOOKUP_BODY=""
LOOKUP_HTTP=""
PARENT_UUID=""
LOOKUP_FILE="$(mktemp)"
RESPONSE_FILE="$(mktemp)"
trap 'rm -f "$LOOKUP_FILE" "$RESPONSE_FILE"' EXIT
find_parent() {
    local name="$1"
    local enc resp
    enc=$(urlencode "$name")
    resp=$(curl -sk -H "X-Api-Key: $API_KEY" \
        -w "\n__HTTP__:%{http_code}" \
        "$DTRACK_URL/api/v1/project?name=$enc&excludeInactive=false")
    LOOKUP_HTTP=$(printf '%s' "$resp" | sed -n 's/^__HTTP__://p' | tail -1)
    LOOKUP_BODY=$(printf '%s' "$resp" | sed '$d')
    debug_response "HTTP=$LOOKUP_HTTP body=$LOOKUP_BODY"
    printf '%s' "$LOOKUP_BODY" > "$LOOKUP_FILE"
    PARENT_UUID=$(filter_first_by_name_no_version "$name" "$LOOKUP_FILE")
}

# --- 1) Lookup del progetto padre (deve gia' esistere) ---
echo "Ricerca progetto padre '$PARENT_NAME' ..."
find_parent "$PARENT_NAME"

if [ -z "$PARENT_UUID" ]; then
    case "$LOOKUP_HTTP" in
        000|"")
            echo "[ERROR] Nessuna risposta da $DTRACK_URL (HTTP '$LOOKUP_HTTP')."
            echo "[HINT]  Server irraggiungibile dal runner, oppure URL errato."
            ;;
        401)
            echo "[ERROR] API key non valida o non autorizzata (HTTP 401)."
            ;;
        403)
            echo "[ERROR] Permesso negato (HTTP 403): serve il permesso VIEW_PORTFOLIO."
            ;;
        200)
            echo "[ERROR] Progetto padre '$PARENT_NAME' non trovato (HTTP 200)."
            echo "[ERROR] La API key raggiunge il server ma non vede un progetto con"
            echo "[ERROR] questo nome esatto e senza versione."
            echo "[HINT]  Progetti senza versione visibili con questa chiave:"
            if list_candidate_names "$LOOKUP_FILE" | grep -q .; then
                list_candidate_names "$LOOKUP_FILE"
            else
                echo "         (nessuno: portfolio vuoto per questa chiave, probabile ACL)"
            fi
            echo "[HINT]  Verificare il nome esatto (maiuscole, trattini, spazi) e gli ACL."
            ;;
        *)
            echo "[ERROR] Lookup fallita con HTTP $LOOKUP_HTTP."
            echo "[ERROR] Body: $LOOKUP_BODY"
            ;;
    esac
    exit 1
fi
echo "[OK] Progetto padre trovato: $PARENT_UUID"

# --- 2) Upload SBOM come figlio del padre ---
echo ""
echo "Upload SBOM in corso ..."

FORM_LATEST=()
if [ "$LATEST" = "true" ]; then
    FORM_LATEST=(-F "isLatest=true")
fi

HTTP_CODE=$(curl -sk -o "$RESPONSE_FILE" -w "%{http_code}" \
    -X POST "$DTRACK_URL/api/v1/bom" \
    -H "X-Api-Key: $API_KEY" \
    -H "Content-Type: multipart/form-data" \
    -F "autoCreate=true" \
    -F "projectName=$PROJECT_NAME" \
    -F "projectVersion=$PROJECT_VERSION" \
    -F "parentUUID=$PARENT_UUID" \
    "${FORM_LATEST[@]}" \
    -F "bom=@$BOM_FILE")

if [ "$HTTP_CODE" = "200" ]; then
    echo "[OK] SBOM caricato con successo per $PROJECT_NAME $PROJECT_VERSION"
    echo "     Figlio del progetto padre $PARENT_NAME ($PARENT_UUID)"
    if [ "$LATEST" = "true" ]; then
        echo "     Marcata come 'latest'"
    fi
elif [ "$HTTP_CODE" = "401" ]; then
    echo "[ERROR] API key non valida o non autorizzata"
    exit 1
elif [ "$HTTP_CODE" = "403" ]; then
    echo "[ERROR] Permesso negato. Verificare che l'API key abbia i permessi:"
    echo "        VIEW_PORTFOLIO, PROJECT_CREATION_UPLOAD, BOM_UPLOAD"
    if [ "$LATEST" = "true" ]; then
        echo "[ERROR] Per DTRACK_LATEST=true serve anche PORTFOLIO_MANAGEMENT"
    fi
    cat "$RESPONSE_FILE" 2>/dev/null
    exit 1
else
    echo "[ERROR] Upload fallito con HTTP $HTTP_CODE"
    cat "$RESPONSE_FILE" 2>/dev/null
    echo ""
    exit 1
fi
