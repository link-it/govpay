.. _integrazione_pagamentiente:

.. NOTE::
   In questa sezione viene descritto il nuovo processo di pagamento
   ad iniziativa Ente tramite il Modello Unico introdotto dalle SANP 3.2. 
   Restano disponibili i servizi che realizzano il precedente processo 
   di pagamento (c.d. Modello Uno) benchè deprecato da pagoPA.

Pagamenti ad iniziativa ente
============================

Il Soggetto Debitore utilizza il Portale dei
Pagamenti dell’Ente Creditore per effettuare i pagamenti dovuti.

1. L'utente debitore utilizza gli strumenti offerti dal Portale dei
   Pagamenti dell’Ente per individuare una posizione presente a sistema
   o per istruirne una nuova utilizzando i servizi di GovPay.
2. Il portale avvia il processo di pagamento con il servizio Checkout di
   pagoPA.
3. Al termine del pagamento, l'utente viene rediretto sul Portale dei 
   Pagamenti dell'Ente che dà conferma dell'esito del pagamento e ne 
   predispone la ricevuta.

Predisposizione delle pendenze oggetto del pagamento
----------------------------------------------------

Il cittadino utilizza le funzionalità del portale per identificare
le pendenze che intende pagare alle quali deve essere associato un
numero avviso.

Le pendenze oggetto di pagamento devono quindi essere rese disponibili
a GovPay in una delle modalità disponibili:

1. Alimentando l'archivio dei pagamenti in attesa interno a GovPay 
   invocando le API Pendenze;
2. Esponendo le API Ente di verifica, consentendo a GovPay l'acquisizione
   dei dati relativi in sede di pagamento;

Una volta predisposte le pendenze, il portale può avviarne il pagamento.

Avvio del Pagamento
-------------------

Al termine della fase di predisposizione del pagamento, il portale
dispone di un carrello di pendenze identificate dagli estremi degli 
avvisi ad esse associati. Per avviare il pagamento è sufficiente 
effettuare una richiesta alle `API Checkout di pagoPA https://docs.pagopa.it/sanp/appendici/primitive#ec-checkout-api` _ ottenendo la redirezione al portale di pagamento.

.. DANGER::
   Attualmente il servizio Checkout consente di pagare un solo
   avviso di pagamento per transazione

Al termine del pagamento si viene rediretti alla URL indicata in sede di 
avvio della transazione

Esito del Pagamento
-------------------

Al ritorno sul Portale di Pagamento, al termine delle operazioni, il portale deve verificare l'effettivo esito del pagamento. 
Invocando le API Pagamenti di GovPay, si ottiene la lista delle ricevute di pagamento dell'avviso individuato:

   `GET /govpay/backend/api/ragioneria/rs/basic/v3/ricevute?idDominio={idDominio}&iuv={iuv}`

Dove:

- idDominio: codice fiscale ente creditore (fisso 05754381001);
- iuv: identificativo univoco di versamento (si ottiene eliminando le prime tre cifre dal numero avviso)idA2A;

In risposta si ottiene l’elenco delle transazioni di pagamento che corrispondono ai criteri di ricerca.
Nel caso fossero presenti piu’ tentativi di pagamento il servizio ordina i risultati per data decrescente.

.. NOTE::
   Il modello unico pagoPA prevede che le ricevute siano emesse
   solo in caso di pagamento completato con successo.

Il seguente esempio mostra l'invocazione della **GET /govpay/backend/api/ragioneria/rs/basic/v3/ricevute?idDominio={idDominio}&iuv={iuv}** per scaricare la lista delle ricevute. Si assume che l'ente creditore sia stato registrato con identificativo **01234567890** e lo iuv sia **10000000000000001**.

.. code-block:: json
      :caption: Richiesta *GET /govpay/backend/api/ragioneria/rs/basic/v3/ricevute?idDominio={idDominio}&iuv={iuv}*
	
      GET https://demo.govcloud.it/govpay/backend/api/ragioneria/rs/basic/v3/ricevute?idDominio=01234567890&iuv=10000000000000001

      HTTP 200 OK
      
      {
         "numRisultati":1,
         "numPagine":1,
         "risultatiPerPagina":25,
         "pagina":1,
         "risultati":[
            {
               "dominio":{
                  "idDominio":"01234567890",
                  "ragioneSociale":"Ente Creditore"
               },
               "iuv":"10000000000000001",
               "idRicevuta":"100000000000902038186",
               "data":"2025-02-21T17:25:11",
               "esito":"ESEGUITO"
            }
         ]
      }
	
E' possibile infine stampare la versione PDF di una ricevuta utilizzando la risorsa `GET /ricevute/{idDominio}/{iuv}/{idRicevuta}` impostando l'header HTTP 
`Accept: application/pdf`

   `GET /govpay/backend/api/ragioneria/rs/basic/v3/ricevute/{idDominio}/{iuv}/{idRicevuta}
   Accept: application/pdf|application/json`

Dove:

   - idDominio: codice fiscale ente creditore (fisso 05754381001)
   - iuv: identificativo univoco di versamento 
   - idRicevuta: identificativo della ricevuta
   - Header HTTP Accept: consente di scaricare l’avviso di pagamento in diversi formati. Per scaricare la ricevuta in formato pdf impostare il valore: ‘Accept: application/pdf’.

Il seguente esempio mostra l'invocazione della **GET /govpay/backend/api/ragioneria/rs/basic/v3/ricevute/{idDominio}/{iuv}/{idRicevuta}** per scaricare la ricevuta in formato pdf. Si assume che l'ente creditore sia stato registrato con identificativo **01234567890**, lo iuv sia **10000000000000001**, l'idRicevuta sia **100000000000902038186**.

.. code-block:: json
      :caption: Richiesta *GET /govpay/backend/api/ragioneria/rs/basic/v3/ricevute/{idDominio}/{iuv}/{idRicevuta}*

      GET https://demo.govcloud.it/govpay/backend/api/ragioneria/rs/basic/v3/ricevute/01234567890/10000000000000001/100000000000902038186
      Accept:application/pdf

      HTTP/1.1 200 OK
      content-disposition: attachment; filename="01234567890_10000000000000001_100000000000902038186.pdf"
      Content-Type: application/pdf
      
      ---[pdf della ricevuta]--- 

.. NOTE::
   Si ricorda inoltre che GovPay notifica le ricevute di pagamento acquisite all'applicativo gestionale tramite le API Ente.
   Questo consente di evitare onerosi sistemi di polling per l'acquisizione.


