Feature: Lettura con codice avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Lettura di una pendenza precaricata da numero avviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And param stampaAvviso = 'true'
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/avvisi', response.idDominio, response.numeroAvviso
And headers basicAutenticationHeader
And header Accept = 'application/pdf'
When method get
Then status 200
And match responseHeaders contains { 'Content-Type': ['application/pdf'] }

