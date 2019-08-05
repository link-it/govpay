Feature: Caricamento tracciato JSON

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* configure retry = { count: 10, interval: 1000 }

Scenario: Pagamento pendenza precaricata anonimo

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get