.. _govpay_scenari_riconciliazione_realizzazione:

Realizzazione
-------------

In questo scenario il Sistema di Gestione Contabile dell'ente creditore riconcilia i movimenti di accredito, presenti nel Giornale di Cassa fornito dalla banca tesoriera, con i singoli pagamenti effettuati su pagoPA. L'applicativo di contabilità effettua l'operazione di riconciliazione utilizzando l' `API Riconciliazione <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-ragioneria/src/main/webapp/v2/govpay-api-ragioneria-v2.yaml>`_.


Riconciliazione Entrate
~~~~~~~~~~~~~~~~~~~~~~~

L'operazione per la riconciliazione delle entrate è *POST /riconciliazioni/{idDominio}*. Qui di seguito vediamo un esempio di chiamata:

.. code-block:: json
    :caption: Registrazione di un movimento di entrata (Richiesta)

    POST /riconciliazioni/01234567890
    {
        "causale": "/PUR/LGPE-RIVERSAMENTO/URI/2017-01-01ABI00000011234",
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

    HTTP 201 CREATED
    {
        "id": "12345",
        "causale": "/PUR/LGPE-RIVERSAMENTO/URI/2017-01-01ABI00000011234",
        "importo": 100.01,
        "dataValuta": "2020-12-31",
        "dataContabile": "2020-12-31",
        "sct": "2017-01-01ABI00000011234",
        "idDominio": "01234567890",
        "riscossioni":
        [
            {
                "idDominio": "01234567890",
                "iuv": "RF23567483937849450550875",
                "iur": "1234acdc",
                "indice": 1,
                "pendenza": "/pendenze/A2A12345/abcdef12345",
                "idVocePendenza": "abcdef12345_1",
                "rpt": "/pendenze/01234567890/abcd12345/n%2Fa",
                "importo": 100.01,
                "ibanAccredito": "IT02L1234512345123456789012",
                "data": "2020-12-31",
                "commissioni": 1.5,
                "allegato":
                {
                    "tipo": "Esito pagamento",
                    "testo": "string"
                }
            }
        ]
    }

La risposta all'operazione invocata presenta un messaggio con la riconciliazione del movimento fornito in input con l'elenco delle riscossioni corrispondenti. Con queste informazioni il Gestionale dell'ente creditore è in grado di effettuare la chiusura contabile di ogni pendenza di pagamento.
