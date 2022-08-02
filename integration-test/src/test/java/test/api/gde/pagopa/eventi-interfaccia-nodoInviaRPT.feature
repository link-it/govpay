Feature: Eventi nodoInviaRpt

Background:

* callonce read('classpath:utils/common-utils.feature')
* def gdeBaseurl = govpay_url + '/govpay/backend/api/gde'

Scenario: Eventi nodoInviaRpt accettato

* call read('classpath:test/workflow/modello3/v1/modello3-pagamento-eseguito.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(200)

Given url gdeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'nodoInviaRPT'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""
And match response._embedded.eventi[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "#notnull",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "nodoInviaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": "##null",
	"parametriRisposta": "##null",
	"_links" : "#ignore"
}
"""
And match response._embedded.eventi[0].datiPagoPA == 
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
Given url gdeBaseurl
And path '/eventi', response._embedded.eventi[0].id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "#notnull",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "nodoInviaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#(ndpsym_url +'/pagopa/PagamentiTelematiciRPTservice')",
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

