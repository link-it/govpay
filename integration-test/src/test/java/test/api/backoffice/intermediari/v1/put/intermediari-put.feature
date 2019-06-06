Feature: Validazione sintattica intermediari

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediario.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediarioBasicAuth.json')
* def intermediarioServerAuth = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediarioServerAuth.json')
* def intermediarioClientAuth = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediarioClientAuth.json')

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
And request intermediario
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
And request intermediarioBasicAuth
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
And request intermediarioBasicAuth
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
And request intermediarioBasicAuth
When method get
Then status 200
And match response == intermediarioClientAuth
