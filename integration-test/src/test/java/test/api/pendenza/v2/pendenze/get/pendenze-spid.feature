Feature: Ricerca pendenze

Background:

* callonce read('classpath:utils/common-utils.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Scenario: Ricerca pendenze utente spid

Given url pendenzeBaseurl
And path '/pendenze'
And headers spidHeaders
When method get
Then status 403

Scenario: Ricerca pendenze utente spid

Given url pendenzeBaseurl
And path '/pendenze', 'test', 'test'
And headers spidHeaders
When method get
Then status 403 

