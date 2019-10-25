.. _govpay_scenari_dovuto1:

Pagamento di un dovuto ad iniziativa ente
=========================================

In questo scenario la pendenza da pagare ha origine nel contesto dell'ente, che si connota come creditore nei confronti di un determinato cittadino. Quest'ultimo provvede al pagamento utilizzando il portale dei pagamenti dell'ente creditore.

Lo scenario si articola complessivamente nelle seguenti fasi:

1. Il gestionale dell'ente, che ha dato origine alla pendenza, effettua il caricamento della medesima sul GovPay, andando ad alimentare l'archivio dei pagamenti in attesa.

2. Il cittadino debitore si collega al portale dei pagamenti dell'ente, effettua la ricerca e selezione della pendenza, procedendo poi al pagamento della medesima seguendo il flusso previsto da pagoPA.

3. Il portale dell'ente recupera l'esito del pagamento, lo mostra all'utente e gli consente di scaricare la ricevuta telematica di pagamento.

Le fasi che andiamo a descrivere per questo scenario sono le seguenti:

1. La :ref:`govpay_scenari_dovuto1_realizzazione`, che prevede:

    - L'impiego delle *API Pendenze* di GovPay per realizzare l'integrazione con i sistemi gestionali e consentire il caricamento delle pendenze.

    - L'impiego delle *API "Pagamento* di GovPay per realizzare l'integrazione con il portale ente e consentire l'esecuzione dei pagamenti da parte del cittadino debitore.

2. La :ref:`govpay_scenari_dovuto1_configurazione` del canale di intermediazione delle tipologie di tributo e delle applicazioni autorizzate. Queste operazioni vengono effettuate tramite il cruscotto (per il relativo manuale consultare la sezione :ref:`govpay_configurazione`).

3. L':ref:`govpay_scenari_dovuto1_esecuzione` di un esempio tramite un ambiente definito su `Postman <https://www.getpostman.com/downloads/>`_.

.. toctree::
   :hidden:

   realizzazione
   configurazione
   esecuzione

