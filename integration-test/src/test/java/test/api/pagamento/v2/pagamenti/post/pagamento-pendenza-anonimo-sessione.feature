Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})

* def idTipoPendenzaCOSAP = 'COSAP'
* def pendenzaCreataMSG = read('classpath:test/api/pagamento/v2/pendenze/post/msg/pendenza-creata-anonimo.json')

# Configurazione tipo pendenza
Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenzaCOSAP
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Canone Occupazione Spazi ed Aree Pubbliche' , codificaIUV: null, pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio =
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  abilitato: true,
  portaleBackoffice: {
        abilitato: true,
        form: {
                tipo: "angular2-json-schema-form",
                definizione: null
          },
          validazione: null,
          trasformazione: {
                tipo: "freemarker",
                definizione: null
          },
          inoltro: null
  },
  portalePagamento: {
        abilitato: true,
        form: {
                tipo: "angular2-json-schema-form",
                definizione: null,
                impaginazione: null
          },
          validazione: null,
          trasformazione: {
                tipo: "freemarker",
                definizione: null
          },
          inoltro: null
  },
  visualizzazione: null,
  tracciatoCsv: {
        tipo: "freemarker",
        intestazione: "id,numeroAvviso,pdfAvviso,anagrafica,indirizzo,civico,localita,cap,provincia",
        richiesta: null,
          risposta: null
  }
}
"""
* set tipoPendenzaDominio.portaleBackoffice = null
* set tipoPendenzaDominio.portalePagamento.form.definizione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-form.json'))
* set tipoPendenzaDominio.portalePagamento.form.impaginazione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-impaginazione.json'))
* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-trasformazione-id-pendenza-da-request.ftl'))
* set tipoPendenzaDominio.portalePagamento.validazione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-validazione.json'))
* set tipoPendenzaDominio.avvisaturaMail = null
* set tipoPendenzaDominio.tracciatoCsv = null

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenzaCOSAP
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario: Pagamento pendenza precaricata anonimo

#Inserimento di una nuova pendenza di tipo spontaneo con utenza anonima
* def idPendenza = getCurrentTimeMillis()

* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

* copy pendenzaCreata = response

* def idPendenza = pendenzaCreata.idPendenza

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')
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


Scenario: Pagamento pendenza precaricata anonimo dopo aggiornamento

#Inserimento di una nuova pendenza di tipo spontaneo con utenza anonima
* def idPendenza = getCurrentTimeMillis()

* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

* copy pendenzaCreata = response

* def idPendenza = pendenzaCreata.idPendenza
* def idA2A = pendenzaCreata.idA2A

# aggiornamento importo

* set requestPendenza.importo = 200.02

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param idA2A = pendenzaCreata.idA2A	
And param idPendenza = pendenzaCreata.idPendenza
And request requestPendenza
When method post
Then status 200
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

* copy pendenzaAggiornata = response


* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')
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
And match response.importo == requestPendenza.importo


Scenario: modifica di una pendenza caricata da un altro utente

#Inserimento di una nuova pendenza di tipo spontaneo con utenza anonima
* def idPendenza = getCurrentTimeMillis()

* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

* copy pendenzaCreata = response

* def idPendenza = pendenzaCreata.idPendenza
* def idA2A = pendenzaCreata.idA2A

* configure cookies = null
Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param idA2A = pendenzaCreata.idA2A	
And param idPendenza = pendenzaCreata.idPendenza
And request requestPendenza
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio == 'Impossibile effettuare l\'operazione di aggiornamento, nessuna pendenza disponibile per l\'utenza.'

Scenario: caricamento di due pendenze con lo stesso id da parte di due utenti

#Inserimento di una nuova pendenza di tipo spontaneo con utenza anonima
* def idPendenza = getCurrentTimeMillis()
* def idPendenzaCheck = idPendenza

* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenzaCheck

* copy pendenzaCreata = response
* def cookieRossi = responseHeaders["Set-Cookie"][0]

* def idPendenza = pendenzaCreata.idPendenza
* def idA2A = pendenzaCreata.idA2A

* configure cookies = null
Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenzaCheck

* copy pendenzaCreataVerdi = response
* def cookieVerdi = responseHeaders["Set-Cookie"][0]
* def idPendenza = response.idPendenza
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')
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

* configure cookies = null
Given url pagamentiBaseurl
And path '/pagamenti'
And header cookie = cookieRossi
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }


* configure cookies = null
Given url pagamentiBaseurl
And path '/pagamenti'
And header cookie = cookieVerdi
And request pagamentoPost
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_024', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '#("La pendenza (IdA2A:" + pendenzaCreata.idA2A + ", Id:" + pendenzaCreata.idPendenza + ") ha un numero avviso (" + pendenzaCreataVerdi.numeroAvviso + ") diverso dall\'originale (" + pendenzaCreata.numeroAvviso + ").")'


Scenario: caricamento di due pendenze con lo stesso id da parte di due utenti e pagamento effettuato dal primo utente

#Inserimento di una nuova pendenza di tipo spontaneo con utenza anonima
* def idPendenza = getCurrentTimeMillis()
* def idPendenzaCheck = idPendenza

* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenzaCheck

* copy pendenzaCreata = response
* def cookieRossi = responseHeaders["Set-Cookie"][0]

* def idPendenza = pendenzaCreata.idPendenza
* def idA2A = pendenzaCreata.idA2A

* configure cookies = null
Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenzaCheck

* copy pendenzaCreataVerdi = response
* def cookieVerdi = responseHeaders["Set-Cookie"][0]
* def idPendenza = response.idPendenza
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')
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

* configure cookies = null
Given url pagamentiBaseurl
And path '/pagamenti'
And header cookie = cookieRossi
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

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


* configure cookies = null
Given url pagamentiBaseurl
And path '/pagamenti'
And header cookie = cookieVerdi
And request pagamentoPost
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_003', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '#("La pendenza (IdA2A:" + pendenzaCreata.idA2A + ", Id:" + pendenzaCreata.idPendenza + ") e\' in uno stato che non consente l\'aggiornamento (ESEGUITO)")'






