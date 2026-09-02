#!/bin/bash

function printHelp() {
echo "Usage $(basename $0) [ -t <repository>:<tagname> | <Installer Sorgente> | <Personalizzazioni> | <Avanzate> | -h ]"
echo 
echo "Options
-t <TAG>       : Imposta il nome del TAG ed il repository locale utilizzati per l'immagine prodotta 
                 NOTA: deve essere rispettata la sintassi <repository>:<tagname>
-h             : Mostra questa pagina di aiuto

Installer Sorgente:
-v <VERSIONE>  : Imposta la versione dell'installer binario da utilizzare per il build (default: ultima release pubblicata su GitHub)
-l <FILE>      : Usa un'installer binario sul filesystem locale (incompatibile con -j)
-j             : Usa l'installer prodotto dalla pipeline jenkins https://jenkins.link.it/govpay/risultati-testsuite/installer/govpay-installer-<version>.tgz

Personalizzazioni:
-d <TIPO>      : Prepara l'immagine per essere utilizzata su un particolare database  (valori: [ hsql, postgresql, mysql, mariadb, oracle] , default: hsql)
-e <PATH>      : Imposta il path interno utilizzato per i file di configurazione di govpay 
-f <PATH>      : Imposta il path interno utilizzato per i log di govpay

Avanzate:
-i <FILE>      : Usa il template ant.installer.properties indicato per la generazione degli archivi dall'installer
-r <DIRECTORY> : Inserisce il contenuto della directory indicata, tra i contenuti custom 
-w <DIRECTORY> : Esegue tutti gli scripts widlfly contenuti nella directory indicata
"
}

# Comando docker: sovrascrivibile con DOCKER_BIN per gli ambienti in cui serve
# un wrapper, per esempio DOCKER_BIN="sudo docker" sugli agent Jenkins.
if [ -n "${DOCKER_BIN}" ]
then
  DOCKERBIN="${DOCKER_BIN}"
else
  DOCKERBIN="$(which docker)"
  if [ -z "${DOCKERBIN}" ]
  then
     echo "Impossibile trovare il comando \"docker\""
     exit 2
  fi
fi



TAG=
VER=
DB=
LOCALFILE=
TEMPLATE=
ARCHIVI=
CUSTOM_MANAGER=
CUSTOM_MANAGER=
CUSTOM_GOVPAY_AS_CLI=
REGISTRY_PREFIX=${REGISTRY_PREFIX:-linkitaly}
#REGISTRY_PREFIX=localhost

# Ultima release pubblicata su GitHub, usata solo quando -v non e' indicato.
# Risolta a richiesta per non fare una chiamata di rete a ogni invocazione:
# la pipeline passa sempre -v e non ha bisogno di raggiungere github.com.
LATEST_GOVPAY_RELEASE=
function latestGovpayRelease() {
  if [ -z "${LATEST_GOVPAY_RELEASE}" ]
  then
    LATEST_LINK="$(curl -qw '%{redirect_url}\n' https://github.com/link-it/govpay/releases/latest 2> /dev/null)"
    LATEST_GOVPAY_RELEASE="${LATEST_LINK##*/}"
    [ -z "${LATEST_GOVPAY_RELEASE}" ] && { echo "Impossibile determinare l'ultima release GovPay: indicare la versione con -v"; exit 2; }
  fi
  echo "${LATEST_GOVPAY_RELEASE}"
}

