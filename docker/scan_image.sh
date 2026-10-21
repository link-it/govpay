#!/bin/bash
#
# Scansione delle vulnerabilita' di un'immagine Docker gia' costruita, con
# Trivy e Grype: sistema operativo di base, JRE e librerie dell'applicazione.
#
# Gate: blocca le vulnerabilita' CRITICAL e HIGH per cui esiste una versione
# corretta, al netto delle eccezioni in .trivyignore.yaml. Il gate e' di Trivy,
# che usa la severita' assegnata dalla distribuzione; Grype produce solo un
# report, come secondo parere: sui pacchetti Alpine usa la severita' NVD, che
# per gli stessi difetti e' spesso piu' alta, e farlo bloccare raddoppierebbe
# i falsi allarmi senza aggiungere difetti trovati.
# Le vulnerabilita' senza correzione non bloccano: non c'e' nulla da fare se
# non aspettare, e restano visibili nei report a ogni build.
#
# Lo script e' usato dalla pipeline (.github/actions/image-scan) e si esegue
# uguale in locale, prima di un push:
#
#   ./scan_image.sh linkitaly/govpay-dev:<versione>_postgres
#
# Trivy e Grype girano nei loro container ufficiali, a versione fissata: non
# c'e' nulla da installare, serve solo docker. I database delle vulnerabilita'
# sono tenuti in SCAN_CACHE_DIR, che la pipeline conserva fra le run.
#
# Variabili d'ambiente:
#   SCAN_OUT_DIR    directory dei report        (default: ./image-scan-report)
#   SCAN_CACHE_DIR  cache dei database          (default: ~/.cache/govpay-image-scan)
#   SCAN_GATE       block | warn                (default: block)
#                   warn e' la deroga temporanea descritta nella procedura:
#                   il gate viene valutato e riportato, ma non fa fallire.
#   TRIVY_IGNOREFILE, GRYPE_CONFIG  file delle eccezioni (default: quelli nella
#                   radice del repository)
#   DOCKER_BIN      comando docker              (default: docker)
#
# Uscita: 0 se il gate e' superato o in modalita' warn, 1 se il gate blocca,
# 2 per un errore dello script o degli strumenti.

set -euo pipefail

TRIVY_IMAGE="aquasec/trivy:0.75.0"
GRYPE_IMAGE="anchore/grype:v0.120.1"

IMAGE="${1:-}"
if [ -z "${IMAGE}" ]; then
  echo "Uso: $(basename "$0") <immagine>" >&2
  exit 2
fi

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$(mkdir -p "${SCAN_OUT_DIR:-./image-scan-report}" && cd "${SCAN_OUT_DIR:-./image-scan-report}" && pwd)"
CACHE="${SCAN_CACHE_DIR:-${HOME}/.cache/govpay-image-scan}"
GATE="${SCAN_GATE:-block}"
TRIVY_IGNOREFILE="${TRIVY_IGNOREFILE:-${REPO_ROOT}/.trivyignore.yaml}"
GRYPE_CONFIG="${GRYPE_CONFIG:-${REPO_ROOT}/.grype.yaml}"
DOCKER_BIN="${DOCKER_BIN:-docker}"

case "${GATE}" in
  block|warn) ;;
  *) echo "SCAN_GATE deve essere block o warn: '${GATE}'" >&2; exit 2 ;;
esac

mkdir -p "${CACHE}/trivy" "${CACHE}/grype"
rm -f "${OUT}"/trivy-* "${OUT}"/grype-* "${OUT}"/sbom-* "${OUT}"/gate.txt "${OUT}"/riepilogo.md

# I container degli strumenti girano come root e scrivono nei volumi montati:
# senza riassegnare i file, in locale la cache e i report resterebbero di root.
proprietario="$(id -u):$(id -g)"

trivy() {
  ${DOCKER_BIN} run --rm \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v "${CACHE}/trivy:/root/.cache/trivy" \
    -v "${OUT}:/out" \
    -v "$(dirname "${TRIVY_IGNOREFILE}"):/conf:ro" \
    "${TRIVY_IMAGE}" "$@"
}

