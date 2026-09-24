Feature: Dettaglio di un intermediario (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/intermediari/get/intermediari-get.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idIntermediario = '11111111113'

Scenario: Lettura di un intermediario censito

Given url consoleBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idIntermediario == idIntermediario
And match response.denominazione == '#notnull'
And match response.abilitato == '#boolean'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: Lettura di un intermediario non esistente

Given url consoleBaseurl
And path 'intermediari', '99999999990'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'intermediari', idIntermediario
When method get
Then status 401
