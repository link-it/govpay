#!/bin/bash

exec 6<> /tmp/entrypoint_debug.log
exec 2>&6
set -x

## Const
GOVPAY_STARTUP_CHECK_SKIP=${GOVPAY_STARTUP_CHECK_SKIP:=FALSE}
GOVPAY_STARTUP_CHECK_FIRST_SLEEP_TIME=${GOVPAY_STARTUP_CHECK_FIRST_SLEEP_TIME:=20}
GOVPAY_STARTUP_CHECK_SLEEP_TIME=${GOVPAY_STARTUP_CHECK_SLEEP_TIME:=5}
GOVPAY_STARTUP_CHECK_MAX_RETRY=${GOVPAY_STARTUP_CHECK_MAX_RETRY:=60}

declare -r JVM_PROPERTIES_FILE='/etc/govpay_as_jvm.properties'
declare -r JVM_PROPERTIES_FILE_DEPRECATO='/etc/wildfly/wildfly.properties'
declare -r ENTRYPOINT_D='/docker-entrypoint-govpay.d/'
declare -r ENTRYPOINT_D_DEPRECATO='/docker-entrypoint-widlflycli.d/'
declare -r CUSTOM_INIT_FILE="${CATALINA_HOME}/conf/custom_govpay_as_init"
declare -r MODULE_INIT_FILE="${CATALINA_HOME}/conf/fix_module_init"
declare -r CONNETTORI_INIT_FILE="${CATALINA_HOME}/conf/fix_connettori_init"
case "${GOVPAY_DB_TYPE:-hsql}" in
mysql|mariadb|postgresql|oracle)

    #
    # Sanity check variabili minime attese
    #
    if [ -n "${GOVPAY_DB_SERVER}" -a -n  "${GOVPAY_DB_USER}" -a -n "${GOVPAY_DB_NAME}" ] 
    then
            [ -n "${GOVPAY_DB_PASSWORD}" ] || echo "WARN: La variabile GOVPAY_DB_PASSWORD non è stata impostata."
            echo "INFO: Sanity check variabili obbligatorie ... ok."
    else
        echo "FATAL: Sanity check variabili obbligatorie ... fallito."
        echo "FATAL: Devono essere settate almeno le seguenti variabili obbligatorie:
