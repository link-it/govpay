.. _govpay_scenari_spontaneo1_realizzazione:

Realizzazione
-------------

In questo scenario, *Modello 1 - Pagamento spontaneo ad iniziativa ente*, il cittadino procede tramite il portale dell'ente creditore alla definizione della pendenza e quindi procede al relativo pagamento sempre dal portale. I passi in cui il cittadino inserisce i dati e compone la pendenza da pagare non coinvolgono GovPay e non sono quindi esposti in questo scenario. Successivamente, con la conferma del cittadino pagatore, si avvia il processo di pagamento tramite GovPay con l'attuazione dei seguenti passaggi che saranno descritti di seguito:

- **Avvio Pagamemto**
    Il portale dell'ente creditore, dopo aver raccolto le informazioni necessarie alla formazione della pendenza, invia la richiesta di pagamento a GovPay.

- **Acquisizione Esito**
    il portale dell'ente creditore, al termine del flusso di navigazione che ha portato al completamento del versamento, acquisisce l'esito da visualizzare e la ricevuta telematica da mettere a disposizione per il download.


Avvio Pagamento
~~~~~~~~~~~~~~~

Ottenuta dall’utente la conferma a procedere, il Portale avvia il pagamento eseguendo l’operazione *POST /pagamenti*, inserendo nel corpo della richiesta la lista delle pendenze (i cui dati sono stati acquisiti dal portale).

Vediamo di seguito un esempio di invocazione della *POST /pagamenti*

.. code-block:: json
    :caption: Richiesta *POST /pagamenti*

    POST /pagamenti
    {
        "urlRitorno":"https://pagamenti.ente.it/pagopa/",
        "pendenze":
        [
            {
                "idA2A":"GestPag",
                "idPendenza":"1527844941778",
                "idDominio":"01234567890",
                "causale":"Prestazione n.1527844941778",
                "soggettoPagatore":
                {
                    "tipo":"F",
                    "identificativo":"RSSMRA30A01H501I",
                    "anagrafica":"Mario Rossi"
                },
                "importo":45.01,
                "numeroAvviso":"002152784494177803",
                "dataCaricamento":"2018-06-01",
                "dataValidita":"2018-06-01",
                "tassonomia":"Ticket n.1527844941778",
                "tassonomiaAvviso":"Ticket e prestazioni sanitarie",
                "voci":
                [
                    {
                    "indice":1,
                    "idVocePendenza":"1527844941778-1100",
                    "importo":45.01,
                    "descrizione":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
                    "codiceContabilita":"COD_CONTABILITA_11",
                    "ibanAccredito":"IT02L1234512345123456789012",
                    "tipoContabilita":"ALTRO"
                    }
                ]
            }
        ]
    }

.. code-block:: json
    :caption: Risposta *POST /pagamenti*

    HTTP 201 CREATED
    {
        "id":"e4518f13ecc14381a689c770449f3711",
        "location":"/pagamenti/e4518f13ecc14381a689c770449f3711",
        "redirect":"http://localhost:8080/govpay-ndpsym/wisp/rs/scelta?idSession=6966661822b14c078191f9e251b1038a",
        "idSession":"6966661822b14c078191f9e251b1038a"
    }

In assenza di errori, la risposta ottenuta contiene le seguenti informazioni:

    - L'identificativo **id** del pagamento assegnato da GovPay

    - Una URL, contenuta in **location**, da utilizzare successivamente per richiedere aggiornamenti sullo stato del pagamento

    - L'identificativo **idSession** necessario a riconciliare la sessione di pagamento al ritorno dell’utente sul portale, nella fase conclusiva di esito

    - Una URL, contenuta in **redirect**, a cui indirizzare l’utente per proseguire nella successiva fase di esecuzione del pagamento.

Una volto confermato il corretto avvio del pagamento, il portale effettua la redirezione dell'utente sulla URL ottenuta come **redirect**, per procedere con la fase di esecuzione del versamento.


Acquisizione Esito
~~~~~~~~~~~~~~~~~~
Quando l'utente ha completato l'esecuzione del versamento, tramite il PSP, torna automaticamente sul portale dove quest'ultimo gli visualizza l'esito complessivo dell'operazione.
L’utente include nella url di redirezione due parametri che l’integratore deve estrarre dalla query string:

    - **idSession**: corrisponde all’omonimo parametro ottenuto da GovPay in fase di avvio, necessario a riconciliare la sessione di pagamento;

    - **esito**: informazione usabile dal portale per la selezione della pagina da presentare all’utente. È importante sottolineare che l’esito certo del pagamento è comunque dato dalla Ricevuta Telematica (RT). I valori di esito possono essere:

        - *OK*: l’operazione di pagamento sul Portale del PSP si è conclusa con l’addebito dell’importo necessario.

        - *ERROR*: l’operazione di pagamento sul Portale del PSP si è conclusa senza l’addebito dell’importo necessario.

        - *DIFFERITO*: l’esito dell’operazione sarà disponibile solo alla ricezione della RT.

In caso di esito di ERROR, il Portale Ente può mostrare all’utente una pagina di errore, in alternativa visualizza una pagina interlocutoria mentre richiede l’esito del pagamento a GovPay.

