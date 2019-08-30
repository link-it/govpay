pipeline {
  agent any
  stages {
    stage('compile') {
      steps {
        sh 'mvn clean install -Denv=installer_template'
      }
    }
    stage('build') {
      steps {
        sh '''GOVPAY_VERSION=$(mvn -q \\
    -Dexec.executable=echo \\
    -Dexec.args=\'${project.version}\' \\
    --non-recursive \\
    exec:exec)

echo "Post compilazione govpay v.$GOVPAY_VERSION"

echo "Shutdown wildfly" 

sudo systemctl stop wildfly@govpay

echo "Creazione dell\'installer"

pushd src/main/resources/setup/

sh prepareSetup.sh

echo "Esecuzione dell\'installer"

dirname=$(ls -d target/*/)
cd $dirname

sed -i -r -e \'s/<installer (.*)/<installer ui="text-auto" loadDefaults="true" \\1/\'  installer/setup/antinstall-config.xml

cp /etc/govpay/ant.install.properties .

sh install.sh text-auto

echo "Setup database GovPay"

sudo -u postgres dropdb --if-exists govpay
sudo -u postgres createdb govpay -O govpay
psql govpay govpay < dist/sql/gov_pay.sql

echo "Cleanup database NdpSym"

sudo rm -rf /var/govpay-ndpsym/cache/*.cache
sudo rm -rf /var/govpay-ndpsym/cache/RH/*
psql ndpsym ndpsym < /etc/govpay/ndpsym-clean.sql

echo "Configurazione govpay.ear per l\'esecuzione della testsuite in corso...";

GOVPAY_WORK_DIR="govpay_ear_tmp"
GOVPAY_SRC_DIR="dist/archivi/"
GOVPAY_EAR_NAME="govpay.ear"
GOVPAY_TMP_DIR="GOVPAY_EAR"

APP_CONTEXT_BASE_DIR="WEB-INF"
APP_CONTEXT_SECURITY_XML=$APP_CONTEXT_BASE_DIR"/applicationContext-security.xml"

#echo "Creazione directory di lavoro" $GOVPAY_WORK_DIR;

rm -rf $GOVPAY_WORK_DIR

mkdir $GOVPAY_WORK_DIR


#echo "Creazione directory di lavoro" $GOVPAY_WORK_DIR "completata, copia archivio originale nella directory di lavoro...";

cp $GOVPAY_SRC_DIR$GOVPAY_EAR_NAME $GOVPAY_WORK_DIR 

pushd $GOVPAY_WORK_DIR

#echo "estrazione dei war da modificare dal file" $GOVPAY_EAR_NAME

unzip -d $GOVPAY_TMP_DIR $GOVPAY_EAR_NAME

pushd $GOVPAY_TMP_DIR

# API-Backoffice
API_BACKOFFICE_WAR="api-backoffice-"

# estrazione del file applicationContext-security.xml
unzip $API_BACKOFFICE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# sblocco delle modalita\' di autenticazione previste
#echo "API-Backoffice abilitazione autenticazione SPID...";

sed -i -e "s#SPID_START#SPID_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#SPID_END#<!-- SPID_END#g" $APP_CONTEXT_SECURITY_XML

#echo "API-Backoffice abilitazione autenticazione SPID completata.";

#echo "API-Backoffice abilitazione HTTP Header-auth ...";

sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML

#echo "API-Backoffice abilitazione HTTP Header-auth completata.";

# ripristino file
zip -r $API_BACKOFFICE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# eliminazione dei file temporanei
rm -rf $APP_CONTEXT_BASE_DIR

# API-Pendenze

API_PENDENZE_WAR="api-pendenze-"

# estrazione del file applicationContext-security.xml
unzip $API_PENDENZE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# sblocco delle modalita\' di autenticazione previste
#echo "API-Pendenze abilitazione HTTP Header-auth ...";

sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML

#echo "API-Pendenze abilitazione HTTP Header-auth  completata.";

# ripristino file
zip -r $API_PENDENZE_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# eliminazione dei file temporanei
rm -rf $APP_CONTEXT_BASE_DIR

# API-Ragioneria

API_RAGIONERIA_WAR="api-ragioneria-"

# estrazione del file applicationContext-security.xml
unzip $API_RAGIONERIA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# sblocco delle modalita\' di autenticazione previste

#echo "API-Ragioneria abilitazione HTTP Header-auth ...";

sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML

#echo "API-Ragioneria abilitazione HTTP Header-auth  completata.";

# ripristino file
zip -r $API_RAGIONERIA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# eliminazione dei file temporanei
rm -rf $APP_CONTEXT_BASE_DIR

# API-Pagamento

API_PAGAMENTO_WAR="api-pagamento-"

# estrazione del file applicationContext-security.xml
unzip $API_PAGAMENTO_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# sblocco delle modalita\' di autenticazione previste

#echo "API-Pagamento abilitazione autenticazione SPID...";

sed -i -e "s#SPID_START#SPID_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#SPID_END#<!-- SPID_END#g" $APP_CONTEXT_SECURITY_XML

#echo "API-Pagamento abilitazione autenticazione SPID completata.";

#echo "API-Pagamento abilitazione HTTP Header-auth ...";

sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_SECURITY_XML

#echo "API-Pagamento abilitazione HTTP Header-auth  completata.";

#echo "API-Pagamento abilitazione pagamenti in forma anonima...";

sed -i -e "s#PUBLIC_START#PUBLIC_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#PUBLIC_END#<!-- PUBLIC_END#g" $APP_CONTEXT_SECURITY_XML

#echo "API-Pagamento abilitazione pagamenti in forma anonima completata.";

# ripristino file
zip -r $API_PAGAMENTO_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# eliminazione dei file temporanei
rm -rf $APP_CONTEXT_BASE_DIR


# API-PagoPA

#echo "API-PagoPA abilitazione HTTP Basic-auth...";

API_PAGOPA_WAR="api-pagopa-"

# estrazione del file applicationContext-security.xml
unzip $API_PAGOPA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# spengo la modalita\' ssl 
sed -i -e "s#BASIC_START#BASIC_START -->#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#BASIC_END#<!-- BASIC_END#g" $APP_CONTEXT_SECURITY_XML

# accendo modalita basic
sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_SECURITY_XML
sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_SECURITY_XML

# ripristino file
zip -r $API_PAGOPA_WAR$GOVPAY_VERSION.war $APP_CONTEXT_SECURITY_XML

# eliminazione dei file temporanei
rm -rf $APP_CONTEXT_BASE_DIR

#echo "API-PagoPA abilitazione HTTP Basic-auth completata.";

zip -r $GOVPAY_EAR_NAME *

popd

mv $GOVPAY_TMP_DIR/$GOVPAY_EAR_NAME .
rm -rf $GOVPAY_TMP_DIR

popd

echo "Deploy govpay";

sudo cp $GOVPAY_WORK_DIR/$GOVPAY_EAR_NAME /opt/wildfly-11.0.0.Final/standalone_govpay/deployments/

#echo "Configurazione govpay.ear per l\'esecuzione della testsuite completata, pulizia directory temporanee...";

rm -rf $GOVPAY_WORK_DIR

#echo "Configurazione govpay.ear per l\'esecuzione della testsuite completata"

#echo "Configurazione govpay.ear per l\'esecuzione della testsuite completata, l\'archivio si trova all\'interno della directory" $GOVPAY_SRC_DIR;

echo "Startup wildfly" 


'''
      }
    }
    stage('deploy') {
      steps {
        sh '''sudo systemctl start wildfly@govpay

sh /etc/govpay/check-govpay-services.sh'''
      }
    }
    stage('integration-test') {
      steps {
        sh '''cd ./integration-test 
/var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn clean test'''
      }
    }
  }
}
