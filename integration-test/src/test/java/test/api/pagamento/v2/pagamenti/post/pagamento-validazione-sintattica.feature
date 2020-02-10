Feature: Validazione sintattica richieste pagamento

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo.json')
* set pagamentoPost.soggettoVersante.tipo = 'F'
* set pagamentoPost.soggettoVersante.identificativo = 'VRDGPP65B03A112N'
* set pagamentoPost.soggettoVersante.anagrafica = 'Giuseppe Verdi'
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |

| pendenze.voci.codiceContabilita | pagamentoPost.pendenze[0].voci[1].codiceContabilita | 'XX' | 'codiceContabilita' |

Scenario: Riferimento pendenza errato

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request { pendenze: [ { idA2A: idA2A, idPendenza: null } ] }
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'idPendenza'

Scenario: Riferimento avviso errato

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request { pendenze: [ { idDominio: '00000000000', numeroAvviso: null } ] }
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'numeroAvviso'

Scenario: Numero voci eccessivo

* set pagamentoPost.pendenze[0].voci[3] = pagamentoPost.pendenze[0].voci[0]
* set pagamentoPost.pendenze[0].voci[3].idVocePendenza = 4
* set pagamentoPost.pendenze[0].voci[4] = pagamentoPost.pendenze[0].voci[0]
* set pagamentoPost.pendenze[0].voci[4].idVocePendenza = 5
* set pagamentoPost.pendenze[0].voci[5] = pagamentoPost.pendenze[0].voci[0]
* set pagamentoPost.pendenze[0].voci[5].idVocePendenza = 6

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'voci'

Scenario: Array pendenze vuoto

* set pagamentoPost.pendenze = []

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'pendenze'


Scenario: Numero pendenze eccessivo

* set pagamentoPost.pendenze[1] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[2] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[3] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[4] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[5] = pagamentoPost.pendenze[0]

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'pendenze'

