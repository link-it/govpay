Feature: Dettaglio di una applicazione (console-api v2)

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idA2A = 'IDA2A01'

Scenario: Lettura di una applicazione censita

Given url consoleBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idA2A == idA2A
And match response.principal == '#notnull'
And match response.abilitato == '#boolean'
And match response.acl == '#[]'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: La rappresentazione non contiene la password e rimanda al connettore

Given url consoleBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response.password == '#notpresent'
And match response._links.connettoreIntegrazione.href == '#notnull'

Scenario: Lettura di una applicazione non esistente

Given url consoleBaseurl
And path 'applicazioni', 'APP-CHE-NON-ESISTE'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'applicazioni', idA2A
When method get
Then status 401
