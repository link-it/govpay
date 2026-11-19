.. _govpay_scenari_riconciliazione_realizzazione:

Realizzazione
-------------

In questo scenario il Sistema di Gestione Contabile dell'ente creditore riconcilia i movimenti di accredito, presenti nel Giornale di Cassa fornito dalla banca tesoriera, con i singoli pagamenti effettuati su pagoPA. L'applicativo di contabilità effettua l'operazione di riconciliazione utilizzando le API Ragioneria v3 (`specifica OpenAPI <https://github.com/link-it/govpay/blob/3.10.x/wars/api-ragioneria/src/main/webapp/v3/govpay-api-ragioneria.yaml>`__).

Riconciliazione Entrate
~~~~~~~~~~~~~~~~~~~~~~~

L'operazione per la riconciliazione delle entrate è *PUT /riconciliazioni/{idDominio}/{id}*, dove *id* è un identificativo
alfanumerico (al massimo 35 caratteri) assegnato dal gestionale di contabilità, che rende l'operazione idempotente.
Qui di seguito vediamo un esempio di richiesta (base path delle API in :ref:`integrazione_api`).

.. code-block:: text
    :caption: Registrazione di un movimento di entrata (Richiesta)

    PUT https://govpay.example.org/govpay-api-ragioneria/rs/basic/v3/riconciliazioni/01234567890/RIC20261118001
    Content-Type: application/json

    {
        "causale": "/PUR/LGPE-RIVERSAMENTO/URI/2026-11-17GovPAYPsp1-15442458845",
        "importo": 100.99,
        "dataValuta": "2026-11-18",
        "dataContabile": "2026-11-18",
        "contoAccredito": "IT02L1234500000999990000001",
        "sct": "2026-11-18ABI00000011234"
    }

Il messaggio di richiesta prevede che vengano forniti i riferimenti al movimento sul Giornale di Cassa. In particolare sono obbligatori i seguenti dati:

    - **importo**
        L'importo del movimento

    - **sct**
        L'identificativo dell'operazione bancaria SCT

    - uno tra i seguenti elementi identificativi:

        - **iuv**
            L'identificativo del singolo versamento pagoPA

        - **idFlussoRendicontazione**
            L'identificativo del flusso di rendicontazione (riversamenti multipli)

        - **causale**
            La causale riportata per l'operazione (GovPay è in grado di estrarre autonomamente gli identificativi necessari). Nell'esempio è stata utilizzata questa modalità.

Sono inoltre facoltativi la data di valuta (**dataValuta**), la data contabile (**dataContabile**) e il conto di tesoreria
su cui sono stati incassati i fondi (**contoAccredito**).

Alla prima invocazione GovPay accetta la richiesta e la elabora in modo asincrono:

.. code-block:: text
    :caption: Registrazione di un movimento di entrata (Risposta)

    HTTP 202 Accepted
    Location: /riconciliazioni/01234567890/RIC20261118001

L'esito dell'elaborazione si ottiene con l'operazione *GET /riconciliazioni/{idDominio}/{id}*; una nuova invocazione
della *PUT* con lo stesso *id* restituisce **200 OK** con lo stesso contenuto. Il campo **stato** vale *IN_ELABORAZIONE*
finché l'elaborazione non è conclusa, poi *ACQUISITA* o *ERRORE* (con il dettaglio in **descrizioneStato**).

.. code-block:: json
    :caption: Consultazione della riconciliazione (Risposta)

    {
       "id": "RIC20261118001",
       "dominio": {
          "idDominio": "01234567890",
          "ragioneSociale": "Comune Dimostrativo"
       },
       "stato": "ACQUISITA",
       "importo": 100.99,
       "data": "2026-11-18T10:30:00",
       "dataValuta": "2026-11-18",
       "dataContabile": "2026-11-18",
       "contoAccredito": "IT02L1234500000999990000001",
       "sct": "2026-11-18ABI00000011234",
       "idFlussoRendicontazione": "2026-11-17GovPAYPsp1-15442458845",
       "causale": "/PUR/LGPE-RIVERSAMENTO/URI/2026-11-17GovPAYPsp1-15442458845",
       "riscossioni": [
          {
             "dominio": {
                "idDominio": "01234567890",
                "ragioneSociale": "Comune Dimostrativo"
             },
             "iuv": "340000013885238",
             "iur": "3400000138852382328",
             "indice": 1,
             "stato": "INCASSATA",
             "tipo": "ENTRATA",
             "importo": 100.99,
             "data": "2026-11-16",
             "vocePendenza": {
                "idVocePendenza": "1",
                "descrizione": "Diritti e segreteria",
                "pendenza": {
                   "idA2A": "GestPag",
                   "idPendenza": "1709909062046",
                   "idTipoPendenza": "SEGRETERIA",
                   "causale": "Pagamento dovuto n.1709909062046",
                   "dataCaricamento": "2026-11-10",
                   "dataValidita": "2900-12-31",
                   "dataScadenza": "2999-12-31",
                   "UUID": "2e75e8f04ace435198ea411b20423315"
                }
             }
          }
       ]
    }

La risposta presenta la riconciliazione del movimento fornito in input con l'elenco delle riscossioni corrispondenti. Con queste informazioni il Gestionale dell'ente creditore è in grado di effettuare la chiusura contabile di ogni pendenza di pagamento.

Nelle API Ragioneria v2 l'operazione era *POST /riconciliazioni/{idDominio}*, sincrona, con la lista delle riscossioni direttamente nella risposta.
