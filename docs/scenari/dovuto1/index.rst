.. _govpay_scenari_dovuto1:

Pagamento di un dovuto ad iniziativa ente
=========================================

In questa sezione descriviamo lo scenario in cui le pendenze da pagare hanno origine nel contesto della gestione amministrativa  dell'ente che, iscrivendo a ruolo un certo numero di cittadini contribuenti, diventa creditore nei loro confronti.

Lo scenario si articola complessivamente nelle seguenti fasi:

1. Il gestionale dell'ente, che ha dato origine alla pendenza, effettua il caricamento della medesima sul Govpay, andando ad alimentare l'archivio dei pagamenti in attesa.

2. Il cittadino debitore si collega al portale dei pagamenti dell'ente, effettua la ricerca e seleziona della pendenza e procede al pagamento seguendo il flusso previsto da pagoPA.

3. Il portale dell'ente recupera l'esito del pagamento, lo mostra all'utente e gli consente di scaricare la ricevuta telematica di pagamento.

Per la realizzazione di questo scenario procediamo con il presentare i passi necessari, che sono:

1. L'impiego delle API "Pendenze" di Govpay, da parte dei responsabili tecnici delle applicazioni di pagamento per realizzare l'integrazione e consentire il caricamento delle pendenze.

2. L'impiego delle API "Pagamento" di Govpay, da parte dei responsabili tecnici del portale dei pagamenti dell'ente, per realizzare l'integrazione e consentire l'esecuzione del pagamento da parte del cittadino debitore.

3. La configurazione del canale di intermediazione delle tipologie di tributo e delle applicazioni autorizzate. Queste operazioni vengono effettuate tramite il cruscotto (per il relativo manuale consultare la sezione :ref:`govpay_configurazione`).

.. toctree::
   :maxdepth: 1

   esecuzione
   configurazione

