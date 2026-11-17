.. _inst_compilazione:

=============
Compilazione
=============

Di seguito sono descritte le operazioni per la produzione dell'installer di GovPay a partire dal codice sorgente.

Requisiti:

- Apache Maven 3.8 o successivo
- Java 21

Step operativi per Tomcat (distribuzione predefinita):

1. Clonare il repository: ``git clone https://github.com/link-it/govpay.git``
2. Eseguire il comando ``mvn -Denv=installer_template clean install``
3. Spostarsi nella cartella ``src/main/resources/setup/``
4. Eseguire lo script ``prepareSetup.sh``

Per WildFly la compilazione usa il profilo ``govpay-ear`` e lo script riceve il parametro ``ear``:

1. ``mvn -Denv=installer_template_wildfly clean install -P govpay-ear``
2. ``prepareSetup.sh ear`` dalla cartella ``src/main/resources/setup/``

Lo script produce l'archivio ``target/govpay-installer-<versione>.tgz`` e la stessa
cartella già scompattata in ``target/govpay-installer-<versione>/``, da cui si avvia
l'installer come descritto in :ref:`inst_configurazione`.
