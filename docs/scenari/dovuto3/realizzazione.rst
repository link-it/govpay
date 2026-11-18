.. _govpay_scenari_dovuto3_realizzazione:

Realizzazione
-------------

In questo scenario, *Pagamento ad Iniziativa PSP*, il cittadino debitore, in possesso dell'avviso di pagamento fornitogli dall'ente creditore, effettua il pagamento tramite il PSP che ha scelto. 

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
	È sufficiente indicare nella richiesta di caricamento di una pendenza (invocando l’operazione PUT /pendenze/{idA2A}/{idPendenza} delle API Pendenze) il parametro **stampaAvviso** valorizzato a **true**.

- Caso **Solo generazione avviso senza caricamento pendenza**
	il Gestionale Pendenze può avvisare in autonomia il pagamento generando internamente il numero avviso identificativo e non alimentare l’archivio dei pagamenti in attesa di GovPay.

Il seguente esempio mostra l'invocazione della **PUT /pendenze/{idA2A}/{id_pendenza}** per caricare la pendenza con la restituzione dell'avviso di pagamento sulla risposta. Si assume che l'applicazione sia stata registrata con identificativo **GestPag** e la pendenza abbia id **ABC-001**.
Il base path ``/govpay-api-pendenze`` è quello del dispiegamento su Tomcat; nel dispiegamento EAR è ``/govpay/backend/api/pendenze`` (vedi :ref:`integrazione_api`).

.. code-block:: text
	:caption: Richiesta *PUT /pendenze/{idA2A}/{id_pendenza}*

	PUT https://govpay.example.org/govpay-api-pendenze/rs/basic/v2/pendenze/GestPag/ABC-001?stampaAvviso=true
	{
		"idDominio":"01234567890",
		"idTipoPendenza":"TICKET",
		"soggettoPagatore":
		{
			"tipo":"F",
			"identificativo":"RSSMRA30A01H501I",
			"anagrafica":"Mario Rossi",
			"email":"mario@dimostrativo.it"
		},
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

.. code-block:: text
	:caption: Risposta *PUT /pendenze/{idA2A}/{id_pendenza}*

	HTTP 201 Created
	{
		"idDominio":"01234567890",
		"numeroAvviso":"001110000000000164",
		"UUID":"a1b2c3d4e5f6...",
		"pdf":"...BASE64..."
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

.. code-block:: text
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
	    "dataValidita":"2026-12-31",
	    "dataScadenza":"2027-12-31",
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

Superata la fase di verifica, il PSP perfeziona la riscossione degli importi dovuti e completa il processo di pagamento. GovPay gestisce il colloquio previsto con la piattaforma pagoPA e notifica l’esito delle operazioni al Gestionale Pendenze tramite l’operazione **PUT /ricevute/{idDominio}/{iuv}/{idRicevuta}** delle API Ente Creditore v2. Lo iuv è quello associato al numero avviso generato in precedenza, mentre l'idRicevuta è l'identificativo della ricevuta assegnato da pagoPA (*receiptId*).

.. code-block:: text
	:caption: Notifica del Pagamento con *PUT /ricevute/{idDominio}/{iuv}/{idRicevuta}*

	PUT /ricevute/01234567890/110000000000164/a1b2c3d4e5f60001
	{
	    "dominio":
	    {
	        "idDominio":"01234567890",
	        "ragioneSociale":"Comune Dimostrativo"
	    },
	    "iuv":"110000000000164",
	    "idRicevuta":"a1b2c3d4e5f60001",
	    "importo":45.01,
	    "esito":"ESEGUITO",
	    "istitutoAttestante":
	    {
	        "idPSP":"ABCDITMMXXX",
	        "denominazione":"Banca Dimostrativa S.p.A.",
	        "idCanale":"01234567890_01"
	    },
	    "pendenza":
	    {
	        "idA2A":"GestPag",
	        "idPendenza":"ABC-001",
	        "idTipoPendenza":"TICKET",
	        "causale":"Prestazione n.ABC-001",
	        "importo":45.01,
	        "numeroAvviso":"001110000000000164",
	        "voci":
	        [
	            {
	                "idVocePendenza":"ABC-001-1100",
	                "descrizione":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
	                "riscossione":
	                {
	                    "iuv":"110000000000164",
	                    "iur":"a1b2c3d4e5f60001",
	                    "indice":1,
	                    "tipo":"ENTRATA",
	                    "importo":45.01,
	                    "data":"2026-11-18"
	                }
	            }
	        ]
	    },
	    "data":"2026-11-18T10:15:00",
	    "dataPagamento":"2026-11-18T10:14:30",
	    "rpt":
	    {
	        "tipo":"ctPaymentPA",
	        "xml":"--[XML originale codificato in base64]--",
	        "json":{ --[OMISSIS]-- }
	    },
	    "rt":
	    {
	        "tipo":"ctReceipt",
	        "xml":"--[XML originale codificato in base64]--",
	        "json":{ --[OMISSIS]-- }
	    },
	    "modello":"Pagamento ad iniziativa PSP"
	}

Il corpo della notifica è descritto dallo schema *Ricevuta* della `specifica OpenAPI delle API Ente Creditore v2 <https://github.com/link-it/govpay/blob/3.10.x/jars/client-api-ente/src/main/resources/v2/govpay-api-ec.yaml>`__.
Con la versione ``REST_1`` del connettore GovPay invoca invece l'operazione *POST /pagamenti/{idDominio}/{iuv}* delle API Ente Creditore v1, mantenuta per compatibilità.

Si noti che una pendenza può essere oggetto di ripetuti tentativi di pagamento da parte del Soggetto Pagatore. In tal caso il Gestionale Pendenze deve saper gestire più notifiche di pagamento, che si distinguono per l'identificativo della ricevuta (*idRicevuta*, corrispondente al *receiptId* di pagoPA o al *ccp* dei pagamenti con il vecchio modello).
