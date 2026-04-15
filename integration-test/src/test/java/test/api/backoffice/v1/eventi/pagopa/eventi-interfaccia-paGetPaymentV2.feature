Feature: Eventi di attivazione della pendenza tramite paGetPaymentV2

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def tipoRicevuta = "R01"

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Eventi attiva pendenza annullata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def ccp = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* set pendenzaPut.stato = 'ANNULLATA'
* set pendenzaPut.descrizioneStato = 'Test annullamento'
* call read('classpath:utils/pa-prepara-avviso-annullato.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-paGetPaymentV2.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paGetPaymentV2'
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
	"ccp":"##string",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paGetPaymentV2",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "PAA_PAGAMENTO_ANNULLATO",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"clusterId" : "#notnull",
	"transactionId" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#(govpay_api_pagopa_url +'/PagamentiTelematiciCCPservice')",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA ==
"""
{
	"idIntermediarioPsp": "##null",
	"modelloPagamento": "UNICO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""


Scenario: Evento attiva pendenza scaduta

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def ccp = getCurrentTimeMillis()

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* set pendenzaPut.stato = 'SCADUTA'
* set pendenzaPut.descrizioneStato = 'Test scadenza'
* call read('classpath:utils/pa-prepara-avviso-scaduto.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-paGetPaymentV2.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paGetPaymentV2'
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
	"ccp":"##string",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paGetPaymentV2",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "PAA_PAGAMENTO_SCADUTO",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"clusterId" : "#notnull",
	"transactionId" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#(govpay_api_pagopa_url +'/PagamentiTelematiciCCPservice')",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA ==
"""
{
	"idIntermediarioPsp": "##null",
	"modelloPagamento": "UNICO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

Scenario: Evento attiva pendenza sconosciuta

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = getCurrentTimeMillis()

* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-paGetPaymentV2.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paGetPaymentV2'
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
	"ccp":"##string",
	"idA2A": "#(idA2A)",
	"idPendenza": "##null",
	"idPagamento": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paGetPaymentV2",
	"sottotipoEvento": "##null",
	"esito": "KO",
	"sottotipoEsito": "PAA_PAGAMENTO_SCONOSCIUTO",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"clusterId" : "#notnull",
	"transactionId" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#(govpay_api_pagopa_url +'/PagamentiTelematiciCCPservice')",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA ==
"""
{
	"idIntermediarioPsp": "##null",
	"modelloPagamento": "UNICO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

Scenario: Evento attiva pendenza

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* def ccp = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* call read('classpath:utils/pa-prepara-avviso.feature')

* def importo = pendenzaPut.importo
* call read('classpath:utils/psp-paGetPaymentV2.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(1000)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'paGetPaymentV2'
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
	"ccp":"##string",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paGetPaymentV2",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"clusterId" : "#notnull",
	"transactionId" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#(govpay_api_pagopa_url +'/PagamentiTelematiciCCPservice')",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA ==
"""
{
	"idIntermediarioPsp": "##null",
	"modelloPagamento": "UNICO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""
