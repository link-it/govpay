export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
GOVPAY_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)

# Radice del progetto: piu' sotto si entra nella directory dell'installer e non
# si torna indietro, quindi i percorsi relativi non valgono piu'.
GOVPAY_ROOT=$(pwd)


#####
## ESECUZIONE INSTALLER
#####

echo "Esecuzione dell'installer..."

pushd src/main/resources/setup/
dirname=$(ls -d target/*/)
cd $dirname

sed -i -r -e 's/<installer (.*)/<installer ui="text-auto" loadDefaults="true" \1/'  installer/setup/antinstall-config.xml

echo "
antinstaller_jndi_name=java:comp/env/it.govpay.datasource
antinstaller_dbusername=govpay
antinstaller_principal=gpadmin
antinstaller_principal_pwd=Password1!
antinstaller_ragione_sociale=amministratore
antinstaller_as=tomcat11
porta-db=5432
basedir=/home/nardi/github/govpay/src/main/resources/setup/target/govpay-installer-${GOVPAY_VERSION}/./installer/setup
antinstaller_tipo_database=postgresql
antinstaller_dbport=5432
Fine-targets=setup_completo,
antinstaller_work_folder=/etc/govpay
antinstaller_hibernate_dialect=org.hibernate.dialect.PostgreSQLDialect
antinstaller_log_folder=/var/log/govpay
antinstaller_log_level=DEBUG
antinstaller_dbhost=127.0.0.1
antinstaller_product_version=${GOVPAY_VERSION}
antinstaller_driver_jdbc=org.postgresql.Driver
antinstaller_ragione_sociale=GovPay Administrator
antinstaller_cod_univoco=00000000000ADMIN
ant.install.config.version=0.0
antinstaller_dbpassword=govpay
antinstaller_dbname=govpay
antinstaller_springsec_ext=true
antinstaller_modulo_postgres=org.postgresql
TABLESPACE=openspcoop2
" >> ant.install.properties

sh install.sh text-auto

#####
## RACCOLTA SQL DEI COMPONENTI
#####

# Compone in un unico script lo SQL dei componenti del rilascio, scaricando
# l'asset sql.zip dalle loro GitHub Release. Sostituisce i file che erano
# copiati a mano in /etc/govpay/docker/<versione>/sql/ (batch-aca.sql,
# batch-fdr.sql, tabelle_batch-create.sql): le versioni ora stanno in
# src/main/resources/db/release-components.env, versionato.
#
# Modalita' componenti: il core NON e' incluso, perche' lo applica l'installer
# qui sotto con dist/sql/gov_pay.sql, che e' il file dell'artefatto in prova.
# Includerlo anche qui duplicherebbe lo schema.
#
# Richiede solo curl e unzip: i repository GovPay sono pubblici e gli asset si
# scaricano in anonimo, senza gh e senza token. Le tabelle di Spring Batch sono
# deduplicate dallo script, perche' ripetute in ogni batch.

echo "Composizione dello SQL dei componenti..."

SQL_COMPONENTI_DIR=${GOVPAY_ROOT}/target/release-sql
bash ${GOVPAY_ROOT}/src/main/resources/db/collect-release-sql.sh \
  --core "${GOVPAY_VERSION}" \
  --mode componenti \
  --dialects postgresql \
  --out ${SQL_COMPONENTI_DIR}
SQL_COMPONENTI=${SQL_COMPONENTI_DIR}/govpay-${GOVPAY_VERSION}-componenti-postgresql.sql

#####
## SETUP DB
#####

echo "Creazione del database..."
sudo -u postgres createdb govpay -O govpay

# ON_ERROR_STOP e' necessario: senza, psql esce 0 anche dopo un errore, quindi un
# CREATE TABLE in conflitto o un componente con SQL rotto lascerebbe lo stage verde
# e il database incompleto, e il guasto emergerebbe piu' tardi nella testsuite su
# qualcosa di apparentemente scollegato. Lo script non ha set -e, per cui il codice
# di uscita va controllato qui.
psql -v ON_ERROR_STOP=1 govpay govpay < dist/sql/gov_pay.sql \
  || { echo "ERRORE: applicazione dello schema del core fallita" >&2; exit 1; }

echo "Creazione tabelle dei componenti e di Spring Batch"
psql -v ON_ERROR_STOP=1 govpay govpay < ${SQL_COMPONENTI} \
  || { echo "ERRORE: applicazione dello SQL dei componenti fallita" >&2; exit 1; }
  
echo "Creazione utenza per batch rt"
psql -v ON_ERROR_STOP=1 govpay govpay < /etc/govpay/docker/${GOVPAY_VERSION}/sql/utenza-batch-rt.sql \
  || { echo "ERRORE: applicazione dello SQL creazione utenza batch rt fallita" >&2; exit 1; }

#####
## SETUP API SECURITY SETTINGS
#####

# echo "Abilitazione delle modalita di autenticazione..."

# sh ../../../scripts/abilitaAuthTomcat.sh -v ${GOVPAY_VERSION} -bo spid,header,ssl,basic,apikey -pag public,spid,header,ssl,basic,apikey -rag spid,header,ssl,basic,apikey -pen spid,header,ssl,basic,apikey -pp basic -jppa basic -src dist/archivi/

echo "Deploy govpay in wildfly...";
sudo cp dist/archivi/govpay*.war /opt/apache-tomcat-govpay/webapps/
rm -rf govpay_ear_tmp
