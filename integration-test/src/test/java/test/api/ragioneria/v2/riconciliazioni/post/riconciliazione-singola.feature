Feature: Riconciliazione pagamento singolo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
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

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789'}
When method post
Then status 201
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

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-singola-response.json')

* def response1 = response

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method post
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

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method post
Then status 201
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

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-singola-response.json')

* def response1 = response

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 200
And match response == response1

