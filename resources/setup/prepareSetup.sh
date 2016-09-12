VERSION=2.2-p4

# Directory
COPYING_FILE=../../COPYING
SQL=../../resources/db/sql/
DATASOURCE=../../resources/db/datasource/
DOC=../../resources/doc/pdf
GOVPAY=../../govpay-ear/target/govpay.ear
GOVPAY_CONSOLE=../../govpay-web-console/target/govpayConsole.war

# Template
rm -rf core.template
cp -r core core.template
find core.template/installer/setup/antinstall-config.xml -type f -exec perl -pi -e "s#PRODUCT_VERSION#${VERSION}#g" {} \;

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

# Prepare DOC
echo "Prepare doc ..."
mkdir -p core.template/doc
if [ ! -e "${DOC}/GovPay-ManualeInstallazione.pdf" ]
then
        echo "Manuale di Installazione non esistente"
        exit 3
fi
cp ${DOC}/GovPay-ManualeInstallazione.pdf core.template/doc/
if [ ! -e "${DOC}/GovPay-ManualeIntegrazioneSOAP.pdf" ]
then
        echo "Manuale di Integrazione SOAP non esistente"
        exit 4
fi 
cp ${DOC}/GovPay-ManualeIntegrazioneSOAP.pdf core.template/doc/
if [ ! -e "${DOC}/GovPay-ManualeIntegrazioneREST.pdf" ]
then
        echo "Manuale di Integrazione REST non esistente"
        exit 4
fi
cp ${DOC}/GovPay-ManualeIntegrazioneREST.pdf core.template/doc/

if [ ! -e "${DOC}/GovPay-ManualeUtente.pdf" ]
then
        echo "Manuale Utente non esistente"
        exit 5
fi 
cp ${DOC}/GovPay-ManualeUtente.pdf core.template/doc/
if [ ! -e "${DOC}/GovPay-PagoPA.pdf" ]
then
        echo "Manuale di Introduzione a GovPay non esistente"
        exit 5
fi
cp ${DOC}/GovPay-PagoPA.pdf core.template/doc/
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
if [ ! -e "${GOVPAY}" ]
then
	echo "Software GovPay.ear non trovato"
	exit 6
fi
#unzip -q ${GOVPAY} -d core.template/installer/archivi/govpay.ear
cp ${GOVPAY} core.template/installer/archivi/
if [ ! -e "${GOVPAY_CONSOLE}" ]
then
	echo "Software GovPay console non trovato"
	exit 7
fi
#unzip -q ${GOVPAY_CONSOLE} -d core.template/installer/archivi/govpayConsole.war
cp ${GOVPAY_CONSOLE} core.template/installer/archivi/
echo "Prepare archivi [completed]"

echo "Creazione archivio compresso ..."
rm -rf target
mkdir target 
mv core.template govpay-installer-${VERSION}
tar czf target/govpay-installer-${VERSION}.tgz govpay-installer-${VERSION}/
mv govpay-installer-${VERSION}/ target/
echo "Creazione archivio compresso [completed]"
