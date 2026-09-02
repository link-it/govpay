#!/bin/bash
exec 6<> /tmp/standalone_wrapper_debug.log
exec 2>&6
set -x

## Const
GOVPAY_STARTUP_CHECK_SKIP=${GOVPAY_STARTUP_CHECK_SKIP:=FALSE}
GOVPAY_STARTUP_CHECK_FIRST_SLEEP_TIME=${GOVPAY_STARTUP_CHECK_FIRST_SLEEP_TIME:=20}
GOVPAY_STARTUP_CHECK_SLEEP_TIME=${GOVPAY_STARTUP_CHECK_SLEEP_TIME:=5}
GOVPAY_STARTUP_CHECK_MAX_RETRY=${GOVPAY_STARTUP_CHECK_MAX_RETRY:=60}

declare -r JVM_PROPERTIES_FILE='/etc/wildfly/wildfly.properties'
declare -r ENTRYPOINT_D='/docker-entrypoint-govpay.d/'
declare -r CUSTOM_INIT_FILE="${JBOSS_HOME}/standalone/configuration/custom_wildlfy_init"
declare -r MODULE_INIT_FILE="${JBOSS_HOME}/standalone/configuration/fix_module_init"
case "${GOVPAY_DB_TYPE:-hsql}" in
mysql|mariadb|postgresql|oracle)

    #
    # Sanity check variabili minime attese
    #
    if [ -n "${GOVPAY_DB_SERVER}" -a -n  "${GOVPAY_DB_USER}" -a -n "${GOVPAY_DB_NAME}" ] 
    then
            [ -n "${GOVPAY_DB_PASSWORD}" ] || echo "WARN: La variabile GOVPAY_DB_PASSWORD non è stata impostata."
            echo "INFO: Sanity check variabili ... ok."
    else
        echo "FATAL: Sanity check variabili ... fallito."
        echo "FATAL: Devono essere settate almeno le seguenti variabili obbligatorie:
