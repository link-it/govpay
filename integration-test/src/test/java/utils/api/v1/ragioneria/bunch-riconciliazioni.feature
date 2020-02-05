Feature: Riconciliazione pagamento singolo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})

Scenario: Riconciliazione singola IUV non ISO


* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)


## idRiconciliazioneSin_DOM1_A2A

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "false"
* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url ragioneriaBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' }
When method post
Then status 201
And match response == read('msg/riconciliazione-singola-response.json')

* def idRiconciliazioneSin_DOM1_A2A = response.idIncasso
* def riconciliazioneSin_DOM1_A2A = response

## idRiconciliazioneSin_DOM1_A2A2

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "false"
* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url ragioneriaBaseurl
And path '/incassi', idDominio
And headers idA2A2BasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' }
When method post
Then status 201

* def idRiconciliazioneSin_DOM1_A2A2 = response.idIncasso
* def riconciliazioneSin_DOM1_A2A2 = response

## idRiconciliazioneCum_DOM1_A2A

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url ragioneriaBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' }
When method post
Then status 201

* def idRiconciliazioneCum_DOM1_A2A = response.idIncasso
* def riconciliazioneCum_DOM1_A2A = response

## idRiconciliazioneCum_DOM1_A2A2

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url ragioneriaBaseurl
And path '/incassi', idDominio
And headers idA2A2BasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' }
When method post
Then status 201

* def idRiconciliazioneCum_DOM1_A2A2 = response.idIncasso
* def riconciliazioneSin_DOM1_A2A2 = response

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

