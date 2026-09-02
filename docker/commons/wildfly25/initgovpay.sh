#!/bin/bash -x
GOVPAY_LIVE_DB_CHECK_CONNECT_TIMEOUT=${GOVPAY_LIVE_DB_CHECK_CONNECT_TIMEOUT:=5}
GOVPAY_LIVE_DB_CHECK_FIRST_SLEEP_TIME=${GOVPAY_LIVE_DB_CHECK_FIRST_SLEEP_TIME:=0}
GOVPAY_LIVE_DB_CHECK_SLEEP_TIME=${GOVPAY_LIVE_DB_CHECK_SLEEP_TIME:=2}
GOVPAY_LIVE_DB_CHECK_MAX_RETRY=${GOVPAY_LIVE_DB_CHECK_MAX_RETRY:=30}
GOVPAY_LIVE_DB_CHECK_SKIP=${GOVPAY_LIVE_DB_CHECK_SKIP:=FALSE}
GOVPAY_READY_DB_CHECK_SKIP_SLEEP_TIME=${GOVPAY_READY_DB_CHECK_SKIP_SLEEP_TIME:=2}
GOVPAY_READY_DB_CHECK_MAX_RETRY=${GOVPAY_READY_DB_CHECK_MAX_RETRY:=5}
GOVPAY_READY_DB_CHECK_SKIP=${GOVPAY_READY_DB_CHECK_SKIP:=FALSE}
GOVPAY_POP_DB_SKIP=${GOVPAY_POP_DB_SKIP:=TRUE}

declare -A mappa_suffissi 
mappa_suffissi[RUN]=''

declare -A mappa_dbinfo
mappa_dbinfo[RUN]='utenze'

declare -A mappa_dbinfostring
mappa_dbinfostring[RUN]='%Database di GovPay'

SQLTOOL_RC_FILE=/tmp/sqltool.rc

# Pronto per reinizializzare file di configurazione
> ${SQLTOOL_RC_FILE}

for DESTINAZIONE in RUN
do
    if [ "${DESTINAZIONE}" == 'RUN' ]
    then
        SERVER="${GOVPAY_DB_SERVER}"
        DBNAME="${GOVPAY_DB_NAME}"
        DBUSER="${GOVPAY_DB_USER}"    
        DBPASS="${GOVPAY_DB_PASSWORD}"
        QUERYSTRING="${DATASOURCE_CONN_PARAM}"
    else

        eval "SERVER=\${GOVPAY_${DESTINAZIONE}_DB_SERVER}"
        eval "DBNAME=\${GOVPAY_${DESTINAZIONE}_DB_NAME}"
        eval "DBUSER=\${GOVPAY_${DESTINAZIONE}_DB_USER}"    
        eval "DBPASS=\${GOVPAY_${DESTINAZIONE}_DB_PASSWORD}"
        eval "QUERYSTRING=\${DATASOURCE_${DESTINAZIONE}_CONN_PARAM}"
    fi
    SERVER_PORT="${SERVER#*:}"
    SERVER_HOST="${SERVER%:*}"
    USE_RUN_DB=FALSE
    [ "${DESTINAZIONE}" != 'RUN' -a "${SERVER}" == "${GOVPAY_DB_SERVER}" -a "${DBNAME}" == "${GOVPAY_DB_NAME}" ] && USE_RUN_DB=TRUE


    case "${GOVPAY_DB_TYPE:-hsql}" in
    oracle)
        [ "${SERVER_PORT}" == "${SERVER_HOST}" ] && SERVER_PORT=1521
        JDBC_URL="jdbc:oracle:thin:@${ORACLE_JDBC_SERVER_PREFIX}${SERVER_HOST}:${SERVER_PORT}${ORACLE_JDBC_DB_SEPARATOR}${DBNAME}${DATASOURCE_CONN_PARAM}"
        START_TRANSACTION=""
    ;;
    postgresql) 
        [ "${SERVER_PORT}" == "${SERVER_HOST}" ] && SERVER_PORT=5432
        JDBC_URL="jdbc:postgresql://${SERVER_HOST}:${SERVER_PORT}/${DBNAME}${QUERYSTRING}"
        START_TRANSACTION="START TRANSACTION;"
    ;;
    mysql) 
        [ "${SERVER_PORT}" == "${SERVER_HOST}" ] && SERVER_PORT=3306
        JDBC_URL="jdbc:mysql://${SERVER_HOST}:${SERVER_PORT}/${DBNAME}${QUERYSTRING}"
        START_TRANSACTION="START TRANSACTION;"
    ;;
    mariadb) 
        [ "${SERVER_PORT}" == "${SERVER_HOST}" ] && SERVER_PORT=3306
        JDBC_URL="jdbc:mariadb://${SERVER_HOST}:${SERVER_PORT}/${DBNAME}${QUERYSTRING}"
        START_TRANSACTION="START TRANSACTION;"
    ;;
    hsql|*)
        DBNAME=govpay
        DBUSER=govpay
        DBPASS=govpay
        JDBC_URL="jdbc:hsqldb:file:/opt/hsqldb-${HSQLDB_FULLVERSION}/hsqldb/database/${DBNAME};shutdown=true"
        START_TRANSACTION="START TRANSACTION;"
    ;;
    esac

    INVOCAZIONE_CLIENT="-Dfile.encoding=UTF-8 -cp ${GOVPAY_DRIVER_JDBC}:/opt/hsqldb-${HSQLDB_FULLVERSION}/hsqldb/lib/sqltool.jar org.hsqldb.cmdline.SqlTool --rcFile=${SQLTOOL_RC_FILE} "
    cat - <<EOSQLTOOL >> ${SQLTOOL_RC_FILE}

