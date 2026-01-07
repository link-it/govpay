Feature: Test campo abilitaGDE nei connettori pagoPA RecuperoRT

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario =
"""
{
	"denominazione": "Soggetto Intermediario",
	"principalPagoPa": '#(ndpsym_user)',
	"servizioPagoPa": {
		"urlRPT": '#(ndpsym_url + "/pagopa/PagamentiTelematiciRPTservice")'
	},
	"servizioPagoPaRecuperoRT": {
		"url": '#(ndpsym_url + "/pagopa/rs/bizEvents")',
		"subscriptionKey" : "ABC123",
		"abilitaGDE": false
	},
	"abilitato": true
}
"""

Scenario: Configurazione connettore RecuperoRT con abilitaGDE false

* set intermediario.servizioPagoPaRecuperoRT.abilitaGDE = false

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediario.idIntermediario = idIntermediario
* set intermediario.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediario
And match response.servizioPagoPaRecuperoRT.abilitaGDE == false

Scenario: Configurazione connettore RecuperoRT con abilitaGDE true

* set intermediario.servizioPagoPaRecuperoRT.abilitaGDE = true

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediario.idIntermediario = idIntermediario
* set intermediario.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediario
And match response.servizioPagoPaRecuperoRT.abilitaGDE == true

Scenario: Configurazione connettore RecuperoRT senza campo abilitaGDE (default false)

* def intermediarioSenzaGDE =
"""
{
	"denominazione": "Soggetto Intermediario",
	"principalPagoPa": '#(ndpsym_user)',
	"servizioPagoPa": {
		"urlRPT": '#(ndpsym_url + "/pagopa/PagamentiTelematiciRPTservice")'
	},
	"servizioPagoPaRecuperoRT": {
		"url": '#(ndpsym_url + "/pagopa/rs/bizEvents")',
		"subscriptionKey" : "ABC123"
	},
	"abilitato": true
}
"""

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioSenzaGDE
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioPagoPaRecuperoRT.abilitaGDE == false

Scenario: Modifica abilitaGDE da false a true

* set intermediario.servizioPagoPaRecuperoRT.abilitaGDE = false

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediario.servizioPagoPaRecuperoRT.abilitaGDE = true

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioPagoPaRecuperoRT.abilitaGDE == true
