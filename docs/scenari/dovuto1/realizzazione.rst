.. _govpay_scenari_dovuto1_realizzazione:

Realizzazione
-------------

In questo scenario il cittadino debitore utilizza il portale di pagamento dell'ente creditore
per consultare la propria posizione debitoria, precedentemente alimentata dai sistemi gestionali delle pendenze,
predisporre un carrello di dovuti e procedere al suo pagamento.

La realizzazione di questo scenario prevede le seguenti tre fasi:

1. Il caricamento della pendenza nell’archivio dei pagamenti in attesa da parte del gestionale

2. La consultazione della posizione debitoria e la gestione del processo di pagamento da parte del portale ente

3. La visualizzazione dell'esito di pagamento e download della ricevuta telematica

Le invocazioni verso GovPay e govpay-portal-api descritte di seguito sono raccolte anche nella
collection Postman :download:`dovuto.postman_collection.json`, da adattare al proprio ambiente
valorizzando le variabili della collection (host, credenziali dell'applicazione, ente creditore).


Caricamento della Pendenza
~~~~~~~~~~~~~~~~~~~~~~~~~~
Il caricamento della pendenza nell'archivio dei pagamenti in attesa di GovPay si realizza
invocando l’operazione `PUT /pendenze/{idA2A}/{idPendenza}` dell’API Pendenze v2 (`specifica OpenAPI <https://github.com/link-it/govpay/blob/3.10.x/wars/api-pendenze/src/main/webapp/v2/govpay-api-pendenze-v2.yaml>`__).

Di seguito un esempio di invocazione. Il base path ``/govpay-api-pendenze`` è quello del dispiegamento su Tomcat;
nel dispiegamento EAR è ``/govpay/backend/api/pendenze`` (vedi :ref:`integrazione_api`).

.. code-block:: text
   :caption: Richiesta

    PUT https://govpay.example.org/govpay-api-pendenze/rs/basic/v2/pendenze/A2A-DEMO/987
    Authorization: Basic aWRBMkEtZGVtbzpwYXNzd29yZA==
    Content-type: application/json
    Accept: application/json

    {
      "idTipoPendenza": "SANZIONE",
      "idDominio": "01234567890",
      "causale": "Sanzione CdS n. abc00000",
      "soggettoPagatore": {
        "tipo": "F",
        "identificativo": "RSSMRA30A01H501I",
        "anagrafica": "Mario Rossi"
      },
      "importo": 105.01,
      "tassonomiaAvviso": "Multe e sanzioni amministrative",
      "dataValidita": "2026-12-31",
      "dataScadenza": "2027-12-31",
      "voci": [
        {
          "idVocePendenza": "987-SANZIONE",
          "importo": 100.01,
          "descrizione": "Sanzione per divieto di sosta",
          "ibanAccredito": "IT02L1234500000999990000001",
          "tipoContabilita": "ALTRO",
          "codiceContabilita": "SANZIONE"
        },
        {
          "idVocePendenza": "987-CONTRIBUTO",
          "importo": 5,
          "descrizione": "Contributo Regionale",
          "ibanAccredito": "IT02L1234500000111110000001",
          "tipoContabilita": "ALTRO",
          "codiceContabilita": "CONTRIBUTO"
        }
      ]
    }

.. code-block:: text
   :caption: Risposta

    HTTP 201 CREATED
    {
      "idDominio": "01234567890",
      "numeroAvviso": "301340000000123456",
      "UUID": "a1b2c3d4e5f6..."
    }

Consultazione della Posizione Debitoria
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Il cittadino, tramite il portale messo a disposizione dall'ente, deve individuare le
pendenze di cui è debitore per avviarne il pagamento.
A tale scopo il componente *govpay-portal-api* (`specifica OpenAPI <https://github.com/link-it/govpay-portal-api/blob/3.10.x/src/main/resources/openapi.yaml>`__)
espone le API che consentono di reperire la posizione debitoria di un cittadino,
il cui pagamento viene poi avviato dal portale tramite il Checkout di pagoPA.

Le operazioni sulla posizione debitoria sono riservate al cittadino autenticato: govpay-portal-api
non gestisce direttamente SPID o CIE, ma riceve le richieste da un reverse proxy che autentica l'utente
e ne inoltra il codice fiscale nell'header ``X-SPID-FISCALNUMBER``. Il servizio risponde alla radice:
il prefisso ``/govpay-portal-api`` usato negli esempi è quello aggiunto dal reverse proxy.

La prima operazione utilizzata è `GET /pendenze/{idDominio}` che, applicando un filtro per
stato delle pendenze, consente all'utente autenticato di ricevere in risposta la propria posizione debitoria.

.. code-block:: text
   :caption: Richiesta

    GET /govpay-portal-api/pendenze/01234567890?stato=NON_ESEGUITA
    Accept: application/json
    X-SPID-FISCALNUMBER: RSSMRA30A01H501I

.. code-block:: text
   :caption: Risposta

    HTTP 200 OK
    Content-type: application/json

    {
      "risultati": [
        {
          "idA2A": "A2A-DEMO",
          "idPendenza": "987",
          "idTipoPendenza": "SANZIONE",
          "dominio": {
            "idDominio": "01234567890",
            "ragioneSociale": "Comune Dimostrativo",
            "indirizzo": "Piazzale Paolino Paperino",
            "civico": "1",
            "cap": "00000",
            "localita": "Roma",
            "provincia": "RO",
            "nazione": "IT",
            "email": "info@comunedimostrativo.it",
            "pec": "protocollo.generale@pec.comunedimostrativo.it",
            "tel": "00 1234 5678",
            "fax": "00 1234 5678",
            "web": "http://www.comunedimostrativo.it"
          },
          "stato": "NON_ESEGUITA",
          "causale": "Sanzione CdS n. abc00000",
          "soggettoPagatore": {
            "tipo": "F",
            "identificativo": "RSSMRA30A01H501I",
            "anagrafica": "Mario Rossi"
          },
          "iuv": "01340000000123456",
          "numeroAvviso": "301340000000123456",
          "importo": 105.01,
          "dataCaricamento": "2026-04-01",
          "dataValidita": "2026-12-31",
          "dataScadenza": "2027-12-31",
          "UUID": "a1b2c3d4e5f6...",
          "voci": [
            {
              "idVocePendenza": "987-SANZIONE",
              "importo": 100.01,
              "descrizione": "Sanzione per divieto di sosta",
              "indice" : 1,
              "tassonomiaPagoPA": "9/SANZIONE"
            },
            {
              "idVocePendenza": "987-CONTRIBUTO",
              "importo": 5,
              "descrizione": "Contributo Regionale",
              "indice" : 2,
              "tassonomiaPagoPA": "9/CONTRIBUTO"
            }
          ]
        }
      ]
    }

Esecuzione del pagamento
~~~~~~~~~~~~~~~~~~~~~~~~

Il portale, tramite le informazioni fornite da GovPay o presenti in archivi propri,
consente al cittadino di predisporre un carrello di pagamenti dovuti.
Una volta terminato, il portale avvia il pagamento utilizzando le API Checkout per gli Enti Creditori descritte nelle `specifiche pagoPA (SANP) <https://docs.pagopa.it/sanp>`__:

.. code-block:: text
   :caption: Richiesta

    POST https://api.platform.pagopa.it/checkout/ec/v1/carts
    Accept: application/json
    Content-type: application/json

    {
      "emailNotice": "mario.rossi@email.it",
      "paymentNotices": [
        {
          "noticeNumber": "301340000000123456",
          "fiscalCode": "01234567890",
          "amount": 10501,
          "companyName": "Comune Dimostrativo",
          "description": "Sanzione CdS n. abc00000"
        }
      ],
      "returnUrls": {
        "returnOkUrl": "https://portale.example.org/pagamento/esito?esito=OK",
        "returnErrorUrl": "https://portale.example.org/pagamento/esito?esito=ERROR",
        "returnCancelUrl": "https://portale.example.org/pagamento/esito?esito=CANCEL"
      }
    }

.. code-block:: text
   :caption: Risposta

    HTTP 302 Found
    Location: https://checkout.pagopa.it/...

In risposta alla chiamata il browser porta l'utente sulla pagina del Checkout dedicata alla scelta del PSP.

Visualizzazione Esito del Pagamento
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Al termine delle operazioni di pagamento su pagoPA, l'utente viene rediretto al portale dell'ente
in base all'esito del pagamento alla URL fornita all'interno del campo returnUrls.

Il portale mostra la posizione debitoria aggiornata tramite l'operazione `GET /pendenze/01234567890?stato=ESEGUITA`:

.. code-block:: text
   :caption: Richiesta

    GET /govpay-portal-api/pendenze/01234567890?stato=ESEGUITA
    Accept: application/json
    X-SPID-FISCALNUMBER: RSSMRA30A01H501I

.. code-block:: text
   :caption: Risposta

    HTTP 200 OK
    Content-type: application/json

    {
      "risultati": [
        {
          "idA2A": "A2A-DEMO",
          "idPendenza": "987",
          "idTipoPendenza": "SANZIONE",
          "dominio": {
            "idDominio": "01234567890",
            "ragioneSociale": "Comune Dimostrativo",
            "indirizzo": "Piazzale Paolino Paperino",
            "civico": "1",
            "cap": "00000",
            "localita": "Roma",
            "provincia": "RO",
            "nazione": "IT",
            "email": "info@comunedimostrativo.it",
            "pec": "protocollo.generale@pec.comunedimostrativo.it",
            "tel": "00 1234 5678",
            "fax": "00 1234 5678",
            "web": "http://www.comunedimostrativo.it"
          },
          "stato": "ESEGUITA",
          "causale": "Sanzione CdS n. abc00000",
          "soggettoPagatore": {
            "tipo": "F",
            "identificativo": "RSSMRA30A01H501I",
            "anagrafica": "Mario Rossi"
          },
          "iuv": "01340000000123456",
          "numeroAvviso": "301340000000123456",
          "importo": 105.01,
          "dataCaricamento": "2026-04-01",
          "dataValidita": "2026-12-31",
          "dataScadenza": "2027-12-31",
          "dataPagamento": "2026-04-10",
          "UUID": "a1b2c3d4e5f6...",
          "voci": [
            {
              "idVocePendenza": "987-SANZIONE",
              "importo": 100.01,
              "descrizione": "Sanzione per divieto di sosta",
              "indice" : 1,
              "tassonomiaPagoPA": "9/SANZIONE"
            },
            {
              "idVocePendenza": "987-CONTRIBUTO",
              "importo": 5,
              "descrizione": "Contributo Regionale",
              "indice" : 2,
              "tassonomiaPagoPA": "9/CONTRIBUTO"
            }
          ]
        }
      ]
    }

Infine è possibile scaricare la ricevuta di pagamento tramite l'operazione `GET /pendenze/01234567890/301340000000123456/ricevuta`:

.. code-block:: text
   :caption: Richiesta

    GET /govpay-portal-api/pendenze/01234567890/301340000000123456/ricevuta
    Accept: application/pdf

.. code-block:: text
   :caption: Risposta

    HTTP 200 OK
    Content-type: application/pdf
    Content-Disposition: attachment; filename="01234567890_301340000000123456_100000000000901234567.pdf"

    ---[pdf della ricevuta]---
