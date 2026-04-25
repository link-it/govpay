Feature: Eventi paGetPaymentV2 e paVerifyPaymentNotice

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* configure followRedirects = false

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Verifica tutti gli eventi di un Pagamento eseguito dovuto non precaricato con verifica

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* call read('classpath:utils/pa-prepara-avviso.feature')

* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')

# Attivo il pagamento

* def tipoRicevuta = "R01"
* def ccp = getCurrentTimeMillis()
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPaymentV2.feature')

# Verifico lo stato della pendenza

* call sleep(10000)

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param componente = 'API_PAGOPA'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	numRisultati: 3,
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#[3]'
}
"""

# Ricevuta RT tramite paSendRTV2

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
	"tipoEvento": "paSendRTV2",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
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
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[0].datiPagoPA ==
"""
{
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"bancomat",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)",
	"idPsp" : "GovPAYPsp1",
	"modelloPagamento" : "UNICO"
}
"""

# paGetPaymentV2

And match response.risultati[1] ==
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
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[1].datiPagoPA ==
"""
{
	"idIntermediarioPsp": "##null",
	"modelloPagamento": "UNICO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# paVerifyPaymentNotice

And match response.risultati[2] ==
"""
{
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paVerifyPaymentNotice",
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
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[2].datiPagoPA ==
"""
{
	"idIntermediarioPsp": "##null",
	"modelloPagamento": "UNICO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""
