.. _govpay_scenari_spontaneo1:

Pagamento spontaneo ad iniziativa ente
======================================

In questo scenario il pagamento ha origine per iniziativa dell'utente/cittadino che, per ottenere un determinato servizio (ad esempio una licenza di pesca), procede con l'immissione delle informazioni relative alla pendenza e richiede di procedere con il versamento attraverso il portale dei pagamenti dell'ente creditore.

Lo scenario si articola nei seguenti passaggi:

    1. L'utente si collega al portale dell'ente creditore e, tipicamente tramite la compilazione di un form, fornisce tutti i dettagli della pendenza che intende pagare. In questa fase non rientra il coinvolgimento di GovPay. Quest'ultimo entra in gioco a partire dalla successiva fase di esecuzione del pagamento.

    2. Quando l'utente conferma la volontà di procedere con il pagamento, il portale avvia la transazione tramite GovPay e riceve le informazioni per procedere con il flusso di pagamento. Questa fase include la selezione del PSP e l'esecuzione sul medesimo del versamento.

    3. Ultimato il versamento, l'utente viene rediretto nuovamente sul portale dell'ente per visualizzare l'esito dell'operazione ed ottenere la ricevuta telematica.

Le fasi che andiamo a descrivere per questo scenario sono le seguenti:

    1. La :ref:`govpay_scenari_spontaneo1_realizzazione`, che prevede:

        - L'uso delle *API Pagamento* per l'avvio del pagamento dopo aver acquisito i dati della pendenza dal portale

        - La ricezione della notifica di pagamento da GovPay tramite un servizio esposto dal portale ente (modalità PUSH), oppure l'acquisizione dell'esito, da parte del portale ente, in modalità PULL sempre tramite l'*API Pagamento*

    2. La :ref:`govpay_scenari_spontaneo1_configurazione` di GovPay per supportare lo scenario descritto utilizzando il cruscotto di gestione.


.. toctree::
   :hidden:

   realizzazione
   configurazione