GovPay può comunicare l'esito del pagamento con le seguenti modalità:

    1 - Se in configurazione è stato fornito un riferimento ad un servizio per la ricezione delle notifiche, GovPay invia una notifica di pagamento comprensiva di RPT, RT e il dettaglio delle riscossioni (quest'ultimo solo nel caso di esito positivo)

.. code-block:: json
    :caption: Notifica di pagamento inviata da GovPay all'ente creditore (modalità PUSH)

    POST /pagamenti/01234567890/152784500130106
    {
        "idA2A":"GestPag",
        "idPendenza":"1527844941778",
        "rpt":
        {
            "versioneOggetto":"6.2",
            "dominio":
            {
                --[OMISSIS]--
            },
            "identificativoMessaggioRichiesta":"e4518f13ecc14381a689c770449f3711",
            "dataOraMessaggioRichiesta":"2018-06-01",
            "autenticazioneSoggetto":"N_A",
            "soggettoVersante":
            {
            --[OMISSIS]--
            },
            "soggettoPagatore":
            {
                --[OMISSIS]--
            },
            "enteBeneficiario":
            {
                --[OMISSIS]--
            },
            "datiVersamento":
            {
                --[OMISSIS]--
            }
        },
        "rt":
        {
            "versioneOggetto":"6.2",
            "dominio":
            {
                --[OMISSIS]--
            },
            "identificativoMessaggioRicevuta":"e4518f13ecc14381a689c770449f3711",
            "dataOraMessaggioRicevuta":"2018-06-01",
            "riferimentoMessaggioRichiesta":"e4518f13ecc14381a689c770449f3711",
            "riferimentoDataRichiesta":"2018-06-01",
            "istitutoAttestante":
            {
                --[OMISSIS]--
            },
            "enteBeneficiario":
            {
                --[OMISSIS]--
            },
            "soggettoVersante":
            {
                --[OMISSIS]--
            },
            "soggettoPagatore":
            {
                --[OMISSIS]--
            },
            "datiPagamento":
            {
                --[OMISSIS]--
            }
        },
        "riscossioni":
        [
            {
                "iur":"idRisc-152784500130106",
                "indice":1,
                "idVocePendenza":"1527844941778-1100",
                "stato":null,
                "tipo":null,
                "importo":45.01,
                "data":"2018-06-01",
                "commissioni":null,
                "allegato":null,
            }
        ]
    }

    2 - Se l'ente non prevede l'esposizione di un servizio per la ricezione delle notifiche di pagamento, può utilizzare il metodo PULL e quindi interrogare di propria iniziativa GovPay per ottenere l'esito del pagamento. In questo caso può utilizzare la URL ottenuta nella risposta dell'avvio pagamento con il campo **location**:

.. code-block:: json
    :caption: Richiesta dettaglio del pagamento (modalità PULL)

    GET /pagamenti/e4518f13ecc14381a689c770449f3711

    {
        "id":"e4518f13ecc14381a689c770449f3711",
        "nome":"Prestazione n.1527844941778",
        "dataRichiestaPagamento":"2018-06-01",
        "idSessionePortale":null,
        "idSessionePsp":"13a3b51f0e6f4875acac761ac96a53a8",
        "importo":45.01,
        "stato":"ESEGUITO",
        "pspRedirectUrl":"http://lab.link.it/govpay-ndpsym/wisp/rs/scelta?idSession=13a3b51f0e6f4875acac761ac96a53a8",
        "urlRitorno":"https://portale.ente.it/pagopa/?idSession=6966661822b14c078191f9e251b1038a",
        "contoAddebito":null,
        "dataEsecuzionePagamento":null,
        "credenzialiPagatore":null,
        "soggettoVersante":
        {
            --[OMISSIS]--
        },
        "autenticazioneSoggetto":null,
        "lingua":"IT",
        "pendenze":
        [
            {
                "causale":"Prestazione n.1527844941778",
                "soggettoPagatore":
                {
                    --[OMISSIS]--
                },
                "importo":45.01,
                "numeroAvviso":"002152784547130177",
                "dataCaricamento":"2018-06-01",
                "dataValidita":"2018-06-01",
                "dataScadenza":null,
                "annoRiferimento":null,
                "cartellaPagamento":null,
                "datiAllegati":null,
                "tassonomia":"Ticket n.1527844941778",
                "tassonomiaAvviso":"Ticket e prestazioni sanitarie",
                "idA2A":"GestPag",
                "idPendenza":"1527844941778",
                "dominio":
                {
                    --[OMISSIS]--
                },
                "unitaOperativa":null,
                "stato":"ESEGUITA",
                "segnalazioni":null,
                "rpp":"/rpp?idA2A=GestPag&idPendenza=1527844941778",
                "pagamenti":"/pagamenti?idA2A=GestPag&idPendenza=1527844941778"
            }
        ],
        "rpp":
        [
            {
                "stato":"RT_ACCETTATA_PA",
                "dettaglioStato":null,
                "segnalazioni":null,
                "rpt":
                {
                    --[OMISSIS]--
                },
                "rt":
                {
                    --[OMISSIS]--
                },
                "pendenza":"/pendenze/GestPag/1527844941778"
            }
        ]
    }


