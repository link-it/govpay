.. _integrazione_rendicontazioni:

Acquisizione delle rendicontazioni
==================================

I Flussi di Rendicontazione (FdR) pubblicati dai PSP su pagoPA sono
acquisiti in GovPay dal componente govpay-fdr-batch, che per ogni dominio
con l'acquisizione delle rendicontazioni abilitata scarica i flussi
pubblicati dopo l'ultimo già acquisito, li salva nel database di GovPay e
li collega ai pagamenti e alle pendenze. Configurazione ed esecuzione del
componente sono descritte nel suo `README <https://github.com/link-it/govpay-fdr-batch>`__
(vedi anche :ref:`inst_componenti_aggiuntivi`).

I flussi acquisiti possono essere recuperati tramite le API Ragioneria.

Per l'acquisizione dei flussi di rendicontazione si procede in prima
istanza a individuare i flussi presenti, eventualmente filtrando per
dominio, data di emissione, stato, identificativo del flusso o IUV
rendicontato. L'operazione di ricerca si effettua tramite
invocazione della *GET /flussiRendicontazione*, fornendo i parametri di
ricerca richiesti.

Un flusso può essere ripubblicato dal PSP in più revisioni: ogni flusso
riporta il numero di *revisione* e le date di pubblicazione
(*dataOraPubblicazione*) e di aggiornamento (*dataOraAggiornamento*).
All'acquisizione di una nuova revisione le precedenti diventano obsolete;
con il parametro *escludiObsoleti=true* la ricerca restituisce solo le
revisioni correnti.

Ad esempio:

.. code-block:: none

    GET /flussiRendicontazione?idDominio=01234567890&dataDa=2017-11-21T00:00:00&dataA=2017-12-31T23:59:59&escludiObsoleti=true&pagina=2
    HTTP 200 OK
    {
        "numRisultati": 30,
        "numPagine": 2,
        "risultatiPerPagina": 25,
        "pagina": 2,
        "risultati":
        [
            {
                "idFlusso": "2017-11-21ABI12345-10:27:27.903",
                "dataFlusso": "2017-11-21T10:27:27.903",
                "data": "2017-11-22T08:00:00",
                "trn": "TRN123445",
                "dataRegolamento": "2017-11-21",
                "revisione": 1,
                "idPsp": "ABI-12345",
                "bicRiversamento": "BIC-12345",
                "dominio":
                {
                    "idDominio": "01234567890",
                    "ragioneSociale": "Ente Creditore"
                },
                "numeroPagamenti": 1,
                "importoTotale": 100.01,
                "stato": "Acquisito"
            },
            {
                …
            }
        ]
    }

Dalla lista dei risultati forniti si estraggono gli identificativi dei
flussi che interessano e si procede all'acquisizione del dettaglio
tramite l'invocazione dell'operazione *GET
/flussiRendicontazione/{idDominio}/{idFlusso}*, che restituisce la
revisione corrente del flusso (per una specifica emissione si usa
*GET /flussiRendicontazione/{idDominio}/{idFlusso}/{dataOraFlusso}*).

Ad esempio:

.. code-block:: none

    GET /flussiRendicontazione/01234567890/2017-11-21ABI12345-10:27:27.903
    Accept: application/json
    HTTP 200 OK
    {
        "idFlusso": "2017-11-21ABI12345-10:27:27.903",
        "dataFlusso": "2017-11-21T10:27:27.903",
        "data": "2017-11-22T08:00:00",
        "trn": "idriversamento123445",
        "dataRegolamento": "2017-11-21",
        "revisione": 1,
        "idPsp": "ABI-12345",
        "bicRiversamento": "BIC-12345",
        "dominio":
        {
            "idDominio": "01234567890",
            "ragioneSociale": "Ente Creditore"
        },
        "numeroPagamenti": 1,
        "importoTotale": 10.01,
        "stato": "Acquisito",
        "rendicontazioni":
        [
            {
                "iuv": "RF23567483937849450550875",
                "iur": "1234acdc",
                "indice": 1,
                "importo": 10.01,
                "esito": 0,
                "data": "2017-11-20",
                "stato": "OK",
                "riscossione":
                {
                    "dominio":
                    {
                        "idDominio": "01234567890",
                        "ragioneSociale": "Ente Creditore"
                    },
                    "iuv": "RF23567483937849450550875",
                    "idRicevuta": "100000000000902038186",
                    "iur": "1234acdc",
                    "indice": 1,
                    "stato": "RISCOSSA",
                    "tipo": "ENTRATA",
                    "importo": 10.01,
                    "data": "2017-11-20T10:15:00",
                    "vocePendenza":
                    {
                        "idVocePendenza": "abcdef12345_1",
                        "descrizione": "Sanzione CdS n. abc00000",
                        "pendenza":
                        {
                            "idA2A": "GestPag",
                            "idPendenza": "abcdef12345",
                            "dataCaricamento": "2017-11-01"
                        }
                    }
                }
            }
        ]
    }

È possibile anche acquisire il tracciato del flusso in formato originale
impostando l'header http Accept ad application/xml.
