.. _integrazione_pagamentiente:

Pagamenti ad iniziativa ente
============================

In questa sezione viene descritto il processo di pagamento
ad iniziativa Ente tramite il Modello Unico introdotto dalle SANP 3.2.

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
avvisi ad esse associati. L'avvio del pagamento non passa da GovPay: il
portale dell'Ente si integra direttamente con il Checkout di pagoPA,
inviando il carrello degli avvisi alle API Checkout per gli Enti
Creditori descritte nelle `specifiche pagoPA (SANP) <https://docs.pagopa.it/sanp>`__
e ottenendo la redirezione alla pagina di pagamento. L'integrazione
richiede le abilitazioni previste da pagoPA per l'Ente (ad esempio la
chiave di sottoscrizione delle API Checkout).


Al termine del pagamento si viene rediretti alla URL indicata in sede di
avvio della transazione: ciascun portale dell'Ente può quindi indicare le
proprie URL di ritorno.

Esito del Pagamento
-------------------

Al ritorno sul Portale di Pagamento, al termine delle operazioni, il portale deve verificare l'effettivo esito del pagamento.
Invocando le API Ragioneria v3 di GovPay, si ottiene la lista delle ricevute di pagamento dell'avviso individuato:

   `GET /govpay-api-ragioneria/rs/basic/v3/ricevute?idDominio={idDominio}&iuv={iuv}`

Dove:

- idDominio: codice fiscale ente creditore;
- iuv: identificativo univoco di versamento: si ottiene dal numero avviso togliendo la prima cifra (aux digit 3) o le prime tre cifre (aux digit 0). In alternativa si può usare il parametro ``numeroAvviso``.

In risposta si ottiene l’elenco delle ricevute di pagamento che corrispondono ai criteri di ricerca.
Nel caso fossero presenti piu’ tentativi di pagamento il servizio ordina i risultati per data decrescente.

.. NOTE::
   Il modello unico pagoPA prevede che le ricevute siano emesse
   solo in caso di pagamento completato con successo.

Il seguente esempio mostra l'invocazione della **GET /govpay-api-ragioneria/rs/basic/v3/ricevute?idDominio={idDominio}&iuv={iuv}** per scaricare la lista delle ricevute. Si assume che l'ente creditore sia stato registrato con identificativo **01234567890** e lo iuv sia **10000000000000001**.

.. code-block:: text
      :caption: Richiesta *GET /govpay-api-ragioneria/rs/basic/v3/ricevute?idDominio={idDominio}&iuv={iuv}*

      GET https://govpay.example.org/govpay-api-ragioneria/rs/basic/v3/ricevute?idDominio=01234567890&iuv=10000000000000001

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

   `GET /govpay-api-ragioneria/rs/basic/v3/ricevute/{idDominio}/{iuv}/{idRicevuta}
   Accept: application/pdf|application/json`

Dove:

   - idDominio: codice fiscale ente creditore
   - iuv: identificativo univoco di versamento
   - idRicevuta: identificativo della ricevuta
   - Header HTTP Accept: consente di scaricare la ricevuta in diversi formati. Per scaricare la ricevuta in formato pdf impostare il valore: ‘Accept: application/pdf’.

Il seguente esempio mostra l'invocazione della **GET /govpay-api-ragioneria/rs/basic/v3/ricevute/{idDominio}/{iuv}/{idRicevuta}** per scaricare la ricevuta in formato pdf. Si assume che l'ente creditore sia stato registrato con identificativo **01234567890**, lo iuv sia **10000000000000001**, l'idRicevuta sia **100000000000902038186**.

.. code-block:: text
      :caption: Richiesta *GET /govpay-api-ragioneria/rs/basic/v3/ricevute/{idDominio}/{iuv}/{idRicevuta}*

      GET https://govpay.example.org/govpay-api-ragioneria/rs/basic/v3/ricevute/01234567890/10000000000000001/100000000000902038186
      Accept:application/pdf

      HTTP/1.1 200 OK
      content-disposition: attachment; filename="01234567890_10000000000000001_100000000000902038186.pdf"
      Content-Type: application/pdf

      ---[pdf della ricevuta]---

.. NOTE::
   Si ricorda inoltre che GovPay notifica le ricevute di pagamento acquisite all'applicativo gestionale tramite le API Ente,
   usando il connettore di integrazione dell'applicazione: con la versione ``REST_2`` l'operazione invocata è
   ``PUT /ricevute/{idDominio}/{iuv}/{idRicevuta}``, con la versione ``REST_1`` ``POST /pagamenti/{idDominio}/{iuv}``.
   In alternativa la spedizione delle ricevute può essere affidata a govpay-notify-batch (solo ``REST_2``, vedi :ref:`inst_componenti_aggiuntivi`).
   Questo consente di evitare onerosi sistemi di polling per l'acquisizione.
