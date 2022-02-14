Feature: Riconciliazione non autorizzata

Background:

* callonce read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

Scenario: Riconciliazione singola da applicazione non autorizzata per il dominio


* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "false"
* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def applicazione = read('msg/applicazione_nonAuthDominio.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method put
Then status 403
And match response == read('msg/errore_auth.json')


Scenario: Riconciliazione cumulativa da applicazione non autorizzata per il dominio

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

* def applicazione = read('msg/applicazione_nonAuthDominio.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789' }
When method put
Then status 403
And match response == read('msg/errore_auth.json')
