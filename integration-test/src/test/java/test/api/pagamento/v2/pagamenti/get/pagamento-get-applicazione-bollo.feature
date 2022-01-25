Feature: Dettaglio pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Lettura dettaglio pagamento bollo

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codDovuto

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* configure followRedirects = false
* def idSession = response.idSession
* def idPagamento = response.id

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice =  "R01"
And param riversamento = "0"
When method get
Then status 302
And match responseHeaders.Location == '#notnull'

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

Given url pagamentiBaseurl
And path '/pagamenti', idPagamento
And headers idA2ABasicAutenticationHeader
When method get
Then status 200








