.. _govpay_scenari_dovuto3_realizzazione:

Realizzazione
-------------

In questo scenario, *Modello 3 - Pagamento ad Inziativa PSP*, il cittadino debitore, in possesso dell'avviso di pagamento fornitogli dall'ente creditore, effettua il pagamento tramite il PSP che ha scelto. 

Ai fini dell'integrazione tra GovPay e l'Ente Creditore, per questo scenario, individuiamo i seguenti passaggi chiave:

- **Stampa Avviso di Pagamento** 
	L'ente creditore genera l'avviso di pagamento da fornire al cittadino debitore.

- **Verifica della Pendenza**
	Il PSP chiede l'aggiornamento dei dati di pagamento a GovPay. Nei casi in cui GovPay non disponga della pendenza, o se la data attuale è compresa tra quella di validità e quella di scadenza della pendenza, si procede con l'interrogazione del sistema di gestione dei pagamenti dell'ente, prima di autorizzare le operazioni di riscossione dell’importo dovuto.

- **Notifica del Pagamento**
	Al termine delle operazioni di riscossione, il gestionale dei pagamenti dell'ente creditore viene notificato dell’esito del pagamento per aggiornare lo stato della pendenza.


Stampa Avviso di Pagamento
~~~~~~~~~~~~~~~~~~~~~~~~~~

L’ente creditore, alla predisposizione di una nuova pendenza, ottiene la stampa dell’Avviso di Pagamento pagoPA ad essa associato e lo consegna al cittadino. 
Distinguiamo:

- Caso **Alimentazione Pendenza su GovPay** 
	È sufficiente indicare nella richiesta di caricamento di una pendenza (invocando l’operazione PUT /pendenze/{idA2A}/{idPendenza} delle API Pendenze) il parametro **stampaAvviso** valorizzato a **true**. Inoltre, nel caso vengo valorizzato a **true** anche il parametro **avvisaturaDigitale**, si istruisce GovPay a gestire in autonomia i processi di avvisatura digitale previsti da pagoPA, aprendo, aggiornando e chiudendo la posizione debitoria associata alla pendenza nelle varie fasi del ciclo di vita del pagamento.

- Caso **Solo generazione avviso senza caricamento pendenza**
	il Gestionale Pendenze può avvisare in autonomia il pagamento generando internamente il numero avviso identificativo e non alimentare l’archivio dei pagamenti in attesa di GovPay.

Il seguente esempio mostra l'invocazione della **PUT /pendenze/{idA2A}/{id_pendenza}** per caricare la pendenza con la restituzione dell'avviso di pagamento sulla risposta. Si assume che l'applicazione sia stata regitrata con identificativo **GestPag** e la pendenza abbia id **ABC-001**.

.. code-block:: json
	:caption: Richiesta *PUT /pendenze/{idA2A}/{id_pendenza}*

	PUT https://demo.govcloud.it/govpay/backend/api/pendenze/rs/basic/v2/pendenze/GestPag/ABC-001?stampaAvviso=true
	{
		"idDominio":"01234567890",
		"idTipoPendenza":"TICKET",
		"soggettoPagatore":
		{
			"tipo":"F",
			"identificativo":"RSSMRA30A01H501I",
			"anagrafica":"Mario Rossi",
			"email":"mario@dimostrativo.it"},
			"causale":"Prestazione n.ABC-001",
			"importo":45.01,
			"voci":
			[
				{
					"idVocePendenza":"ABC-001-1100",
					"importo":45.01,
					"descrizione":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
					"ibanAccredito":"IT02L1234512345123456789012",
					"tipoContabilita":"ALTRO",
					"codiceContabilita":"1100"
				}
			]
		}
	}

.. code-block:: json
	:caption: Risposta *PUT /pendenze/{idA2A}/{id_pendenza}*
	
	HTTP 200 OK
	{
		"idDominio":"01234567890",
		"numeroAvviso":"001110000000000164"
		"pdf":"JVBERi0xLjUKJYCBgoMKMSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgMTQxL04gMjAvTGVuZ3=="
	}


