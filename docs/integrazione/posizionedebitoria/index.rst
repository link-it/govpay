.. _integrazione_posizionedebitoria:

.. INFO::
   In questa sezione viene descritto il processo di gestione di una posizione debitoria 
   tramite le API GovPay.

Caricamento di una posizione debitoria
======================================

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
