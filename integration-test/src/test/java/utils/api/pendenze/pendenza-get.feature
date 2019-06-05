Feature: Lettura pendenza 

Background: 

* def pendenzaGet = read('classpath:test/api/pendenza/pendenze/v1/get/msg/pendenza-get-dettaglio.json')

Scenario: 

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

