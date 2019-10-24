.. _govpay_scenari_dovuto1_esecuzione:

Esecuzione
----------

Lo scenario fin qui descritto è stato arricchito di una collection Postman che consente di eseguire autonomamente i passi dello scenario utilizzando come riferimento l':ref:`govpay_scenari_demo` di GovPay.

 .. figure:: ../_images/postman_dovuto1_menu.png
   :align: center
   :name: postman_dovuto1_menu

   Elenco operazioni presenti nella collection postman

Le operazioni elencate (:numref:`postman_dovuto1_menu`) possono essere eseguite in sequenza al fine di riprodurre i passi già descritti nella sezione di :ref:`govpay_scenari_dovuto1_realizzazione`.

Di seguito la sequenza di esecuzione delle operazioni:

- **PUT Pendenza**: l'operazione prevede l'invio di un messaggio contenente una pendenza con due voci di pagamento (quindi senza la generazione dell'avviso). Per quanto riguarda i parametri:

    * Il primo parametro è l'identificativo dell'applicazione gestionale che inserisce la pendenza. In questo caso si utilizza l'applicazione A2A-DEMO, censita nell'ambiente demo.

    * Il secondo parametro è l'identificativo della pendenza, fornito dall'applicazione. In questo caso l'identificativo viene automaticamente generato tramite uno script che utilizza numeri casuali.

    L'operazione si ritiene conclusa con successo se restituisce il codice *HTTP 201* (:numref:`postman_dovuto1_put_response`).

 .. figure:: ../_images/postman_dovuto1_put_response.png
   :align: center
   :name: postman_dovuto1_put_response

   Risposta ottenuta dalla *PUT Pendenza*

- **GET Posizione Debitoria**: l'operazione, eseguita tipicamente dall'applicazione corrispondente al portale di pagamento, prevede la ricerca delle pendenze filtrando rispetto all'identificativo dell'utente debitore. Tra le pendenze restituite ci sarà quella caricata al passo precedente (:numref:`postman_dovuto1_getposizione_response`).

 .. figure:: ../_images/postman_dovuto1_getposizione_response.png
   :align: center
   :name: postman_dovuto1_getposizione_response

   Risposta ottenuta dalla *GET Posizione Debitoria*

- **POST Pagamento**: l'operazione, eseguita in seguito alla conferma dell'utente per effettuare il pagamento, prevede che il body contenga i seguenti elementi:

    * *idA2A*: identificativo del gestionale che ha caricato la pendenza

    * *idPendenza*: identificativo della pendenza che si vuol pagare

    L'operazione si ritiene conclusa correttamente se viene restituito il codice *HTTP 201* (:numref:`postman_dovuto1_post_pagamento`). La risposta ottenuta contiene i seguenti dati:

    * *id*: identificativo del pagamento creato

    * *location*: uri per la visualizzazione del dettaglio del pagamento

    * *redirect*: url per il reindirizzamento del browser utente verso il prossimo passo del flusso di pagamento

    * *idSession*: identificativo della sessione assegnato da pagoPA

 .. figure:: ../_images/postman_dovuto1_post_pagamento.png
   :align: center
   :name: postman_dovuto1_post_pagamento

   Operazione POST Pagamento

- **GET Pagamento**: questa operazione viene eseguita dal portale di pagamento, al termine dell'operazione di versamento da parte dell'utente, per verificare l'esito dell'operazione e consentire lo scaricamento della ricevuta telematica (:numref:`postman_dovuto1_get_pagamento`). L'operazione utilizzata per il recupero del dettaglio del pagamento è quella che prevede la ricerca basata sull'identificativo di sessione assegnato da pagoPA. Tale valore viene estratto dalla risposta alla POST del passo precedente ed inserito nella richiesta corrente.

 .. figure:: ../_images/postman_dovuto1_get_pagamento.png
   :align: center
   :name: postman_dovuto1_get_pagamento

   Operazione GET Pagamento
