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
antinstaller_log_level=DEBUG
antinstaller_dbhost=127.0.0.1
antinstaller_product_version=${GOVPAY_VERSION}
antinstaller_driver_jdbc=org.postgresql.Driver
antinstaller_domain_name=localhost
antinstaller_ragione_sociale=GovPay Administrator
antinstaller_cod_univoco=00000000000ADMIN
ant.install.config.version=0.0
antinstaller_dbpassword=govpay
antinstaller_dbname=govpay
antinstaller_springsec_ext=false
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

echo "Abilitazione delle modalita di autenticazione..."

sh ../../../scripts/abilitaAuth.sh -v ${GOVPAY_VERSION} -bo spid,header,ssl,basic -pag public,spid,header,ssl,basic -rag spid,header,ssl,basic -pen spid,header,ssl,basic -pp basic -src dist/archivi/

echo "Deploy govpay in wildfly...";
sudo cp dist/archivi/govpay.ear /opt/wildfly-11.0.0.Final/standalone_govpay/deployments/
rm -rf govpay_ear_tmp 
