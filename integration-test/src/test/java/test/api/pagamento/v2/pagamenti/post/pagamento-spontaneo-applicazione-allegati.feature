Feature: Caricamento pendenze con allegati

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario: Caricamento di una pendenza con allegato

* def idPendenza = getCurrentTimeMillis()

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "RSSMRA30A01H501I",
  "anagrafica": "Mario Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-1234567"
}
"""
* set pagamentoPost.pendenze[0].allegati[0].nome = 'tipoPendenza-promemoria-oggetto-freemarker.ftl'
* set pagamentoPost.pendenze[0].allegati[0].tipo = 'application/json'
* set pagamentoPost.pendenze[0].allegati[0].contenuto = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/pendenze/put/msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.allegati[0].nome == pagamentoPost.pendenze[0].allegati[0].nome
* match response.allegati[0].tipo == pagamentoPost.pendenze[0].allegati[0].tipo

Scenario Outline: <field> non valida

* def idPendenza = getCurrentTimeMillis()

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "RSSMRA30A01H501I",
  "anagrafica": "Mario Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-1234567"
}
"""

* set pagamentoPost.pendenze[0].allegati[0].nome = 'tipoPendenza-promemoria-oggetto-freemarker.ftl'
* set pagamentoPost.pendenze[0].allegati[0].tipo = 'application/json'
* set pagamentoPost.pendenze[0].allegati[0].contenuto = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/pendenze/put/msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))

* set <fieldRequest> = <fieldValue>

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| allegati.nome | pagamentoPost.pendenze[0].allegati[0].nome | null | 'nome' |
| allegati.nome | pagamentoPost.pendenze[0].allegati[0].nome | loremIpsum | 'nome' |
| allegati.tipo | pagamentoPost.pendenze[0].allegati[0].tipo | loremIpsum | 'tipo' |
| allegati.contenuto | pagamentoPost.pendenze[0].allegati[0].contenuto | null | 'contenuto' |

