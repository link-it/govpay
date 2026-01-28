Feature: Configurazione Applicazione e Reset Cache

Background: 

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: 

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione
And headers gpAdminBasicAutenticationHeader
And request stazioneRequest
When method put
Then assert responseStatus == 200 || responseStatus == 201

* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')