GOVPAY_DB_SERVER: ${GOVPAY_DB_SERVER}
GOVPAY_DB_NAME: ${GOVPAY_DB_NAME}
GOVPAY_DB_USER: ${GOVPAY_DB_USER}
"
        exit 1
    fi


    if [ -n "${GOVPAY_DS_JDBC_LIBS}" ] 
    then
        if [ ! -d "${GOVPAY_DS_JDBC_LIBS}" ]
        then
            echo "FATAL: Sanity check JDBC ... fallito."
            echo "FATAL: Il path alla directory che contiene il driver JDBC, non è leggibile o non è una directory: [GOVPAY_DS_JDBC_LIBS=${GOVPAY_DS_JDBC_LIBS}] "
            exit 1
        fi
    fi

    case "${GOVPAY_DB_TYPE:-hsql}" in
    postgresql)

        # ATTENZIONE la variabile GOVPAY_POSTGRESQL_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS.
        # se solo GOVPAY_POSTGRESQL_JDBC_PATH è valorizzata provo a mantenere la compatibilità usando il nome della directory
        # se nessuna delle due viene specificata si tratta di un errore per il db oracle
        # se sono valorizzate entrambe viene usata GOVPAY_DS_JDBC_LIBS
        if [ -n "${GOVPAY_POSTGRESQL_JDBC_PATH}" ]
        then
            echo "WARN: Sanity check JDBC ... La variabile GOVPAY_POSTGRESQL_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS."
            if [ -z "${GOVPAY_DS_JDBC_LIBS}" ]
            then
                export GOVPAY_DS_JDBC_LIBS="$(dirname ${GOVPAY_POSTGRESQL_JDBC_PATH})"
                #export GOVPAY_DRIVER_JDBC="${GOVPAY_DS_JDBC_LIBS}"
            else
                echo "WARN: Recupero librerie per il driver jdbc da [GOVPAY_DS_JDBC_LIBS=${GOVPAY_DS_JDBC_LIBS}]."
            fi
        elif [ -z "${GOVPAY_POSTGRESQL_JDBC_PATH}" -a -z "${GOVPAY_DS_JDBC_LIBS}" ]
        then
            echo "FATAL: Sanity check JDBC ... fallito."
            echo "FATAL: Il path alla directory che contiene il driver JDBC, deve essere indicato tramite la variabile GOVPAY_DS_JDBC_LIBS "
            exit 1
        fi

        export GOVPAY_DS_DRIVER_CLASS='org.postgresql.Driver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

    ;;
    mysql)
        # ATTENZIONE la variabile GOVPAY_MYSQL_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS.
        # se solo GOVPAY_MYSQL_JDBC_PATH è valorizzata provo a mantenere la compatibilità usando il nome della directory
        # se nessuna delle due viene specificata si tratta di un errore per il db oracle
        # se sono valorizzate entrambe viene usata GOVPAY_DS_JDBC_LIBS
        if [ -n "${GOVPAY_MYSQL_JDBC_PATH}" ]
        then
            echo "WARN: Sanity check JDBC ... La variabile GOVPAY_MYSQL_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS."
            if [ -z "${GOVPAY_DS_JDBC_LIBS}" ]
            then
                export GOVPAY_DS_JDBC_LIBS="$(dirname ${GOVPAY_MYSQL_JDBC_PATH})"
                #export GOVPAY_DRIVER_JDBC="${GOVPAY_DS_JDBC_LIBS}"
            else
                echo "WARN: Recupero librerie per il driver jdbc da [GOVPAY_DS_JDBC_LIBS=${GOVPAY_DS_JDBC_LIBS}]."
            fi
        elif [ -z "${GOVPAY_MYSQL_JDBC_PATH}" -a -z "${GOVPAY_DS_JDBC_LIBS}" ]
        then
            echo "FATAL: Sanity check JDBC ... fallito."
            echo "FATAL: Il path alla directory che contiene il driver JDBC, deve essere indicato tramite la variabile GOVPAY_DS_JDBC_LIBS "
            exit 1
        fi
        if [ -n "${GOVPAY_DS_CONN_PARAM}" ]
        then
            GOVPAY_DS_CONN_PARAM="${GOVPAY_DS_CONN_PARAM}&zeroDateTimeBehavior=convertToNull"
        else
            GOVPAY_DS_CONN_PARAM='?zeroDateTimeBehavior=convertToNull'
        fi
        export GOVPAY_DS_DRIVER_CLASS='com.mysql.cj.jdbc.Driver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.MySQL57Dialect
    ;;
    mariadb)
        # ATTENZIONE la variabile GOVPAY_MARIADB_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS.
        # se solo GOVPAY_MARIADB_JDBC_PATH è valorizzata provo a mantenere la compatibilità usando il nome della directory
        # se nessuna delle due viene specificata si tratta di un errore per il db oracle
        # se sono valorizzate entrambe viene usata GOVPAY_DS_JDBC_LIBS
        if [ -n "${GOVPAY_MARIADB_JDBC_PATH}" ]
        then
            echo "WARN: Sanity check JDBC ... La variabile GOVPAY_MARIADB_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS."
            if [ -z "${GOVPAY_DS_JDBC_LIBS}" ]
            then
                export GOVPAY_DS_JDBC_LIBS="$(dirname ${GOVPAY_MARIADB_JDBC_PATH})"
                #export GOVPAY_DRIVER_JDBC="${GOVPAY_DS_JDBC_LIBS}"
            else
                echo "WARN: Recupero librerie per il driver jdbc da [GOVPAY_DS_JDBC_LIBS=${GOVPAY_DS_JDBC_LIBS}]."
            fi
        elif [ -z "${GOVPAY_MARIADB_JDBC_PATH}" -a -z "${GOVPAY_DS_JDBC_LIBS}" ]
        then
            echo "FATAL: Sanity check JDBC ... fallito."
            echo "FATAL: Il path alla directory che contiene il driver JDBC, deve essere indicato tramite la variabile GOVPAY_DS_JDBC_LIBS "
            exit 1
        fi
        if [ -n "${GOVPAY_DS_CONN_PARAM}" ]
        then
            GOVPAY_DS_CONN_PARAM="${GOVPAY_DS_CONN_PARAM}&zeroDateTimeBehavior=convertToNull"
        else
            GOVPAY_DS_CONN_PARAM='?zeroDateTimeBehavior=convertToNull'
        fi
        export GOVPAY_DS_DRIVER_CLASS='org.mariadb.jdbc.Driver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.MySQL57Dialect
    ;;

    oracle)
        # ATTENZIONE la variabile GOVPAY_ORACLE_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS.
        # se solo GOVPAY_ORACLE_JDBC_PATH è valorizzata provo a mantenere la compatibilità usando il nome della directory 
        # se nessuna delle due viene specificata si tratta di un errore per il db oracle
        # se sono valorizzate entrambe viene usata GOVPAY_DS_JDBC_LIBS
        if [ -n "${GOVPAY_ORACLE_JDBC_PATH}" ]
        then
            echo "WARN: Sanity check JDBC ... La variabile GOVPAY_ORACLE_JDBC_PATH è stata deprecata in favore di GOVPAY_DS_JDBC_LIBS."
            if [ -z "${GOVPAY_DS_JDBC_LIBS}" ]
            then
                export GOVPAY_DS_JDBC_LIBS="$(dirname ${GOVPAY_ORACLE_JDBC_PATH})"
                export GOVPAY_DRIVER_JDBC="${GOVPAY_DS_JDBC_LIBS}"
            else
                echo "WARN: Recupero librerie per il driver jdbc da [GOVPAY_DS_JDBC_LIBS=${GOVPAY_DS_JDBC_LIBS}]."
            fi
        elif [ -z "${GOVPAY_ORACLE_JDBC_PATH}" -a -z "${GOVPAY_DS_JDBC_LIBS}" ]
        then
            echo "FATAL: Sanity check JDBC ... fallito."
            echo "FATAL: Il path alla directory che contiene il driver JDBC, deve essere indicato tramite la variabile GOVPAY_DS_JDBC_LIBS "
            exit 1
        fi

        if [ "${GOVPAY_ORACLE_JDBC_URL_TYPE^^}" != 'SERVICENAME' -a "${GOVPAY_ORACLE_JDBC_URL_TYPE^^}" != 'SID' ]
        then
            echo "FATAL: Sanity check JDBC ... fallito."
            echo "FATAL: Valore non consentito per la variabile GOVPAY_ORACLE_JDBC_URL_TYPE: [GOVPAY_ORACLE_JDBC_URL_TYPE=${GOVPAY_ORACLE_JDBC_URL_TYPE}]."
            echo "       Valori consentiti: [ servicename , sid ]"
            exit 1
        fi
        export GOVPAY_DS_DRIVER_CLASS='oracle.jdbc.OracleDriver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1 FROM DUAL'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.Oracle10gDialect

        if [ "${GOVPAY_ORACLE_JDBC_URL_TYPE^^}" != 'SID' ] 
        then
            export ORACLE_JDBC_SERVER_PREFIX='//'
            export ORACLE_JDBC_DB_SEPARATOR='/'
        else
            export ORACLE_JDBC_SERVER_PREFIX=''
            export ORACLE_JDBC_DB_SEPARATOR=':'
        fi
    ;;
    esac

