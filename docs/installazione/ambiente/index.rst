.. _inst_ambiente:

Ambiente e architettura di riferimento
======================================

L'ambiente di esecuzione di GovPay richiede la presenza di software di
base, precedentemente installato i cui riferimenti sono:

-  JVM Java 21
-  Application Server Apache Tomcat 11

Relativamente alla versione di RDBMS, l'attuale versione di
GovPay consente di selezionare tra i seguenti:

-  PostgreSQL
-  MySQL (o MariaDB)
-  Oracle
-  SQL Server
-  HyperSQL

La versione 3.10 è verificata con PostgreSQL 16, MySQL 8.4, Oracle 23,
SQL Server 2022 e HyperSQL 2.7.

Componenti esterni
------------------

Alcune funzionalità sono svolte da componenti distribuiti separatamente
dal core, descritti in :ref:`inst_componenti_aggiuntivi`. I componenti
minimi necessari sono:

- **govpay-fdr-batch**: acquisizione dei flussi di rendicontazione da pagoPA;
- **govpay-aca-batch**: invio a pagoPA delle posizioni debitorie (ACA);
- **govpay-rt-batch**: recupero delle ricevute mancanti, svolto solo da
  questo componente;
- **govpay-gde-api**: il Giornale degli Eventi dei componenti esterni.
