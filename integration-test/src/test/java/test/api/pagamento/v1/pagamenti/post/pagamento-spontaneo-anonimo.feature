Feature: Pagamenti spontanei anonimi

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'public'})

Scenario: Pagamento spontaneo anonimo con entrata riferita

* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
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
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{
	identificativoUnivocoVersante: { 
		tipoIdentificativoUnivoco: 'F',
		codiceIdentificativoUnivoco: 'ANONIMO'
	},
	anagraficaVersante: 'ANONIMO',
	indirizzoVersante: null, 
	civicoVersante: null, 
	capVersante: null, 
	localitaVersante: null, 
	provinciaVersante: null, 
	nazioneVersante: null,
	e-mailVersante: '#(pagamentoPost.soggettoVersante.email)'
}
"""
And match response.rpp[0].rpt.soggettoPagatore == null

Scenario: Pagamento spontaneo anonimo con entrata riferita non consentita su spontaneo

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codDovuto
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
And request pagamentoPost
When method post
Then status 422
And match response == 
"""
{
	"categoria":"RICHIESTA",
	"codice":"UAN_002",
	"descrizione":"Richiesta non valida",
	"dettaglio":'#notnull'
}
"""


Scenario: Pagamento spontaneo anonimo senza versante

* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.soggettoVersante = null

Given url pagamentiBaseurl
And path '/pagamenti'
And request pagamentoPost
When method post
Then status 400

Scenario: Pagamento spontaneo anonimo senza email

* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
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
  "cellulare": "+39 000-1234567"
}
"""
Given url pagamentiBaseurl
And path '/pagamenti'
And request pagamentoPost
When method post
Then status 400
