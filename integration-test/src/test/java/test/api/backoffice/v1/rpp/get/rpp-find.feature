Feature: Ricerca rpp

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Ricerca rpp anonimo

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'public'})

Given url backofficeBaseurl
And path '/rpp' 
When method get
Then status 403

Scenario: Ricerca rpp cittadino

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

* configure cookies = null

Given url backofficeBaseurl
And headers spidHeadersVerdi
And path '/rpp' 
When method get
Then status 403
