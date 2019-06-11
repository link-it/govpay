Feature: Eventi di verifica della pendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def tipoRicevuta = "R01"

Scenario: Eventi attiva pendenza annullata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* def ccp = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.stato = 'ANNULLATA'
* set pendenzaPut.descrizioneStato = 'Test annullamento'
* call read('classpath:utils/pa-prepara-avviso-annullato.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-attiva-rpt.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paaAttivaRPT'
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
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "PAA_PAGAMENTO_ANNULLATO",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""


Scenario: Evento attiva pendenza scaduta

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* def ccp = getCurrentTimeMillis()

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.stato = 'SCADUTA'
* set pendenzaPut.descrizioneStato = 'Test scadenza'
* call read('classpath:utils/pa-prepara-avviso-scaduto.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-attiva-rpt.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paaAttivaRPT'
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
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "PAA_PAGAMENTO_SCADUTO",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

Scenario: Evento attiva pendenza sconosciuta

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()

* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-attiva-rpt.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paaAttivaRPT'
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
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "PAA_PAGAMENTO_SCONOSCIUTO",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

Scenario: Evento attiva pendenza 

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

* def ccp = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-attiva-rpt.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paaAttivaRPT'
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
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento" : "#notnull",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

Scenario: Evento attiva pendenza applicazione non disponibile

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* def ccp = getCurrentTimeMillis()

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

* set applicazione.servizioIntegrazione.url = 'http://badhost:8888/paServiceImpl/v1' 

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-attiva-rpt.feature')

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paaAttivaRPT'
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
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "FAIL",
	"sottotipoEsito": "PAA_SYSTEM_ERROR",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

Scenario: Evento attiva pendenza applicazione risposta errata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* def ccp = getCurrentTimeMillis()

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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-attiva-rpt.feature')

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paaAttivaRPT'
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
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "FAIL",
	"sottotipoEsito": "PAA_SYSTEM_ERROR",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

Scenario: Evento attiva pendenza applicazione risposta con errori di sintassi

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

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-attiva-rpt.feature')

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paaAttivaRPT'
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
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "FAIL",
	"sottotipoEsito": "PAA_SYSTEM_ERROR",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

