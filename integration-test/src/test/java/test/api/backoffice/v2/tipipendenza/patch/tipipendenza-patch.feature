Feature: Modifica parziale di un tipo pendenza (console-api v2)

# Non ha corrispettivo in v1.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idTipoPendenza = 'TIPO_TEST_V2'
* def creaTipo = call read('classpath:utils/api/v2/backoffice/crea-tipo-pendenza.feature') { idTipoPendenza: '#(idTipoPendenza)' }
* def jsonPatchHeader = { 'Content-Type': 'application/json-patch+json' }

Scenario: Modifica della sola descrizione

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def abilitatoPrima = response.abilitato

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And headers jsonPatchHeader
And header If-Match = etag
And request [ { op: 'replace', path: '/descrizione', value: 'Descrizione da patch' } ]
When method patch
Then status 200
And match response.descrizione == 'Descrizione da patch'
And match response.abilitato == abilitatoPrima

Scenario: Modifica parziale senza If-Match

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And headers jsonPatchHeader
And request [ { op: 'replace', path: '/descrizione', value: 'x' } ]
When method patch
Then status 428

Scenario: Modifica parziale con If-Match non corrispondente

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And headers jsonPatchHeader
And header If-Match = '"non-corrispondente"'
And request [ { op: 'replace', path: '/descrizione', value: 'x' } ]
When method patch
Then status 412
