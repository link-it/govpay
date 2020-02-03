Feature: Pagamento avviso precaricato

Background:

Scenario: Pagamento avviso precaricato anonimo

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
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')

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

* def idPagamentoAnonimo = response.id
* def idSessionAnonimo = response.idSession

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
	indirizzoVersante: '##null', 
	civicoVersante: '##null', 
	capVersante: '##null', 
	localitaVersante: '##null', 
	provinciaVersante: '##null', 
	nazioneVersante: '##null',
	e-mailVersante: '#(pagamentoPost.soggettoVersante.email)'
}
"""
And match response.rpp[0].rpt.soggettoPagatore == null