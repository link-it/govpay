.. _inst_dispiegamento:

Fase di Dispiegamento
======================

Al termine dell’esecuzione dell’utility di installazione vengono
prodotti i files necessari per effettuare il dispiegamento nell’ambiente
di esercizio. Tali files sono disponibili nella directory **dist**
prodotta dall’utility.

Per il dispiegamento nell'ambiente di destinazione devono essere
effettuati i seguenti passi:

1. Creare un database per ospitare le tabelle dell’applicazione avente
   il nome indicato durante la fase di setup. Il charset da utilizzare è
   UTF-8.
2. Creare un utente sul RDBMS avente i medesimi valori di username e
   password indicati in fase di setup e diritti di lettura, inserimento e
   modifica sulle tabella nel database creato in precedenza.
3. Garantire la raggiungibilità dell'application server al RDBMS
   indicato in fase di setup.
4. Eseguire lo script **sql/gov_pay.sql** per la creazione dello schema
   del database utilizzando un'utenza con sufficienti diritti per la 
   creazione delle tabelle. Ad esempio, nel caso di PostgreSQL, si potrà eseguire
   il comando:

   -  **psql -h <hostname> -d <database> -U <username> -f sql/gov_pay.sql**

5. Copiare il file **datasource/govpay-ds.xml**, contenente la
   definizione del datasource, nella directory
   **<JBOSS_HOME>/standalone/deployments** dell'application server.
6. Copiare le applicazioni presenti nella directory **archivi** nella
   directory **<JBOSS_HOME>/standalone/deployments** dell'application server.
7. Installare il DriverJDBC, relativo al tipo di RDBMS indicato in fase
   di setup, come modulo dell'application server.
8. Editare i datasources installati al **punto 5** sostituendo la
   keyword **NOME_DRIVER_JDBC.jar** con il nome del modulo corrispondente
   al driver jdbc.
9. Verificare che la directory di lavoro e quella di log di GovPay,
   inserite in fase di configurazione, esistano o altrimenti crearle con
   permessi tali da consentire la scrittura all’utente di esecuzione del
   processo java dell’application server.
10. In ambiente linux, intallare i pacchetti **fontconfig** e **urw-fonts**
    necessari alle stampe degli avvisi di pagamento
11. Impostare le seguenti properties nella JVM dell'Application Server:
	
	- **java.awt.headless=true**
	- **file.encoding=UTF-8**
	- **user.timezone="Europe/Rome"**
   
12. Avviare l'application server (ad esempio su Linux con il comando
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

.. _inst_troubleshooting:

Troubleshooting
--------------------------

In caso di deploy su versioni non supportate di WildFly e' possibile incorrere nell'errore `Caused by: org.jboss.modules.ModuleNotFoundException: jdk.unsupported` in fase di deploy. Un efficace workaround e' quello di registrare un modulo fittizio come suggerito in `https://stackoverflow.com/a/68318243`_
