Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')

Scenario: Pagamento avviso precaricato autenticato spid da soggetto debitore

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "XXXYYY30A01H501I",
  "anagrafica": "Abigail Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-0000000"
}
"""

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def idPagamentoRossi = response.id

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers spidHeaders
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{ 
	identificativoUnivocoVersante: { 
		tipoIdentificativoUnivoco: 'F',
		codiceIdentificativoUnivoco: 'RSSMRA30A01H501I'
	},
	anagraficaVersante: 'Mario Rossi',
	indirizzoVersante: null, 
	civicoVersante: null, 
	capVersante: null, 
	localitaVersante: null, 
	provinciaVersante: null, 
	nazioneVersante: null,
	e-mailVersante: 'mario.rossi@host.eu'
}
"""



Scenario: Pagamento avviso precaricato autenticato spid da altro soggetto

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def idPagamentoVerdi = response.id

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers spidHeaders
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{ 
	identificativoUnivocoVersante: { 
		tipoIdentificativoUnivoco: 'F',
		codiceIdentificativoUnivoco: 'VRDGPP65B03A112N'
	},
	anagraficaVersante: 'Giuseppe Verdi',
	indirizzoVersante: null, 
	civicoVersante: null, 
	capVersante: null, 
	localitaVersante: null, 
	provinciaVersante: null, 
	nazioneVersante: null,
	e-mailVersante: 'gverdi@mailserver.host.it'
}
"""

