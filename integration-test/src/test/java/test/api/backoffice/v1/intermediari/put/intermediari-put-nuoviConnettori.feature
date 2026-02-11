Feature: Test nuovi connettori PagoPA per intermediari (ACA, GPD, FR, BackofficeEC)

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})


Scenario: Configurazione intermediario con servizio ACA

* def intermediarioACA = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-ACA.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioACA
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioACA.idIntermediario = idIntermediario
* set intermediarioACA.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioACA
And match response.servizioPagoPaACA.url == '#present'
And match response.servizioPagoPaACA.subscriptionKey == 'ACA123456'


Scenario: Configurazione intermediario con servizio GPD

* def intermediarioGPD = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-GPD.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioGPD
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioGPD.idIntermediario = idIntermediario
* set intermediarioGPD.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioGPD
And match response.servizioPagoPaGPD.url == '#present'
And match response.servizioPagoPaGPD.subscriptionKey == 'GPD123456'


Scenario: Configurazione intermediario con servizio FR

* def intermediarioFR = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-FR.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioFR
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioFR.idIntermediario = idIntermediario
* set intermediarioFR.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioFR
And match response.servizioPagoPaFR.url == '#present'
And match response.servizioPagoPaFR.subscriptionKey == 'FR123456'


Scenario: Configurazione intermediario con servizio BackofficeEC

* def intermediarioBackofficeEC = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-BackofficeEC.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBackofficeEC
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioBackofficeEC.idIntermediario = idIntermediario
* set intermediarioBackofficeEC.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioBackofficeEC
And match response.servizioPagoPaBackofficeEC.url == '#present'
And match response.servizioPagoPaBackofficeEC.subscriptionKey == 'BOEC123456'


Scenario: Configurazione intermediario con tutti i connettori PagoPA

* def intermediarioCompleto = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-completo.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioCompleto
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioCompleto.idIntermediario = idIntermediario
* set intermediarioCompleto.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioCompleto
And match response.servizioPagoPa.urlRPT == '#present'
And match response.servizioPagoPaRecuperoRT.url == '#present'
And match response.servizioPagoPaRecuperoRT.subscriptionKey == 'RT123456'
And match response.servizioPagoPaACA.url == '#present'
And match response.servizioPagoPaACA.subscriptionKey == 'ACA123456'
And match response.servizioPagoPaGPD.url == '#present'
And match response.servizioPagoPaGPD.subscriptionKey == 'GPD123456'
And match response.servizioPagoPaFR.url == '#present'
And match response.servizioPagoPaFR.subscriptionKey == 'FR123456'
And match response.servizioPagoPaBackofficeEC.url == '#present'
And match response.servizioPagoPaBackofficeEC.subscriptionKey == 'BOEC123456'


Scenario: Aggiornamento intermediario esistente aggiungendo connettore ACA

* def intermediarioBase = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBase
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioBase.servizioPagoPaACA = { "url": '#(ndpsym_url + "/pagopa/api/v1/notices")', "subscriptionKey": "ACA123456", "abilitaGDE": false }

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBase
When method put
Then status 200

* set intermediarioBase.idIntermediario = idIntermediario
* set intermediarioBase.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioBase
And match response.servizioPagoPaACA.url == '#present'


Scenario: Rimozione connettore ACA da intermediario

* def intermediarioACA = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-ACA.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioACA
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def intermediarioBase = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBase
When method put
Then status 200

* set intermediarioBase.idIntermediario = idIntermediario
* set intermediarioBase.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioBase
And match response.servizioPagoPaACA == '#notpresent'


Scenario: Aggiornamento intermediario esistente aggiungendo connettore BackofficeEC

* def intermediarioBase = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBase
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioBase.servizioPagoPaBackofficeEC = { "url": '#(ndpsym_url + "/pagopa/backoffice/api/v1/ec")', "subscriptionKey": "BOEC123456", "abilitaGDE": false }

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBase
When method put
Then status 200

* set intermediarioBase.idIntermediario = idIntermediario
* set intermediarioBase.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioBase
And match response.servizioPagoPaBackofficeEC.url == '#present'


Scenario: Rimozione connettore BackofficeEC da intermediario

* def intermediarioBackofficeEC = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-BackofficeEC.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBackofficeEC
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def intermediarioBase = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBase
When method put
Then status 200

* set intermediarioBase.idIntermediario = idIntermediario
* set intermediarioBase.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioBase
And match response.servizioPagoPaBackofficeEC == '#notpresent'


Scenario: Configurazione intermediario con servizio BackofficeEC e abilitaGDE

* def intermediarioBackofficeECGDE = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-BackofficeEC-abilitaGDE.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBackofficeECGDE
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioBackofficeECGDE.idIntermediario = idIntermediario
* set intermediarioBackofficeECGDE.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioBackofficeECGDE
And match response.servizioPagoPaBackofficeEC.url == '#present'
And match response.servizioPagoPaBackofficeEC.subscriptionKey == 'BOEC123456'
And match response.servizioPagoPaBackofficeEC.abilitaGDE == true
