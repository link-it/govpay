
Realizzazione
=============

Nell’ambito di questa tipologia di pagamento individuiamo i seguenti
casi:

-  Consegna dell’Avviso di Pagamento

L’ente creditore, alla predisposizione di una nuova pendenza, stampa
l’Avviso di Pagamento pagoPA ad essa associata e la consegna al
cittadino.

-  Verifica della pendenza collegata all'Avviso di Pagamento

Il cittadino si reca presso il PSP per pagare tramite l'avviso Avviso di
Pagamento. Il sistema verifica gli estremi della pendenza associata
prima di autorizzare le operazioni di riscossione dell'importo dovuto.

-  Notifica del pagamento di un Avviso di Pagamento

Al termine delle operazioni di riscossione, il gestionale viene
notificato dell'esito del pagamento per aggiornare lo stato della
pendenza.

Avvisatura della pendenza
-------------------------

L’ente creditore, alla predisposizione di una nuova pendenza, ottiene la
stampa dell’Avviso di Pagamento pagoPA ad essa associato e lo consegna
al cittadino. E' sufficiente indicare nella richiesta di caricamento di
una pendenza (invocando l'operazione *PUT
/pendenze/{idA2A}/{idPendenza}* delle API Pendenze) il parametro
*stampaAvviso* valorizzato a true.

Inoltre, valorizzando a true anche il patametro *avvisaturaDigitale*,
istruisce la piattaforma a gestire in autonomia i processi di avvisatura
digitale previsti da pagoPA, aprendo, aggiornando e chiudendo la
posizione debitoria associata alla pendenza nelle varie fasi del ciclo
di vita del pagamento.

In alternativa, il Gestionale Pendenze può avvisare in autonomia il
pagamento generando internamente il numero avviso identificativo e non
alimentare l'archivio dei pagamenti in attesa di GovPay.

Pagamento dell'Avviso pagoPA
----------------------------

Il Soggetto Debitore avvia con il PSP il pagamento dell'Avviso pagoPA,
ad esempio tramite scansione dei codici grafici, utilizzando
l'applicazione mobile di home banking, o presentandone una stampa allo
sportello di una filiale. Questa fase non prevede nessuna interazione
con l'Ente Creditore.

Verifica della pendenza
-----------------------

Il tentativo di pagamento di un Avviso attiva una serie di verifiche da
parte della piattaforma pagoPA. GovPay gestisce il colloquio e, se
necessario, effettua verso il Gestore Pendenze titolare dell'Avviso
oggetto di pagamento una richiesta di verifica della pendenza associata
all'avviso tramite l'operazione *GET
/avvisi/{idDominio}/{numeroAvviso}*.

Ad esempio:

.. code-block:: none

    GET /avvisi/02315520920/000000000000141
    HTTP 200 OK
    {
        "idDominio":"02315520920",
        "causale":"Prestazione n.1527843621141",
        "soggettoPagatore":
        {
            "tipo":"F",
            "identificativo":"RSSMRA30A01H501I",
            "anagrafica":"Mario Rossi"
        },
        "importo":45.01,
        "numeroAvviso":"002000000000000141",
        "dataValidita":"2018-06-01",
        "dataScadenza":"2018-12-31",
        "tassonomiaAvviso":"Ticket e prestazioni sanitarie",
        "voci":
        [
            {
                "idVocePendenza":"1527843621141-1100",
                "importo":45.01,
                "descrizione":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
                "codiceContabilita":"1100",
                "ibanAccredito":"IT02L1234512345123456789012",
                "tipoContabilita":"ALTRO"
            }
        ],
        "idA2A":"PAG-GEST-ENTE",
        "idPendenza":"1527843621141",
        "stato":"NON_ESEGUITA"
    }

Ci sono due scenari in cui GovPay esegue la richiesta di verifica:

-  se la pendenza associata all'avviso non è presente nell'archivio dei
   pagamenti in attesa;
-  se la pendenza è presente in archivio, ma la data di validità
   comunicata risulti decorsa, pur essendo la pendenza non ancora
   scaduta;

Per data di validità si intende pertanto la data entro la quale la
pendenza non subisce variazioni ai fini del pagamento. Alla sua
decorrenza, il gestionale potrebbe applicare delle variazioni di importo
a causa di sanzioni o interessi, che saranno recepiti da GovPay al
momento del pagamento tramite le operazioni di verifica.

Quando invece decorre la data di scadenza, GovPay gestisce eventuali
verifiche che l'avviso è scaduto, interrompendone il pagamento.

Notifica del pagamento
----------------------

Superata la fase di verifica, il PSP perfeziona la riscossione degli
importi dovuti e completa il processo di pagamento. GovPay gestisce il
colloquio previsto con la piattaforma pagoPA e notifica l'esito delle
operazioni al Gestionale Pendenze tramite l'operazione *POST
/pagamenti/{idDominio}/{iuv}*.

Ad esempio:

.. code-block:: none

    POST /pagamenti/02315520920/000000000000141
    {
        "idA2A":"PAG-GEST-ENTE",
        "idPendenza":"1527843621141",
        "rpt":
        {
            "versioneOggetto":"6.2",
            "dominio":
            {
                --[OMISSIS]--
            },
            "identificativoMessaggioRichiesta":"3014931b62ab4333be07164c2fda6fa3",
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
            "identificativoMessaggioRicevuta":"3014931b62ab4333be07164c2fda6fa3",
            "dataOraMessaggioRicevuta":"2018-06-01",
            "riferimentoMessaggioRichiesta":"3014931b62ab4333be07164c2fda6fa3",
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
                "idDominio":"02315520920",
                "iuv":"000000000000141",
                "iur":"idRisc-152784362114159",
                "indice":1,
                "pendenza":"/pendenze/PAG-GEST-ENTE/1527843621141",
                "idVocePendenza":"1527843621141-1100",
                "rpp":"/rpp/02315520920/000000000000141/1871148690",
                "stato":null,
                "tipo":null,
                "importo":45.01,
                "data":"2018-06-01",
                "commissioni":null,
                "allegato":null,
                "incasso":null
            }
        ]
    }

Si fa notare che una pendenza può essere oggetto di ripetuti tentativi
di pagamento da parte del Soggetto Pagatore. In tal caso il Gestionale
Pendenze deve saper gestire più notifiche di pagamento che si
distinguono per il parametro ccp (Codice Contesto Pagamento) indicato
nella notifica.
