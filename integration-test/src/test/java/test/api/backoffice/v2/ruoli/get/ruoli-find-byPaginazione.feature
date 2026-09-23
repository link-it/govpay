Feature: Ricerca ruoli con i parametri di paginazione (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/ruoli/get/ruoli-find-byMetadatiPaginazione.feature.
#
# L'involucro delle risposte paginate e' diverso e non traducibile campo per
# campo: la v1 ha numRisultati, numPagine, risultatiPerPagina, pagina,
# maxRisultati e risultati; la v2 ha results e pagination, con page, limit e
# hasNextPage, piu' totalResults e totalPages solo se si chiede total=true.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Ricerca ruoli senza parametri

Given url consoleBaseurl
And path 'ruoli'
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

Scenario: Ricerca ruoli con total true

Given url consoleBaseurl
And path 'ruoli'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'
And match response.pagination.totalPages == '#number'

Scenario: Ricerca ruoli con total false

Given url consoleBaseurl
And path 'ruoli'
And param total = false
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#notpresent'
And match response.pagination.totalPages == '#notpresent'

Scenario: Ricerca ruoli con limit

Given url consoleBaseurl
And path 'ruoli'
And param limit = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ <= 2]'
And match response.pagination.limit == 2

Scenario: Ricerca ruoli con page

Given url consoleBaseurl
And path 'ruoli'
And param limit = 1
And param page = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.page == 2

Scenario: Ricerca ruoli ordinata

# Non si confronta l'ordine con un ordinamento calcolato qui: il server ordina
# con la collazione del database, che non coincide con quella di JavaScript, e
# un confronto del genere fallirebbe per un motivo che non e' il comportamento
# sotto prova. Si verifica che i due versi diano estremi diversi, che e' cio'
# che distingue un ordinamento applicato da uno ignorato.

Given url consoleBaseurl
And path 'ruoli'
And param sort = 'idRuolo'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 1]'
* def primoAscendente = response.results[0].idRuolo

Given url consoleBaseurl
And path 'ruoli'
And param sort = '-idRuolo'
And headers basicAutenticationHeader
When method get
Then status 200
* def primoDiscendente = response.results[0].idRuolo

And match primoAscendente != primoDiscendente

Scenario: Ricerca ruoli con filtro su idRuolo

Given url consoleBaseurl
And path 'ruoli'
And param idRuolo = 'Amministratore'
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].idRuolo contains 'Amministratore'

Scenario: Ricerca ruoli senza autenticazione

Given url consoleBaseurl
And path 'ruoli'
When method get
Then status 401
