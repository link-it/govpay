#!/bin/bash -x
CLI_SCRIPT_FILE="$1"  
CLI_SCRIPT_CUSTOM_DIR="${CATALINA_HOME}/conf/custom_cli"

case "${GOVPAY_DB_TYPE:-hsql}" in
postgresql)
    
    GOVPAY_DS_DRIVER_CLASS='org.postgresql.Driver'
    GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'

    # Le variabili DATASOURCE_CONN_PARAM, sono impostate dallo standalone_wrapper.sh
    JDBC_RUN_URL='jdbc:postgresql://${GOVPAY_DB_SERVER}/${GOVPAY_DB_NAME}${DATASOURCE_CONN_PARAM}'

;;
mysql)
    
    GOVPAY_DS_DRIVER_CLASS='com.mysql.cj.jdbc.Driver'
    GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'

    # Le variabili DATASOURCE_CONN_PARAM, sono impostate dallo standalone_wrapper.sh
    JDBC_RUN_URL='jdbc:mysql://${GOVPAY_DB_SERVER}/${GOVPAY_DB_NAME}${DATASOURCE_CONN_PARAM}'

;;
mariadb)
    
    GOVPAY_DS_DRIVER_CLASS='org.mariadb.jdbc.Driver'
    GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1;'


    # Le variabili DATASOURCE_CONN_PARAM, sono impostate dallo standalone_wrapper.sh
    JDBC_RUN_URL='jdbc:mariadb://${GOVPAY_DB_SERVER}/${GOVPAY_DB_NAME}${DATASOURCE_CONN_PARAM}'

;;
oracle)
    
    GOVPAY_DS_DRIVER_CLASS='oracle.jdbc.OracleDriver'
    GOVPAY_DS_VALID_CONNECTION_SQL='SELECT 1 FROM DUAL'

    # Le variabili ORACLE_JDBC_SERVER_PREFIX ed ORACLE_JDBC_DB_SEPARATOR sono impostate dallo standalone_wrapper.sh
    # Le variabili DATASOURCE_CONN_PARAM, sono impostate dallo standalone_wrapper.sh
    JDBC_RUN_URL='jdbc:oracle:thin:@${ORACLE_JDBC_SERVER_PREFIX}${GOVPAY_DB_SERVER}${ORACLE_JDBC_DB_SEPARATOR}${GOVPAY_DB_NAME}${DATASOURCE_CONN_PARAM}'

;;
hsql|*)
    GOVPAY_DS_DRIVER_CLASS='org.hsqldb.jdbc.JDBCDriver'
    GOVPAY_DS_VALID_CONNECTION_SQL='SELECT * FROM (VALUES(1));'

    JDBC_RUN_URL="jdbc:hsqldb:file:/opt/hsqldb-${HSQLDB_FULLVERSION}/hsqldb/database/\${GOVPAY_DB_NAME};shutdown=true"
;;
esac




cat - << EOCLI >> "${CLI_SCRIPT_FILE}"
# Aggiungi Resource it.govpay.datasource
/Server/GlobalNamingResources/Resource:add name=it.govpay.datasource, auth=Container, type=javax.sql.DataSource, driverClassName=\${GOVPAY_DS_DRIVER_CLASS}, url=${JDBC_RUN_URL}, username=\${GOVPAY_DB_USER}, password=\${GOVPAY_DB_PASSWORD}, initialSize=\${GOVPAY_INITIALSIZE_POOL}, maxTotal=\${GOVPAY_MAX_POOL}, minIdle=\${GOVPAY_MINIDLE_POOL}, maxIdle=\${GOVPAY_MAXIDLE_POOL}, maxWaitMillis=\${GOVPAY_DS_BLOCKING_TIMEOUT:-30000}, defaultTransactionIsolation=READ_COMMITTED, validationQuery=\${GOVPAY_DS_VALID_CONNECTION_SQL}, validationQueryTimeout=10, testOnBorrow=true, testOnReturn=false, testWhileIdle=true, minEvictableIdleTimeMillis=300000, numTestsPerEvictionRun=10, timeBetweenEvictionRunsMillis=60000, poolPreparedStatements=true, maxOpenPreparedStatements=\${GOVPAY_DS_PSCACHESIZE:-100}, logAbandoned=true, removeAbandonedOnBorrow=\${GOVPAY_DS_REMOVEABANDONED:-false}, removeAbandonedOnMaintenance=\${GOVPAY_DS_REMOVEABANDONED:-false}, removeAbandonedTimeout=\${GOVPAY_DS_REMOVEABANDONED_TIMEOUT:-300}
# Aggiungi ResourceLink it.govpay.datasource
/Context/ResourceLink:add name=it.govpay.datasource, global=it.govpay.datasource, type=javax.sql.DataSource

EOCLI



if [ -d "${CLI_SCRIPT_CUSTOM_DIR}" -a -n "$(ls -A ${CLI_SCRIPT_CUSTOM_DIR} 2>/dev/null)" ]
then
    cli=""
	for cli in ${CLI_SCRIPT_CUSTOM_DIR}/*
    do
		echo >> "${CLI_SCRIPT_FILE}"
        cat ${cli} >> "${CLI_SCRIPT_FILE}"
	done
fi

