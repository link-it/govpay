Feature: Riconciliazione pagamento cumulativo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
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

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' }
When method post
Then status 201
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

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' }
When method post
Then status 201
And match response == read('msg/riconciliazione-cumulativa-response.json')

* def response1 = response

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' }
When method post
Then status 200
And match response == response1

