Feature: Ricerca intermediari con i parametri di paginazione (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/intermediari/get/intermediari-find-byMetadatiPaginazione.feature.
# Vale la stessa nota dei ruoli: l'involucro paginato della v2 non e'
# traducibile campo per campo da quello della v1.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Ricerca intermediari senza parametri

Given url consoleBaseurl
And path 'intermediari'
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

Scenario: Ricerca intermediari con total true

Given url consoleBaseurl
And path 'intermediari'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'
And match response.pagination.totalPages == '#number'

Scenario: Ricerca intermediari con total false

Given url consoleBaseurl
And path 'intermediari'
And param total = false
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#notpresent'
And match response.pagination.totalPages == '#notpresent'

Scenario: Ricerca intermediari con limit

Given url consoleBaseurl
And path 'intermediari'
And param limit = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ <= 2]'
And match response.pagination.limit == 2

Scenario: Ricerca intermediari con page

Given url consoleBaseurl
And path 'intermediari'
And param limit = 1
And param page = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.page == 2

Scenario: Ricerca intermediari ordinata

# Il campo di ordinamento e' codIntermediario e non idIntermediario, che e' il
# nome con cui il campo compare nella rappresentazione: e' una incoerenza
# dell'API, non una scelta di questo test.

Given url consoleBaseurl
And path 'intermediari'
And param sort = 'codIntermediario'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 1]'
* def primoAscendente = response.results[0].idIntermediario

Given url consoleBaseurl
And path 'intermediari'
And param sort = '-codIntermediario'
And headers basicAutenticationHeader
When method get
Then status 200
* def primoDiscendente = response.results[0].idIntermediario

And match primoAscendente != primoDiscendente

Scenario: Ricerca intermediari senza autenticazione

Given url consoleBaseurl
And path 'intermediari'
When method get
Then status 401
