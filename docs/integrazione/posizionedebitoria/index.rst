.. _integrazione_posizionedebitoria:

.. INFO::
   In questa sezione viene descritto il processo di gestione di una posizione debitoria 
   tramite le API GovPay.

Caricamento di una posizione debitoria
======================================

Il caricamento di una posizione debitoria su GovPay si realizza tramite tramite chiamata alle API Pendenze:

	PUT /pendenze/{idA2A}/{idPendenza}[?stampaAvviso=true|false]

Dove:

- idA2A: codice identificativo dell’applicativo chiamante assegnato in sede di onboarding;
- idPendenza: codice identificativo della pendenza nel dominio dell’applicativo chiamante;
- stampaAvviso: parametro opzionale che indica se includere l’avviso di pagamento in formato pdf all’interno del messaggio di risposta.

In risposta si ottengono i riferimenti all’avviso associato alla pendenza e la stampa in formato pdf.

Il seguente esempio mostra l'invocazione della **PUT /pendenze/{idA2A}/{id_pendenza}** per caricare la pendenza con la restituzione dell'avviso di pagamento sulla risposta. Si assume che l'applicazione sia stata registrata con identificativo **GestPag** e la pendenza abbia id **ABC-001**.

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

.. code-block:: json
	:caption: Risposta *PUT /pendenze/{idA2A}/{id_pendenza}*
	
	HTTP 200 OK
	{
		"idDominio":"01234567890",
		"numeroAvviso":"001110000000000164"
		"pdf":"...BASE64..."
	}


Download avviso di pagamento
============================

L’avviso di pagamento può essere scaricato anche in una fase successiva alla creazione con la seguente chiamata:

	GET /avvisi/{idDominio}/{numeroAvviso}
	Accept: application/pdf

Dove:

- idDominio: è il codice fiscale dell'Ente Creditore in PagoPA;
- numeroAvviso: numero identificativo dell'avviso di pagamento;

Il seguente esempio mostra l'invocazione della **GET /avvisi/{idDominio}/{numeroAvviso}** per scaricare l'avviso di pagamento in formato pdf. Si assume che l'ente creditore sia stato registrato con identificativo **01234567890** e il numero avviso sia **310000000000000001**.

.. code-block:: json
	:caption: Richiesta *GET /avvisi/{idDominio}/{numeroAvviso}*
	
	GET https://demo.govcloud.it/govpay/backend/api/pendenze/rs/basic/v2/avvisi/01234567890/310000000000000001
	Accept:application/pdf
	
	HTTP/1.1 200 OK
	content-disposition: attachment; filename="01234567890_310000000000000001.pdf"
	Content-Type: application/pdf
	---[pdf dell’avviso]---


Consultazione dello stato di una pendenza
=========================================

È possibile consultare lo stato di una pendenza ed eventuali ricevute di pagamento tramite la seguente risorsa:

	GET /pendenze/{idA2A}/{idPendenza}

Dove:

- idA2A: l’identificativo con cui l’applicativo chiamante è registrato nell’anagrafica di GovPay.
- idPendenza: identificativo della pendenza nel dominio dell’applicativo

Si fa presente che questo servizio non deve essere utilizzato in procedure che prevedono strategie di polling, ad esempio per verificare l’esito di un pagamento, poiché difficilmente sostenibili.

Il seguente esempio mostra l'invocazione della **GET /pendenze/{idA2A}/{id_pendenza}** per leggere la pendenza. Si assume che l'applicazione sia stata registrata con identificativo **GestPag** e la pendenza abbia id **ABC-001**.
Di seguito un esempio di chiamata in ambiente di simulazione:

