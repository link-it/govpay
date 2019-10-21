
Esecuzione
----------

In questo scenario i debitori utilizzano il portale di pagamento dell'ente creditore per consultare la propria posizione debitoria, selezionare le pendenze che vuol pagare e procedere al pagamento dopo aver composto un carrello di pendenze.

La realizzazione di questo scenario prevede due distinti gruppi di interazioni con le API di Govpay:

1- *Il caricamento della pendenza nell’archivio dei pagamenti in attesa da parte del gestionale*

2- *La consultazione della posizione debitoria e la gestione del processo di pagamento da parte del portale ente*


Caricamento della Pendenza
~~~~~~~~~~~~~~~~~~~~~~~~~~
Il caricamento della pendenza, nell'archivio dei pagamenti in attesa di Govpay, viene effettuato dall'applicazione tramite l'invocazione dell’operazione PUT /pendenze/{idA2A}/{idPendenza} dell’API Pendenze.

Utilizzando l'ambiente DEMO di Govpay vediamo di seguito un esempio di caricamento di una pendenza:

.. code-block:: none

    PUT /govpay/backend/api/pendenze/rs/basic/v2/pendenze/A2A-DEMO/987

    HTTP 201
    {
      "idTipoPendenza": "SANZIONE",
      "idDominio": "01234567890",
      "causale": "Sanzione CdS n. abc00000",
      "soggettoPagatore": {
        "tipo": "F",
        "identificativo": "RSSMRA30A01H501I",
        "anagrafica": "Mario Rossi"
      },
      "importo": 10.01,
      "tassonomiaAvviso": "Multe e sanzioni amministrative",
      "dataValidita": "2019-12-31",
      "dataScadenza": "2020-12-31",
      "voci": [
        {
          "idVocePendenza": "12345-RATA1",
          "importo": 10.01,
          "descrizione": "Sanzione per divieto di sosta",
          "ibanAccredito": "IT02L1234500000999990000001",
          "tipoContabilita": "ALTRO",
          "codiceContabilita": "ABC"
        }
      ]
    }

Dove **A2A-DEMO** è l'identificativo del gestionale autenticato che effettua il caricamento della pendenza e **987** è l'identificativo della pendenza fornita.

La risposta ottenuta è il messaggio seguente:

.. code-block:: none

    HTTP 201
    {
      "idDominio": "01234567890",
      "numeroAvviso": "001990000000003301"
    }

Dove, oltre all'idDominio ricevuto per conferma, si ottiene anche il numero dell'avviso di pagamento automaticamente generato da Govpay.


Esecuzione del Pagamento da portale
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
L'utente pagatore si autentica sul portale di pagamento e seleziona la pendenza tra quelle presenti nella propria posizione debitoria.
Il portale recupera la posizione debitoria del soggetto autenticato richiedendo tramite l’operazione GET /pendenze della API Pagamento, la lista delle pendenze disponibili sul sistema filtrando per identificativo debitore.

.. code-block:: none

    GET /govpay/frontend/api/pagamento/rs/basic/v2/pendenze?idDebitore=RSSMRA30A01H501I&stato=NON_ESEGUITA

    HTTP 200
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
          "numeroAvviso": "001990000000003301",
          "dataCaricamento": "2019-10-18",
          "dataValidita": "2019-12-31",
          "dataScadenza": "2020-12-31",
          "tassonomiaAvviso": "Multe e sanzioni amministrative",
          "rpp": "/rpp?idA2A=A2A-DEMO&idPendenza=987",
          "pagamenti": "/pagamenti?idA2A=A2A-DEMO&idPendenza=987"
        }
      ]
    }

Le pendenze ottenute saranno visualizzate all’utente che procederà alla selezione e successivo pagamento. Al termine della selezione, il portale necessita dei soli riferimenti identificativi delle pendenze, ovvero la coppia di parametri idA2A e idPendenza, per la successiva fase di avvio del pagamento.

.. code-block:: none

    POST /govpay/frontend/api/pagamento/rs/basic/v2/pagamenti
    {
      "pendenze": [
        {
          "idA2A": "A2A-DEMO",
          "idPendenza": "987"
        }
      ]
    }

La risposta che si ottiene è la seguente:

.. code-block:: none

    HTTP 201
    {
      "id": "1d16d7b741024c6a8a3e3596957482b8",
      "location": "/pagamenti/1d16d7b741024c6a8a3e3596957482b8",
      "redirect": "https://demo.govcloud.it/govpay-ndpsym/wisp/rs/scelta?idSession=18cb852db0f041068b0063d8d580380c",
      "idSession": "18cb852db0f041068b0063d8d580380c"
    }

Il portale farà proseguire la navigazione all'indirizzo ottenuto con il campo **redirect** al fine di consentire l'esecuzione del pagamento in accordo al flusso pagoPA. Al termine dell'operazione verrà eseguito il redirect alla pagina di ritorno impostata per il portale al fine di visualizzare l'esito. Il portale è in grado di verificare direttamente l'esito dell'operazione di pagamento utilizzando l'identificativo ottenuto in precedenza (campo **id**) ed effettuando la seguente chiamata:

.. code-block:: none

    GET /govpay/frontend/api/pagamento/rs/basic/v2/pagamenti/1d16d7b741024c6a8a3e3596957482b8

    HTTP 200
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
               *RPT originale JSON*
          },
          "rt": {
               *RT originale JSON*
          },
          "pendenza": "/pendenze/A2A-DEMO/987"
        }
      ],
      "pendenze": [
            *Elenco delle pendenze presenti nel pagamento*
     ]
    }

Nella risposta ottenuta l'esito del pagamento è rappresentato dal campo **stato**. Sono inoltre presenti ulteriori elementi quali:

- Il dettaglio JSON della RPT che ha dato origine alla transazione

- Il dettaglio JSON della RT che è stata inviata dal Nodo dei Pagamenti al termine dell'operazione

- Il dettaglio delle pendenze che fanno parte del pagamento effettuato