#!/bin/bash
# Confronto tra le immagini docker presenti sull'host e quelle pubblicate sul
# registry, per ogni immagine dichiarata nel compose.
#
# Viene invocato prima e dopo la 'docker compose pull': il primo giro mostra
# quali immagini la pull deve aggiornare, il secondo quali sono rimaste
# disallineate perche' la pull non e' riuscita.
#
# Il digest remoto si legge dalla riga 'Digest:' dell'output di
# 'buildx imagetools inspect' e non con '--format', che la versione di buildx
# installata sull'host ignora.
#
# Lo script segnala e basta: non fa fallire la build.

DOCKER_COMPOSE_DIR="${DOCKER_COMPOSE_DIR:-/etc/govpay/docker}"
FASE="${1:-}"

cd "${DOCKER_COMPOSE_DIR}/${PROJECT_VERSION}" || exit 0

echo "Allineamento immagini docker ${FASE}"
for img in $(sudo docker compose config --images); do
  remoto=$(sudo docker buildx imagetools inspect "$img" | awk '/^Digest:/{print $2; exit}')
  locale=$(sudo docker image inspect --format '{{join .RepoDigests " "}}' "$img" 2>/dev/null)
  case " $locale " in
    *"@$remoto "*) echo "OK           $img" ;;
    *)             echo "DISALLINEATA $img (locale: ${locale:-assente}, registry: ${remoto:-non letto})" ;;
  esac
done

exit 0
