Feature: Ricerca dei flussi di rendicontazione (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/flussiRendicontazione/get/
# flussiRendicontazione-find.feature, -find-byIdFlusso, -find-byIuv,
# -find-byMetadatiPaginazione e -find-sintassi.
#
# Il percorso cambia nome, da flussiRendicontazione a flussi-rendicontazione, e
# l'involucro paginato e' quello della v2. I filtri sono gli stessi piu'
# incassato e lo stato del flusso.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Elenco paginato

Given url consoleBaseurl
And path 'flussi-rendicontazione'
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
And path 'flussi-rendicontazione'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'
And match response.pagination.totalPages == '#number'

Scenario: Filtro per dominio

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param idDominio = '12345678901'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].idDominio == '12345678901'

Scenario: Filtro per idFlusso

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[1]'
* def flusso = response.results[0]

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param idFlusso = flusso.idFlusso
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].idFlusso == flusso.idFlusso

Scenario: Filtro per psp

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def flusso = response.results[0]

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param idPsp = flusso.idPsp
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].idPsp == flusso.idPsp

Scenario: Filtro per stato

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param stato = 'ACQUISITO'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].stato == 'ACQUISITO'

Scenario: Filtro con uno stato fuori dall'enumerazione

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param stato = 'STATO_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Filtro che non seleziona nulla

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param idDominio = '99999999999'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'flussi-rendicontazione'
When method get
Then status 401
