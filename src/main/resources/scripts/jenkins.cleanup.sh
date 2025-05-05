echo "Shutdown tomcat e wildfly..."
sudo systemctl stop wildfly@govpay wildfly-26.1.3.Final@standalone tomcat_govpay wildfly-26.1.3.Final@ndpsym
echo "Rimozione dati del simulatore pagoPA..."
sudo rm -rf /var/govpay-ndpsym/cache/*.cache
sudo rm -rf /var/govpay-ndpsym/cache/RH/*
psql ndpsym ndpsym -c "delete from rpt;"
psql ndpsym ndpsym -c "delete from wisp;"
psql ndpsym ndpsym -c "update domini set versione = 1;"
echo "Rimozione database e deploy di govpay..."
sudo -u postgres dropdb --if-exists govpay
sudo rm -rf /opt/apache-tomcat-govpay/webapps/govpay*
sudo rm -rf /var/log/govpay/*
sudo rm -rf /opt/apache-tomcat-govpay/log/*
sudo rm -rf /var/log/govpay-ndp-sym/*
echo "Rimozione file jacoco.exec..."
sudo rm -f /tmp/jacoco.exec
