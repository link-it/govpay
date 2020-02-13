Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')

Scenario: Ricerca pendenze utente anonimo

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'public'})

Given url pendenzeBaseurl
And path '/pendenze'
When method get
Then status 403 

Scenario: Ricerca pendenze utente anonimo

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'public'})

Given url pendenzeBaseurl
And path '/pendenze', 'test', 'test'
When method get
Then status 403 