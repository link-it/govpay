#!/bin/bash
# Esternalizza Config.js della console GovPay sul volume /etc/govpay e ripunta index.html.
# La dir esterna viene montata DENTRO il context /govpay-console (non un context separato),
# cosi' la URL resta sotto /govpay-console/static/... ed e' compatibile con reverse proxy
# che gia' inoltrano /govpay-console/ (nessuna regola proxy aggiuntiva richiesta).
#
#   Volume:     /etc/govpay/static/govpay/web-console/assets/Config.js   (seed da war se assente)
#   Mount:      DirResourceSet base=/etc/govpay/static -> webAppMount /static (nel context console)
#   URL:        /govpay-console/static/govpay/web-console/assets/Config.js
#   index.html: <script src="static/govpay/web-console/assets/Config.js">  (relativo al base href)
#   Descriptor: conf/Catalina/localhost/govpay-console.xml (mount dir esterna + override index.html)
#
# Bakata nell'immagine in /docker-entrypoint-govpay.d/ : eseguita dall'entrypoint prima
# dell'avvio di Tomcat. Disattivabile con GOVPAY_CONSOLE_EXTERNAL_CONFIG=false.
# Scritta senza 'exit'/'set -e' (logica in una funzione) per essere sicura anche se
# l'entrypoint la esegue con 'source'.

_govpay_console_external_config() {
    local GOVPAY_HOME="${GOVPAY_HOME:-/etc/govpay}"
    local STATIC_ROOT="${GOVPAY_HOME}/static"
    local CONSOLE_ASSETS_DIR="${STATIC_ROOT}/govpay/web-console/assets"
    local CONFIG_JS="${CONSOLE_ASSETS_DIR}/Config.js"

    # src RELATIVO in index.html: con <base href="/govpay-console/"> il browser lo risolve
    # in /govpay-console/static/... (path gia' inoltrato dal reverse proxy).
    local CONFIG_JS_SRC="static/govpay/web-console/assets/Config.js"

    # War / webapp esplosa della console
    local CONSOLE_WAR CONSOLE_EXPLODED
    CONSOLE_WAR=$(ls "${CATALINA_HOME}"/webapps/govpay-console*.war 2>/dev/null | head -1)
    CONSOLE_EXPLODED=$(ls -d "${CATALINA_HOME}"/webapps/govpay-console*/ 2>/dev/null | head -1)

    # Descriptor Tomcat del context console (il nome file = context path /govpay-console)
    local CTX_DIR="${CATALINA_HOME}/conf/Catalina/localhost"
    local CONSOLE_CTX="${CTX_DIR}/govpay-console.xml"

    # index.html modificato (file derivato: rigenerato ad ogni avvio, fuori dal volume)
    local OVERRIDE_DIR="${CATALINA_HOME}/conf/govpay-console-override"
    local OVERRIDE_INDEX="${OVERRIDE_DIR}/index.html"

    mkdir -p "${CONSOLE_ASSETS_DIR}" "${CTX_DIR}" "${OVERRIDE_DIR}"

    # --- 1. Seed Config.js dal war (solo se assente nel volume: dato utente, persiste) ---
    if [ -f "${CONFIG_JS}" ]; then
        echo "INFO: Config.js gia' presente nel volume: ${CONFIG_JS} (uso quello)."
    elif [ -n "${CONSOLE_WAR}" ] && [ -f "${CONSOLE_WAR}" ]; then
        echo "INFO: Estraggo assets/Config.js da ${CONSOLE_WAR} -> ${CONFIG_JS}"
        unzip -o -j "${CONSOLE_WAR}" "assets/Config.js" -d "${CONSOLE_ASSETS_DIR}"
    elif [ -n "${CONSOLE_EXPLODED}" ] && [ -f "${CONSOLE_EXPLODED}assets/Config.js" ]; then
        echo "INFO: Copio assets/Config.js dalla war esplosa -> ${CONFIG_JS}"
        cp -f "${CONSOLE_EXPLODED}assets/Config.js" "${CONFIG_JS}"
    else
        echo "WARN: govpay-console.war non trovata: seed di Config.js saltato."
    fi

    # --- 2. index.html: ripunta il Config.js sotto /govpay-console/static (derivato, ogni avvio) ---
    local INDEX_SRC=""
    if [ -n "${CONSOLE_WAR}" ] && [ -f "${CONSOLE_WAR}" ]; then
        unzip -p "${CONSOLE_WAR}" "index.html" > "${OVERRIDE_INDEX}.orig" 2>/dev/null
        [ -s "${OVERRIDE_INDEX}.orig" ] && INDEX_SRC="${OVERRIDE_INDEX}.orig"
    elif [ -n "${CONSOLE_EXPLODED}" ] && [ -f "${CONSOLE_EXPLODED}index.html" ]; then
        INDEX_SRC="${CONSOLE_EXPLODED}index.html"
    fi

    if [ -n "${INDEX_SRC}" ]; then
        sed -E \
            -e "s#src=\"assets/Config\.js\"#src=\"${CONFIG_JS_SRC}\"#g" \
            -e "s#src=\"@GOVPAY_CONFIG_JS_FILE_PATH@\"#src=\"${CONFIG_JS_SRC}\"#g" \
            "${INDEX_SRC}" > "${OVERRIDE_INDEX}"
        rm -f "${OVERRIDE_INDEX}.orig"
    else
        echo "WARN: index.html non trovato nel war: override index.html saltato."
    fi

    # --- 3. Context della console: monta la dir esterna sotto /static e sovrascrive index.html ---
    #        (nessun context /static separato -> tutto sotto /govpay-console/, proxy-friendly)
    {
        echo '<?xml version="1.0" encoding="UTF-8"?>'
        echo '<!-- Risorse statiche console GovPay dal volume, servite sotto /govpay-console/static/ -->'
        echo '<Context>'
        echo '    <Resources allowLinking="false">'
        echo "        <PreResources"
        echo "            className=\"org.apache.catalina.webresources.DirResourceSet\""
        echo "            base=\"${STATIC_ROOT}\""
        echo "            webAppMount=\"/static\""
        echo "            readOnly=\"true\" />"
        if [ -f "${OVERRIDE_INDEX}" ]; then
            echo "        <PreResources"
            echo "            className=\"org.apache.catalina.webresources.FileResourceSet\""
            echo "            base=\"${OVERRIDE_INDEX}\""
            echo "            webAppMount=\"/index.html\""
            echo "            readOnly=\"true\" />"
        fi
        echo '    </Resources>'
        echo '</Context>'
    } > "${CONSOLE_CTX}"
    echo "INFO: govpay-console.xml scritto: /govpay-console/static -> ${STATIC_ROOT}; index.html -> src=${CONFIG_JS_SRC}"

    # --- Permessi (tomcat e' nel gruppo 0) ---
    chmod -R g+rwX "${STATIC_ROOT}" "${OVERRIDE_DIR}" 2>/dev/null

    echo "INFO: Esternalizzazione console GovPay completata."
}

if [ "${GOVPAY_CONSOLE_EXTERNAL_CONFIG:-true}" = "true" ]; then
    _govpay_console_external_config
else
    echo "INFO: Esternalizzazione Config.js console disabilitata (GOVPAY_CONSOLE_EXTERNAL_CONFIG=${GOVPAY_CONSOLE_EXTERNAL_CONFIG})."
fi
