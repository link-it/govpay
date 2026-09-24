Feature: Ricerca entrate con i parametri di paginazione (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/entrate/get/entrate-find-byMetadatiPaginazione.feature.
# Vale la stessa nota dei ruoli: l'involucro paginato della v2 non e'
# traducibile campo per campo da quello della v1.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Ricerca entrate senza parametri

Given url consoleBaseurl
And path 'entrate'
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

Scenario: Ricerca entrate con total true

Given url consoleBaseurl
And path 'entrate'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'
And match response.pagination.totalPages == '#number'

Scenario: Ricerca entrate con total false

Given url consoleBaseurl
And path 'entrate'
And param total = false
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#notpresent'
And match response.pagination.totalPages == '#notpresent'

Scenario: Ricerca entrate con limit

Given url consoleBaseurl
And path 'entrate'
And param limit = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ <= 2]'
And match response.pagination.limit == 2

Scenario: Ricerca entrate con page

Given url consoleBaseurl
And path 'entrate'
And param limit = 1
And param page = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.page == 2

Scenario: Ricerca entrate ordinata

Given url consoleBaseurl
And path 'entrate'
And param sort = 'idEntrata'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 1]'
* def primoAscendente = response.results[0].idEntrata

Given url consoleBaseurl
And path 'entrate'
And param sort = '-idEntrata'
And headers basicAutenticationHeader
When method get
Then status 200
* def primoDiscendente = response.results[0].idEntrata

And match primoAscendente != primoDiscendente

Scenario: Ricerca entrate senza autenticazione

Given url consoleBaseurl
And path 'entrate'
When method get
Then status 401
