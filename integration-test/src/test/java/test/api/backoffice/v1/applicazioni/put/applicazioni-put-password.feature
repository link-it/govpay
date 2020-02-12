Feature: Censimento applicazioni con definizione della password per l'autenticazione HTTP-Basic

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')

Scenario: Aggiunta di una applicazione, test login, modifica dell'applicazione (non del campo password ) e login con le stesse credenziali

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 200

# modifica applicazione

* set applicazione.servizioIntegrazione = null

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201
And match response.servizioIntegrazione == '#notpresent'

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 200

Scenario: Aggiunta di una applicazione, test login, modifica dell'applicazione (anche campo password ) e login con le nuove credenziali

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 200

# modifica applicazione

* def nuovaPasswordA2A = 'Password2'
* set applicazione.password = nuovaPasswordA2A

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 401

* def nuovoIdA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: nuovaPasswordA2A } )

Given url backofficeBaseurl
And path 'profilo'
And headers nuovoIdA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 200




















