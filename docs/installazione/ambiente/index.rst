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
dal core, descritti in :ref:`inst_componenti_aggiuntivi`. Per un
ambiente completo servono almeno:

- **govpay-rt-batch**: il recupero delle ricevute mancanti è svolto solo
  da questo componente e la sua URL è richiesta dall'installer;
- **govpay-gde-api**: il Giornale degli Eventi dei componenti esterni;
- **govpay-console** e **govpay-console-api**, se si adotta la nuova
  console di backoffice al posto di quella inclusa nel core.