GOVPAY_DB_SERVER: ${GOVPAY_DB_SERVER}
GOVPAY_DB_NAME: ${GOVPAY_DB_NAME}
GOVPAY_DB_USER: ${GOVPAY_DB_USER}
"
        exit 1
    fi


    if [ -n "${GOVPAY_DS_JDBC_LIBS}" ] 
    then
        export GOVPAY_DRIVER_JDBC="${GOVPAY_DS_JDBC_LIBS}"
        if [ ! -d "${GOVPAY_DS_JDBC_LIBS}" ]
        then
            echo "FATAL: Sanity check JDBC ... fallito."
            echo "FATAL: Il path alla directory che contiene il driver JDBC, non è leggibile o non è una directory: [GOVPAY_DS_JDBC_LIBS=${GOVPAY_DS_JDBC_LIBS}] "
            exit 1
        fi
    fi


    case "${GOVPAY_DB_TYPE:-hsql}" in
    postgresql)
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

        if [ -z "${GOVPAY_POSTGRESQL_JDBC_PATH}" -o ! -f "${GOVPAY_POSTGRESQL_JDBC_PATH}" ]
        then
            echo "FATAL: Sanity check jdbc mysql ... fallito."
            echo "FATAL: Il path al driver jdbc postgresql, non è stato indicato o non è leggibile: [GOVPAY_POSTGRESQL_JDBC_PATH=${GOVPAY_POSTGRESQL_JDBC_PATH}] "
            exit 1
        fi
        export GOVPAY_DRIVER_JDBC="${JBOSS_HOME}/modules/govpayJDBCMod/main/postgresql-jdbc.jar"
        export GOVPAY_DS_DRIVER_CLASS='org.postgresql.Driver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
        rm -rf "${GOVPAY_DRIVER_JDBC}"
        cp "${GOVPAY_POSTGRESQL_JDBC_PATH}"  "${GOVPAY_DRIVER_JDBC}"

    ;;
    mysql)
        if [ -z "${GOVPAY_MYSQL_JDBC_PATH}" -o ! -f "${GOVPAY_MYSQL_JDBC_PATH}" ]
        then
            echo "FATAL: Sanity check jdbc mysql ... fallito."
            echo "FATAL: Il path al driver jdbc mysql, non è stato indicato o non è leggibile: [GOVPAY_MYSQL_JDBC_PATH=${GOVPAY_MYSQL_JDBC_PATH}] "
            exit 1
        fi
        if [ -n "${DATASOURCE_CONN_PARAM}" ]
        then
            DATASOURCE_CONN_PARAM="${DATASOURCE_CONN_PARAM}&zeroDateTimeBehavior=convertToNull"
        else
            DATASOURCE_CONN_PARAM='?zeroDateTimeBehavior=convertToNull'
        fi
        export GOVPAY_DRIVER_JDBC="${JBOSS_HOME}/modules/govpayJDBCMod/main/mysql-jdbc.jar"
        export GOVPAY_DS_DRIVER_CLASS='com.mysql.cj.jdbc.Driver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.MySQL57Dialect
        rm -rf "${GOVPAY_DRIVER_JDBC}"
        cp "${GOVPAY_MYSQL_JDBC_PATH}"  "${GOVPAY_DRIVER_JDBC}"
    ;;
    mariadb)
        if [ -z "${GOVPAY_MARIADB_JDBC_PATH}" -o ! -f "${GOVPAY_MARIADB_JDBC_PATH}" ]
        then
            echo "FATAL: Sanity check jdbc mariadb ... fallito."
            echo "FATAL: Il path al driver jdbc mariadb, non è stato indicato o non è leggibile: [GOVPAY_MARIADB_JDBC_PATH=${GOVPAY_MYSQL_JDBC_PATH}] "
            exit 1
        fi
        if [ -n "${DATASOURCE_CONN_PARAM}" ]
        then
            DATASOURCE_CONN_PARAM="${DATASOURCE_CONN_PARAM}&zeroDateTimeBehavior=convertToNull"
        else
            DATASOURCE_CONN_PARAM='?zeroDateTimeBehavior=convertToNull'
        fi
        export GOVPAY_DRIVER_JDBC="${JBOSS_HOME}/modules/govpayJDBCMod/main/mariadb-jdbc.jar"
        export GOVPAY_DS_DRIVER_CLASS='org.mariadb.jdbc.Driver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.MySQL57Dialect
        rm -rf "${GOVPAY_DRIVER_JDBC}"
        cp "${GOVPAY_MARIADB_JDBC_PATH}"  "${GOVPAY_DRIVER_JDBC}"
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
        export GOVPAY_DRIVER_JDBC="${JBOSS_HOME}/modules/govpayJDBCMod/main/oracle-jdbc.jar"
        export GOVPAY_DS_DRIVER_CLASS='oracle.jdbc.OracleDriver'
        export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1 FROM DUAL'
        export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.Oracle10gDialect
        rm -rf "${GOVPAY_DRIVER_JDBC}"
        cp "${GOVPAY_ORACLE_JDBC_PATH}"  "${GOVPAY_DRIVER_JDBC}"

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
    ## parametri di connessione URL JDBC (default vuoto)
    [ -n "${GOVPAY_DS_CONN_PARAM}" ] &&  export DATASOURCE_CONN_PARAM="?${GOVPAY_DS_CONN_PARAM}"
;;
hsql|*)
    export GOVPAY_DRIVER_JDBC="/opt/hsqldb-${HSQLDB_FULLVERSION}/hsqldb/lib/hsqldb-jdk8.jar"
    export GOVPAY_DS_DRIVER_CLASS='org.hsqldb.jdbc.JDBCDriver'
    export GOVPAY_DS_VALID_CONNECTION_SQL='SELECT * FROM (VALUES(1));'
    export GOVPAY_HYBERNATE_DIALECT=org.hibernate.dialect.HSQLDialect
esac

# Impostazioni keystore e truststore da utilizzare nei connettori https
if [ -z "${WILDFLY_KEYSTORE}"  ]
then
    export WILDFLY_KEYSTORE="${JBOSS_HOME}/standalone/configuration/testkeystore.jks"
    export WILDFLY_KEYSTORE_PASSWORD=123456
fi
# Se non specificato altrimenti, per la password della pk utilizzo la stessa password del keystore
if [ -z "${WILDFLY_KEYSTORE_KEY_PASSWORD}" ]
then
    export WILDFLY_KEYSTORE_KEY_PASSWORD="${WILDFLY_KEYSTORE_PASSWORD}"
fi
if [ -z "${WILDFLY_TRUSTSTORE}" ]
then
    export WILDFLY_TRUSTSTORE="${JBOSS_HOME}/standalone/configuration/testkeystore.jks"
    export WILDFLY_TRUSTSTORE_PASSWORD=123456