grype() {
  ${DOCKER_BIN} run --rm \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v "${CACHE}/grype:/root/.cache/grype" \
    -v "${OUT}:/out" \
    -v "$(dirname "${GRYPE_CONFIG}"):/conf:ro" \
    "${GRYPE_IMAGE}" "$@"
}

# Un errore degli strumenti (immagine inesistente, database non scaricabile)
# deve uscire con 2: con set -e uscirebbe con il codice dello strumento, che
# per Trivy e Grype e' 1, indistinguibile da un gate non superato.
errore() { echo "Errore: $*" >&2; exit 2; }

ignorefile=()
[ -f "${TRIVY_IGNOREFILE}" ] && ignorefile=(--ignorefile "/conf/$(basename "${TRIVY_IGNOREFILE}")")
grypeconfig=()
[ -f "${GRYPE_CONFIG}" ] && grypeconfig=(--config "/conf/$(basename "${GRYPE_CONFIG}")")

echo "== Trivy ${TRIVY_IMAGE##*:} su ${IMAGE}"
# Una sola analisi dell'immagine: gli altri formati si ricavano dal report JSON
# con 'trivy convert', che non rilegge l'immagine. --list-all-pkgs serve allo
# SBOM, che deve elencare tutti i pacchetti e non solo quelli vulnerabili.
trivy image --quiet --scanners vuln --list-all-pkgs --format json -o /out/trivy-report.json \
  "${ignorefile[@]}" "${IMAGE}" || errore "scansione Trivy di ${IMAGE} non riuscita"
trivy convert --quiet --format sarif -o /out/trivy-report.sarif /out/trivy-report.json \
  || errore "conversione SARIF del report Trivy non riuscita"
trivy convert --quiet --format template --template "@/contrib/html.tpl" \
  -o /out/trivy-report.html /out/trivy-report.json || errore "conversione HTML del report Trivy non riuscita"
# SBOM dell'immagine: oltre alle dipendenze Maven contiene i pacchetti del
# sistema operativo e la JRE, che lo SBOM generato dal pom non vede.
trivy convert --quiet --format cyclonedx -o /out/sbom-image.cdx.json /out/trivy-report.json \
  || errore "generazione dello SBOM non riuscita"

echo "== Grype ${GRYPE_IMAGE##*:v} su ${IMAGE}"
# Anche qui una sola analisi, con tre formati in uscita.
grype "${grypeconfig[@]}" "${IMAGE}" -q \
  -o json=/out/grype-report.json -o sarif=/out/grype-report.sarif -o table=/out/grype-report.txt \
  || errore "scansione Grype di ${IMAGE} non riuscita"

${DOCKER_BIN} run --rm -v "${OUT}:/out" -v "${CACHE}:/cache" --entrypoint chown "${TRIVY_IMAGE}" \
  -R "${proprietario}" /out /cache >/dev/null 2>&1 || true

# Gate: CRITICAL e HIGH per cui esiste una versione corretta. Si calcola sul
# report JSON di Trivy, in cui le eccezioni di .trivyignore.yaml sono gia'
# state tolte dall'elenco delle vulnerabilita'. Uscita 1 se resta qualcosa,
# che qui non e' un errore dello script; l'elenco va in trivy-gate.md.
echo "== Gate: CRITICAL e HIGH con correzione disponibile"
set +e
python3 - "${OUT}" > "${OUT}/trivy-gate.md" <<'PY'
import json, sys
out = sys.argv[1]
t = json.load(open(f"{out}/trivy-report.json"))
righe = []
for r in t.get("Results", []):
    for v in r.get("Vulnerabilities") or []:
        if v["Severity"] in ("CRITICAL", "HIGH") and v.get("FixedVersion"):
            righe.append((v["Severity"], v["PkgName"], v.get("InstalledVersion", ""), v["FixedVersion"],
                          v["VulnerabilityID"], r.get("Target", "")))
