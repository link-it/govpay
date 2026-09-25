Feature: Dettaglio di un dominio (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/domini/get/dominio-get.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '12345678901'

Scenario: Lettura di un dominio censito

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idDominio == idDominio
And match response.ragioneSociale == '#notnull'
And match response.abilitato == '#boolean'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: Lettura di un dominio non esistente

Given url consoleBaseurl
And path 'domini', '99999999999'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura del logo

Given url consoleBaseurl
And path 'domini', idDominio, 'logo'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'domini', idDominio
When method get
Then status 401
