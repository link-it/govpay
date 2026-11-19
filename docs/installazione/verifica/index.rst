.. _inst_verifica:

Verifica dell'Installazione
===========================

Per la fase di verifica dell'installazione, effettuare i seguenti passi:

1. Avviare Tomcat e attendere il termine dell'avvio. Nel log di Tomcat
   non devono comparire errori di dispiegamento dei war di GovPay.

2. Verificare che ciascun war risponda, aprendo dal browser (o con
   ``curl``) gli indirizzi della tabella, dove ``<hostname>`` e
   ``<port>`` sono quelli di Tomcat. Gli indirizzi indicati non richiedono
   autenticazione.

   .. list-table::
      :header-rows: 1
      :widths: 22 43 35

      * - War
        - URL da verificare
        - Esito atteso
      * - ``govpay-api-pagopa``

          servizi SOAP invocati dal Nodo dei Pagamenti
        - ``http://<hostname>:<port>/govpay-api-pagopa/PagamentiTelematiciCCPservice?wsdl``

          ``http://<hostname>:<port>/govpay-api-pagopa/PagamentiTelematiciRTservice?wsdl``
        - HTTP 200 con il WSDL del servizio
      * - ``govpay-api-pendenze``

          API Pendenze, per i gestionali dell'ente
        - ``http://<hostname>:<port>/govpay-api-pendenze/v2/govpay-api-pendenze-v2.yaml``
        - HTTP 200 con la specifica OpenAPI
      * - ``govpay-api-ragioneria``

          API Ragioneria, per la riconciliazione degli incassi
        - ``http://<hostname>:<port>/govpay-api-ragioneria/v3/govpay-api-ragioneria.yaml``
        - HTTP 200 con la specifica OpenAPI
      * - ``govpay-api-backoffice``

          API Backoffice, usate dalla console
        - ``http://<hostname>:<port>/govpay-api-backoffice/rs/form/v1/info``
        - HTTP 200 con nome e versione del prodotto, ad esempio
          ``{"appName":"GovPay","versione":"3.10.0","build":"..."}``
      * - ``govpay-console``

          console di gestione e monitoraggio
        - ``http://<hostname>:<port>/govpay-console/``
        - HTTP 200 con la pagina di autenticazione della console

   Le operazioni delle API richiedono invece l'autenticazione: ad esempio
   ``http://<hostname>:<port>/govpay-api-pendenze/rs/basic/v2/pendenze``
   invocata senza credenziali risponde HTTP 401 ("Autenticazione
   richiesta"). Le modalità di autenticazione sono descritte in
   :ref:`inst_dispiegamento_auth`.

3. Accedere alla console usando l'utenza amministratore indicata
   nell'installer (predefinita ``gpadmin``).

   L’utenza creata in precedenza ha accesso a tutte le funzionalità
   compresa la gestione degli utenti. Utilizzando questo accesso
   potranno quindi essere registrati dei nuovi utenti.

4. Completata l’installazione di GovPay, per proseguire con l'utilizzo
   del sistema si rimanda alla sezione :ref:`govpay_console`.