righe.sort(key=lambda x: (x[0] != "CRITICAL", x[1], x[4]))
print("| Severita' | Pacchetto | Installata | Corretta in | Vulnerabilita' | Dove |")
print("|---|---|---|---|---|---|")
for sev, pkg, inst, fix, vid, tgt in righe:
    print(f"| {sev} | {pkg} | {inst} | {fix} | {vid} | {tgt} |")
sys.exit(1 if righe else 0)
PY
esito_gate=$?
set -e
[ "${esito_gate}" -le 1 ] || { echo "Calcolo del gate terminato con errore (${esito_gate})" >&2; exit 2; }

# Riepilogo per severita', dai due report completi.
python3 - "${OUT}" "${IMAGE}" "${esito_gate}" "${GATE}" > "${OUT}/riepilogo.md" <<'PY'
import json, sys, collections as C
out, image, esito, gate = sys.argv[1], sys.argv[2], int(sys.argv[3]), sys.argv[4]
sev = ["CRITICAL", "HIGH", "MEDIUM", "LOW", "UNKNOWN"]
t = json.load(open(f"{out}/trivy-report.json"))
tc = C.Counter(); tf = C.Counter()
for r in t.get("Results", []):
    cls = "sistema operativo" if r.get("Class") == "os-pkgs" else "librerie"
    for v in r.get("Vulnerabilities") or []:
        tc[(cls, v["Severity"])] += 1
        if v.get("FixedVersion"): tf[(cls, v["Severity"])] += 1
g = json.load(open(f"{out}/grype-report.json"))
gc = C.Counter()
for m in g.get("matches", []):
    tipo = {"apk": "sistema operativo", "deb": "sistema operativo", "rpm": "sistema operativo", "binary": "JRE e binari"}.get(m["artifact"]["type"], "librerie")
    gc[(tipo, m["vulnerability"]["severity"].upper())] += 1
os_name = (t.get("Metadata", {}).get("OS") or {})
print(f"### Scansione di `{image}`\n")
print(f"Sistema operativo: {os_name.get('Family', '?')} {os_name.get('Name', '?')}\n")
if esito == 0:
    print("**Gate superato**: nessuna vulnerabilita' CRITICAL o HIGH con correzione disponibile.\n")
elif gate == "warn":
    print("**Gate NON superato, in deroga** (`SCAN_GATE=warn`): l'immagine viene pubblicata comunque. Dettaglio in `trivy-gate.md`.\n")
else:
    print("**Gate NON superato**: ci sono vulnerabilita' CRITICAL o HIGH con correzione disponibile. L'immagine non viene pubblicata. Dettaglio in `trivy-gate.md`.\n")
print("| Strumento | Componente | " + " | ".join(s.capitalize() for s in sev) + " |")
print("|---|---|" + "---|" * len(sev))
for cls in ("sistema operativo", "librerie"):
    print(f"| Trivy | {cls} | " + " | ".join(f"{tc[(cls, s)]} ({tf[(cls, s)]} corr.)" if tc[(cls, s)] else "0" for s in sev) + " |")
for cls in ("sistema operativo", "JRE e binari", "librerie"):
    print(f"| Grype | {cls} | " + " | ".join(str(gc[(cls, s)]) for s in sev) + " |")
print("\n_corr. = con una versione corretta disponibile. Grype e' solo informativo._")
PY
cat "${OUT}/riepilogo.md"
if [ -n "${GITHUB_STEP_SUMMARY:-}" ]; then
  cat "${OUT}/riepilogo.md" >> "${GITHUB_STEP_SUMMARY}"
  if [ "${esito_gate}" -eq 1 ]; then
    { echo; echo "#### Vulnerabilita' che bloccano il gate"; echo; cat "${OUT}/trivy-gate.md"; } >> "${GITHUB_STEP_SUMMARY}"
  fi
fi

if [ "${esito_gate}" -eq 1 ]; then
  echo
  cat "${OUT}/trivy-gate.md"
  echo "gate=fallito" > "${OUT}/gate.txt"
  [ "${GATE}" = "warn" ] && exit 0
  exit 1
fi
echo "gate=superato" > "${OUT}/gate.txt"
exit 0
