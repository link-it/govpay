Feature: Caricamento tracciato JSON

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* configure retry = { count: 5, interval: 5000 }

Scenario: Tracciato corretto 

* def idPendenza = getCurrentTimeMillis()
* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.json')

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

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200


Scenario: Tracciato senza operazioni

* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-vuoto.json')

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


Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Tracciato errore semantico

* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-erroresemantica.json')

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
And retry until response.stato == 'ESEGUITO_CON_ERRORI'
When method get


Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Tracciato errore sintassi

* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-erroresintassi.json')

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 400
