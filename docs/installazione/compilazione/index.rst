.. _govpay_installazione:

=============
Compilazione
=============

Di seguito sono descritte le operazioni per la produzione dell'installer di GovPay a partire dal codice sorgente.

Requisiti:

- Apache Maven 3.8
- Java 21

Step operativi:

1. Eseguire il comando di `mvn -Denv=installer_template clean install`
2. Spostarsi nella cartella `src/main/resources/setup/`
3. Eseguire lo script `prepareSetup.sh`
