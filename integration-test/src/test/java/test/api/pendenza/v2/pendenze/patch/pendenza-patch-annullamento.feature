Feature: Annullamento pendenza

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

Scenario: Annullamento pendenza non eseguita

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request [ { "op": "REPLACE", "path": "/stato", "value": "ANNULLATA" }, { "op": "REPLACE", "path": "/descrizioneStato", "value": "Test annullamento" }]
When method patch
Then status 200

* set pendenzaGet.stato = 'ANNULLATA'
* set pendenzaGet.descrizioneStato = 'Test annullamento'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

