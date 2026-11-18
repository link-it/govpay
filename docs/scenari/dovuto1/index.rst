.. _govpay_scenari_dovuto1:

Pagamento ad iniziativa ente
=========================================

In questo scenario la pendenza da pagare ha origine nel contesto dell'ente,
che si connota come creditore nei confronti di un determinato cittadino.
Quest'ultimo provvede al pagamento accedendo al portale dei pagamenti dell'ente
ed avvia la transazione utilizzando gli strumenti disponibili.

Lo scenario estende quanto indicato nella sezione :ref:`integrazione_pagamentiente` fornendo un esempio di integrazione
con il componente *govpay-portal-api*, distribuito separatamente dal core di GovPay
(`README di govpay-portal-api <https://github.com/link-it/govpay-portal-api>`__, vedi anche :ref:`inst_componenti_aggiuntivi`).
Il componente espone le API REST usate dal portale dei pagamenti *govpay-portal*
(`README di govpay-portal <https://github.com/link-it/govpay-portal>`__), ma può essere utilizzato anche da un portale realizzato dall'ente.


Lo scenario si articola complessivamente nelle seguenti fasi:

1. L'applicativo gestionale dell'ente, che ha dato origine alla pendenza,
   effettua il caricamento della medesima sul GovPay, andando ad alimentare
   l'archivio dei pagamenti in attesa.

2. Il cittadino debitore accede al portale dei pagamenti dell'ente,
   utilizza gli strumenti a sua disposizione per identificare la pendenza
   di cui è debitore, procedendo poi al pagamento della medesima seguendo
   il flusso previsto da pagoPA.

3. Al termine del processo di pagamento, il portale recupera l'esito del pagamento e
   predispone il download della relativa ricevuta.

Le fasi che andiamo a descrivere per questo scenario sono le seguenti:

1. La :ref:`govpay_scenari_dovuto1_realizzazione`, che prevede:

   - L'uso delle *API Pendenze* di GovPay per l'integrazione con i sistemi verticali gestionali
     e consentire il caricamento delle pendenze nell'archivio dei pagamenti in attesa.

   - L'uso delle API del componente *govpay-portal-api* per l'integrazione con il portale al cittadino
     e del Checkout di pagoPA per realizzare il workflow di pagamento ad iniziativa Ente previsto da pagoPA.

2. La :ref:`govpay_scenari_dovuto1_configurazione` di GovPay per supportare lo scenario
   descritto utilizzando il cruscotto di gestione.

.. toctree::
   :hidden:

   realizzazione
   configurazione
