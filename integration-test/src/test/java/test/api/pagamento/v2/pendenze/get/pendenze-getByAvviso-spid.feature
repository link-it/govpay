Feature: Lettura pendenza by numero avviso per utenze cittadino

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* configure cookies = null

Scenario: Lettura pendenza by numero avviso per utenze cittadino

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}
* def pendenzePagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)

Given url pendenzePagamentoBaseurl
And path '/pendenze/byAvviso', idDominio, numeroAvviso
And headers spidHeaders
And header Accept = 'application/json'
When method get
Then status 403