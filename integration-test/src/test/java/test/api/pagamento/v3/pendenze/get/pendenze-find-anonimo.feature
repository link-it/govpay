Feature: Ricerca pagamenti

Background:

* call read('classpath:utils/common-utils.feature')

Scenario: Ricerca pagamenti anonimo

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'public'})

Given url pagamentiBaseurl
And path '/pendenze' 
When method get
Then status 403

