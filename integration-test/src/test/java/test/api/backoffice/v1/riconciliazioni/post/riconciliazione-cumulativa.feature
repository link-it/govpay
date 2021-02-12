Feature: Riconciliazione pagamento cumulativo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Scenario: Riconciliazione cumulativa da applicazione

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

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-cumulativa-list-response.json')

Scenario: Idempotenza riconciliazione cumulativa da applicazione

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

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-cumulativa-list-response.json')

* def response1 = response

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 200
And match response == response1

Scenario: Riconciliazione cumulativa da operatore

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

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers basicAutenticationHeader
And request read('msg/operatore_auth.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers spidHeadersRossi
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-cumulativa-list-response.json')

Scenario: Idempotenza riconciliazione cumulativa da operatore

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

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers basicAutenticationHeader
And request read('msg/operatore_auth.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers spidHeadersRossi
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-cumulativa-list-response.json')

* def response1 = response

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers spidHeadersRossi
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 200
And match response == response1
