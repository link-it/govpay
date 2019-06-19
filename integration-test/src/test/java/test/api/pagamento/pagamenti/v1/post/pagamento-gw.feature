Feature: Gateway

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenza = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})
* def pagamentoPost = read('classpath:test/api/pagamento/pagamenti/v1/post/msg/pagamento-post_riferimento_avviso.json')

Scenario: Verifica gateway

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* set pagamentoPost.urlRitorno = 'https://my.pretty.url/' 

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url govpay_url + "/govpay/frontend/web/connector/ecsp/psp"
And param idSession = response.idSession
And param idDominio = pendenza.idDomino
And param esito = 'ERROR'
When method get
Then status 302
And match responseHeaders.Location == pagamentoPost.urlRitorno
