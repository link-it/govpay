Feature: Pagamenti spontanei con pendenze con proprieta definite

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e pendenza con linguaSecondaria = false

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

* set pagamentoPost.pendenze[0].proprieta = 
"""
{
	linguaSecondaria : 'false'
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

And match response.pendenze[0].proprieta === 
"""
{
	linguaSecondaria : 'false'
}
"""

Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e pendenza con linguaSecondaria = en

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

* set pagamentoPost.pendenze[0].proprieta = 
"""
{
	linguaSecondaria : 'en'
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

And match response.pendenze[0].proprieta === 
"""
{
	linguaSecondaria : 'en'
}
"""

Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e pendenza con linguaSecondaria errata

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

* set pagamentoPost.pendenze[0].proprieta = 
"""
{
	linguaSecondaria : 'XXXX'
}
"""

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 400
* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains 'linguaSecondaria'




