Feature: Lettura pendenza 

Background: 

* def pendenzaGet = read('classpath:test/api/pagamento/v1/pendenze/get/msg/pendenza-get-dettaglio.json')
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

Scenario: 

Given url pagamentiBaseurl
And path '/pendenze', idA2A, idPendenza
And headers spidHeaders
When method get
Then status 200
And match response == pendenzaGet

