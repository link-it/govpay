Feature: Acquisizione del logo EC

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario:  Acquisizione del logo EC

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path '/domini', idDominio, 'logo'
And headers basicAutenticationHeader
When method get
Then status 200