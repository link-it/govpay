Feature: Esecuzione di un pagamento con invio ricevuta all'endpoint unico 

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* configure followRedirects = false

* def stazioneNdpSymPut = read('classpath:test/workflow/modellounico/v1/msg/stazione.json')
* set stazioneNdpSymPut.urlRT = govpay_api_pagopa_url + '/PagamentiTelematiciCCPservice'
* call read('classpath:utils/nodo-config-stazione-put.feature')

Scenario: Pagamento di una pendenza precaricata con invio della ricevuta al nuovo endpoint unico

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

# Verifica endpoint RT tramite giornale Eventi.

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
	numRisultati: 2,
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#[2]'
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

