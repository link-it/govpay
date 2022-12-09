Feature: Pagamento di una pendenza con MBT con le varie versioni delle API SANP

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def esitoPaGetPaymentV2 = read('classpath:test/workflow/modello3/v2/msg/getPaymentV2-response-ok.json')

* def faultBean = 
"""
	{
		"faultCode":"PAA_SEMANTICA",
		"faultString":"Errore semantico.",
		"id":"#(idDominio)",
		"description":'#("Il versamento contiene una marca da bollo telematica, non ammessa per pagamenti ad iniziativa psp.")',
		"serial":'##null'
	}
"""

* def faultBean2 = 
"""
	{
		"faultCode":"PAA_SEMANTICA",
		"faultString":"Errore semantico.",
		"id":"#(idDominio)",
		"description":'#("Il versamento contiene una marca da bollo telematica, non ammessa per pagamenti ad iniziativa psp.")',
		"serial":'##null',
		"originalFaultCode":'##null',
		"originalFaultString":'##null',
		"originalDescription":'##null'
	}
"""

@test1
Scenario: Attivazione di una RPT per una pendenza con MBT Sanp 2.3.0

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean

@test2
Scenario: Attivazione di una RPT per una pendenza con MBT Sanp 2.4.0

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def versionePagamento = 2
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.faultBean == faultBean2


@test3
Scenario: Attivazione di una RPT per una pendenza con MBT Sanp 3.2.1

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def versionePagamento = 3
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoPaGetPaymentV2


