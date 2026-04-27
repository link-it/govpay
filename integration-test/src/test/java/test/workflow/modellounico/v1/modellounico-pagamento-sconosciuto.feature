Feature: Verifica o attivazione RPT con iuv sconosciuto

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')

* def esitoVerificaRPT = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* configure followRedirects = false

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Scenario: Verifica pagamento sconosciuto

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == 
"""
	{
		"faultCode":"PAA_PAGAMENTO_SCONOSCIUTO",
		"faultString":"Pagamento in attesa risulta sconosciuto all’Ente Creditore.",
		"id":"#(idDominio)",
		"description": #notnull,
		"serial":'##null'
	}
"""

Scenario: Attiva pagamento sconosciuto

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.faultBean == 
"""
	{
		"faultCode":"PAA_PAGAMENTO_SCONOSCIUTO",
		"faultString":"Pagamento in attesa risulta sconosciuto all’Ente Creditore.",
		"id":"#(idDominio)",
		"description": #notnull,
		"serial":'##null'
	}
"""
