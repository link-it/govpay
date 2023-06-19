.. _integrazione_pagamentiente:

.. INFO::
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
effettuare una richiesta alle :ref:`API Checkout di pagoPA https://docs.pagopa.it/sanp/appendici/primitive#ec-checkout-api`_
ottenendo la redirezione al portale di pagamento.

.. DANGER::
   Attualmente il servizio Checkout consente di pagare un solo
   avviso di pagamento per transazione

Al termine del pagamento si viene rediretti alla URL indicata in sede di 
avvio della transazione

Esito del Pagamento
-------------------

Al ritorno sul Portale di Pagamento, al termine delle operazioni,
il portale deve verificare l'effettivo esito del pagamento. 

Invocando le API Pagamenti di GovPay, in particolare il servizio `GET /ricevute/{idDominio}/{iuv}`, 
si ottiene la lista delle ricevute di pagamento dell'avviso 
individuato

.. INFO::
   Il modello unico pagoPA prevede che le ricevute siano emesse
   solo in caso di pagamento completato con successo.

E' possibile infine stampare la versione PDF di una ricevuta invocando
la risorsa `GET /ricevute/{idDominio}/{iuv}/{idRicevuta}` impostando l'header HTTP 
`Accept: application/pdf`

Si ricorda inoltre che GovPay notifica le ricevute di pagamento acquisite
all'applicativo gestionale tramite le API Ente. Questo consente di evitare
onerosi sistemi di polling per l'acquisizione.
