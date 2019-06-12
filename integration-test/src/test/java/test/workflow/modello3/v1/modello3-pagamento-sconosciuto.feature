Feature: Verifica o attivazione RPT con iuv sconosciuto

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* def esitoVerificaRPT = read('msg/verifica-response-ok.json')
* configure followRedirects = false


Scenario: Verifica pagamento sconosciuto

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == 
"""
	{
		"faultCode":"PAA_PAGAMENTO_SCONOSCIUTO",
		"faultString":"Pagamento in attesa risulta sconosciuto all’Ente Creditore.",
		"id":"#(idDominio)",
		"description": #notnull,
		"serial":null
	}
"""

Scenario: Attiva pagamento sconosciuto

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == 
"""
	{
		"faultCode":"PAA_PAGAMENTO_SCONOSCIUTO",
		"faultString":"Pagamento in attesa risulta sconosciuto all’Ente Creditore.",
		"id":"#(idDominio)",
		"description": #notnull,
		"serial":null
	}
"""