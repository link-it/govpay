Feature: Validazione semantica richieste

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo.json')
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pagamentoPostEntrataRiferita = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')

Scenario: Data pagamento decorsa

* set pagamentoPost.dataEsecuzionePagamento = '2015-12-31'

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 422
And match response == { categoria: 'RICHIESTA', codice: '#notnull', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.codice == 'SEMANTICA'
And match response.dettaglio contains 'dataEsecuzionePagamento'



Scenario: idVocePendenza non univoci

* set pagamentoPost.pendenze[0].voci[1].idVocePendenza = pagamentoPost.pendenze[0].voci[0].idVocePendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: '#notnull', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.codice == 'VER_001'
And match response.dettaglio == 'Il versamento (' + idPendenza + ') dell\'applicazione (IDA2A01) presenta il codSingoloVersamentoEnte (1) piu\' di una volta'

Scenario: importi pagamento non coerenti

* set pagamentoPost.pendenze[0].importo = pagamentoPost.pendenze[0].importo + 10.00

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 422

* match response contains { categoria: 'RICHIESTA', codice: 'VER_002', descrizione: 'Richiesta non valida', id: '#notnull', location: '#notnull', dettaglio: '#notnull' }


Scenario: numeroAvviso non conforme alle specifiche pagoPA

* set pagamentoPostEntrataRiferita.pendenze[0].numeroAvviso = '000000000000000000'

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPostEntrataRiferita
When method post
Then status 422
And match response == { categoria: 'RICHIESTA', codice: '#notnull', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.codice == 'VER_026'
And match response.dettaglio == 'Lo IUV (000000000000000000) non e\' conforme alle specifiche agid, application code (00) non valido per la stazione (11111111113_01)'
