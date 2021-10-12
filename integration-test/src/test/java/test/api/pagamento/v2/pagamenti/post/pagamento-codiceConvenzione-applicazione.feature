Feature: Pagamenti spontanei definizione del parametro opzionale codiceConvenzione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* configure retry = { count: 25, interval: 10000 }

@test1
Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e codiceConvenzione = CCOK-REDIRECT

* def codiceConvenzione = 'CCOK-REDIRECT'

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
And param codiceConvenzione = codiceConvenzione
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def idPagamento = response.id

* configure followRedirects = false
* def idSession = response.idSession
* def idPagamento = response.id
* def tipoRicevuta = "R01"
* def cumulativo = "0"

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = tipoRicevuta
And param riversamento = cumulativo
When method get
Then status 302
And match responseHeaders.Location == '#notnull'

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def rptToCheck = rptNotificaTerminazione

Given url pagamentiBaseurl
And path '/pagamenti/', idPagamento
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
And match response.rpp[0].stato == 'RT_ACCETTATA_PA' 
And match response.rpp[0].rt.datiPagamento.datiSingoloPagamento[0].commissioniApplicatePA == '1.00'

@test2
Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e codiceConvenzione = CCOK-NOREDIRECT

* def codiceConvenzione = 'CCOK-NOREDIRECT'

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
And param codiceConvenzione = codiceConvenzione
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '##null', idSession: '##null' }

* def idPagamento = response.id

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def rptToCheck = rptNotificaTerminazione

Given url pagamentiBaseurl
And path '/pagamenti/', idPagamento
And headers idA2ABasicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then status 200
And match response.rpp[0].stato == 'RT_ACCETTATA_PA' 
And match response.rpp[0].rt.datiPagamento.datiSingoloPagamento[0].commissioniApplicatePA == '1.00'

@test3
Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e codiceConvenzione = CCKOX

* def codiceConvenzione = 'CCKOX'

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
And param codiceConvenzione = codiceConvenzione
And request pagamentoPost
When method post
Then status 502
And match response == { id: '#notnull', location: '#notnull', categoria : 'PAGOPA', codice : 'PPT_OPER_CODICE_CONVENZIONE_ERRATO', descrizione: '##string', dettaglio: '#notnull' }
And match response.dettaglio == 'Simulazione errore codice convenzione.'

@test4
Scenario: Pagamento spontaneo basic con entrata riferita, versante specificato e codiceConvenzione in errore sintassi

* def codiceConvenzione = 'XXXXX'

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
And param codiceConvenzione = codiceConvenzione
And request pagamentoPost
When method post
Then status 502
And match response == { id: '#notnull', location: '#notnull', categoria : 'PAGOPA', codice : 'PPT_SINTASSI_XSD', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.dettaglio == 'Errore di sintassi nel campo codiceConvenzione.'


@test5
Scenario Outline: Validazione sintassi parametro codiceConvenzione

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
And param codiceConvenzione = <codiceConvenzione>
And request pagamentoPost
When method post
Then status 400
And match response == { categoria : 'RICHIESTA', codice : 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'codiceConvenzione'

Examples:
| codiceConvenzione |
| 'XXX' |
| '123456789012345678901234567890123456' |


