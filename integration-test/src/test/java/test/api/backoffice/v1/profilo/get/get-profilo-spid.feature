Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')



Scenario: Acquisizione del profilo autenticato spid

* def userBaseurl = getGovPayApiBaseUrl({api: 'user', versione: 'v1', autenticazione: 'spid'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def backofficeFormBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'form'})
* def backofficeSessionBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'session'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

* configure cookies = null
* configure followRedirects = false

Given url userBaseurl
And path '/login', 'SPID_PROFILO'
And headers spidHeaders
When method get
Then status 303

* def profiloLocation = responseHeaders['Location'][0]

Given url profiloLocation
When method get
Then status 200

Given url backofficeFormBaseurl
And path '/profilo'
When method get
Then status 403

Given url backofficeBaseurl
And path '/profilo'
When method get
Then status 401
