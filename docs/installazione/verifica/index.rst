.. _inst_verifica:

Verifica dell'Installazione
===========================

Per la fase di verifica dell'installazione, effettuare i seguenti passi:

1. Avviare l'application server
2. Al termine della fase di avvio, sono riscontrabili i seguenti
   contesti dispiegati, suddivisi tra servizi di frontend (rivolti
   all'utente finale) e servizi di backend (rivolti all'utenza interna):

2.1 **Frontend:**

      -  **/govpay/frontend/web/connector**

         **web application per la gestione delle redirezioni durante i
         flussi di pagamento**

      -  **/govpay/frontend/api/pagamento**

         **api per l'esecuzione dei pagamenti da parte del debitore**

      -  **/govpay/frontend/api/pagopa**

         **api per la gestione del colloquio con la piattaforma centrale
         pagoPA**

2.2 **Backend:**

      -  **/govpay/backend/api/pendenze**

         **api per la gestione dell'archivio dei pagamenti in attesa
         (pendenze, pagamenti, ecc.)**

      -  **/govpay/backend/api/ragioneria**

         **api relative ai servizi di riconciliazione degli incassi con
         le pendenze/pagamenti di origine**

      -  **/govpay/backend/api/backoffice**

         **api relative ai servizi di configurazione della piattaforma
         (domini, applicazioni, operatori, ecc.)**

      -  **/govpay/backend/gui/backoffice**

         **web application che corrisponde al cruscotto di gestione e
         monitoraggio di GovPay**

3. Verificare che i servizi esposti da GovPay verso pagoPA siano
   raggiungibili verificando sul browser le seguenti URL:
   
-  http://<hostname>:<port>/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice?wsdl
-  http://<hostname>:<port>/govpay/frontend/api/pagopa/PagamentiTelematiciRTservice?wsdl

4. Verificare che la **govpayConsole**, l’applicazione web per la
   gestione della configurazione e monitoraggio di GovPay, sia
   accessibile tramite browser all’indirizzo:

   -  **http://<hostname>:<port>/govpay/backend/gui/backoffice**

   In caso di corretto funzionamento verrà visualizzata la pagina di
   autenticazione per l'accesso alla console.

5. Accedere alla govpayConsole usando l'utenza di jboss configurata in
   fase di dispiegamento.

   L’utenza creata in precedenza ha accesso a tutte le funzionalità
   compresa la gestione degli utenti. Utilizzando questo accesso
   potranno quindi essere registrati dei nuovi utenti.

6. Completata l’installazione di GovPay, per proseguire con l'utilizzo
   del sistema si rimanda al “Manuale Utente”.

