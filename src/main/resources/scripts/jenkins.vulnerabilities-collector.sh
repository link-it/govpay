echo "Colleziono tutte le vulnerabilita' trovate..."
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
echo "Branch corrente: $CURRENT_BRANCH"
CURRENT_COMMIT=$(git rev-parse HEAD)
echo "Commit reference: $CURRENT_COMMIT"
CURRENT_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)
echo "Versione corrente: $CURRENT_VERSION"
DIR_VERSIONE_GOVPAY_VERIFICATA=/opt/apache-tomcat-govpay-vulnerabilita/webapps/govpay-testsuite/${CURRENT_BRANCH}/${CURRENT_VERSION}/${CURRENT_COMMIT}
echo "Directory dove vengono salvate le evidenze dell'esecuzione corrente della testsuite: [${DIR_VERSIONE_GOVPAY_VERIFICATA}]"
sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}"

echo "Copia risultati testsuite..."
sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}/test_karate"
sudo su -c "cp integration-test/target/surefire-reports/*.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/test_karate/"

echo "Copia risultati spotbugs..."
sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis"

sudo su -c "cp jars/api-commons/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-commons.html"
sudo su -c "cp jars/appio-beans/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_appio-beans.html"
sudo su -c "cp jars/client-api-ente/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_client-api-ente.html"
sudo su -c "cp jars/core-beans/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_core-beans.html"
sudo su -c "cp jars/core/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_core.html"
sudo su -c "cp jars/jppapdp-beans/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_jppadpd-beans.html"
sudo su -c "cp jars/orm-beans/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_orm-beans.html"
sudo su -c "cp jars/orm/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_orm.html"
sudo su -c "cp jars/pagopa-beans/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_pagopa-beans.html"
sudo su -c "cp jars/stampe/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_stampe.html"
sudo su -c "cp jars/xml-adapters/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_xml-adapters.html"

sudo su -c "cp wars/api-backoffice/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-backoffice.html"
sudo su -c "cp wars/api-jppapdp/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-jppapdp.html"
sudo su -c "cp wars/api-pagamento/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-pagamento.html"
sudo su -c "cp wars/api-pagopa/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-pagopa.html"
sudo su -c "cp wars/api-pendenze/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-pendenze.html"
sudo su -c "cp wars/api-ragioneria/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-ragioneria.html"
sudo su -c "cp wars/api-user/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_api-user.html"
sudo su -c "cp wars/web-connector/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_web-connector.html"
sudo su -c "cp wars/web-console/target/spotbugs.html ${DIR_VERSIONE_GOVPAY_VERIFICATA}/static_analysis/spotbugs_report_web-console.html"

echo "Copia risultati dependency-check..."
sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}/owasp_dependency_check"
sudo su -c "cp target/dependency-check-* ${DIR_VERSIONE_GOVPAY_VERIFICATA}/owasp_dependency_check/"

echo "Copia risultati sonarqube..."
sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}/coverage"
sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}/coverage/xml"
sudo su -c "cp target/jacoco.xml ${DIR_VERSIONE_GOVPAY_VERIFICATA}/coverage/xml/report.xml"

sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}/coverage/csv"
sudo su -c "cp target/jacoco.csv ${DIR_VERSIONE_GOVPAY_VERIFICATA}/coverage/csv/report.csv"

sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_VERIFICATA}/coverage/html"
sudo su -c "cp -r target/jacoco-html/* ${DIR_VERSIONE_GOVPAY_VERIFICATA}/coverage/html/"

echo "Leggo commit message..."
COMMIT_MSG="$(git log -1 --pretty=%B)"
echo "commit message: ${COMMIT_MSG}"
COMMIT_MSG_TRIMMED="$(printf '%s' "$COMMIT_MSG" | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')"
echo "commit message trimmed: [${COMMIT_MSG_TRIMMED}]"

echo "Creo file metadati build..."
BUILD_TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
cat > /tmp/build-metadata.json <<EOF
{
  "version": "${CURRENT_VERSION}",
  "branch": "${CURRENT_BRANCH}",
  "commit": "${CURRENT_COMMIT}",
  "commitMessage": "${COMMIT_MSG_TRIMMED}",
  "buildTimestamp": "${BUILD_TIMESTAMP}",
  "buildDate": "$(date '+%Y-%m-%d')",
  "buildTime": "$(date '+%H:%M:%S')"
}
EOF
sudo su -c "cp /tmp/build-metadata.json ${DIR_VERSIONE_GOVPAY_VERIFICATA}/build-metadata.json"
rm /tmp/build-metadata.json

sudo su -c "chown -R tomcat:tomcat ${DIR_VERSIONE_GOVPAY_VERIFICATA}/"

DATA_VERSIONE_GOVPAY_IN_RILASCIO=$(date '+%Y_%m_%d')
echo "Data rilascio: ${DATA_VERSIONE_GOVPAY_IN_RILASCIO}"

if [[ "$CURRENT_BRANCH" == "master" ]]; then
    echo "Build sul branch master; salvo le evidenze dei test nella directory releases ..."
    DIR_VERSIONE_GOVPAY_IN_RILASCIO=/opt/apache-tomcat-govpay-vulnerabilita/webapps/govpay-releases/${CURRENT_VERSION}/${DATA_VERSIONE_GOVPAY_IN_RILASCIO}
    echo "Versione: [${CURRENT_VERSION}]"
    echo "Directory dove vengono salvate le evidenze: [${DIR_VERSIONE_GOVPAY_IN_RILASCIO}]"
    sudo su -c "mkdir -p ${DIR_VERSIONE_GOVPAY_IN_RILASCIO}"
    sudo su -c "cp -r ${DIR_VERSIONE_GOVPAY_VERIFICATA}/* ${DIR_VERSIONE_GOVPAY_IN_RILASCIO}/"
	sudo su -c "rm -f ${DIR_VERSIONE_GOVPAY_IN_RILASCIO}/WEB-INF/web.xml"
    sudo su -c "rmdir ${DIR_VERSIONE_GOVPAY_IN_RILASCIO}/WEB-INF"
    sudo su -c "chown -R tomcat:tomcat ${DIR_VERSIONE_GOVPAY_IN_RILASCIO}/"
else
	echo "Build non sul branch master; le evidenze sono salvate solo nella directory testsuite"
fi