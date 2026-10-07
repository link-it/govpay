#!/bin/bash
# Analisi di sicurezza dei tre frontend con OWASP ZAP.
#
# Scansione passiva: ZAP percorre l'applicazione e giudica cio' che vede, senza
# attaccarla. Una scansione attiva su un ambiente condiviso con la testsuite ne
# altererebbe i dati e allungherebbe di molto la notturna: se servira', andra'
# su un ambiente suo.
#
# Ogni bersaglio ha il suo piano (Automation Framework) accanto a questo script.
# Il piano, invece dell'equivalente "zap-baseline.py -t <url>", serve a
# dichiarare l'ambito: il baseline costruisce il contesto dal solo host, e sul
# cruscotto attuale - servito dallo stesso Tomcat delle war di API - lo spider
# risaliva alla radice percorrendo /examples, /docs, /manager e /host-manager.
#
# L'immagine e' quella ufficiale: le vecchie owasp/zap2docker-* sono ritirate.
# Rete dell'host perche' i bersagli stanno su localhost; di conseguenza la porta
# del proxy di ZAP va spostata, altrimenti sbatte contro il Tomcat sull'8080.

ZAP_IMAGE="${ZAP_IMAGE:-ghcr.io/zaproxy/zaproxy:stable}"
ZAP_PORT="${ZAP_PORT:-18090}"
# Su Jenkins docker si invoca con sudo; in locale, per provare lo script,
# basta DOCKER_BIN=docker.
DOCKER_BIN="${DOCKER_BIN:-sudo docker}"

OUT="$(pwd)/target/zap"
PIANI="$(pwd)/src/main/resources/scripts/zap"

mkdir -p "${OUT}"
rm -f "${OUT}"/*
# ZAP gira come utente non root nel container e scrive in /zap/wrk.
chmod 777 "${OUT}"

scansiona() {
  nome="$1"
  echo "== ZAP su ${nome}"

  # Il timeout e il nome fisso vengono da una prova sul campo: il container non
  # sempre esce da solo dopo aver scritto i report, e uno scarto per nome e'
  # deterministico, a differenza di "quale container e' rimasto appeso".
  ${DOCKER_BIN} rm -f "zap-${nome}" >/dev/null 2>&1
  timeout --kill-after=60 900 ${DOCKER_BIN} run --rm --name "zap-${nome}" --network host \
    -v "${OUT}:/zap/wrk/:rw" \
    -v "${PIANI}:/zap/conf/:ro" \
    "${ZAP_IMAGE}" zap.sh -cmd -port "${ZAP_PORT}" -autorun "/zap/conf/${nome}.yaml"
  esito=$?
  ${DOCKER_BIN} rm -f "zap-${nome}" >/dev/null 2>&1
  echo "   esito ZAP su ${nome}: ${esito}"
}

scansiona console-legacy
scansiona console-nuova
scansiona portale

# Indice per publishHTML: i tre report sono file separati.
{
  echo "<!doctype html><meta charset='utf-8'><title>ZAP - GovPay ${PROJECT_VERSION}</title>"
  echo "<h1>OWASP ZAP - GovPay ${PROJECT_VERSION}</h1><ul>"
  for n in console-legacy console-nuova portale; do
    [ -f "${OUT}/${n}.html" ] && echo "<li><a href='${n}.html'>${n}</a></li>"
  done
  echo "</ul>"
} > "${OUT}/index.html"

exit 0
