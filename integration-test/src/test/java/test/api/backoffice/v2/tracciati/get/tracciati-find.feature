Feature: Ricerca dei tracciati di pendenze (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/tracciati/get/
# tracciati-find-byMetadatiPaginazione.feature e tracciati-find-byStato.feature.
#
# Il percorso cambia: in v1 era /tracciati, in v2 e' /pendenze/tracciati, cioe'
# una sotto-risorsa delle pendenze.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Elenco paginato

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
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

Scenario: Elenco con total

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'

Scenario: Elenco con limit

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
And param limit = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ <= 2]'
And match response.pagination.limit == 2

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
When method get
Then status 401
