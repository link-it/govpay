Feature: Registrazione evento al caricamento di una pendenza da api-pendenze

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Scenario: Evento creazione da applicazione in api Pendenze

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

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
	"componente": "API_PENDENZE",
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
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#('/govpay/backend/api/pendenze/rs/basic/v1/pendenze/' + idA2A + '/' + idPendenza)",
		"method": "PUT",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
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

Scenario: Evento creazione rifiutata per errore sintassi

* def pendenzaGet = read('classpath:test/api/pendenza/v1/pendenze/get/msg/pendenza-get-dettaglio.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* set pendenzaPut.idDominio = null

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* def pendenzaPutResponse = response

* call sleep(200)
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param messaggi = true
And param esito = 'KO'
And param tipoEvento = 'addPendenza'
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
	"idDominio": "##null",
	"iuv": "##null",
	"ccp": "##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",		
	"componente": "API_PENDENZE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "addPendenza",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "SINTASSI",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "#(idA2A)",
		"utente": "#(idA2A)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#('/govpay/backend/api/pendenze/rs/basic/v1/pendenze/' + idA2A + '/' + idPendenza)",
		"method": "PUT",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 400,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
* json payloadRichiesta = decodeBase64(response.risultati[0].parametriRichiesta.payload)
* match payloadRichiesta == pendenzaPut
* json payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta == pendenzaPutResponse

Scenario: Evento creazione rifiutata per errore semantica

* def pendenzaGet = read('classpath:test/api/pendenza/v1/pendenze/get/msg/pendenza-get-dettaglio.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* set pendenzaPut.importo = 0

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* def pendenzaPutResponse = response

* call sleep(200)
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param messaggi = true
And param esito = 'KO'
And param tipoEvento = 'addPendenza'
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
	"idDominio": "##null",
	"iuv": "##null",
	"ccp": "##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_PENDENZE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "addPendenza",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "SINTASSI",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "#(idA2A)",
		"utente": "#(idA2A)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#('/govpay/backend/api/pendenze/rs/basic/v1/pendenze/' + idA2A + '/' + idPendenza)",
		"method": "PUT",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 400,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
* json payloadRichiesta = decodeBase64(response.risultati[0].parametriRichiesta.payload)
* match payloadRichiesta == pendenzaPut
* json payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta == pendenzaPutResponse


Scenario: Evento creazione rifiutata per errore di autorizzazione

* def pendenzaGet = read('classpath:test/api/pendenza/v1/pendenze/get/msg/pendenza-get-dettaglio.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

* set applicazione.apiPendenze = false

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 403

* def pendenzaPutResponse = response

* call sleep(200)
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param messaggi = true
And param esito = 'KO'
And param tipoEvento = 'addPendenza'
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
	"idDominio": "##null",
	"iuv": "##null",
	"ccp": "##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_PENDENZE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "addPendenza",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "AUTORIZZAZIONE",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "#(idA2A)",
		"utente": "#(idA2A)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#('/govpay/backend/api/pendenze/rs/basic/v1/pendenze/' + idA2A + '/' + idPendenza)",
		"method": "PUT",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 403,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
* json payloadRichiesta = decodeBase64(response.risultati[0].parametriRichiesta.payload)
* match payloadRichiesta == pendenzaPut
* json payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta == pendenzaPutResponse