;;
hsql|*)
    export GOVPAY_DS_JDBC_LIBS="/tmp/hsql-jdbc"
    mkdir /tmp/hsql-jdbc
    /bin/cp -f "/opt/hsqldb-${HSQLDB_FULLVERSION}/hsqldb/lib/hsqldb.jar" ${GOVPAY_DS_JDBC_LIBS}

    export GOVPAY_DS_DRIVER_CLASS='org.hsqldb.jdbc.JDBCDriver'
    export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT * FROM (VALUES(1));'
    export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.HSQLDialect

    export GOVPAY_DB_USER=govpay
    export GOVPAY_DB_NAME=govpay
    export GOVPAY_DB_PASSWORD=govpay
esac


# Setting valori di Default per i datasource GOVPAY
## parametri di connessione URL JDBC (default vuoto)
if [ -n "${GOVPAY_DS_CONN_PARAM}" ]; then export DATASOURCE_CONN_PARAM="?${GOVPAY_DS_CONN_PARAM}"; else export DATASOURCE_CONN_PARAM=""; fi


## Pooling
export GOVPAY_MAX_POOL=${GOVPAY_MAX_POOL:-10}
export GOVPAY_MIN_POOL=${GOVPAY_MIN_POOL:-2}
export GOVPAY_INITIALSIZE_POOL=${GOVPAY_INITIALSIZE_POOL:-${GOVPAY_MIN_POOL}}
export GOVPAY_MINIDLE_POOL=${GOVPAY_MINIDLE_POOL:-${GOVPAY_MIN_POOL}}
export GOVPAY_MAXIDLE_POOL=${GOVPAY_MAXIDLE_POOL:-${GOVPAY_MAX_POOL}}


