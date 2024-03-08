.. _govpay_scenari_riconciliazione_realizzazione:

Realizzazione
-------------

In questo scenario il Sistema di Gestione Contabile dell'ente creditore riconcilia i movimenti di accredito, presenti nel Giornale di Cassa fornito dalla banca tesoriera, con i singoli pagamenti effettuati su pagoPA. L'applicativo di contabilità effettua l'operazione di riconciliazione utilizzando l' `API Riconciliazione <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-ragioneria/src/main/webapp/v2/govpay-api-ragioneria-v2.yaml&nocors>`_.

Riconciliazione Entrate
~~~~~~~~~~~~~~~~~~~~~~~

L'operazione per la riconciliazione delle entrate è *POST /riconciliazioni/{idDominio}*. Qui di seguito vediamo un esempio di richiesta:

.. code-block:: json
    :caption: Registrazione di un movimento di entrata (Richiesta)

    {
        "causale": "/PUR/LGPE-RIVERSAMENTO/URI/2023-03-08GovPAYPsp1-15442458845",
        "importo": 100.01,
        "dataValuta": "2020-12-31",
        "dataContabile": "2020-12-31"
        "sct": "2017-01-01ABI00000011234"
    }

Il messaggio di richiesta prevede che vengano forniti i riferimenti al movimento sul Giornale di Cassa. In particolare sono obbligatori i seguenti dati:

    - **importo**
        L'importo del movimento

    - **sct**
        L'identificativo dell'operazione bancaria SCT

    - uno tra i seguenti elementi identificativi:

        - **iuv**
            L'identificativo del singolo versamento pagoPA

        - **idFlusso**
            L'identificativo del flusso di versamento (riversamenti multipli)

        - **causale**
            La causale riportata per l'operazione (GovPay è in grado di estrarre autonomamente gli identificativi necessari). Nell'esempio è stata utilizzata questa modalità.

.. code-block:: json
    :caption: Registrazione di un movimento di entrata (Risposta)

    {
       "importo":100.99,
       "sct":"SCT0123456789",
       "idDominio":"12345678901",
       "idRiconciliazione":"2023-03-08GovPAYPsp1-15442458845",
       "causale":"/PUR/LGPE-RIVERSAMENTO/URI/2023-03-08GovPAYPsp1-15442458845"   
       "riscossioni":[
          {
             "dominio":{
                "idDominio":"12345678901",
                "ragioneSociale":"Ente Creditore Test"
             },
             "iuv":"340000013885238",
             "iur":"3400000138852382328",
             "indice":1,
             "stato":"INCASSATA",
             "tipo":"ENTRATA",
             "importo":100.99,
             "data":"2024-03-08",
             "vocePendenza":{
                "indice":1,
                "idVocePendenza":"1",
                "importo":100.99,
                "descrizione":"Diritti e segreteria",
                "pendenza":{
                   "idA2A":"IDA2A01",
                   "idPendenza":"1709909062046",
                   "idDominio":"12345678901",
                   "idTipoPendenza":"SEGRETERIA",
                   "causale":"Pagamento dovuto n.1709909062046",
                   "dataCaricamento":"2024-03-08",
                   "dataValidita":"2900-12-31",
                   "dataScadenza":"2999-12-31",
                   "UUID":"2e75e8f04ace435198ea411b20423315"
                }
             },
             "riconciliazione":"/riconciliazioni/12345678901/2024-03-08GovPAYPsp1-15442458845"
          }
       ]    
    }

La risposta all'operazione invocata presenta un messaggio con la riconciliazione del movimento fornito in input con l'elenco delle riscossioni corrispondenti. Con queste informazioni il Gestionale dell'ente creditore è in grado di effettuare la chiusura contabile di ogni pendenza di pagamento.
