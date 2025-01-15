#!/bin/bash

# Verifica se il parametro AS è stato passato come argomento, altrimenti usa un valore predefinito
if [ -z "$1" ]; then
  echo "Parametro AS non fornito, valori disponibili [ear|tomcat]. Utilizzo valore predefinito 'ear'"
  AS="ear"  # Imposta un valore di default
else
  AS="$1"  # Imposta il valore passato come argomento
fi

VERSION=$(mvn -f ../../../../pom.xml -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)
echo "Creazione installer GovPay v.${VERSION}"
# Non e' piu' possibile eseguire la compilazione da qua poiche' 
# il plugin di maven per la generazione dei bean da xsd non risolve
# correttamente i path relativi.

# mvn -f ../../../../pom.xml -Denv=installer_template clean package 

# Directory
COPYING_FILE=../../../../COPYING
SQL=../../resources/db/sql/
DATASOURCE=../../resources/db/datasource/
DOC=../../resources/doc/pdf
GOVPAY=../../../../ear/target/govpay.ear
GOVPAY_BO=../../../../wars/api-backoffice/target/govpay-api-backoffice.war
GOVPAY_JPPA=../../../../wars/api-jppapdp/target/govpay-api-jppapdp.war
GOVPAY_PAG=../../../../wars/api-pagamento/target/govpay-api-pagamento.war
GOVPAY_PP=../../../../wars/api-pagopa/target/govpay-api-pagopa.war
GOVPAY_PEN=../../../../wars/api-pendenze/target/govpay-api-pendenze.war
GOVPAY_RAG=../../../../wars/api-ragioneria/target/govpay-api-ragioneria.war
GOVPAY_USR=../../../../wars/api-user/target/govpay-api-user.war
GOVPAY_WC=../../../../wars/web-connector/target/govpay-web-connector.war
GOVPAY_CONSOLE=../../../../wars/web-console/target/govpay-console.war


# Template
rm -rf core.template
cp -r core core.template
#find core.template/installer/setup/antinstall-config.xml -type f -exec perl -pi -e "s#PRODUCT_VERSION#${VERSION}#g" {} \;
perl -pi -e "s#PRODUCT_VERSION#${VERSION}#g" core.template/installer/setup/antinstall-config.xml

if [ "$AS" = "ear" ]; then
    # Se AS è 'ear', valorizza la variabilie APPLICATION_SERVER come wildfly28
    perl -pi -e "s#APPLICATION_SERVER#wildfly28#g" core.template/installer/setup/antinstall-config.xml
elif [ "$AS" = "tomcat" ]; then
    # Se AS è 'tomcat', valorizza la variabilie APPLICATION_SERVER come tomcat11
    perl -pi -e "s#APPLICATION_SERVER#tomcat11#g" core.template/installer/setup/antinstall-config.xml
else
    echo "Parametro AS non valido. Valori accettati: ear | tomcat"
    exit 8
fi

if [ "$AS" = "ear" ]; then
    # Se AS è 'ear', valorizza la variabilie JNDI_NAME come govpay
    perl -pi -e "s#JNDI_NAME#govpay#g" core.template/installer/setup/antinstall-config.xml
elif [ "$AS" = "tomcat" ]; then
    # Se AS è 'tomcat', valorizza la variabilie JNDI_NAME come java:comp/env/it.govpay.datasource
    perl -pi -e "s#JNDI_NAME#java:comp/env/it.govpay.datasource#g" core.template/installer/setup/antinstall-config.xml
else
    echo "Parametro AS non valido. Valori accettati: ear | tomcat"
    exit 8
fi

# Prepare SQL
echo "Prepare sql script ..."
mkdir -p core.template/installer/sql/
if [ ! -d "${SQL}" ]
then
	echo "Directory contenente gli script sql non esistente"
	exit 1
fi
cp -r ${SQL}/* core.template/installer/sql/
rm -f core.template/installer/sql/*.sql
rm -f core.template/installer/sql/*/delete.sql
rm -f core.template/installer/sql/*/drop.sql
cp ${SQL}/init.sql core.template/installer/sql/
echo "Prepare sql script [completed]"

# Prepare Datasource
echo "Prepare datasource ..."
mkdir -p core.template/installer/datasource
if [ ! -d "${DATASOURCE}" ]
then
	echo "Directory contenente i datasource non esistente"
	exit 2
fi
cp -r ${DATASOURCE} core.template/installer/
echo "Prepare datasource [completed]"

if [ ! -e "${COPYING_FILE}" ]
then
        echo "Copying file non esistente"
        exit 6
fi 
cp ${COPYING_FILE} core.template/
mv core.template/doc/README.txt core.template/
echo "Prepare doc [completed]"

# Prepare SOFTWARE
echo "Prepare archivi ..."
mkdir -p core.template/installer/archivi/

if [ "$AS" = "ear" ]; then
    # Se AS è 'ear', copia il file .ear
    if [ ! -e "${GOVPAY}" ]; then
        echo "Software GovPay.ear non trovato"
        exit 6
    fi
    cp ${GOVPAY} core.template/installer/archivi/
    echo "GovPay.ear copiato"

elif [ "$AS" = "tomcat" ]; then
    # Se AS è 'tomcat', verifica e copia tutti i war
    WAR_FILES=("${GOVPAY_BO}" "${GOVPAY_JPPA}" "${GOVPAY_PAG}" "${GOVPAY_PP}" "${GOVPAY_PEN}" "${GOVPAY_RAG}" "${GOVPAY_USR}" "${GOVPAY_WC}" "${GOVPAY_CONSOLE}")

    for WAR_FILE in "${WAR_FILES[@]}"; do
        if [ ! -e "${WAR_FILE}" ]; then
            echo "File WAR non trovato: ${WAR_FILE}"
            exit 7
        fi
        cp "${WAR_FILE}" core.template/installer/archivi/
        echo "Copiato ${WAR_FILE}"
    done
else
    echo "Parametro AS non valido. Valori accettati: ear | tomcat"
    exit 8
fi
echo "Prepare archivi [completed]"

echo "Creazione archivio compresso ..."
rm -rf target
mkdir target 
mv core.template govpay-installer-${VERSION}
tar czf target/govpay-installer-${VERSION}.tgz govpay-installer-${VERSION}/
mv govpay-installer-${VERSION}/ target/
echo "Creazione archivio compresso [completed]"