urlid govpayDB${DESTINAZIONE}
url ${JDBC_URL}
username ${DBUSER}
password ${DBPASS}
driver ${GOVPAY_DS_DRIVER_CLASS}
transiso TRANSACTION_READ_COMMITTED
charset UTF-8
EOSQLTOOL

    # Server liveness
    if [ "${GOVPAY_LIVE_DB_CHECK_SKIP^^}" == "FALSE" -a "${GOVPAY_DB_TYPE:-hsql}" != 'hsql' ]
    then
    	echo "INFO: Liveness base dati ${DESTINAZIONE} ... attendo"
	    sleep ${GOVPAY_LIVE_DB_CHECK_FIRST_SLEEP_TIME}s
	    DB_READY=1
	    NUM_RETRY=0
	    while [ ${DB_READY} -ne 0 -a ${NUM_RETRY} -lt ${GOVPAY_LIVE_DB_CHECK_MAX_RETRY} ]
	    do
            nc  -w "${GOVPAY_LIVE_DB_CHECK_CONNECT_TIMEOUT}" -z "${SERVER_HOST}" "${SERVER_PORT}"
            DB_READY=$?
            NUM_RETRY=$(( ${NUM_RETRY} + 1 ))
            if [  ${DB_READY} -ne 0 ]
            then
                echo "INFO: Liveness base dati ${DESTINAZIONE} ... attendo"
                sleep ${GOVPAY_LIVE_DB_CHECK_SLEEP_TIME}s
            fi
	    done
       	if [  ${DB_READY} -ne 0 -a ${NUM_RETRY} -eq ${GOVPAY_LIVE_DB_CHECK_MAX_RETRY} ]
	    then
		    echo "FATAL: Liveness base dati ${DESTINAZIONE} ... Base dati NON disponibile dopo $((${GOVPAY_LIVE_DB_CHECK_SLEEP_TIME=} * ${GOVPAY_LIVE_DB_CHECK_MAX_RETRY})) secondi"
		    exit 1
        else
            echo "INFO: Liveness base dati ${DESTINAZIONE} ... Base dati disponibile"
	    fi
    fi
    # Server Readyness
    if [ "${GOVPAY_READY_DB_CHECK_SKIP^^}" == "FALSE" ]
    then
        ## REINIZIALIZZO VARIABILI DI CONTROLLO
        POP=0
        DB_POP=1


        DBINFO="${mappa_dbinfo[${DESTINAZIONE}]}"    
        
        case "${GOVPAY_DB_TYPE:-hsql}" in
        oracle)
        EXIST_QUERY="SELECT count(table_name) FROM all_tables WHERE  LOWER(table_name)='${DBINFO,,}' AND LOWER(owner)='${DBUSER,,}';" 
        ;;
        *)         
        EXIST_QUERY="SELECT count(table_name) FROM information_schema.tables WHERE LOWER(table_name)='${DBINFO,,}' and (LOWER(table_schema)='${DBNAME,,}' or LOWER(table_schema)='public' );" 
        ;;
        hsql|*)
        ;;
        esac

        DB_READY=1
	    NUM_RETRY=0
        while [ ${DB_READY} -ne 0 -a ${NUM_RETRY} -lt ${GOVPAY_READY_DB_CHECK_MAX_RETRY} ]
	    do
            EXIST=$(java ${INVOCAZIONE_CLIENT} --sql="${EXIST_QUERY}" govpayDB${DESTINAZIONE} 2> /dev/null)
            DB_READY=$?
            NUM_RETRY=$(( ${NUM_RETRY} + 1 ))
            if [  ${DB_READY} -ne 0 ]
            then
                echo "INFO: Readyness base dati ${DESTINAZIONE} ... riprovo"
                sleep ${GOVPAY_READY_DB_CHECK_SKIP_SLEEP_TIME}
            fi
        done
        if [ ${DB_READY} -ne 0 -a ${NUM_RETRY} -eq ${GOVPAY_READY_DB_CHECK_MAX_RETRY}  ]
        then
            echo "FATAL: Readyness base dati ${DESTINAZIONE} ... Base dati NON disponibile dopo $(( ${GOVPAY_READY_DB_CHECK_SKIP_SLEEP_TIME} * ${GOVPAY_READY_DB_CHECK_MAX_RETRY} ))secondi"
		    exit 1
        else
            ##ripulisco gli spazi
            EXIST="${EXIST// /}"
        fi
        if [ ${EXIST} -eq 1 ]
        then
            #  possibile che il db sia usato per piu' funzioni devo verifcare che non sia gia' stato popolato
            #DBINFONOTES="${mappa_dbinfostring[${DESTINAZIONE}]}"
            #POP_QUERY="SELECT count(*) FROM ${DBINFO} where notes LIKE '${DBINFONOTES}';"

            POP_QUERY="SELECT count(*) FROM ${DBINFO};"
            POP=$(java ${INVOCAZIONE_CLIENT} --sql="${POP_QUERY}" govpayDB${DESTINAZIONE} 2> /dev/null)
            ##ripulisco gli spazi
            POP="${POP// /}"

        fi
        # Popolamento automatico del db 
        if [ "${GOVPAY_POP_DB_SKIP^^}" == "FALSE" ]
        then 
            if [ -n "${POP}" -a ${POP} -eq 0 ] \
            || [ -n "${POP}" -a ${POP} -ge 1 -a "${USE_RUN_DB^^}" == "TRUE" ]
            then
                echo "WARN: Readyness base dati ${DESTINAZIONE} ... non inizializzato"
                SUFFISSO="${mappa_suffissi[${DESTINAZIONE}]}"
                mkdir -p /var/tmp/${GOVPAY_DB_TYPE:-hsql}/
                #
                # Ignoro in caso il file SQL non esista
                #
                INSTALLER_SQL_DIR="${GOVPAY_DB_TYPE:-hsql}"
                [ "${GOVPAY_DB_TYPE:-hsql}" == 'mariadb' ] && INSTALLER_SQL_DIR='mysql'
                [ ! -f /opt/${INSTALLER_SQL_DIR}/gov_pay${SUFFISSO}.sql ] && continue
                /bin/cp -f /opt/${INSTALLER_SQL_DIR}/gov_pay${SUFFISSO}*.sql /var/tmp/${GOVPAY_DB_TYPE:-hsql}/
                #
                # Elimino la creazione di tabelle comuni se il database e' utilizzato per piu funzioni (evita errore tabella gia' esistente)
                #
                # if [ "${DESTINAZIONE}" != 'RUN' ]
                # then
                #     if [[ ( "${GOVPAY_DB_TYPE:-hsql}" == 'hsql' && ${DBINFO} == "db_info" ) || ( ${DBINFO} == "db_info" && "${USE_RUN_DB}" == "TRUE" ) ]]
                #     then
                #         # Esempio 
                #         sed  \
                #         -e '/CREATE TABLE db_info/,/;/d' \
                #         -e '/CREATE SEQUENCE seq_db_info/d' \
                #         -e '/CREATE TABLE OP2_SEMAPHORE/,/;/d' \
                #         -e '/CREATE SEQUENCE seq_OP2_SEMAPHORE/d' \
                #         -e '/CREATE TRIGGER trg_OP2_SEMAPHORE/,/\//d' \
                #         -e '/CREATE UNIQUE INDEX idx_semaphore_1/d' \
                #         -e '/CREATE TRIGGER trg_db_info/,/\//d' \
                #         /opt/${GOVPAY_DB_TYPE:-hsql}/GovPay${SUFFISSO}.sql > /var/tmp/${GOVPAY_DB_TYPE:-hsql}/GovPay${SUFFISSO}.sql 
                #     fi
                # fi
                #
                # Aggiusto l'SQL per il database mysql e mariadb 
                #
                if [ "${GOVPAY_DB_TYPE:-hsql}" == 'mysql' -o "${GOVPAY_DB_TYPE:-hsql}" == 'mariadb' ]
                then
                    # I COMMENT delle colonne e delle tabelle contengono il carattere apice con escape; "\'"
                    # sembra che questo causi dei problemi nell'interpretare corettamente lo script al client 
                    # Sostituisco la coppia di caratteri con uno spazio singolo
                    #
                    sed -i -e "/COMMENT/s%\\\'% %g" /var/tmp/${GOVPAY_DB_TYPE:-hsql}/gov_pay${SUFFISSO}.sql
                fi
                #
                # Aggiusto l'SQL per il database oracle 
                #
                if [ "${GOVPAY_DB_TYPE:-hsql}" == 'oracle' ]
                then
                    # La sintassi dei trigger ed delle functions è problematica
                    # utilizzo la raw mode per evitare errori di sintassi
                    # http://www.hsqldb.org/doc/2.0/util-guide/sqltool-chapt.html#sqltool_raw-sect
                    #
		    sed -i -r -e '/^CREATE( OR REPLACE)? (TRIGGER|FUNCTION) .*$/i \
\\.' -e 's/^\/$/.\n:;/'  /var/tmp/${GOVPAY_DB_TYPE:-hsql}/gov_pay${SUFFISSO}.sql

                fi
                #
                # Inizializzazione database ${DESTINAZIONE}
                # 
                echo "INFO: Readyness base dati ${DESTINAZIONE} ... inizializzazione avviata."
                java ${INVOCAZIONE_CLIENT} --continueOnErr=false govpayDB${DESTINAZIONE} << EOSCRIPT
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
${START_TRANSACTION}
\i /var/tmp/${GOVPAY_DB_TYPE:-hsql}/gov_pay${SUFFISSO}.sql
COMMIT;
EOSCRIPT
                DB_POP=$?
            fi
            if [ $POP -ge 1 -o $DB_POP -eq 0 ] 
            then
                #TODO: da valutare come soluzione per il caso delle connessioni in blocking-timeut
                #      quando il db è hsql
                #if  [ "${GOVPAY_DB_TYPE:-hsql}" != 'hsql' ]
                #then
                #    echo
                #    echo "INFO: Readyness base dati ${DESTINAZIONE} ... setto dtatase in modalita MVCC."
                #    java ${INVOCAZIONE_CLIENT} --continueOnErr=false --autoCommit govpayDB${DESTINAZIONE} << EOSCRIPT    
    #SET DATABASE TRANSACTION CONTROL MVCC;
    #EOSCRIPT
                #fi
                echo
                echo "INFO: Readyness base dati ${DESTINAZIONE} ... inizializzazione completata."   
            else
                echo
                echo "INFO: Readyness base dati ${DESTINAZIONE} ... inizializzazione fallita."
                exit $DB_POP
            fi 
        fi
    fi
done



exit 0
