Feature: Ricerca delle riconciliazioni (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/riconciliazioni/get/
# riconciliazione-find.feature, riconciliazioni-find-byIdFlusso,
# -byIuv, -bySct, -byStato, -byMetadatiPaginazione e -find-sintassi.
#
# In v1 le riconciliazioni passavano dal percorso /incassi; in v2 la risorsa si
# chiama come il concetto, /riconciliazioni, e il dominio non e' piu' un
# parametro obbligatorio della ricerca.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Elenco paginato

Given url consoleBaseurl
And path 'riconciliazioni'
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
And path 'riconciliazioni'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'

Scenario: Il dominio e' un riferimento con l'etichetta

# Come per operatori, in lettura il server arricchisce il riferimento: il
# dominio porta anche la ragione sociale.

Given url consoleBaseurl
And path 'riconciliazioni'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[1]'
And match response.results[0].dominio.idDominio == '#notnull'
And match response.results[0].dominio.ragioneSociale == '#notnull'

Scenario: Filtro per dominio

Given url consoleBaseurl
And path 'riconciliazioni'
And param idDominio = '12345678901'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].dominio.idDominio == '12345678901'

Scenario: Filtro per stato

Given url consoleBaseurl
And path 'riconciliazioni'
And param stato = 'ACQUISITA'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].stato == 'ACQUISITA'

Scenario: Filtro con uno stato fuori dall'enumerazione

Given url consoleBaseurl
And path 'riconciliazioni'
And param stato = 'STATO_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Filtro per idFlusso

Given url consoleBaseurl
And path 'riconciliazioni'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def riconciliazione = response.results[0]

Given url consoleBaseurl
And path 'riconciliazioni'
And param idFlusso = riconciliazione.idFlusso
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].idFlusso == riconciliazione.idFlusso

Scenario: Filtro che non seleziona nulla

Given url consoleBaseurl
And path 'riconciliazioni'
And param idDominio = '99999999999'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'riconciliazioni'
When method get
Then status 401
