Feature: Riconciliazione pagamento singolo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario: Riconciliazione singola IUV non ISO

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "false"
* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789'}
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(10000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-singola-response.json')

Scenario: Idempotenza riconciliazione singola IUV non ISO

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "false"
* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(10000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-singola-response.json')

* def response1 = response

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method put
Then status 200
And match response == response1

Scenario: Riconciliazione singola IUV ISO

* def tipoRicevuta = "R01"
* def cumulativo = "0"
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(10000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-singola-response.json')

Scenario: Idempotenza riconciliazione singola IUV ISO

* def tipoRicevuta = "R01"
* def cumulativo = "0"
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(10000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-singola-response.json')

* def response1 = response

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method put
Then status 200
And match response == response1

Scenario: Idempotenza riconciliazione singola IUV ISO

* def tipoRicevuta = "R01"
* def cumulativo = "0"
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def idRiconciliazione = getCurrentTimeMillis()
* def iuv = estraiIuvDallaCausale(causale)

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { iuv: '#(iuv)', importo: '#(importo)', sct : 'SCT0123456789' }
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(10000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-singola-iuv-response.json')

* def response1 = response

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { iuv: '#(iuv)', importo: '#(importo)', sct : 'SCT0123456789' }
When method put
Then status 200
And match response == response1


