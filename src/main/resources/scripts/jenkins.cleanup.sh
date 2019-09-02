echo "Shutdown wildfly..."
sudo systemctl stop wildfly@govpay
echo "Rimozione dati del simulatore pagoPA..."
sudo rm -rf /var/govpay-ndpsym/cache/*.cache
sudo rm -rf /var/govpay-ndpsym/cache/RH/*
psql ndpsym ndpsym -c "delete from rpt;"
psql ndpsym ndpsym -c "delete from wisp;"
echo "Rimozione database e deploy di govpay..."
sudo -u postgres dropdb --if-exists govpay
sudo rm /opt/wildfly-11.0.0.Final/standalone_govpay/deployments/govpay.ear
