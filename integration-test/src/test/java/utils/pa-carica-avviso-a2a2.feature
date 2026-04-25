Feature: Caricamento pagamento dovuto

Background: 

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A2, password: pwdA2A2 } )

Scenario: 

Given url pendenzeBaseurl
And path 'pendenze', idA2A2, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
