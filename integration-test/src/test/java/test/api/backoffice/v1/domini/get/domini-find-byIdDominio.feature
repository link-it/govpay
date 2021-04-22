Feature: Filtri sulla lista Domini

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario: Filtro per idDominio

Given url backofficeBasicBaseurl
And path 'domini'
And headers basicAutenticationHeader
And param idDominio = '1234567890'
When method get
Then status 200
And match response.numRisultati == 2

Given url backofficeBasicBaseurl
And path 'domini'
And headers basicAutenticationHeader
And param idDominio = '901'
When method get
Then status 200
And match response.numRisultati == 1

Given url backofficeBasicBaseurl
And path 'domini'
And headers basicAutenticationHeader
And param idDominio = '902'
When method get
Then status 200
And match response.numRisultati == 1

Given url backofficeBasicBaseurl
And path 'domini'
And headers basicAutenticationHeader
And param idDominio = 'XXX'
When method get
Then status 200
And match response.numRisultati == 0


Scenario: Filtro per ragioneSociale

Given url backofficeBasicBaseurl
And path 'domini'
And headers basicAutenticationHeader
And param ragioneSociale = 'Ente Creditore Test'
When method get
Then status 200
And assert response.numRisultati >= 2

Given url backofficeBasicBaseurl
And path 'domini'
And headers basicAutenticationHeader
And param ragioneSociale = 'Ente Creditore Test 2'
When method get
Then status 200
And match response.numRisultati == 1

Given url backofficeBasicBaseurl
And path 'domini'
And headers basicAutenticationHeader
And param ragioneSociale = 'XXX'
When method get
Then status 200
And match response.numRisultati == 0
