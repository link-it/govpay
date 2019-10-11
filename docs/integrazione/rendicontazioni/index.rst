.. _integrazione_rendicontazioni:

Acquisizione delle rendicontazioni
==================================

I flussi di rendicontazione acquisiti da GovPay possono essere
recuperati tramite le API di Rendicontazione.

Per l'acquisizione dei flussi di rendicontazione si procede in prima
istanza a individuare i flussi presenti, eventualmente filtrando per
dominio o data di emissione. L'operazione di ricerca si effettua tramite
invocazione della *GET /flussiRendicontazione*, fornendo i parametri di
ricerca richiesti.

Ad esempio:

.. code-block:: none

    GET /flussiRendicontazione?idDominio=01234567890&dataDa=2017-11-21&dataA=2017-12-31&pagina=2
    HTTP 200 OK
    {
        "numRisultati": "30",
        "numPagine": "2",
        "risultatiPerPagina": "25",
        "pagina": "1",
        "prossimiRisultati": "/rendicontazioni?idDominio=01234567890&dataDa=2017-11-21&dataA=2017-12-31&pagina=2",
        "risultati":
        [
            {
                "idFlusso": "2017-11-21ABI12345-10:27:27.903",
                "dataFlusso": "2020-12-31",
                "trn": "TRN123445",
                "dataRegolamento": "2020-12-31",
                "idPsp": "ABI-12345",
                "bicRiversamento": "BIC-12345",
                "idDominio": "01234567890",
                "numeroPagamenti": 1,
                "importoTotale": 100.01
            },
            {
                …
            },
        ]
    }

Dalla lista dei risultati forniti si estraggono gli identificativi dei
flussi che interessano e si procede all'acquisizione del dettaglio
tramite l'invocazione dell'operazione *GET
/flussiRendicontazione/{idFlusso}*.

Ad esempio:

.. code-block:: none

    GET /flussiRendicontazione/2017-11-21ABI12345-10:27:27.903
    Accept: application/json
    HTTP 200 OK
    {
        "idFlusso": "2017-11-21GovPAYPsp1-10:27:27.903",
        "dataFlusso": "2020-12-31",
        "trn": "idriversamento123445",
        "dataRegolamento": "2020-12-31",
        "idPsp": "ABI-12345",
        "bicRiversamento": "BIC-12345",
        "idDominio": "01234567890",
        "numeroPagamenti": 1,
        "importoTotale": 10.01
        "rendicontazioni":
        [
            {
                "iuv": "RF23567483937849450550875",
                "iur": "1234acdc",
                "indice": 1,
                "importo": 10.01,
                "esito": 0,
                "data": "2018-12-31",
                "riscossione":
                {
                    "pendenza": "/pendenze/A2A_ENTE/abcdef12345",
                    "idVocePendenza": "abcdef12345_1",
                    "rpt": "/rpt/01234567890/RF23567483937849450550875/n%2Fa",
                    "importo": 10.01,
                    "ibanAccredito": "IT02L1234512345123456789012",
                    "data": "2018-12-29",
                    "commissioni": 1.5,
                    "allegato":
                    {
                        "tipo": "Esito pagamento",
                        "testo": "...."
                    }
                }
            }
        ]
    }
    
È possibile anche acquisire il tracciato del flusso in formato originale
impostando l'header http Accept ad application/xml.

