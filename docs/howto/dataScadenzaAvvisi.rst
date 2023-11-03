.. _howto_dataScadenzaAvvisi:

Gestione della data scadenza negli avvisi PagoPA
================================================

All'interno dell'avviso PagoPA è presente la sezione **Quanto e quando pagare** contenente le informazioni sul quanto pagare, in che modalità e con le relative scadenze.

L'etichetta **Entro il** mostra al cittadino debitore una data che puo' assumere diversi significati in base alle date definite nella pendenza al momento del caricamento su GovPay.

Data scadenza e data validità nelle richieste json
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

E' possibile indicare all'interno di una pendenza due differenti date opzionali:

.. code-block:: json
  :caption: Data scadenza e data validità all'interno di una pendenza
  
  {
    ...
    "dataValidita": "2023-11-30",
    "dataScadenza": "2023-12-31"
    ...
  }

La **data validità** indica fino a quando è valido l'importo corrente dell'avviso di pagamento. Un pagamento effettuato dopo questa data comporterà il ricalcolo e l'aggiornamento dell'importo in base alle procedure di integrazione previste.

D'altra parte, la **data scadenza** rappresenta la data successiva alla quale l'avviso di pagamento scade e non può più essere pagato.

Se durante il caricamento della pendenza non viene specificata alcuna data, allora nell'avviso l'etichetta **Entro il** verra' omessa e non verrà visualizzata alcuna data.

Gestione automatica della data validità
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Il sistema consente di visualizzare una data sull'avviso di pagamento per le pendenze che non specificano alcuna data, indicando il numero dei giorni di validità da assegnare alla pendenza attraverso la proprietà di sistema: 

  -  **it.govpay.modello3.sanp24.giorniValiditaDaAssegnarePendenzaSenzaDataValidita=Num**

la data visualizzata nell'avviso sarà calcolata come la somma tra la data di creazione della pendenza e il numero di giorni definito nella proprietà.
