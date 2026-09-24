Feature: Stazioni di un intermediario (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/intermediari/get/intermediari-stazioni-find.feature e
# intermediari-stazioni-get.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idIntermediario = '11111111113'
* def idStazione = '11111111113_01'

Scenario: Elenco paginato delle stazioni

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'stazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	results: '#[]',
	pagination: { page: '#number', limit: '#number', hasNextPage: '#boolean' }
}
"""
And match response.results[*].idStazione contains idStazione

Scenario: Elenco delle stazioni di un intermediario inesistente

Given url consoleBaseurl
And path 'intermediari', '99999999990', 'stazioni'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Dettaglio di una stazione

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idStazione == idStazione
And match response.abilitato == '#boolean'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: Dettaglio di una stazione inesistente

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'stazioni', 'STAZIONE_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'stazioni'
When method get
Then status 401
