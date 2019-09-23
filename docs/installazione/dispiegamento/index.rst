.. _inst_dispiegamento:

Fase di Dispiegamento
======================

Al termine dell’esecuzione dell’utility di installazione vengono
prodotti i files necessari per effettuare il dispiegamento nell’ambiente
di esercizio. Tali files sono disponibili nella directory **dist**
prodotta dall’utility.

Per il dispiegamento nell'ambiente di destinazione devono essere
effettuati i seguenti passi:

1. Creare un utente sul RDBMS avente i medesimi valori di username e
   password indicati in fase di setup.
2. Creare un database, per ospitare le tabelle dell’applicazione, avente
   il nome indicato durante la fase di setup. Il charset da utilizzare è
   UTF-8.
3. Impostare i permessi di accesso in modo che l’utente creato al passo
   1 abbia i diritti di lettura/scrittura sul database creato al **passo
   2**.
4. Garantire la raggiungibilità dell'application server al RDBMS
   indicato in fase di setup.
5. Eseguire lo script **sql/gov_pay.sql** per la creazione dello schema
   del database. Ad esempio, nel caso di PostgreSQL, si potrà eseguire
   il comando:

   -  **psql -h <hostname> -d <database> -U <username> -f sql/gov_pay.sql**

6. In riferimento al valore indicato come "Username
   Amministratore", creare l’utenza
   applicativa sull'application server che
   rappresenti l'amministratore di GovPay. Per farlo è possibile
   utilizzare lo script presente nella distribuzione di WildFly
   in ./bin/add-user.sh o ./bin/add-user.bat, fornendo i
   seguenti parametri:

   -  *Type of user*: indicare b) Application User
   -  *Realm*: lasciare il valore di default
   -  *Username*: utenza amministratore di GovPay indicata durante
      l'esecuzione dell'Installer (es. Gpadmin)
   -  *Password*: password associata all'utenza
   -  *Roles*: lasciare il valore di default
   -  *Group:* lasciare il valore di default
   -  *Is this new user going to be used for one AS process to connect
      to another AS process?: Indicare “no”*.

7. Copiare il file **datasource/govpay-ds.xml**, contenente la
   definizione del datasource, nella directory
   **<JBOSS_HOME>/standalone/deployments** dell'application server.
8. Copiare le applicazioni presenti nella directory **archivi** nella
   directory **<JBOSS_HOME>/standalone/deployments** dell'application server.
9. Installare il DriverJDBC, relativo al tipo di RDBMS indicato in fase
   di setup, nella directory **<JBOSS_HOME>/standalone/deployments** dell'application server.
10. Editare i datasources installati al **punto 7**. sostituendo la
   keyword **NOME_DRIVER_JDBC.jar** con il nome del file corrispondente
   al driver jdbc.
11. Verificare che la directory di lavoro e quella di log di GovPay,
   inserite in fase di configurazione, esistano o altrimenti crearle con
   permessi tali da consentire la scrittura all’utente di esecuzione del
   processo java dell’application server.
12. Avviare l'application server (ad esempio su Linux con il comando
   **<JBOSS_HOME>/bin/standalone.sh** oppure utilizzando il relativo
   service).

