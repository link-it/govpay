Feature: Validazione sintattica intermediari

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioBasicAuth.json')
* def intermediarioServerAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioServerAuth.json')
* def intermediarioClientAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioClientAuth.json')
* def intermediarioHeaderAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioHeaderAuth.json')
* def intermediarioApiKeyAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioApiKeyAuth.json')
* def intermediarioOauth2ClientCredentialsAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioOauth2ClientCredentialsAuth.json')

Scenario: Configurazione intermediario senza autenticazione verso pagoPA

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediario.idIntermediario = idIntermediario
* set intermediario.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediario

Scenario: Configurazione intermediario con autenticazione basic da pagoPA

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBasicAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioBasicAuth.idIntermediario = idIntermediario
* set intermediarioBasicAuth.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioBasicAuth

Scenario: Configurazione intermediario con autenticazione server verso pagoPA

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioServerAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioServerAuth.idIntermediario = idIntermediario
* set intermediarioServerAuth.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioServerAuth

Scenario: Configurazione intermediario con autenticazione client verso pagoPA

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioClientAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioClientAuth.idIntermediario = idIntermediario
* set intermediarioClientAuth.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioClientAuth

@test1
Scenario: Configurazione subscriptionKey intermediario senza autenticazione verso pagoPA

* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')
* set intermediario.servizioPagoPa.subscriptionKey = '8daebdf9-558c-4203-aade-0dee45bfc08d'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediario.idIntermediario = idIntermediario
* set intermediario.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediario

Scenario: Configurazione intermediario con autenticazione header verso pagoPA

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioHeaderAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioHeaderAuth.idIntermediario = idIntermediario
* set intermediarioHeaderAuth.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioHeaderAuth

Scenario: Configurazione intermediario con autenticazione apiKey verso pagoPA

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioApiKeyAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioApiKeyAuth.idIntermediario = idIntermediario
* set intermediarioApiKeyAuth.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioApiKeyAuth

Scenario: Configurazione intermediario con autenticazione oauth2 client credentials verso pagoPA

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioOauth2ClientCredentialsAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set intermediarioOauth2ClientCredentialsAuth.idIntermediario = idIntermediario
* set intermediarioOauth2ClientCredentialsAuth.stazioni = '#ignore'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response == intermediarioOauth2ClientCredentialsAuth


