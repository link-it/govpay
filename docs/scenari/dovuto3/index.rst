.. _govpay_scenari_dovuto3:

Pagamento di un dovuto ad iniziativa PSP
========================================

In questo scenario la pendenza da pagare ha origine nel contesto dell'ente, 
che si connota come creditore nei confronti di un determinato cittadino. 
L'ente creditore genera l'*Avviso di Pagamento* che viene consegnato al debitore, tramite il quale egli si reca presso le strutture del PSP (sportello, ATM, Home banking, Mobile APP, …) per l’esecuzione del versamento.

Lo scenario si articola complessivamente nelle seguenti fasi:

1. L’Ente Creditore, alla predisposizione di una nuova pendenza, stampa l’Avviso di Pagamento pagoPA ad essa associato e lo consegna al Soggetto Debitore, in formato digitale o cartaceo, secondo le modalità previte dell’Ente.

2. Munito dell’avviso, il Soggetto Debitore interagisce con il PSP che acquisisce gli estremi dell’Avviso, tramite scansione dei glifi grafici o trascrizione manuale dei codici di riferimento.

3. Il PSP verifica gli estremi di pagamento della pendenza, eventualmente interagendo con il Gestionale Pendenze, e li prospetta al Soggetto Debitore.

4. Il Soggetto Debitore perfeziona il pagamento e GovPay lo notifica al Gestionale Pendenze.

Le fasi che andiamo a descrivere per questo scenario sono le seguenti:

1. La :ref:`govpay_scenari_dovuto3_realizzazione`, che prevede:

   - L'uso delle *API Pendenze* di GovPay per l'integrazione con i sistemi verticali gestionali 
     e consentire il caricamento delle pendenze nell'archivio dei pagamenti in attesa e generare l'avviso di pagamento.

   - L'uso delle *API Verifica e Notifica*, esposte dall'ente, per consentire a GovPay l'interrogazione del gestionale dell'ente nei casi in cui sia necessario accedere ai dati della pendenza in corso di pagamento. Le stesse API sono successivamente usate da GovPay per notificare all'ente l'avvenuto pagamento.

2. La :ref:`govpay_scenari_dovuto3_configurazione` di GovPay per supportare lo scenario 
   descritto utilizzando il cruscotto di gestione.

.. toctree::
   :hidden:

   realizzazione
   configurazione