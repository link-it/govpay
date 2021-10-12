Feature: Pagamenti spontanei definizione del parametro opzionale codiceConvenzione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

@test1
Scenario: Pagamento spontaneo cittadino con codiceConvenzione = CCOK-REDIRECT

* def codiceConvenzione = 'CCOK-REDIRECT'

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And param codiceConvenzione = codiceConvenzione
And headers spidHeaders
And request pagamentoPost
When method post
Then status 403
And match response == { categoria : 'AUTORIZZAZIONE', codice : '403000', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.descrizione == 'Operazione non autorizzata'
And match response.dettaglio == 'Il richiedente non è autorizzato ad indicare un codice convenzione per il pagamento'

