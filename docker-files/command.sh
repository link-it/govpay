#!/bin/bash

until psql -h $POSTGRES_PORT_5432_TCP_ADDR -U $POSTGRES_ENV_POSTGRES_USER -c '\l'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

>&2 echo "Postgres is up - executing command"

psql -h $POSTGRES_PORT_5432_TCP_ADDR -U $POSTGRES_ENV_POSTGRES_USER -f /tmp/govpay/installer/sql/postgresql/gov_pay.sql
psql -h $POSTGRES_PORT_5432_TCP_ADDR -U $POSTGRES_ENV_POSTGRES_USER -f /tmp/govpay/installer/sql/init.sql

sed -i -e 's/@HOST@/'"$POSTGRES_PORT_5432_TCP_ADDR"'/g' -e 's/@PORT@/'"$POSTGRES_PORT_5432_TCP_PORT"'/g' \
 -e 's/@DATABASE@/'"$POSTGRES_ENV_POSTGRES_DATABASE"'/g' -e 's/@USERNAME@/'"$POSTGRES_ENV_POSTGRES_USER"'/g' \
 -e 's/@PASSWORD@/'"$PGPASSWORD"'/g' -e 's/NOME_DRIVER_JDBC.jar/'"$1"'/g' \
 /tmp/govpay/installer/datasource/postgresql/govpay-ds.xml
 
mkdir /var/govpay
mkdir /var/govpay/estrattoConto
mkdir /var/govpay/logo 

cp /tmp/govpay/installer/datasource/postgresql/govpay-ds.xml $JBOSS_HOME/standalone/deployments/
cp /tmp/govpay/installer/archivi/govpayConsole.war $JBOSS_HOME/standalone/deployments/
cp /tmp/govpay/installer/archivi/govpay.ear $JBOSS_HOME/standalone/deployments/

$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0

