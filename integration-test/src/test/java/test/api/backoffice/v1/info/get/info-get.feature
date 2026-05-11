Feature: Lettura delle informazioni di sistema

Background:

* callonce read('classpath:utils/common-utils.feature')


Scenario: Lettura info senza autenticazione (endpoint permitAll)

* def backofficePublicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficePublicBaseurl
And path '/info'
When method get
Then status 200
And match response ==
"""
{
	appName: '#string',
	versione: '#string',
	build: '#string',
	ambiente: '##string'
}
"""


Scenario: Lettura info con autenticazione basic

* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBasicBaseurl
And path '/info'
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	appName: '#string',
	versione: '#string',
	build: '#string',
	ambiente: '##string'
}
"""
