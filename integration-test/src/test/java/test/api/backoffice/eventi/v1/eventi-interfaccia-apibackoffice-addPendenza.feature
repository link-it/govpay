Feature: Registrazione evento al caricamento di una pendenza da api-backoffice

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

Scenario: Evento creazione da operatore basic in api Backoffice

* def pendenzaGet = read('classpath:test/api/pendenza/pendenze/v1/get/msg/pendenza-get-dettaglio.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

* call sleep(200)
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"##null",
	"iuv":"##null",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",		
	"componente": "API_BACKOFFICE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "addPendenza",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"sottotipoEsito": "201",		
	"dettaglioEsito": "##null",	
	"datiPagoPA" : "##null",	
	"parametriRichiesta": {
		"principal": "gpadmin",
		"utente": "Amministratore",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#('/govpay/backend/api/backoffice/rs/basic/v1/pendenze/' + idA2A + '/' + idPendenza)",
		"method": "PUT",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 201,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
* json payloadRichiesta = decodeBase64(response.risultati[0].parametriRichiesta.payload)
* match payloadRichiesta == pendenzaPut
* json payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta == pendenzaPutResponse

Scenario: Evento creazione da operatore spid in api Backoffice

* def pendenzaGet = read('classpath:test/api/pendenza/pendenze/v1/get/msg/pendenza-get-dettaglio.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers spidHeaders
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

* call sleep(200)
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"##null",
	"iuv":"##null",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",		
	"componente": "API_BACKOFFICE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "addPendenza",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "201",	
	"dettaglioEsito": "##null",	
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",	
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "RSSMRA30A01H501I",
		"utente": "Mario Rossi",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#('/govpay/backend/api/backoffice/rs/spid/v1/pendenze/' + idA2A + '/' + idPendenza)",
		"method": "PUT",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 201,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
* json payloadRichiesta = decodeBase64(response.risultati[0].parametriRichiesta.payload)
* match payloadRichiesta == pendenzaPut
* json payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta == pendenzaPutResponse

Scenario: Evento creazione da applicazione in api Backoffice

* def pendenzaGet = read('classpath:test/api/pendenza/pendenze/v1/get/msg/pendenza-get-dettaglio.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

* call sleep(200)
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"##null",
	"iuv":"##null",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",		
	"componente": "API_BACKOFFICE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "addPendenza",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "201",	
	"dettaglioEsito": "##null",	
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",	
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "#(idA2A)",
		"utente": "#(idA2A)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#('/govpay/backend/api/backoffice/rs/basic/v1/pendenze/' + idA2A + '/' + idPendenza)",
		"method": "PUT",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 201,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
* json payloadRichiesta = decodeBase64(response.risultati[0].parametriRichiesta.payload)
* match payloadRichiesta == pendenzaPut
* json payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta == pendenzaPutResponse

