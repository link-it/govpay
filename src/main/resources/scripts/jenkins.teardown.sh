#!/bin/bash
# Smontaggio dell'ambiente di prova: container, application server, mailhog.
#
# Estratto dal post dello stage 'test' per lasciare in piedi l'ambiente durante
# le analisi che hanno bisogno delle applicazioni vive (ZAP, accessibilita').
# Viene invocato due volte sul percorso felice - dallo stage 'teardown' e dal
# post della pipeline - quindi ogni comando tollera di trovare le cose gia'
# spente: e' la ragione dei '|| true'.
#
# Il post della pipeline e' la rete di sicurezza che prima era implicita nel
# post 'always' del test: senza, un fallimento negli stage di analisi
# lascerebbe acceso tutto fino alla build successiva.

DOCKER_COMPOSE_DIR="${DOCKER_COMPOSE_DIR:-/etc/govpay/docker}"

if [ -d "${DOCKER_COMPOSE_DIR}/${PROJECT_VERSION}" ]; then
  (cd "${DOCKER_COMPOSE_DIR}/${PROJECT_VERSION}" && sudo docker compose down -v) || true
fi

sudo systemctl stop \
  wildfly@govpay \
  wildfly-26.1.3.Final@standalone \
  wildfly-26.1.3.Final@ndpsym \
  wildfly-28.0.1.Final@ndpsym \
  tomcat_govpay \
  tomcat_govpay_jdk25 || true

sudo docker stop mailhog || true

exit 0
