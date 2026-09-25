Feature: Ricerca delle pendenze (console-api v2)

# Corrispettivo v2 delle tredici feature di
# test/api/backoffice/v1/pendenze/get/: pendenze-find-byData, -byIdTipoPendenza,
# -byIUV, -byMetadatiPaginazione, -byStato, -find-applicazioni, -auth-uo e le
# altre.
#
# Le pendenze in v2 sono una risorsa di SOLA LETTURA: console-api non le crea e
# non le modifica. Le venticinque feature di v1/pendenze/put/, le tre di post/ e
# quella di patch/ non hanno quindi un corrispettivo qui: in v2 le pendenze si
# scrivono dalle API di integrazione, non dal backoffice. E' la differenza piu'
# grande fra le due versioni, e riguarda ventinove delle quarantadue feature
# della v1.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Elenco paginato

Given url consoleBaseurl
And path 'pendenze'
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
And path 'pendenze'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'
And match response.pagination.totalPages == '#number'

Scenario: Dominio e tipo pendenza sono riferimenti con l'etichetta

Given url consoleBaseurl
And path 'pendenze'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[1]'
And match response.results[0].dominio.idDominio == '#notnull'
And match response.results[0].dominio.ragioneSociale == '#notnull'
And match response.results[0].tipoPendenza.idTipoPendenza == '#notnull'
And match response.results[0].tipoPendenza.descrizione == '#notnull'

Scenario: Filtro per dominio

Given url consoleBaseurl
And path 'pendenze'
And param idDominio = '12345678901'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].dominio.idDominio == '12345678901'

Scenario: Filtro per applicazione

Given url consoleBaseurl
And path 'pendenze'
And param idA2A = 'IDA2A01'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].idA2A == 'IDA2A01'

Scenario: Filtro per stato

Given url consoleBaseurl
And path 'pendenze'
And param stato = 'PAGATA'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].stato == 'PAGATA'

Scenario: Filtro con uno stato fuori dall'enumerazione

Given url consoleBaseurl
And path 'pendenze'
And param stato = 'STATO_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Filtro per tipo pendenza

Given url consoleBaseurl
And path 'pendenze'
And param idTipoPendenza = 'LIBERO'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].tipoPendenza.idTipoPendenza == 'LIBERO'

Scenario: Filtro per identificativo della pendenza

Given url consoleBaseurl
And path 'pendenze'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def pendenza = response.results[0]

Given url consoleBaseurl
And path 'pendenze'
And param idPendenza = pendenza.idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results[*].idPendenza contains pendenza.idPendenza

Scenario: Filtro che non seleziona nulla

Given url consoleBaseurl
And path 'pendenze'
And param idDominio = '99999999999'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'pendenze'
When method get
Then status 401