while getopts "ht:v:d:jl:i:a:r:m:w:o:e:f:g:" opt; do
  case $opt in
    t) TAG="$OPTARG"; NO_COLON=${TAG//:/}
      [ ${#TAG} -eq ${#NO_COLON} -o "${TAG:0:1}" == ':' -o "${TAG:(-1):1}" == ':' ] && { echo "Il tag fornito \"$TAG\" non utilizza la sintassi <repository>:<tagname>"; exit 2; } ;;
    v) VER="$OPTARG"  ;;
    d) DB="${OPTARG}"; case "$DB" in hsql);;postgresql);;mysql);;mariadb);;oracle);;*) echo "Database non supportato: $DB"; exit 2;; esac ;;
    g) APPSERV="${OPTARG}"; case "$APPSERV" in tomcat11);;wildfly25);;*) echo "Application server non supportato: $APPSERV"; exit 2;; esac ;;
    l) LOCALFILE="$OPTARG"
        [ ! -f "${LOCALFILE}" ] && { echo "Il file indicato non esiste o non e' raggiungibile [${LOCALFILE}]."; exit 3; } 
       ;;
    j) JENKINS="true"
        [ -n "${LOCALFILE}" ] && { echo "Le opzioni -j e -l sono incompatibili. Impostare solo una delle due."; exit 2; }
       ;;
    i) TEMPLATE="${OPTARG}"
        [ ! -f "${TEMPLATE}" ] && { echo "Il file indicato non esiste o non e' raggiungibile [${TEMPLATE}]."; exit 3; } 
        ;;
    a) ARCHIVI="${OPTARG}"; case "${ARCHIVI}" in govpay);;gde);;aca);;all);;*) echo "Tipologia archivi da inserire non riconosciuta: ${ARCHIVI}"; exit 2;; esac ;;
    r) CUSTOM_RUNTIME="${OPTARG}"
        [ ! -d "${CUSTOM_RUNTIME}" ] && { echo "la directory indicata non esiste o non e' raggiungibile [${CUSTOM_RUNTIME}]."; exit 3; }
        [ -z "$(ls -A ${CUSTOM_RUNTIME})" ] && { echo "la directory [${CUSTOM_RUNTIME}] e' vuota.";  }
        ;;
    w) CUSTOM_WIDLFLY_CLI="${OPTARG}"
        [ ! -d "${CUSTOM_WIDLFLY_CLI}" ] && { echo "la directory indicata non esiste o non e' raggiungibile [${CUSTOM_WIDLFLY_CLI}]."; exit 3; }
        [ -z "$(ls -A ${CUSTOM_WIDLFLY_CLI})" ] && { echo "la directory [${CUSTOM_WIDLFLY_CLI}] e' vuota.";  }
        ;;
    e) CUSTOM_GOVPAY_HOME="${OPTARG}" ;;
    f) CUSTOM_GOVPAY_LOG="${OPTARG}" ;;
    h) printHelp
       exit 0
       ;;
    \?)
      echo "Opzione non valida: -$opt"
      exit 1
      ;;
  esac
done
[ "${ARCHIVI}" == 'aca' -o "${ARCHIVI}" == 'gde' -a "${DB:-hsql}" == 'hsql' ] && { echo "Il build dell'immagine batch ACA o del Microservizio GDE non puo' essere eseguita per il database HSQL"; exit 4; }

# Versione risolta una sola volta: da qui in avanti VER e' sempre valorizzata,
# quindi i default ${VER:-...} piu' sotto non innescano la chiamata di rete.
VER="${VER:-$(latestGovpayRelease)}"

# Il contesto di build e i Dockerfile sono indicati con percorsi relativi alla
# directory dello script, quindi lo script si sposta qui. I percorsi passati
# dall'utente sono invece relativi alla directory da cui e' stato invocato:
# vanno resi assoluti prima dello spostamento, altrimenti invocarlo da fuori
# (per esempio ./docker/build_image.sh dalla radice del progetto) non trova
# ne' il contesto ne' l'installer.
function abspath() { echo "$(cd "$(dirname "$1")" && pwd)/$(basename "$1")"; }
[ -n "${LOCALFILE}" ] && LOCALFILE="$(abspath "${LOCALFILE}")"
[ -n "${TEMPLATE}" ] && TEMPLATE="$(abspath "${TEMPLATE}")"
[ -n "${CUSTOM_RUNTIME}" ] && CUSTOM_RUNTIME="$(cd "${CUSTOM_RUNTIME}" && pwd)"
[ -n "${CUSTOM_WIDLFLY_CLI}" ] && CUSTOM_WIDLFLY_CLI="$(cd "${CUSTOM_WIDLFLY_CLI}" && pwd)"
cd "$(dirname "$0")" || { echo "Impossibile spostarsi nella directory dello script"; exit 2; }

