.. _govpay_scenari_dovuto1_realizzazione:

Realizzazione
-------------

In questo scenario il cittadino debitore utilizza il portale di pagamento dell'ente creditore 
per consultare la propria posizione debitoria, precedentemente alimentata dai sistemi gestionali delle pendenze, 
predisporre un carrello di dovuti e procedere al suo pagamento.

La realizzazione di questo scenario prevede le seguenti tre fasi:

1- *Il caricamento della pendenza nell’archivio dei pagamenti in attesa da parte del gestionale*

2- *La consultazione della posizione debitoria e la gestione del processo di pagamento da parte del portale ente*

3- *La visualizzazione dell'esito di pagamento e download della ricevuta telematica*


Caricamento della Pendenza
~~~~~~~~~~~~~~~~~~~~~~~~~~
Il caricamento della pendenza nell'archivio dei pagamenti in attesa di GovPay si realizza
invocando l’operazione `PUT /pendenze/{idA2A}/{idPendenza}` dell’API `Pendenze <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pendenze/src/main/webapp/v2/govpay-api-pendenze-v2.yaml>`_.

Facendo riferimento all':ref:`govpay_scenari_demo` di GovPay vediamo di seguito un esempio 
di invocazione:

.. code-block:: json

    PUT /govpay/backend/api/pendenze/rs/basic/v2/pendenze/A2A-DEMO/987

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
      "dataValidita": "2019-12-31",
      "dataScadenza": "2020-12-31",
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

Dove il parametro `A2A-DEMO` è l'identificativo del gestionale autenticato che effettua 
il caricamento della pendenza e `987` è l'identificativo della pendenza fornita.

Se la pendenza è stata inserita con successo si ottiene in risposta il codice `HTTP 201 CREATED`, 
mentre se la pendenza era già presente ed è stata aggiornata, si ottiene un `HTTP 200 OK`. Per 
maggiori informazioni sugli altri esiti possibili, si rimanda alla descrizione OpenAPI del servizio.

Esecuzione del Pagamento da Posizione Debitoria
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Il cittadino, tramite il portale messo a disposizione dall'ente, deve individuare le
pendenze di cui è debitore per avviarne il pagamento. 

GovPay consente al portale di reperire la posizione debitoria di un cittadino ed avviarne il pagamento utilizzando le API di 
`Pagamento <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pagamento/src/main/webapp/v2/govpay-api-pagamento-v2.yaml>`_, 

La prima operazione utilizzata è `GET /pendenze` applicando un filtro per codice fiscale
e stato delle pendenze, ricevendo in risposta la posizione debitoria del cittadino.

.. code-block:: json

    GET /govpay/frontend/api/pagamento/rs/basic/v2/pendenze?idDebitore=RSSMRA30A01H501I&stato=NON_ESEGUITA

    HTTP 200 OK
    {
      "numRisultati": 1,
      "numPagine": 1,
      "risultatiPerPagina": 25,
      "pagina": 1,
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
            "web": "http://www.comunedimostrativo.it",
            "gln": "8088888000000",
            "logo": "/domini/01234567890/logo",
            "unitaOperative": "/domini/01234567890/unitaOperative",
            "tipiPendenza": "/domini/01234567890/tipiPendenza"
          },
          "stato": "NON_ESEGUITA",
          "causale": "Sanzione CdS n. abc00000",
          "soggettoPagatore": {
            "tipo": "F",
            "identificativo": "RSSMRA30A01H501I",
            "anagrafica": "Mario Rossi"
          },
          "importo": 10.01,
          "dataCaricamento": "2019-10-18",
          "dataValidita": "2019-12-31",
          "dataScadenza": "2020-12-31",
          "tassonomiaAvviso": "Multe e sanzioni amministrative",
          "rpp": "/rpp?idA2A=A2A-DEMO&idPendenza=987",
          "pagamenti": "/pagamenti?idA2A=A2A-DEMO&idPendenza=987"
        }
      ]
    }

Il portale utilizza le informazioni ricevute per prospettare al cittadino la scelta delle
pendenze da pagare. Una volta selezionate, il portale avvia il pagamento 

.. code-block:: json

    POST /govpay/frontend/api/pagamento/rs/basic/v2/pagamenti
    {
      "pendenze": [
        {
          "idA2A": "A2A-DEMO",
          "idPendenza": "987"
        }
      ]
    }
    
    HTTP 201 CREATED
    {
      "id": "1d16d7b741024c6a8a3e3596957482b8",
      "location": "/pagamenti/1d16d7b741024c6a8a3e3596957482b8",
      "redirect": "https://demo.govcloud.it/govpay-ndpsym/wisp/rs/scelta?idSession=18cb852db0f041068b0063d8d580380c",
      "idSession": "18cb852db0f041068b0063d8d580380c"
    }

La risposta che si ottiene, in caso di pagamento avviato con successo, è la seguente:

.. code-block:: json

    

La URL indicata dal campo **redirect** dovrà essere utilizzata dal portale per far proseguire l'utente 
nel pagamento, come previsto dal modello pagoPA. 

Visualizzazione Esito del Pagamento
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Al termine delle operazioni di pagamento su pagoPA, l'utente viene rediretto al portale dell'ente 
alla URL fornita a pagoPA in sede di configurazione della Stazione, con il parametro **idSession** 
nella queryString:

.. code-block:: none

    GET /govpay/frontend/api/pagamento/rs/basic/v2/pagamenti/byIdSession/18cb852db0f041068b0063d8d580380c

    HTTP 200 OK
    {
      "autenticazioneSoggetto": "N/A",
      "id": "1d16d7b741024c6a8a3e3596957482b8",
      "nome": "Sanzione CdS n. abc00000",
      "stato": "NON_ESEGUITO",
      "importo": 10.01,
      "idSessionePsp": "18cb852db0f041068b0063d8d580380c",
      "pspRedirectUrl": "https://demo.govcloud.it/govpay-ndpsym/wisp/rs/scelta?idSession=18cb852db0f041068b0063d8d580380c",
      "dataRichiestaPagamento": "2019-10-21T14:16:07.022+0000",
      "rpp": [
        {
          "stato": "RT_ACCETTATA_PA",
          "rpt": {
               *RPT in formato JSON*
          },
          "rt": {
               *RT in formato JSON*
          },
          "pendenza": "/pendenze/A2A-DEMO/987"
        }
      ],
      "pendenze": [
            *Elenco delle pendenze presenti nel pagamento*
      ]
    }

Nella risposta ottenuta l'esito del pagamento è rappresentato dal campo `stato` con i seguenti possibili valori:
- IN_CORSO
- ESEGUITO
- NON_ESEGUITO
- PARZIALMENTE_ESEGUITO

In aggiunta si ottiene la lista delle coppie RPT ed RT scambiate con pagoPA e la lista delle pendenze oggetto del pagamento. 

