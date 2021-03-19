Feature: Pagamenti spontanei con autenticazione spid

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Scenario: Pagamento spontaneo basic con entrata riferita e versante specificato

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

Scenario: Pagamento spontaneo basic autodeterminato e versante non specificato

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == null

Scenario: Pagamento spontaneo basic con pendenza non consentita su spontaneo

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codDovuto

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

@TEST1
Scenario: Pagamento spontaneo di una pendenza cambiando id Applicazione

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

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo.json')
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
* set pagamentoPost.pendenze = []
* set pendenza.idA2A = idA2A2
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pagamentoPost.pendenze[0] = pendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 422
And match response == { categoria: 'RICHIESTA', codice: 'VER_025', descrizione: 'Richiesta non valida', dettaglio: '#notnull', id: '#notnull', location: '#notnull'  }
And match response.dettaglio == '#("La pendenza (IdA2A:"+ idA2A2 +", Id:"+ idPendenza +") ha un numero avviso ("+numeroAvviso+") gia\' utilizzato (IdA2A:"+ idA2A +", Id:"+ idPendenza +").")'