Verifica della Pendenza
~~~~~~~~~~~~~~~~~~~~~~~

Il tentativo di pagamento di un Avviso attiva una serie di verifiche da parte della piattaforma pagoPA. GovPay gestisce il colloquio e, se necessario, effettua verso il Gestore Pendenze titolare dell’Avviso oggetto di pagamento una richiesta di verifica della pendenza associata all’avviso. Ci sono due scenari in cui GovPay esegue la richiesta di verifica:

	1. Se la pendenza associata all’avviso non è presente nell’archivio dei pagamenti in attesa
	
	2. Se la pendenza è presente in archivio, ma la data di validità comunicata risulta decorsa, pur essendo la pendenza non ancora scaduta

Per data di validità si intende pertanto la data entro la quale la pendenza non subisce variazioni ai fini del pagamento. Alla sua decorrenza, il gestionale potrebbe applicare delle variazioni di importo a causa di sanzioni o interessi, che saranno recepiti da GovPay al momento del pagamento tramite le operazioni di verifica. 

Quando invece decorre la data di scadenza, GovPay gestisce eventuali verifiche che l’avviso è scaduto, interrompendone il pagamento.

GovPay interroga il gestionale dell'ente, per verificare gli estremi della pendenza da pagare, tramite l’operazione **GET /avvisi/{idDominio}/{numeroAvviso}**. I riferimenti dell'avviso generato al passo precedente sono:

	- idDominio: 01234567890

	- numeroAvviso: 001110000000000164

.. code-block:: json
	:caption: Verifica Pendenza con *GET /avvisi/{idDominio}/{numeroAvviso}*

	GET /avvisi/01234567890/001110000000000164

	HTTP 200 OK
	{
	    "idDominio":"01234567890",
	    "causale":"Prestazione n.ABC-001",
	    "soggettoPagatore":
	    {
		"tipo":"F",
		"identificativo":"RSSMRA30A01H501I",
		"anagrafica":"Mario Rossi"
	    },
	    "importo":45.01,
	    "numeroAvviso":"001110000000000164",
	    "dataValidita":"2018-06-01",
	    "dataScadenza":"2018-12-31",
	    "tassonomiaAvviso":"Ticket e prestazioni sanitarie",
	    "voci":
	    [
		{
		    "idVocePendenza":"ABC-001-1100",
		    "importo":45.01,
		    "descrizione":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
		    "codiceContabilita":"1100",
		    "ibanAccredito":"IT02L1234512345123456789012",
		    "tipoContabilita":"ALTRO"
		}
	    ],
	    "idA2A":"GestPag",
	    "idPendenza":"ABC-001",
	    "stato":"NON_ESEGUITA"
	}


Notifica del Pagamento
~~~~~~~~~~~~~~~~~~~~~~

Superata la fase di verifica, il PSP perfeziona la riscossione degli importi dovuti e completa il processo di pagamento. GovPay gestisce il colloquio previsto con la piattaforma pagoPA e notifica l’esito delle operazioni al Gestionale Pendenze tramite l’operazione **POST /pagamenti/{idDominio}/{iuv}**.

.. code-block:: json
	:caption: Notifica del Pagamento con *POST /pagamenti/{idDominio}/{iuv}*

	POST /pagamenti/01234567890/000000000000141
	{
	    "idA2A":"GestPag",
	    "idPendenza":"ABC-001",
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
		    "idDominio":"01234567890",
		    "iuv":"000000000000141",
		    "iur":"idRisc-152784362114159",
		    "indice":1,
		    "pendenza":"/pendenze/GestPag/ABC-001",
		    "idVocePendenza":"ABC-001-1100",
		    "rpp":"/rpp/01234567890/000000000000141/1871148690",
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

Si noti che una pendenza può essere oggetto di ripetuti tentativi di pagamento da parte del Soggetto Pagatore. In tal caso il Gestionale Pendenze deve saper gestire più notifiche di pagamento, che si distinguono per il parametro ccp (Codice Contesto Pagamento) indicato nella notifica medesima.