VERSION=v2.0.RC1

# Directory
COPYING_FILE=../../COPYING
SQL=../../resources/sql/2.0/
DATASOURCE=../../resources/datasource/
DOC=../../resources/doc/
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
cp ${SQL}/init.sql core.template/installer/sql/
echo "Prepare sql script [completed]"

# Prepare Datasource
echo "Prepare datasource ..."
mkdir -p core.template/installer/datasource
if [ ! -d "${DATASOURCE}" ]
then
	echo "Directory contenente gli script sql non esistente"
	exit 2
fi
cp -r ${DATASOURCE} core.template/installer/
echo "Prepare datasource [completed]"

# Prepare DOC
echo "Prepare doc ..."
mkdir -p core.template/docs
if [ ! -e "${DOC}/GovPay-ManualeInstallazione.pdf" ]
then
        echo "Manuale di Installazione non esistente"
        exit 3
fi
cp ${DOC}/GovPay-ManualeInstallazione.pdf core.template/docs/
if [ ! -e "${DOC}/GovPay-ManualeIntegrazione.pdf" ]
then
        echo "Manuale di Integrazione non esistente"
        exit 4
fi 
cp ${DOC}/GovPay-ManualeIntegrazione.pdf core.template/docs/
if [ ! -e "${DOC}/GovPay-ManualeUtente.pdf" ]
then
        echo "Manuale Utente non esistente"
        exit 5
fi 
cp ${DOC}/GovPay-ManualeUtente.odt core.template/docs/
if [ ! -e "${COPYING_FILE}" ]
then
        echo "Copying file non esistente"
        exit 6
fi 
cp ${COPYING_FILE} core.template/
cp core.template/docs/README.txt core.template/
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
mv core.template govpay-installer-${VERSION}
tar czf govpay-installer-${VERSION}.tgz govpay-installer-${VERSION}/
rm -rf govpay-installer-${VERSION}/
echo "Creazione archivio compresso [completed]"
