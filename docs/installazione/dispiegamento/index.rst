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

6. Copiare il file **datasource/govpay-ds.xml**, contenente la
   definizione del datasource, nella directory
   **<JBOSS_HOME>/standalone/deployments** dell'application server.
7. Copiare le applicazioni presenti nella directory **archivi** nella
   directory **<JBOSS_HOME>/standalone/deployments** dell'application server.
8. Installare il DriverJDBC, relativo al tipo di RDBMS indicato in fase
   di setup, come modulo dell'application server.
9. Editare i datasources installati al **punto 7**. sostituendo la
    keyword **NOME_DRIVER_JDBC.jar** con il nome del modulo corrispondente
    al driver jdbc.
10. Verificare che la directory di lavoro e quella di log di GovPay,
    inserite in fase di configurazione, esistano o altrimenti crearle con
    permessi tali da consentire la scrittura all’utente di esecuzione del
    processo java dell’application server.
11. Avviare l'application server (ad esempio su Linux con il comando
    **<JBOSS_HOME>/bin/standalone.sh** oppure utilizzando il relativo
    service).

.. _inst_dispiegamento_auth:

Modalità di Autenticazione
--------------------------

Il processo di configurazione e dispiegamento fin qui descritto prevede che vengano attivate le modalità di autenticazione di default sulle API. Le modalità di autenticazione attivate per default sono le seguenti:

- **SSL** per le API che interfacciano i servizi pagoPA
- **HTTP-BASIC** ed **SSL** per tutte le altre API di GovPay

Per abilitare/disabilitare ulteriori modalità di autenticazione, rispetto a quelle attive per default, è necessario procedere con le seguenti operazioni:

1.  **Abilitare la configurazione esterna di spring-security**

	- durante l'esecuzione dell'installer (:ref:`inst_configurazione`), selezionare l'opzione "Visualizza impostazioni avanzate" nel passaggio "Informazioni Preliminari", quindi l'opzione "Usa file di configurazione esterni" per la configurazione di Spring Security nel passaggio "Configurazione Avanzata".

2.  **Estrarre le configurazioni di Spring Security**

	- copiare dagli archivi war contenuti nell'ear, i file /WEB-INF/classes/spring-sec/api-$$$-applicationContext-security.xml nella della work directory (default /etc/govpay/)

3.  **Commentare o scommentare le modalità di autenticazione desiderate**

	- la configurazione di spring contiene già il codice necessario a tutte le modalità di autenticazione. I vari commenti individuano i blocchi di codice che gestiscono ciascuna modalità eventualmente inclusa in blocchi commentati. Rimuovendo i commenti o impostandoli se ne determina l'abilitazione o disabilitazione.

Eventuali modifiche richiedono il riavvio dell'applicazione per renderle operative. Per i dettagli sulle modalità di autenticazione supportate si faccia riferimento alla sezione :ref:`integrazione_autenticazione`.