# Mantenimento delle variabili precedenti per compatibilita
[ -n "${WILDFLY_KEYSTORE}" -a -z "${GOVPAY_AS_KEYSTORE}" ] && { echo "WARN: LA variabile WILDFLY_KEYSTORE è stata deprecata in favore di GOVPAY_AS_KEYSTORE."; export GOVPAY_AS_KEYSTORE="${WILDFLY_KEYSTORE}"; }
[ -n "${WILDFLY_TRUSTSTORE}" -a -z "${GOVPAY_AS_HTTPS_MTLS_WORKER_MAX_THREADS}" ] && { echo "WARN: LA variabile WILDFLY_TRUSTSTORE è stata deprecata in favore di GOVPAY_AS_TRUSTSTORE."; export GOVPAY_AS_TRUSTSTORE="${WILDFLY_TRUSTSTORE}"; }
[ -n "${WILDFLY_KEYSTORE_PASSWORD}" -a -z "${GOVPAY_AS_KEYSTORE_PASSWORD}" ] && { echo "WARN: LA variabile WILDFLY_KEYSTORE_PASSWORD è stata deprecata in favore di GOVPAY_AS_KEYSTORE_PASSWORD."; export GOVPAY_AS_KEYSTORE_PASSWORD="${WILDFLY_KEYSTORE_PASSWORD}"; }
[ -n "${WILDFLY_TRUSTSTORE_PASSWORD}" -a -z "${GOVPAY_AS_TRUSTSTORE_PASSWORD}" ] && { echo "WARN: LA variabile WILDFLY_TRUSTSTORE_PASSWORD è stata deprecata in favore di GOVPAY_AS_TRUSTSTORE_PASSWORD."; export GOVPAY_AS_TRUSTSTORE_PASSWORD="${WILDFLY_TRUSTSTORE_PASSWORD}"; }
[ -n "${WILDFLY_KEYSTORE_TIPO}" -a -z "${GOVPAY_AS_KEYSTORE_TIPO}" ] && { echo "WARN: LA variabile WILDFLY_KEYSTORE_TIPO è stata deprecata in favore di GOVPAY_AS_KEYSTORE_TIPO."; export GOVPAY_AS_KEYSTORE_TIPO="${WILDFLY_KEYSTORE_TIPO}"; }
[ -n "${WILDFLY_TRUSTSTORE_TIPO}" -a -z "${GOVPAY_AS_TRUSTSTORE_TIPO}" ] && { echo "WARN: LA variabile WILDFLY_TRUSTSTORE_TIPO è stata deprecata in favore di GOVPAY_AS_TRUSTSTORE_TIPO."; export GOVPAY_AS_TRUSTSTORE_TIPO="${WILDFLY_TRUSTSTORE_TIPO}"; }
[ -n "${WILDFLY_KEYSTORE_KEY_PASSWORD}" -a -z "${GOVPAY_AS_KEYSTORE_KEY_PASSWORD}" ] && { echo "WARN: LA variabile WILDFLY_KEYSTORE_KEY_PASSWORD è stata deprecata in favore di GOVPAGOVPAY_AS_KEYSTORE_KEY_PASSWORDY_AS_KEYSTORE."; export WILDFLY_KEYSTORE_KEY_PASSWORD="${GOVPAY_AS_KEYSTORE_KEY_PASSWORD}"; }


