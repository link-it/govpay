Feature: Riconciliazione pagamento cumulativo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario: Riconciliazione cumulativa

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

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
* call sleep(5000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-cumulativa-response.json')

Scenario: Idempotenza riconciliazione cumulativa

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

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
* call sleep(5000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-cumulativa-response.json')

* def response1 = response

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method put
Then status 200
And match response == response1


Scenario: Riconciliazione cumulativa con IDFlusso

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

* def idRiconciliazione = getCurrentTimeMillis()
* def idFlussoRendicontazione = estraiIdFlussoDallaCausale(causale)

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { idFlussoRendicontazione: '#(idFlussoRendicontazione)', importo: '#(importo)' , sct : 'SCT0123456789'}
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(5000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-cumulativa-idflusso-response.json')

