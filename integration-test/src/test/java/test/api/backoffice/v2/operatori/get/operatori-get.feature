Feature: Dettaglio di un operatore (console-api v2)

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Lettura di un operatore censito

Given url consoleBaseurl
And path 'operatori', govpay_backoffice_user
And headers basicAutenticationHeader
When method get
Then status 200
And match response.principal == govpay_backoffice_user
And match response.nome == '#notnull'
And match response.abilitato == '#boolean'
And match response.acl == '#[]'
And match response.domini == '#[]'
And match response.ruoli == '#[]'
And match response.tipiPendenza == '#[]'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: La rappresentazione non contiene la password

# In v1 la password era un campo dell'operatore; in v2 e' una sotto-risorsa e
# non compare mai in lettura.

Given url consoleBaseurl
And path 'operatori', govpay_backoffice_user
And headers basicAutenticationHeader
When method get
Then status 200
And match response.password == '#notpresent'

Scenario: Lettura di un operatore non esistente

Given url consoleBaseurl
And path 'operatori', 'OPERATORE_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'operatori', govpay_backoffice_user
When method get
Then status 401
