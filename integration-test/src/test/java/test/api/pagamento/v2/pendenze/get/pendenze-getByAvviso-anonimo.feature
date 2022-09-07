Feature: Lettura pendenza by numero avviso per utenze anonimo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Lettura pendenza by numero avviso per utenze anonimo

* def pendenzePagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)

Given url pendenzePagamentoBaseurl
And path '/pendenze/byAvviso', idDominio, numeroAvviso
And header Accept = 'application/json'
When method get
Then status 403