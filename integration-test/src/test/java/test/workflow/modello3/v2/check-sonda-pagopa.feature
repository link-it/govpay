Feature: Verifica chiamata di check attivita di pagoPA

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* configure followRedirects = false

* def versionePagamento = 2

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')

* def faultBean = 
"""
	{
		"faultCode":"PAA_PAGAMENTO_SCONOSCIUTO",
		"faultString":"Pagamento in attesa risulta sconosciuto all’Ente Creditore.",
		"id":"#(idDominio)"
	}
"""

Scenario: Verifica sonda pagoPA

* def numeroAvviso = '000000000000000000'
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = 0

# Configurazione dell'applicazione

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == faultBean
