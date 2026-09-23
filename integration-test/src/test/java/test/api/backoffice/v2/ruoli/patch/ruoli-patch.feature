Feature: Modifica parziale di un ruolo (console-api v2)

# Non ha corrispettivo in v1: la PATCH e' una capacita' nuova della v2, con
# JSON Patch (RFC 6902) e If-Match. Il server accetta puntatori ai soli campi di
# primo livello: /acl si', /acl/0/autorizzazioni no. E' l'ultimo scenario.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idRuolo = 'RuoloTestV2'
* def creaRuolo = call read('classpath:utils/api/v2/backoffice/crea-ruolo.feature') { idRuolo: '#(idRuolo)' }
* def jsonPatchHeader = { 'Content-Type': 'application/json-patch+json' }

Scenario: Sostituzione delle acl con JSON Patch

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And headers jsonPatchHeader
And header If-Match = etag
And request [ { op: 'replace', path: '/acl', value: [ { servizio: 'Pendenze', autorizzazioni: [ 'R' ] } ] } ]
When method patch
Then status 200
And match response.acl[0].servizio == 'Pendenze'
And match response.acl[0].autorizzazioni contains only [ 'R' ]

Scenario: JSON Patch senza If-Match

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And headers jsonPatchHeader
And request [ { op: 'replace', path: '/acl', value: [ { servizio: 'Pendenze', autorizzazioni: [ 'R' ] } ] } ]
When method patch
Then status 428

Scenario: JSON Patch con If-Match non corrispondente

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And headers jsonPatchHeader
And header If-Match = '"non-corrispondente"'
And request [ { op: 'replace', path: '/acl', value: [ { servizio: 'Pendenze', autorizzazioni: [ 'R' ] } ] } ]
When method patch
Then status 412

Scenario: JSON Patch su un puntatore non di primo livello

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And headers jsonPatchHeader
And header If-Match = etag
And request [ { op: 'replace', path: '/acl/0/autorizzazioni', value: [ 'R', 'W' ] } ]
When method patch
Then status 400
And match response.detail contains 'JSON Pointer'
