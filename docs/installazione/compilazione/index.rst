.. _govpay_installazione:

=============
Compilazione
=============

Di seguito sono descritte le operazioni per la produzione dell'installer di GovPay a partire dal codice sorgente.

Requisiti:

- Apache Maven 3.8
- Java 11

Step operativi:

1. Eseguire il comando di `mvn -Denv=installer_template clean install`
2. Spostarsi nella cartella `src/main/resources/setup/`
3. Eseguire lo script `prepareSetup.sh`

E' possibile passare al comando maven di compilazione uno o piu' dei seguenti profili con il paramtero `-P`:

- `java-1.8`: compilazione in con JAVA 8
- `wildfly-18`: iniezione del jboss-deployment-structure.xml compatibile con wildfly 11 e 18


