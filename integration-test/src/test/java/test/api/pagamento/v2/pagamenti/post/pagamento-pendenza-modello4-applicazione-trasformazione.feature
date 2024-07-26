Feature: Pagamento spontaneo modello 4 con trasformazione dell'input senza inoltro

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def idTipoPendenza = codSpontaneo
* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: false,
  portalePagamento: {
  	form: null,
	  trasformazione: {
	  	tipo: "freemarker",
	  	definizione: null
	  },
	  validazione: null
  },
  abilitato: true
}
"""  
* set tipoPendenzaDominio.portalePagamento.validazione = encodeBase64InputStream(karate.readAsString('msg/tipoPendenza-spontanea-validazione-form.json.payload'))

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_modello4.json')
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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

Scenario: Pagamento spontaneo modello 4 autenticato basic

* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-freemarker.ftl'))

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers basicAutenticationHeader
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

Scenario: Pagamento spontaneo modello 4 autenticato basic template di trasformazione non valido

* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = 'eyAidHlwZSI6ICJvYmplY3QiIH0='

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 500
* match response == { categoria: 'INTERNO', codice: 'VAL_003', descrizione: 'Errore durante la trasformazione', dettaglio: '#notnull' }
# * match response.dettaglio contains decodeBase64(tipoPendenzaDominio.portalePagamento.trasformazione.definizione)
* match response.dettaglio contains 'Unrecognized field "type"'

Scenario: Pagamento spontaneo modello 4 autenticato basic template di trasformazione crea una pendenza con errori di sintassi

* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-sintassi-freemarker.ftl'))

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 500
* match response == { categoria: 'INTERNO', codice: 'VAL_003', descrizione: 'Errore durante la trasformazione', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il campo idPendenza non deve essere vuoto'