rm -rf buildcontext
mkdir -p buildcontext/
cp -fr "commons/${APPSERV:-tomcat11}" buildcontext/commons
cp -f commons/* buildcontext/commons 2> /dev/null

#export DOCKER_BUILDKIT=0
DOCKERBUILD_OPTS=('--build-arg' "govpay_appserver=${APPSERV:-tomcat11}")
DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "govpay_fullversion=${VER:-${LATEST_GOVPAY_RELEASE}}")
[ -n "${TEMPLATE}" ] &&  cp -f "${TEMPLATE}" buildcontext/commons/
[ -n "${CUSTOM_GOVPAY_HOME}" ] && DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "govpay_home=${CUSTOM_GOVPAY_HOME}")
[ -n "${CUSTOM_GOVPAY_LOG}" ] && DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "govpay_log=${CUSTOM_GOVPAY_LOG}")
if [ -n "${CUSTOM_RUNTIME}" ]
then
  cp -r ${CUSTOM_RUNTIME}/ buildcontext/runtime
  DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "runtime_custom_archives=runtime")
fi

# Build immagine installer
if [ -n "${JENKINS}" ]
then
  INSTALLER_DOCKERFILE="govpay/Dockerfile.jenkins"
elif [ -n "${LOCALFILE}" ]
then
  INSTALLER_DOCKERFILE="govpay/Dockerfile.daFile"
  cp -f "${LOCALFILE}" buildcontext/
else
  INSTALLER_DOCKERFILE="govpay/Dockerfile.github"
fi

if [ -n "${DB}" ]
then
  if [ "${DB}" == 'mariadb' ]
  then
    DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "govpay_database_vendor=mysql")
  else
    DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "govpay_database_vendor=${DB}")
  fi
fi

export DOCKER_BUILDKIT=false
${DOCKERBIN} build "${DOCKERBUILD_OPTS[@]}" \
  -t ${REGISTRY_PREFIX}/govpay-installer_${DB:-hsql}:${VER:-${LATEST_GOVPAY_RELEASE}} \
  -f ${INSTALLER_DOCKERFILE} buildcontext
RET=$?
[ ${RET} -eq  0 ] || exit ${RET}
 
if [ "${DB}" == 'mariadb' ]
then
  c=$(( ${#DOCKERBUILD_OPTS[@]} - 1 ))
  unset  DOCKERBUILD_OPTS[$c]
  DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} "govpay_database_vendor=mariadb")
fi
# Build imagine GovPay

[ -n "${ARCHIVI}" ] && DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "govpay_archives_type=${ARCHIVI}")
if [ -z "$TAG" ] 
then
    REPO=${REGISTRY_PREFIX}/govpay
  TAGNAME=${VER:-${LATEST_GOVPAY_RELEASE}}
  [ -n "${ARCHIVI}" -a "${ARCHIVI}" != 'all' ] && TAGNAME=${VER:-${LATEST_GOVPAY_RELEASE}}_${ARCHIVI}
  
  # mantengo i nomi dei tag compatibili con quelli usati in precedenza
  case "${DB:-hsql}" in
  hsql) TAG="${REPO}:${TAGNAME}" ;;
  postgresql) TAG="${REPO}:${TAGNAME}_postgres" ;;
  *) TAG="${REPO}:${TAGNAME}_${DB}" ;;
  esac

  # il tag per tomcat11 diventa quello di default. Tutti gli altri hanno l'indicazione dell AS usato
  [ "${APPSERV:-tomcat11}" != "tomcat11" -a "${ARCHIVI}" != 'aca'  -a "${ARCHIVI}" != 'gde' ] && TAG="${TAG}_${APPSERV}"

fi

if [ -n "${CUSTOM_GOVPAY_AS_CLI}" ]
then
  cp -r ${CUSTOM_GOVPAY_AS_CLI}/ buildcontext/custom_govpay_as_cli
  DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "govpay_as_custom_scripts=custom_govpay_as_cli")
fi

if [ -n "${CUSTOM_ORACLE_JDBC}" ]
then
  cp -r ${CUSTOM_ORACLE_JDBC}/ buildcontext/custom_oracle_jdbc
  DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "oracle_custom_jdbc=custom_oracle_jdbc")
fi

DOCKERBUILD_OPTS=(${DOCKERBUILD_OPTS[@]} '--build-arg' "source_image=${REGISTRY_PREFIX}/govpay-installer_${DB:-hsql}:${VER:-${LATEST_GOVPAY_RELEASE}}")


if [ "${ARCHIVI}" == 'aca' ]
then
  DOCKERFILE="govpay/Dockerfile.govpay_aca"
elif [ "${ARCHIVI}" == 'gde' ]
then
  DOCKERFILE="govpay/Dockerfile.govpay_gde"
else
  DOCKERFILE="govpay/${APPSERV:-tomcat11}/Dockerfile.govpay"
fi

VCS_REF=$(git rev-parse --short HEAD 2>/dev/null || echo unknown)
${DOCKERBIN} build "${DOCKERBUILD_OPTS[@]}" \
--build-arg "vcs_ref=${VCS_REF}" \
-t "${TAG}" \
-f $DOCKERFILE buildcontext
RET=$?
[ ${RET} -eq  0 ] || exit ${RET}



if [ "${DB:-hsql}" != 'hsql' -a "${ARCHIVI}" != 'aca' -a "${ARCHIVI}" != 'gde' ]
then
  mkdir -p compose/govpay_{conf,log}
  chmod 777 compose/govpay_{conf,log}

  SHORT=${TAG#*:}
  cat - << EOYAML > compose/docker-compose.yaml
version: '2'
services:
  govpay:
    container_name: govpay_${SHORT}
    image: ${TAG}
    depends_on:
        - database
    ports:
        - 8080:8080
        - 8443:8443
        - 8445:8445
    volumes:
        - ./govpay_log:${CUSTOM_GOVPAY_LOG:-/var/log/govpay}
EOYAML
  if [ "${DB:-hsql}" == 'postgresql' ]
  then
    cat - << EOYAML >> compose/docker-compose.yaml
          # Il driver deve essere compiato manualmente nella directory corrente
        - ./postgresql-42.4.0.jar:/tmp/postgresql-42.4.0.jar 
    environment:
        - GOVPAY_DB_SERVER=pg_govpay_${SHORT}
        - GOVPAY_DB_NAME=govpaydb
        - GOVPAY_DB_USER=govpay
        - GOVPAY_DB_PASSWORD=govpay
        - GOVPAY_POSTGRESQL_JDBC_PATH=/tmp/postgresql-42.4.0.jar 
        - GOVPAY_POP_DB_SKIP=false
  database:
    container_name: pg_govpay_${SHORT}
    image: postgres:13
    environment:
        - POSTGRES_DB=govpaydb
        - POSTGRES_USER=govpay
        - POSTGRES_PASSWORD=govpay
EOYAML
    echo 
    echo "ATTENZIONE: Copiare il driver jdbc postgresql 'postgresql-42.4.0.jar' dentro la directory './compose/'"
    echo
    echo "ATTENZIONE: Copiare il driver jdbc postgresql 'postgresql-42.4.0.jar' dentro la directory './compose/'" > compose/README.first
  elif [ "${DB:-hsql}" == 'mariadb' ]
  then
    cat - << EOYAML >> compose/docker-compose.yaml
        # Il driver deve essere compiato manualmente nella directory corrente
        - ./mariadb-java-client-3.0.6.jar:/tmp/mariadb-java-client-3.0.6.jar 
    environment:
        - GOVPAY_DB_SERVER=my_govpay_${SHORT}
        - GOVPAY_DB_NAME=govpaydb
        - GOVPAY_DB_USER=govpay
        - GOVPAY_DB_PASSWORD=govpay
        - GOVPAY_MARIADB_JDBC_PATH=/tmp/mariadb-java-client-3.0.6.jar
        - GOVPAY_POP_DB_SKIP=false
  database:
    container_name: my_govpay_${SHORT}
    image: mariadb:10.6
    environment:
      - MARIADB_DATABASE=govpaydb
      - MARIADB_USER=govpay
      - MARIADB_PASSWORD=govpay
      - MARIADB_ROOT_PASSWORD=my-secret-pw
    ports:
       - 3306:3306
EOYAML
    echo 
    echo "ATTENZIONE: Copiare il driver jdbc Mariadb 'mariadb-java-client-3.0.6.jar' dentro la directory './compose/'"
    echo
    echo "ATTENZIONE: Copiare il driver jdbc Mariadb 'mariadb-java-client-3.0.6.jar' dentro la directory './compose/'" > compose/README.first
  elif [ "${DB:-hsql}" == 'mysql' ]
  then
    cat - << EOYAML >> compose/docker-compose.yaml
        # Il driver deve essere compiato manualmente nella directory corrente
        - ./mysql-connector-java-8.0.29.jar:/tmp/mysql-connector-java-8.0.29.jar 
    environment:
        - GOVPAY_DB_SERVER=my_govpay_${SHORT}
        - GOVPAY_DB_NAME=govpaydb
        - GOVPAY_DB_USER=govpay
        - GOVPAY_DB_PASSWORD=govpay
        - GOVPAY_MYSQL_JDBC_PATH=/tmp/mysql-connector-java-8.0.29.jar
        - GOVPAY_POP_DB_SKIP=false
  database:
    container_name: my_govpay_${SHORT}
    image: mysql:8.0
    environment:
      - MYSQL_DATABASE=govpaydb
      - MYSQL_USER=govpay
      - MYSQL_PASSWORD=govpay
      - MYSQL_ROOT_PASSWORD=my-secret-pw
    ports:
       - 3306:3306
EOYAML
    echo 
    echo "ATTENZIONE: Copiare il driver jdbc Mysql 'mysql-connector-java-8.0.29.jar' dentro la directory './compose/'"
    echo
    echo "ATTENZIONE: Copiare il driver jdbc Mysql 'mysql-connector-java-8.0.29.jar' dentro la directory './compose/'" > compose/README.first


  elif [ "${DB:-hsql}" == 'oracle' ]
  then
    mkdir -p compose/oracle_startup
    mkdir compose/ORADATA
    chmod 777 compose/ORADATA
    cat - << EOSQL > compose/oracle_startup/create_db_and_user.sql
alter session set container = GOVPAYPDB;
-- USER GOVPAY
CREATE USER "GOVPAY" IDENTIFIED BY "GOVPAY"  
DEFAULT TABLESPACE "USERS"
TEMPORARY TABLESPACE "TEMP";
ALTER USER "GOVPAY" QUOTA UNLIMITED ON "USERS";
GRANT "CONNECT" TO "GOVPAY" ;
GRANT "RESOURCE" TO "GOVPAY" ;
GRANT CREATE VIEW TO "GOVPAY" ;
EOSQL

    cat - << EOYAML >> compose/docker-compose.yaml
        # Il driver deve essere compiato manualmente nella directory corrente
        - ./ojdbc10.jar:/tmp/ojdbc10.jar 
    environment:
        - GOVPAY_DB_SERVER=or_govpay_${SHORT}
        - GOVPAY_DB_NAME=GOVPAYPDB
        - GOVPAY_DB_USER=GOVPAY
        - GOVPAY_DB_PASSWORD=GOVPAY
        - GOVPAY_ORACLE_JDBC_PATH=/tmp/ojdbc10.jar
        - GOVPAY_ORACLE_JDBC_URL_TYPE=servicename
        - GOVPAY_POP_DB_SKIP=false
        # il container oracle puo impiegare anche 20 minuti ad avviarsi
        - GOVPAY_LIVE_DB_CHECK_MAX_RETRY=120
        - GOVPAY_READY_DB_CHECK_MAX_RETRY=600
  database:
    container_name: or_govpay_${SHORT}
    image: container-registry.oracle.com/database/enterprise:19.3.0.0
    shm_size: 2g
    ulimits:
      nofile: 65536
    environment:
      - ORACLE_PDB=GOVPAYPDB
      - ORACLE_PWD=123456
    volumes:
       - ./ORADATA:/opt/oracle/oradata
       - ./oracle_startup:/opt/oracle/scripts/startup
    ports:
       - 1521:1521
EOYAML
    echo 
    echo "ATTENZIONE: Copiare il driver jdbc Oracle 'ojdbc10.jar' dentro la directory './compose/'"
    echo
    echo "ATTENZIONE: Copiare il driver jdbc Oracle 'ojdbc10.jar' dentro la directory './compose/'" > compose/README.first
  fi
fi
exit 0