# Impostazioni keystore e truststore da utilizzare nei connettori https
if [ -z "${GOVPAY_AS_KEYSTORE}"  ]
then
    export GOVPAY_AS_KEYSTORE="${CATALINA_HOME}/conf/server_keystore.p12"
    export GOVPAY_AS_KEYSTORE_PASSWORD='123456'
    export GOVPAY_AS_KEYSTORE_TIPO='PKCS12'
    export GOVPAY_AS_KEYSTORE_KEY_PASSWORD='123456'

    echo "WARN: Sanity check HTTPS ... La variabile GOVPAY_AS_KEYSTORE è vuota."
    echo "WARN: Verra generato un keystore di default con le seguenti caratteristiche:"
    echo " - GOVPAY_AS_KEYSTORE='${GOVPAY_AS_KEYSTORE}'"
    echo " - GOVPAY_AS_KEYSTORE_PASSWORD='${GOVPAY_AS_KEYSTORE_PASSWORD}'"
    echo " - GOVPAY_AS_KEYSTORE_TIPO='${GOVPAY_AS_KEYSTORE_TIPO}'"
    echo " - GOVPAY_AS_KEYSTORE_KEY_PASSWORD='${GOVPAY_AS_KEYSTORE_KEY_PASSWORD}'"

    keytool -genkey \
        -keystore "${GOVPAY_AS_KEYSTORE}"  -storetype "${GOVPAY_AS_KEYSTORE_TIPO}" -storepass "${GOVPAY_AS_KEYSTORE_PASSWORD}" \
        -alias tomcat_govpay  -keypass "${GOVPAY_AS_KEYSTORE_KEY_PASSWORD}" -keyalg RSA -keysize 2048 \
        -validity 10950  -dname "CN=test.govpay.it,C=IT"    

fi
# Se non specificato altrimenti, per la password della pk utilizzo la stessa password del keystore
if [ -z "${GOVPAY_AS_KEYSTORE_KEY_PASSWORD}" ]
then
    echo "WARN: Sanity check HTTPS ... La variabile GOVPAY_AS_KEYSTORE_KEY_PASSWORD è vuota."
    echo "Verrà utilizzato la password del keystore presente in GOVPAY_AS_KEYSTORE_PASSWORD"
    export GOVPAY_AS_KEYSTORE_KEY_PASSWORD="${GOVPAY_AS_KEYSTORE_PASSWORD}"
fi
if [ -z "${GOVPAY_AS_TRUSTSTORE}" ]
then

    export GOVPAY_AS_TRUSTSTORE="${CATALINA_HOME}/conf/server_truststore.jks"
    export GOVPAY_AS_TRUSTSTORE_PASSWORD="123456"
    export GOVPAY_AS_TRUSTSTORE_TIPO="JKS"

    echo "WARN: Sanity check HTTPS ... La variabile GOVPAY_AS_TRUSTSTORE è vuota."
    echo "WARN: Verra generato un truststore di default con le seguenti caratteristiche"
    echo " - GOVPAY_AS_TRUSTSTORE='${GOVPAY_AS_TRUSTSTORE}'"
    echo " - GOVPAY_AS_TRUSTSTORE_PASSWORD='${GOVPAY_AS_TRUSTSTORE_PASSWORD}'"
    echo " - GOVPAY_AS_TRUSTSTORE_TIPO='${GOVPAY_AS_TRUSTSTORE_TIPO}'"

    # Il truststore non puo essere vuoto o contenere una chiave privata 
    # Altrimenti il connettore HTTPS va in errore con:
    # java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty
    #
    # Genero una coppia pk/x509 per un client di default
    keytool -genkey \
        -keystore "${CATALINA_HOME}/conf/server_keystore.jks"  -storetype PKCS12 -storepass 123456 \
        -alias client -keypass 123456 -keyalg RSA -keysize 2048 \
        -validity 3650 -dname "CN=GovPay Default Client,C=IT"
    # Importo l'x509 nel truststore
    keytool -exportcert \
        -keystore "${CATALINA_HOME}/conf/server_keystore.jks" -storepass 123456 -storetype PKCS12 \
        -alias client -rfc | keytool -importcert \
             -keystore "${GOVPAY_AS_TRUSTSTORE}" -storepass "${GOVPAY_AS_TRUSTSTORE_PASSWORD}" \
             -alias govpay_default_client -noprompt -storetype "${GOVPAY_AS_TRUSTSTORE_TIPO}"
fi
# Recupero l'indirizzo ip usato dal container (utilizzato dalle funzionalita di clustering / orchestration)
export GP_IPADDRESS=$(grep -E "[[:space:]]${HOSTNAME}[[:space:]]*" /etc/hosts|head -n 1|awk '{print $1}')
export JAVA_OPTS="$JAVA_OPTS -Dit.govpay.clusterId=${GP_IPADDRESS}"

#
# Startup
#

