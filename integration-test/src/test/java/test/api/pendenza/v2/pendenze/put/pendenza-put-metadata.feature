Feature: Caricamento pendenza con campi metadata

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_definito.json')
* def pendenzaResponse = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def metadataCustom = 
"""
{
	"mapEntries" : [
		{
			"key":"chiave1", "value":"valore1"
		}
	]
}
"""

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Caricamento pendenza con metadata definiti

* set pendenzaPut.voci[0].metadata = metadataCustom

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.voci[0].metadata == metadataCustom
* match response.stato == 'NON_ESEGUITA'

Scenario Outline: <field> non valida

* set pendenzaPut.voci[0].metadata = metadataCustom
* set <fieldRequest> = <fieldValue>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| mapEntries | pendenzaPut.voci[0].metadata | {} | 'mapEntries' |
| mapEntries | pendenzaPut.voci[0].metadata.mapEntries | null | 'mapEntries' |
| mapEntries | pendenzaPut.voci[0].metadata.mapEntries | [] | 'mapEntries' |
| key | pendenzaPut.voci[0].metadata.mapEntries[0].key | null | 'key' |
| key | pendenzaPut.voci[0].metadata.mapEntries[0].key | '' | 'key' |
| key | pendenzaPut.voci[0].metadata.mapEntries[0].key | loremIpsum | 'key' |
| value | pendenzaPut.voci[0].metadata.mapEntries[0].value | null | 'value' |
| value | pendenzaPut.voci[0].metadata.mapEntries[0].value | '' | 'value' |
| value | pendenzaPut.voci[0].metadata.mapEntries[0].value | loremIpsum | 'value' |


Scenario: Pagamento di una pendenza con metadata definiti

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def versionePagamento = 3

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.voci[0].metadata = metadataCustom

* call read('classpath:utils/v2/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
#* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
#* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')

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
	"ccp":"#(''+ccp)",
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
And match response.risultati[0].dettaglioEsito == "Acquisita ricevuta di pagamento [IUV: "+ iuv +" CCP:"+ ccp +"] emessa da Banco di Ponzi S.p.A."
And match response.risultati[0].datiPagoPA == 
"""
{
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# Nodo Invia RPT tramite paGetPaymentV2

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
	"idPsp": "##null",
	"idIntermediarioPsp": "##null",
	"idCanale": "##null",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# Verifica RPT tramite paVerifyPaymentNotice

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
	"idPsp": "##null",
	"idIntermediarioPsp": "##null",
	"idCanale": "##null",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