.. code-block:: json
	:caption: Richiesta *GET /pendenze/{idA2A}/{idPendenza}*

	GET https://demo.govcloud.it/govpay/backend/api/pendenze/rs/basic/v2/pendenze/GestPag/1648377136499
	HTTP 200 OK
	
	{
	   "idA2A":"GestPag",
	   "idPendenza":"1648377136499",
	   "idTipoPendenza":"ABC-001",
	   "dominio":{
	      "idDominio":"01234567890",
	      "ragioneSociale":"Ente Creditore"
	   },
	   "stato":"ESEGUITA",
	   "iuvAvviso":"10000000000000001",
	   "iuvPagamento":"10000000000000001",
	   "dataPagamento":"2023-09-07",
	   "causale":"Prestazione n.ABC-001",
	   "soggettoPagatore":{
	      "tipo":"F",
	      "identificativo":"RSSMRA30A01H501I",
	      "anagrafica":"Mario Rossi",
	      "email":"mario.rossi@testmail.it",
	      "cellulare":"+39 000-0000000"
	   },
	   "importo":45.01,
	   "numeroAvviso":"310000000000000001",
	   "dataCaricamento":"2023-09-07",
	   "dataValidita":"2023-12-31",
	   "dataScadenza":"2023-12-31",
	   "tipo":"dovuto",
	   "UUID":"a0dda98cbdf342d78883c5faec1c4c0c",
	   "voci":[
	      {
	         "idVocePendenza":"ABC-001-1100",
	         "importo":45.01,
	         "descrizione":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
	         "indice":1,
	         "stato":"Eseguito",
	         "ibanAccredito":"IT02L1234512345123456789012",
		 "tipoContabilita":"ALTRO",
		 "codiceContabilita":"1100"
	      }
	   ],
	   "rpp":[
	      {
	         "stato":"RT_ACCETTATA_PA",
	         "rpt":{
	            "creditorReferenceId":"10000000000000001",
	            "paymentAmount":"45.01",
	            "dueDate":"2023-12-31",
	            "retentionDate":null,
	            "lastPayment":true,
	            "description":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
	            "companyName":"Ente Creditore",
	            "officeName":null,
	            "debtor":{
	               "uniqueIdentifier":{
	                  "entityUniqueIdentifierType":"F",
	                  "entityUniqueIdentifierValue":"RSSMRA30A01H501I"
	               },
	               "fullName":"Mario Rossi",
	               "streetName":null,
	               "civicNumber":null,
	               "postalCode":null,
	               "city":null,
	               "stateProvinceRegion":null,
	               "country":null,
	               "e-mail":"mario.rossi@testmail.it"
	            },
	            "transferList":{
	               "transfer":[
	                  {
	                     "idTransfer":1,
	                     "transferAmount":"45.01",
	                     "fiscalCodePA":"01234567890",
	                     "IBAN":"IT02L1234512345123456789012",
	                     "remittanceInformation":"/RFB/10000000000000001/45.01/TXT/Compartecipazione alla spesa per prestazioni sanitarie",
	                     "transferCategory":"9/1100",
	                     "metadata":null
	                  }
	               ]
	            },
	            "metadata":null
	         },
	         "rt":{
	            "receiptId":"100000000000902038186",
	            "noticeNumber":"310000000000000001",
	            "fiscalCode":"01234567890",
	            "outcome":"OK",
	            "creditorReferenceId":"10000000000000001",
	            "paymentAmount":"45.01",
	            "description":"Compartecipazione alla spesa per prestazioni sanitarie (ticket)",
	            "companyName":"Ente Creditore",
	            "officeName":null,
	            "debtor":{
	               "uniqueIdentifier":{
	                  "entityUniqueIdentifierType":"F",
	                  "entityUniqueIdentifierValue":"RSSMRA30A01H501I"
	               },
	               "fullName":"Mario Rossi",
	               "streetName":null,
	               "civicNumber":null,
	               "postalCode":null,
	               "city":null,
	               "stateProvinceRegion":null,
	               "country":null,
	               "e-mail":"mario.rossi@testmail.it"
	            },
	            "transferList":{
	               "transfer":[
	                  {
	                     "idTransfer":1,
	                     "transferAmount":"45.01",
	                     "fiscalCodePA":"01234567890",
	                     "IBAN":"IT02L1234512345123456789012",
	                     "remittanceInformation":"/RFB/10000000000000001/45.01/TXT/Compartecipazione alla spesa per prestazioni sanitarie",
	                     "transferCategory":"9/1100",
	                     "metadata":null
	                  }
	               ]
	            },
	            "idPSP":"GovPAYPsp1",
	            "pspFiscalCode":"22222222222",
	            "pspPartitaIVA":null,
	            "PSPCompanyName":"Banco di Ponzi S.p.A.",
	            "idChannel":"GovPAYPsp1_PO",
	            "channelDescription":"PO",
	            "payer":null,
	            "paymentMethod":"bancomat",
	            "fee":"1.00",
	            "paymentDateTime":"2023-09-07T16:17:08",
	            "applicationDate":"2023-09-07",
	            "transferDate":"2023-09-07",
	            "metadata":null
	         }
	      }
	   ]
	}

Stampa della ricevuta di pagamento
==================================

La piattaforma consente la stampa in versione analogica della ricevuta di pagamento tramite servizio REST:

	GET /rpp/{idDominio}/{iuv}/{receiptId}/rt
	Accept: application/pdf

Dove:

- idDominio: è il codice fiscale dell'Ente Creditore in PagoPA;
- iuv: identificativo univoco versamento;
- receiptId: identificativo della ricevuta;

Il seguente esempio mostra l'invocazione della **GET /rpp/{idDominio}/{iuv}/{receiptId}/rt** per scaricare la ricevuta di pagamento in formato pdf. Si assume che l'ente creditore sia stato registrato con identificativo **01234567890**, lo iuv sia **10000000000000001** e il receiptId sia **100000000000902038186**.

.. code-block:: json
	:caption: Richiesta *GET /rpp/{idDominio}/{iuv}/{receiptId}/rt*
	
	GET
	https://demo.govcloud.it/govpay/backend/api/pendenze/rs/basic/v2/rpp/80184430587/10000000000000001/100000000000902038186/rt
	Accept: application/pdf
	
	HTTP/1.1 200 OK
	content-disposition: attachment; filename="80184430587_310000000000090203.pdf"
	Content-Type: application/pdf
	---[pdf della ricevuta]---





