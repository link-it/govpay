Feature: Caricamento pendenza multivoce

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario: Caricamento pendenza con entrata definita, riferita e marca da bollo

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

# TODO VALIDAZIONE RISPOSTA


@test1
Scenario: Caricamento pendenza multivoce definita

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

@test2
Scenario: Caricamento pendenza multivoce definita con numero avviso

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
Then match response.numeroAvviso == numeroAvviso


