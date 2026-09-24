Feature: Modifica parziale di un operatore (console-api v2)

# Non ha corrispettivo in v1: la patch della v1 agiva sulla password, che in v2
# e' una sotto-risorsa a se' ed e' coperta da put/operatori-password.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def principal = 'OPERATORE_TEST_V2'
* def creaOperatore = call read('classpath:utils/api/v2/backoffice/crea-operatore.feature') { principal: '#(principal)' }
* def jsonPatchHeader = { 'Content-Type': 'application/json-patch+json' }

Scenario: Modifica del solo nome

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def abilitatoPrima = response.abilitato

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And headers jsonPatchHeader
And header If-Match = etag
And request [ { op: 'replace', path: '/nome', value: 'Nome da patch' } ]
When method patch
Then status 200
And match response.nome == 'Nome da patch'
And match response.abilitato == abilitatoPrima

Scenario: Modifica parziale senza If-Match

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And headers jsonPatchHeader
And request [ { op: 'replace', path: '/nome', value: 'x' } ]
When method patch
Then status 428

Scenario: Modifica parziale con If-Match non corrispondente

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And headers jsonPatchHeader
And header If-Match = '"non-corrispondente"'
And request [ { op: 'replace', path: '/nome', value: 'x' } ]
When method patch
Then status 412
