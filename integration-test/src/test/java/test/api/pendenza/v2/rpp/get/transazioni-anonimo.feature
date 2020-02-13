Feature: Lettura transazioni anonimo

Background:

* callonce read('classpath:utils/common-utils.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'public'})

Scenario: Ricerca pendenze utente anonimo

Given url pendenzeBaseurl
And path '/rpp'
When method get
Then status 403

Scenario: Ricerca pendenze utente anonimo

Given url pendenzeBaseurl
And path '/rpp', 'test', 'test'
When method get
Then status 403 

