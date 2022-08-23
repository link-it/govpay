Feature: Pagamento Frontend EC di avvisi con SANP 3.1.0

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

# configurazione della stazione per utilizzare la nuova modalita'

* def stazione = read('classpath:configurazione/v1/msg/stazione.json')
* set stazione.versione = 'V2'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione 
And headers gpAdminBasicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario: Pagamento di un avviso precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
* set pagamentoPost.urlRitorno = 'http://localhost:8080/portaleEC'

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 200

Scenario: Pagamento di un avviso precaricato urlRitorno assente

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'urlRitorno'


Scenario: Pagamento di un carrello contenente un avviso precaricato e una pendenza

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def idPendenza = getCurrentTimeMillis()
* def pendenza2 = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza2
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
* set pagamentoPost.urlRitorno = 'http://localhost:8080/portaleEC'
* set pagamentoPost.pendenze[1] = 
"""
{
	idPendenza: '#(idPendenza)',
	idA2A: '#(idA2A)'
}
"""

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il pagamento presso frontend dell\'EC si puo\' effettuare solo indicando i riferimenti avviso.'




