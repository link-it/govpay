#!/bin/bash
# Analisi di accessibilita' WCAG dei tre frontend con gov-a11y.
#
# Il tool e' esterno e black-box: pilota le applicazioni via Playwright, quindi
# GovPay non acquisisce alcuna dipendenza. I target (rotte, autenticazione,
# flussi) stanno nel repository di gov-a11y, sotto targets/govpay/, insieme a
# quelli degli altri prodotti: una sola fonte di verita' per lo schema.
#
# Le credenziali arrivano dall'ambiente, mai dai file di configurazione:
# gov-a11y le cerca in A11Y_<CHIAVE-TARGET-MAIUSCOLA>_USER / _PASS. Le chiavi
# dei target sono govpayConsole, govpayConsoleLegacy e govpayPortal, da cui i
# nomi sotto. La credenziale Jenkins e' una sola, di tipo "Username with
# password", che la pipeline espone come GOVPAY_CONSOLE_USR/_PSW: i due
# cruscotti condividono l'utenza dell'ambiente di prova.
# Il portale e' lato cittadino e il suo target non ha login a form: non gli
# serve alcuna credenziale.
export A11Y_GOVPAYCONSOLE_USER="${GOVPAY_CONSOLE_USR:-}"
export A11Y_GOVPAYCONSOLE_PASS="${GOVPAY_CONSOLE_PSW:-}"
export A11Y_GOVPAYCONSOLELEGACY_USER="${GOVPAY_CONSOLE_USR:-}"
export A11Y_GOVPAYCONSOLELEGACY_PASS="${GOVPAY_CONSOLE_PSW:-}"

A11Y_HOME="${GOV_A11Y_HOME:-/opt/gov-a11y}"
NODE="${NODE_BIN:-/opt/nodejs/22.14.0/bin/node}"
OUT="$(pwd)/target/a11y"

mkdir -p "${OUT}"

if [ ! -f "${A11Y_HOME}/a11y-scan.mjs" ]; then
  echo "gov-a11y non trovato in ${A11Y_HOME}: installarlo (git clone + npm ci) o valorizzare GOV_A11Y_HOME."
  exit 2
fi

scansiona() {
  nome="$1"
  base="$2"
  config="$3"
  echo "== accessibilita' su ${nome} (${base})"

  # --check-deps non scansiona: dice solo se le dipendenze opzionali richieste
  # da questa esecuzione (screen reader, lighthouse) sono installate. Senza, il
  # tool si ferma comunque, ma a meta' scansione e con un messaggio piu' oscuro.
  "${NODE}" "${A11Y_HOME}/a11y-scan.mjs" --check-deps \
    --config "${A11Y_HOME}/targets/govpay/${config}" || {
      echo "   dipendenze mancanti per ${nome}: salto la scansione."
      return 0
    }

  # --fail-on serious: il gate del tool. L'esito non ferma lo script, lo
  # raccoglie il Jenkinsfile dai report (junit + sarif).
  "${NODE}" "${A11Y_HOME}/a11y-scan.mjs" \
    --base "${base}" \
    --config "${A11Y_HOME}/targets/govpay/${config}" \
    --out "${OUT}/${nome}" \
    --product-version "${PROJECT_VERSION}" \
    --fail-on serious
  echo "   esito gov-a11y su ${nome}: $?"
}

scansiona console-attuale "${URL_CONSOLE_ATTUALE}" targets.govpay-console-legacy.json
scansiona console-nuova   "${URL_CONSOLE_NUOVA}"   targets.govpay-console.json
scansiona portale         "${URL_PORTALE}"         targets.govpay-portal.json

exit 0
