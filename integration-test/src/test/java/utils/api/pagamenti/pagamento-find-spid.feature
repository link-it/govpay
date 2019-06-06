Feature: Ricerca pagamenti di una pendenza

Background: 

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

Scenario: Ricerca pagamenti di una pendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
When method get
Then status 200