fi

#
# Startup
#

# Impostazione Dinamica dei limiti di memoria per container
export JAVA_OPTS="$JAVA_OPTS -XX:MaxRAMPercentage=${MAX_JVM_PERC:-80.0}"


# Inizializzazione del database
${JBOSS_HOME}/bin/initgovpay.sh || { echo "FATAL: Database non inizializzato."; exit 1; }

# Eventuali inizializzazioni custom widfly
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
        elif [ ${#lista_jar[@]} -eq 1 ]
        then
            # è presente solo un jar: lo utilizzo
            LIBRERIE="${lista_jar[0]}" 
        elif [ ${#lista_jar[@]} -gt 1 ]
        then
            # sono presenti diversi jar concateno i path separandoli con ':'
            LIBRERIE="${lista_jar[0]}"
            for j in ${lista_jar[@]:1}
            do
                LIBRERIE="${j}:${LIBRERIE}"
            done
        fi

        cat - << EOCLI > /tmp/__standalone_fix_module.cli   
embed-server --server-config=standalone.xml --std-out=echo
echo "Rimuovo modulo ${GOVPAY_DB_TYPE:-hsql}Mod"
module remove --name=${GOVPAY_DB_TYPE:-hsql}Mod
echo "Ricreo modulo ${GOVPAY_DB_TYPE:-hsql}Mod con risorse aggiornate"
module add --name=${GOVPAY_DB_TYPE:-hsql}Mod --resources="${LIBRERIE}" --dependencies=javax.api,javax.transaction.api
EOCLI

        ${JBOSS_HOME}/bin/jboss-cli.sh --file="/tmp/__standalone_fix_module.cli"
    fi

    touch "${MODULE_INIT_FILE}"
fi
if [ -d "${ENTRYPOINT_D}" -a ! -f ${CUSTOM_INIT_FILE} ]
then
    local f
	for f in ${ENTRYPOINT_D}/*
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
				if ! grep -q embed-server "$f"
				then
				    # Mi assicuro che sia presente la direttiva embed-server in cima allo script
				    # perche l'application server a questo punto non è ancora attivo
				    echo -e 'embed-server --server-config=standalone.xml --std-out=echo\n' > "/tmp/$(basename $f).fix"
				    cat "$f" >> "/tmp/$(basename $f).fix"
				    echo -e '\nstop-embedded-server\n' >> "/tmp/$(basename $f).fix"
				    ${JBOSS_HOME}/bin/jboss-cli.sh --file="/tmp/$(basename $f).fix"
				else
				    ${JBOSS_HOME}/bin/jboss-cli.sh --file="$f"
				fi
				;;
			*)  
                echo "INFO: Customizzazioni ... IGNORO $f"
                ;;
		esac
		echo
	done
    touch ${CUSTOM_INIT_FILE}
fi

# Azzero un'eventuale log di startup precedente (utile in caso di restart)
> ${GOVPAY_LOGDIR}/govpay_startup.log

# Forzo file di un eventuale file di properties jvm da passare all'avvio
if [ -f ${JVM_PROPERTIES_FILE} ]
then
    declare -a CMDLINARGS
    SKIP=0
    FOUND=0
    for prop in $@
    do
        [ $SKIP -eq 1 ] && SKIP=0 && continue
        if [ "$prop" == '-p' ]
        then
            CMDLINARGS+=("-p")
            CMDLINARGS+=("${JVM_PROPERTIES_FILE}")
            SKIP=1
            FOUND=1
        elif [ "${prop%%=*}" == '--properties' ]
        then
            CMDLINARGS+=("--properties=${JVM_PROPERTIES_FILE}")
            FOUND=1
        else
            CMDLINARGS+=($prop)
        fi
    done
    [ $FOUND -eq 0 ] && CMDLINARGS+=("--properties=${JVM_PROPERTIES_FILE}")
    ${JBOSS_HOME}/bin/standalone.sh -b 0.0.0.0 ${CMDLINARGS[@]} &
else
    ${JBOSS_HOME}/bin/standalone.sh -b 0.0.0.0 $@ &
fi

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
        HTTP_CODE=$(curl -s -w '%{http_code}' -o /tmp/info.json http://localhost:8080/govpay/backend/api/backoffice/rs/form/v1/info )
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
