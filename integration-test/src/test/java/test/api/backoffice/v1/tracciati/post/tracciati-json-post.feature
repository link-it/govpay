Feature: Caricamento tracciato JSON

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* configure retry = { count: 5, interval: 10000 }

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

* def idPendenza = getCurrentTimeMillis()
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

* def idPendenza = getCurrentTimeMillis()
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

* def idPendenza = getCurrentTimeMillis()
* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-erroresintassi.json')

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 400


Scenario: Tracciato con proprieta' non previste nel campo pendenza

* def idPendenza = getCurrentTimeMillis()
* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-prop-pendenza-non-valide.json')

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 400


Scenario: Tracciato con proprieta' non previste nel campo voce pendenza

* def idPendenza = getCurrentTimeMillis()
* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-prop-vocependenza-non-valide.json')

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 400

Scenario: Annullamento tracciato  

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
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 3
Then match response.numeroOperazioniEseguite == 3
Then match response.numeroOperazioniFallite == 0

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

* def tracciatoDel = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-annullamenti.json')

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And request tracciatoDel
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 3
Then match response.numeroOperazioniEseguite == 3
Then match response.numeroOperazioniFallite == 0

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

# verifico stato pendenze

Given url backofficeBaseurl
And path '/pendenze', idA2A, (idPendenza + '-0')
And headers basicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ANNULLATA'

Given url backofficeBaseurl
And path '/pendenze', idA2A, (idPendenza + '-1')
And headers basicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ANNULLATA'

Given url backofficeBaseurl
And path '/pendenze', idA2A, (idPendenza + '-2')
And headers basicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ANNULLATA'





