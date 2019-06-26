Feature: Eventi di verifica della pendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Evento verifica pendenza annullata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.stato = 'ANNULLATA'
* set pendenzaPut.descrizioneStato = 'Test annullamento'
* call read('classpath:utils/pa-prepara-avviso-annullato.feature')

* call read('classpath:utils/psp-verifica-rpt.feature')

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'getAvviso'
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
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#ignore",
	"idPagamento":"##null",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "Pendenza Annullata",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#('http://localhost:8888/paServiceImpl/v1/avvisi/' + idDominio + '/' + iuv)",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].dettaglioEsito == pendenzaPut.descrizioneStato

Scenario: Evento verifica pendenza scaduta

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.stato = 'SCADUTA'
* set pendenzaPut.descrizioneStato = 'Test scadenza'
* call read('classpath:utils/pa-prepara-avviso-scaduto.feature')
* call read('classpath:utils/psp-verifica-rpt.feature')

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'getAvviso'
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
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#ignore",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "Pendenza Scaduta",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "##null",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#('http://localhost:8888/paServiceImpl/v1/avvisi/' + idDominio + '/' + iuv)",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].dettaglioEsito == pendenzaPut.descrizioneStato

Scenario: Evento verifica pendenza sconosciuta

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

* call read('classpath:utils/psp-verifica-rpt.feature')

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'getAvviso'
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
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "Pendenza Sconosciuta",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "##null",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#('http://localhost:8888/paServiceImpl/v1/avvisi/' + idDominio + '/' + iuv)",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""

Scenario: Evento verifica pendenza 

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* call read('classpath:utils/psp-verifica-rpt.feature')

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'getAvviso'
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
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"principal": "##null",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#('http://localhost:8888/paServiceImpl/v1/avvisi/' + idDominio + '/' + iuv)",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""

Scenario: Evento verifica pendenza applicazione non disponibile

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* set applicazione.servizioIntegrazione.url = 'http://badhost:8888/paServiceImpl/v1' 

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:utils/psp-verifica-rpt.feature')

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'getAvviso'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	"numRisultati": "#number",
	"numPagine": "#number",
	"risultatiPerPagina": 25,
	"pagina": 1,
	"prossimiRisultati": "#ignore",
	"risultati": "#array"
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "FAIL",
	"sottotipoEsito": "403",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#(applicazione.servizioIntegrazione.url + '/avvisi/' + idDominio + '/' + iuv)",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 403,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""

* def payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta contains "UnknownHostException"

Scenario: Evento verifica pendenza applicazione risposta errata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* set applicazione.servizioIntegrazione.url = ente_api_url + "/vERROR"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:utils/psp-verifica-rpt.feature')

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'getAvviso'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	"numRisultati": "#number",
	"numPagine": "#number",
	"risultatiPerPagina": 25,
	"pagina": 1,
	"prossimiRisultati": "#ignore",
	"risultati": "#array"
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "FAIL",
	"sottotipoEsito": "500",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#(applicazione.servizioIntegrazione.url + '/avvisi/' + idDominio + '/' + iuv)",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 500,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
* def payloadRisposta = decodeBase64(response.risultati[0].parametriRisposta.payload)
* match payloadRisposta ==
"""
<SOAP-ENV:Envelope xmlns:SOAP-ENV = "http://schemas.xmlsoap.org/soap/envelope/"
   xmlns:xsi = "http://www.w3.org/1999/XMLSchema-instance"
   xmlns:xsd = "http://www.w3.org/1999/XMLSchema">
   <SOAP-ENV:Body>
      <SOAP-ENV:Fault>
         <faultcode xsi:type = "xsd:string">SOAP-ENV:Client</faultcode>
         <faultstring xsi:type = "xsd:string">Failed to locate method</faultstring>
      </SOAP-ENV:Fault>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
"""

Scenario: Evento verifica pendenza applicazione risposta con errori di sintassi

* def idPendenza = getCurrentTimeMillis()

* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'

* set pendenza.soggettoPagatore = null

Given url ente_api_url
And path '/v1/avvisi', idDominio, iuv
And request pendenza
When method post
Then status 200

* call read('classpath:utils/psp-verifica-rpt.feature')

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'getAvviso'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	"numRisultati": "#number",
	"numPagine": "#number",
	"risultatiPerPagina": 25,
	"pagina": 1,
	"prossimiRisultati": "#ignore",
	"risultati": "#array"
}
"""
And match response.risultati[0] ==
"""
{  
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "Pendenza non valida",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#(applicazione.servizioIntegrazione.url + '/avvisi/' + idDominio + '/' + iuv)",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].dettaglioEsito contains 'soggettoPagatore'
