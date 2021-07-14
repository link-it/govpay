Feature: Pagamenti spontanei con pendenze con contabilita definita

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* def pendenzaResponse = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-get_monovoce_contabilita.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'


Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e contabilita definita

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_contabilita.json')
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

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{
	"identificativoUnivocoVersante": {
		"tipoIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.tipo)",
		"codiceIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.identificativo)"
	},
	"anagraficaVersante":"#(pagamentoPost.soggettoVersante.anagrafica)",
	"indirizzoVersante":"#(pagamentoPost.soggettoVersante.indirizzo)",
	"civicoVersante":"#(pagamentoPost.soggettoVersante.civico)",
	"capVersante":"#(pagamentoPost.soggettoVersante.cap + '')",
	"localitaVersante":"#(pagamentoPost.soggettoVersante.localita)",
	"provinciaVersante":"#(pagamentoPost.soggettoVersante.provincia)",
	"nazioneVersante":"#(pagamentoPost.soggettoVersante.nazione)",
	"e-mailVersante":"#(pagamentoPost.soggettoVersante.email)"
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.voci[0].contabilita == pendenzaResponse.voci[0].contabilita
* match response.stato == 'NON_ESEGUITA'

Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e contabilita con proprieta custom definita

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_contabilita.json')

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

* def proprietaCustom = 
"""
{ "test1" : 1, "test2" : "test", "test3" : false, "test4" : { "test1" : 1 } }
"""
* set pagamentoPost.pendenze[0].voci[0].contabilita.proprietaCustom = proprietaCustom
* set pendenzaResponse.voci[0].contabilita.proprietaCustom = proprietaCustom

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{
	"identificativoUnivocoVersante": {
		"tipoIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.tipo)",
		"codiceIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.identificativo)"
	},
	"anagraficaVersante":"#(pagamentoPost.soggettoVersante.anagrafica)",
	"indirizzoVersante":"#(pagamentoPost.soggettoVersante.indirizzo)",
	"civicoVersante":"#(pagamentoPost.soggettoVersante.civico)",
	"capVersante":"#(pagamentoPost.soggettoVersante.cap + '')",
	"localitaVersante":"#(pagamentoPost.soggettoVersante.localita)",
	"provinciaVersante":"#(pagamentoPost.soggettoVersante.provincia)",
	"nazioneVersante":"#(pagamentoPost.soggettoVersante.nazione)",
	"e-mailVersante":"#(pagamentoPost.soggettoVersante.email)"
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.voci[0].contabilita == pendenzaResponse.voci[0].contabilita
* match response.stato == 'NON_ESEGUITA'

Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e contabilita.quote[0] con proprieta custom definita

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_contabilita.json')

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

* def proprietaCustom = 
"""
{ "test1" : 1, "test2" : "test", "test3" : false, "test4" : { "test1" : 1 } }
"""
* set pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].proprietaCustom = proprietaCustom
* set pendenzaResponse.voci[0].contabilita.quote[0].proprietaCustom = proprietaCustom

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{
	"identificativoUnivocoVersante": {
		"tipoIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.tipo)",
		"codiceIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.identificativo)"
	},
	"anagraficaVersante":"#(pagamentoPost.soggettoVersante.anagrafica)",
	"indirizzoVersante":"#(pagamentoPost.soggettoVersante.indirizzo)",
	"civicoVersante":"#(pagamentoPost.soggettoVersante.civico)",
	"capVersante":"#(pagamentoPost.soggettoVersante.cap + '')",
	"localitaVersante":"#(pagamentoPost.soggettoVersante.localita)",
	"provinciaVersante":"#(pagamentoPost.soggettoVersante.provincia)",
	"nazioneVersante":"#(pagamentoPost.soggettoVersante.nazione)",
	"e-mailVersante":"#(pagamentoPost.soggettoVersante.email)"
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.voci[0].contabilita == pendenzaResponse.voci[0].contabilita
* match response.stato == 'NON_ESEGUITA'

Scenario Outline: Pagamento spontaneo basic con entrata riferita, versante specificato e pendenza con contabilita errata

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_contabilita.json')

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
| importo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].importo | null | 'importo' |
| importo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].importo | '10.001' | 'importo' |
| importo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].importo | '10,000' | 'importo' |
| importo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].importo | '10,00.0' | 'importo' |
| importo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].importo | 'aaaa' | 'importo' |
| importo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].importo | '12345678901234567,89' | 'importo' |
| capitolo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].capitolo | loremIpsum | 'capitolo' |
| capitolo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].capitolo | null | 'capitolo' |
| accertamento | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].accertamento | loremIpsum | 'accertamento' |
| annoEsercizio | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].annoEsercizio | 12345 | 'annoEsercizio' |
| annoEsercizio | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].annoEsercizio | loremIpsum | 'annoEsercizio' |
| annoEsercizio | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].annoEsercizio | null | 'annoEsercizio' |
| titolo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].titolo | loremIpsum | 'titolo' |
| tipologia | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].tipologia | loremIpsum | 'tipologia' |
| categoria | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].categoria | loremIpsum | 'categoria' |
| articolo | pagamentoPost.pendenze[0].voci[0].contabilita.quote[0].articolo | loremIpsum | 'articolo' |