# Impostazione Dinamica dei limiti di memoria per container
export JAVA_OPTS="$JAVA_OPTS -XX:MaxRAMPercentage=${MAX_JVM_PERC:-80.0}"


# Inizializzazione del database
/usr/local/bin/initgovpay.sh || { echo "FATAL: Database non inizializzato."; exit 1; }

# Eventuali inizializzazioni custom 
if [ ! -f "${MODULE_INIT_FILE}" ]
then

    if [ -n "${GOVPAY_DS_JDBC_LIBS}" ]
    then

        declare -a lista_jar=( ${GOVPAY_DS_JDBC_LIBS}/*.jar )
        if [ ${#lista_jar[@]} -eq 1 -a "${lista_jar[0]}" == "${GOVPAY_DS_JDBC_LIBS}/*.jar" ]
        then
            echo "FATAL: Sanity check JDBC ... fallito"
            echo "FATAL: Nessuna libreria JDBC è presente in ${GOVPAY_DS_JDBC_LIBS}."
            exit 1
        fi

        /bin/cp -f ${lista_jar[@]} ${CATALINA_HOME}/lib

    fi

    touch "${MODULE_INIT_FILE}"
fi
if [ ! -f "${CONNETTORI_INIT_FILE}" ]
then
    # Riconversione variabili con il carattere '-' nel nome
    for e in $(env | grep 'MAX-' ); do varname="${e%=*}"; varval="${e#*=}"; eval  "export ${varname//-/_}=\"${varval}\""; done

    # Mantenimento delle variabili precedenti per compatibilita
    [ -n "${WILDFLY_HTTPS_CLIENTAUTH_WORKER_MAX_THREADS}" -a -z "${GOVPAY_AS_HTTPS_MTLS_WORKER_MAX_THREADS}" ] && { echo "WARN: LA variabile WILDFLY_HTTPS_CLIENTAUTH_WORKER-MAX-THREADS è stata deprecata in favore di GOVPAY_AS_HTTPS_MTLS_WORKER_MAX_THREADS."; export GOVPAY_AS_HTTPS_MTLS_WORKER_MAX_THREADS="${WILDFLY_HTTPS_CLIENTAUTH_WORKER_MAX_THREADS}"; }
    [ -n "${WILDFLY_HTTPS_WORKER_MAX_THREADS}" -a -z "${GOVPAY_AS_HTTPS_WORKER_MAX_THREADS}" ] && { echo "WARN: LA variabile WILDFLY_HTTPS_WORKER-MAX-THREADS è stata deprecata in favore di GOVPAY_AS_HTTPS_WORKER_MAX_THREADS."; export GOVPAY_AS_HTTPS_WORKER_MAX_THREADS="${WILDFLY_HTTPS_WORKER_MAX_THREADS}"; }
    [ -n "${WILDFLY_HTTP_WORKER_MAX_THREADS}" -a -z "${GOVPAY_AS_HTTP_WORKER_MAX_THREADS}" ] && { echo "WARN: LA variabile WILDFLY_HTTP_WORKER-MAX-THREADS è stata deprecata in favore di GOVPAY_AS_HTTP_WORKER_MAX_THREADS."; export GOVPAY_AS_HTTP_WORKER_MAX_THREADS="${WILDFLY_HTTP_WORKER_MAX_THREADS}"; }
    [ -n "${WILDFLY_AJP_WORKER_MAX_THREADS}" -a -z "${GOVPAY_AS_AJP_WORKER_MAX_THREADS}" ] && { echo "WARN: LA variabile WILDFLY_AJP_WORKER-MAX-THREADS è stata deprecata in favore di GOVPAY_AS_AJP_WORKER_MAX_THREADS."; export GOVPAY_AS_AJP_WORKER_MAX_THREADS="${WILDFLY_AJP_WORKER_MAX_THREADS}"; }
    [ -n "${WILDFLY_MAX_POST_SIZE}" -a -z "${GOVPAY_AS_MAX_POST_SIZE}" ] && { echo "WARN: LA variabile WILDFLY_MAX-POST-SIZE è stata deprecata in favore di GOVPAY_AS_MAX_POST_SIZE."; export GOVPAY_AS_MAX_POST_SIZE="${WILDFLY_MAX_POST_SIZE}"; }

    [ "${GOVPAY_AS_AJP_LISTENER^^}" == 'FALSE' -a "${GOVPAY_AS_HTTP_LISTENER^^}" == 'FALSE' ] && echo "WARN: Tutti i connettori verranno disabilitati. Non sarà più possibile accedere ai servizi"

    # I connettori AJP sono disabilitati per default a meno che non siano esplicitamente abilitati
    if [ "${GOVPAY_AS_AJP_LISTENER^^}" == 'TRUE' ]
    then
        cat - << EOCLI > /tmp/__fix_connettori.cli
/Server/Executor:add name=ajp-worker, namePrefix=ajp-worker-, maxThreads=\${GOVPAY_AS_AJP_OUT_WORKER_MAX_THREADS:-100}\n\
/Server/Service/Connector:add port=8009, protocol=AJP/1.3, redirectPort=8443, executor=ajp-worker, maxPostSize=\${GOVPAY_AS_MAX_POST_SIZE:-10485760}, secretRequired=\${GOVPAY_AS_AJP_SECRET:-false}\n\
EOCLI
    elif  [ "${GOVPAY_AS_AJP_LISTENER^^}" == 'FALSE' ]
    then
        # Elimino il connettore AJP solo se esplicitmante richiesto
        # per mantenere la compatibilità con le immagini preesistenti che
        #   lo avevano attivo all'avvio comunque
        cat - << EOCLI > /tmp/__fix_connettori.cli
/Server/Service/Connector[@port="8009"]:delete
/Server/Executor[@name="ajp-worker"]:delete
EOCLI
    fi


    # I connettori HTTP sono abilitati per default a meno che non siano esplicitamente disabilitati
    if [ "${GOVPAY_AS_HTTP_LISTENER^^}" == 'FALSE' ]
    then      
        cat - << EOCLI >> /tmp/__fix_connettori.cli
/Server/Service/Connector[@port="8080"]:delete
/Server/Executor[@name="http-worker"]:delete
EOCLI
    fi

    [ -f /tmp/__fix_connettori.cli ] && /usr/local/bin/tomcat-cli.sh "/tmp/__fix_connettori.cli"
    touch "${CONNETTORI_INIT_FILE}"
fi

# Preparo le rewrite di comatibilità delle URL
mkdir -p ${CATALINA_HOME}/conf/Catalina/localhost/
echo 'RewriteRule ^/govpay/backend/api/backoffice(/?.*)$ /govpay-api-backoffice$1 [L,QSA]
RewriteRule ^/govpay/backend/api/ragioneria(/?.*)$ /govpay-api-ragioneria$1 [L,QSA]
RewriteRule ^/govpay/backend/api/pendenze(/?.*)$ /govpay-api-pendenze$1 [L,QSA]
RewriteRule ^/govpay/frontend/api/pagopa(/?.*)$ /govpay-api-pagopa$1 [L,QSA]
RewriteRule ^/govpay/frontend/api/user(/?.*)$ /govpay-api-user$1 [L,QSA]
' > ${CATALINA_HOME}/conf/Catalina/localhost/rewrite.config

# Context descriptor per l'API backoffice: cookie di sessione compatibili con
# reverse proxy / cross-site (SameSite=None, path /, crossContext).
echo '<Context sessionCookiePath="/" sessionCookieSameSite="None" crossContext="true">
</Context>' > ${CATALINA_HOME}/conf/Catalina/localhost/govpay-api-backoffice.xml


if [ -d "${ENTRYPOINT_D}" -o  -d "${ENTRYPOINT_D_DEPRECATO}" ]
then
    if [ ! -f ${CUSTOM_INIT_FILE} ] 
    then
        f=
        for f in ${ENTRYPOINT_D}/* ${ENTRYPOINT_D_DEPRECATO}/*
        do
            case "$f" in
                *.sh)
                    if [ -x "$f" ]; then
                        echo "INFO: Customizzazioni ... eseguo $f"
                        "$f"
                    else
                        echo "INFO: Customizzazioni ... importo $f"
                        . "$f"
                    fi
                    ;;
                *.cli)
                    echo "INFO: Customizzazioni ... eseguo $f"; 
                        /usr/local/bin/tomcat-cli.sh "$f"
                    ;;
                *)  
                    echo "INFO: Customizzazioni ... IGNORO $f"
                    ;;
            esac
            echo
        done
        touch ${CUSTOM_INIT_FILE}
    fi
fi

# Aggiungo un javaagent all'avvio
if [ -f "${GOVPAY_JVM_AGENT_JAR}" ]
then
    echo "INFO: Carico all'avvio l'agent: [${GOVPAY_JVM_AGENT_JAR}]"
    export JAVA_TOOL_OPTIONS="-javaagent:${GOVPAY_JVM_AGENT_JAR}"
elif [ -n "${GOVPAY_JVM_AGENT_JAR}" ]
then
    echo "WARN: Impossibile caricare all'avvio l'agent: [${GOVPAY_JVM_AGENT_JAR}]"
    echo "WARN: Verificare che il path indicato sia corretto e leggibile dall'utente $(id -u -n)"
fi


# Azzero un'eventuale log di startup precedente (utile in caso di restart)
> ${GOVPAY_LOGDIR}/govpay_startup.log
rm -rf ${CATALINA_HOME}/work/Catalina/

# Forzo file di un eventuale file di properties jvm da passare all'avvio
if [ -f "${JVM_PROPERTIES_FILE}" -o -f "${JVM_PROPERTIES_FILE_DEPRECATO}" ]
then
    if ! grep -q "#PROPRIETA CUSTOM GOVPAY#"  "${CATALINA_HOME}/conf/catalina.properties" 
    then 
        GOVPAY_AS_PROP_FILE="${JVM_PROPERTIES_FILE}"
        [ ! -f "${JVM_PROPERTIES_FILE}" -a -f "${JVM_PROPERTIES_FILE_DEPRECATO}" ] && GOVPAY_AS_PROP_FILE="${JVM_PROPERTIES_FILE_DEPRECATO}"
        echo >> "${CATALINA_HOME}/conf/catalina.properties"
        echo "#PROPRIETA CUSTOM GOVPAY#" >> "${CATALINA_HOME}/conf/catalina.properties" 
        cat "${GOVPAY_AS_PROP_FILE}" >> "${CATALINA_HOME}/conf/catalina.properties"
    fi
fi

export UMASK=0022
ulimit -Sn 8192  # Soft limit per nofile
ulimit -Hn 8192  # Hard limit per nofile
ulimit -Su 4096  # Soft limit per nproc
ulimit -Hu 4096  # Hard limit per nproc
${CATALINA_HOME}/bin/catalina.sh run &


PID=$!
trap "kill -TERM $PID; export NUM_RETRY=${GOVPAY_STARTUP_CHECK_MAX_RETRY};" TERM INT


if [ "${GOVPAY_STARTUP_CHECK_SKIP^^}" == "FALSE" ]
then

	/bin/rm -f  /tmp/govpay_ready
	echo "INFO: Avvio di GovPay ... attendo"
	sleep ${GOVPAY_STARTUP_CHECK_FIRST_SLEEP_TIME}s
	GOVPAY_READY=1
	NUM_RETRY=0
	while [ ${GOVPAY_READY} -ne 0 -a ${NUM_RETRY} -lt ${GOVPAY_STARTUP_CHECK_MAX_RETRY} ]
	do
        HTTP_CODE=$(curl -s -w '%{http_code}' -o /tmp/info.json http://localhost:8080/govpay-api-backoffice/rs/form/v1/info )
        [ "${HTTP_CODE}" == "200" ]
		GOVPAY_READY=$?
		NUM_RETRY=$(( ${NUM_RETRY} + 1 ))
		if [  ${GOVPAY_READY} -ne 0 ]
                then
			echo "INFO: Avvio di GovPay ... attendo"
			sleep ${GOVPAY_STARTUP_CHECK_SLEEP_TIME}s
		fi
	done

	if [ ${NUM_RETRY} -eq ${GOVPAY_STARTUP_CHECK_MAX_RETRY} ]
	then
		echo "FATAL: Avvio di GovPay ... NON avviato dopo $((${GOVPAY_STARTUP_CHECK_SLEEP_TIME=} * ${GOVPAY_STARTUP_CHECK_MAX_RETRY})) secondi"
		kill -15 ${PID}
	else
		touch /tmp/govpay_ready
		echo "INFO: Avvio di Govpay ... GovPay avviato"
	fi
else
		touch /tmp/govpay_ready
fi



wait $PID
wait $PID
EXIT_STATUS=$?

echo "INFO: GovPay arrestato"
exec 6>&-

exit $EXIT_STATUS
