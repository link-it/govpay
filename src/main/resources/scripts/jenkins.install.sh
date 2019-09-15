export JAVA_HOME=/opt/jdk1.8.0_191/
GOVPAY_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)


#####
## ESECUZIONE INSTALLER
#####

echo "Esecuzione dell'installer..."

pushd src/main/resources/setup/
dirname=$(ls -d target/*/)
cd $dirname

sed -i -r -e 's/<installer (.*)/<installer ui="text-auto" loadDefaults="true" \1/'  installer/setup/antinstall-config.xml

echo "
antinstaller_dbusername=govpay
antinstaller_principal=gpadmin
antinstaller_as=wildfly11
antinstaller_domain_port=8080
porta-db=5432
basedir=/home/nardi/github/govpay/src/main/resources/setup/target/govpay-installer-${GOVPAY_VERSION}/./installer/setup
antinstaller_tipo_database=postgresql
antinstaller_dbport=5432
Fine-targets=setup_completo,
antinstaller_work_folder=/etc/govpay
antinstaller_hibernate_dialect=org.hibernate.dialect.PostgreSQLDialect
protocollo=http
antinstaller_log_folder=/var/log/govpay
antinstaller_dbhost=127.0.0.1
antinstaller_product_version=3.1
antinstaller_driver_jdbc=org.postgresql.Driver
antinstaller_domain_name=localhost
antinstaller_ragione_sociale=GovPay Administrator
antinstaller_cod_univoco=00000000000ADMIN
ant.install.config.version=0.0
antinstaller_dbpassword=govpay
antinstaller_dbname=govpay
TABLESPACE=openspcoop2
" >> ant.install.properties

sh install.sh text-auto

#####
## SETUP DB
#####

echo "Creazione del database..."
sudo -u postgres createdb govpay -O govpay
psql govpay govpay < dist/sql/gov_pay.sql

#####
## SETUP API SECURITY SETTINGS
#####

echo "Abilitazione delle modalita di autenticazione header e basic..."

GOVPAY_WORK_DIR="govpay_ear_tmp"
GOVPAY_SRC_DIR="dist/archivi/"
GOVPAY_EAR_NAME="govpay.ear"
GOVPAY_TMP_DIR="GOVPAY_EAR"
APP_CONTEXT_BASE_DIR="WEB-INF"
APP_CONTEXT_SECURITY_XML=$APP_CONTEXT_BASE_DIR"/applicationContext-security.xml"
rm -rf $GOVPAY_WORK_DIR
mkdir $GOVPAY_WORK_DIR
cp $GOVPAY_SRC_DIR$GOVPAY_EAR_NAME $GOVPAY_WORK_DIR 
pushd $GOVPAY_WORK_DIR
unzip -d $GOVPAY_TMP_DIR $GOVPAY_EAR_NAME
pushd $GOVPAY_TMP_DIR

# API-Backoffice
API_BACKOFFICE_WAR="api-backoffice-"
unzip $API_BACKOFFICE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
sed -i -e "s#SPID_START#SPID_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#SPID_END#<!-- SPID_END#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML
zip -r $API_BACKOFFICE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
rm -rf $APP_CONTEXT_BASE_DIR

# API-Pendenze
API_PENDENZE_WAR="api-pendenze-"
unzip $API_PENDENZE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML
zip -r $API_PENDENZE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
rm -rf $APP_CONTEXT_BASE_DIR

# API-Ragioneria
API_RAGIONERIA_WAR="api-ragioneria-"
unzip $API_RAGIONERIA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML
zip -r $API_RAGIONERIA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
rm -rf $APP_CONTEXT_BASE_DIR

# API-Pagamento
API_PAGAMENTO_WAR="api-pagamento-"
unzip $API_PAGAMENTO_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
sed -i -e "s#SPID_START#SPID_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#SPID_END#<!-- SPID_END#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#PUBLIC_START#PUBLIC_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#PUBLIC_END#<!-- PUBLIC_END#g" $APP_CONTEXT_SECURITY_XML
zip -r $API_PAGAMENTO_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
rm -rf $APP_CONTEXT_BASE_DIR

# API-PagoPA
API_PAGOPA_WAR="api-pagopa-"
unzip $API_PAGOPA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
sed -i -e "s#BASIC_START#BASIC_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#BASIC_END#<!-- BASIC_END#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_SECURITY_XML
zip -r $API_PAGOPA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML
rm -rf $APP_CONTEXT_BASE_DIR

zip -r $GOVPAY_EAR_NAME *
popd
mv $GOVPAY_TMP_DIR/$GOVPAY_EAR_NAME .
rm -rf $GOVPAY_TMP_DIR
popd

echo "Deploy govpay in wildfly...";
sudo cp $GOVPAY_WORK_DIR/$GOVPAY_EAR_NAME /opt/wildfly-11.0.0.Final/standalone_govpay/deployments/

rm -rf $GOVPAY_WORK_DIR
