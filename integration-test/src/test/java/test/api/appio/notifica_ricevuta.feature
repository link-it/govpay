Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def ndpsym_psp_url = ndpsym_url + '/psp/'

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* def path_batch_appio = '/appIOBatch'
* def path_avvisatura_appio = '/avvisaturaAppIO'

* def configurazione_patch = 
"""
[
  {
    "op": "REPLACE",
    "path": "/appIOBatch",
    "value": null
  }
]
"""

* def configurazione_batchAppIO = 
"""
{
	"abilitato": true, 
	"url": "http://localhost:8888/appio/",
	"timeToLive": 3600
}
"""

* def configurazione_avvisaturaAppIO = 
"""
{
	"promemoriaAvviso": {
		"tipo": "freemarker",
		"oggetto": "..base64 freemarker..",
		"messaggio": "..base64 freemarker.."
	},
	"promemoriaRicevuta": {
		"tipo": "freemarker",
		"oggetto": "..base64 freemarker..",
		"messaggio": "..base64 freemarker..",
		"soloEseguiti": true
	},
	"promemoriaScadenza": {
		"tipo": "freemarker",
		"oggetto": "..base64 freemarker..",
		"messaggio": "..base64 freemarker..",
		"preavviso": 0
	}
}
"""

* def tipoPendenzaDominio_appIO_apiKey = 'ABC...........'
* def tipoPendenzaDominio_appIO = 
"""
{
	"abilitato" : true,
  "codificaIUV" : null,
  "pagaTerzi": true,
  portaleBackoffice: null,
  portalePagamento: null,
  avvisaturaMail: null,
  avvisaturaAppIO: {
	  "apiKey" : null,
  	"promemoriaAvviso": {
  		abilitato: true,
  		"tipo": "freemarker"
  		},
		"promemoriaRicevuta": {
  		abilitato: true,
  		"tipo": "freemarker"
  		},
		"promemoriaScadenza": null
  }
}
"""

* set configurazione_avvisaturaAppIO.promemoriaAvviso.oggetto = encodeBase64InputStream(read('classpath:configurazione/v1/msg/appio-subject-freemarker.ftl'))
* set configurazione_avvisaturaAppIO.promemoriaAvviso.messaggio = encodeBase64InputStream(read('classpath:configurazione/v1/msg/appio-body-freemarker.ftl'))

* set configurazione_avvisaturaAppIO.promemoriaRicevuta.oggetto = encodeBase64InputStream(read('classpath:configurazione/v1/msg/appio-subject-ricevuta-freemarker.ftl'))
* set configurazione_avvisaturaAppIO.promemoriaRicevuta.messaggio = encodeBase64InputStream(read('classpath:configurazione/v1/msg/appio-body-ricevuta-freemarker.ftl'))

* def debitoreNoAuthAppIO = 
"""
{
		"tipo": "F",
		"identificativo": "VRDGPP65B03A113N",
		"anagrafica": "Giuseppino Verdi",
		"email": "giuseppe.verdi@testmail.it",
		"cellulare": "+39 000-0000000"
	}
"""

* def debitoreNonCensitoAppIO = 
"""
{
		"tipo": "F",
		"identificativo": "VRDGPP65B03A112N",
		"anagrafica": "Giuseppe Verdi",
		"email": "giuseppe.verdi@testmail.it",
		"cellulare": "+39 000-0000000"
	}
"""

* def debitoreCensitoNonAbilitatoAppIO = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H502I",
		"anagrafica": "Marco Rossi",
		"email": "marco.rossi@testmail.it",
		"cellulare": "+39 000-0000000"
	}
"""

* def debitoreCensitoAbilitatoAppIO = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it",
		"cellulare": "+39 000-0000000"
	}
"""

* def debitoreCensitoAppIOErrore400 = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H503I",
		"anagrafica": "Marioo Rossi",
		"email": "marioo.rossi@testmail.it",
		"cellulare": "+39 000-0000000"
	}
"""

* def debitoreCensitoAppIOErrore500 = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H504I",
		"anagrafica": "Mariooo Rossi",
		"email": "mariooo.rossi@testmail.it",
		"cellulare": "+39 000-0000000"
	}
"""

@appio-ricevuta @appio-ricevuta-1
Scenario: Notifica pagamento avvenuto

# Svuoto Cache Simulatore AppIO
Given url appio_api_url
And path 'resetCacheAppIO'
When method get
Then status 200

# Configurazione AppIO

* set configurazione_patch[0].path = path_batch_appio
* set configurazione_patch[0].value = configurazione_batchAppIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then status 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.appIOBatch == configurazione_batchAppIO

* set configurazione_patch[0].path = path_avvisatura_appio
* set configurazione_patch[0].value = configurazione_avvisaturaAppIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then status 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.avvisaturaAppIO == configurazione_avvisaturaAppIO

# abilitazione invio notifica appIO nel tipoPendenzaDominio

Given url backofficeBasicBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, pagaTerzi: true, abilitato : true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set tipoPendenzaDominio_appIO.avvisaturaAppIO.apiKey = tipoPendenzaDominio_appIO_apiKey

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio_appIO
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaRinnovo
* set pendenzaPut.soggettoPagatore = debitoreCensitoAbilitatoAppIO

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' , UUID: '#notnull'}

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

# eseguo il pagamento

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
* set pagamentoPost.urlRitorno = "https://localhost/"


Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* configure followRedirects = false

Given url ndpsym_psp_url
And path '/eseguiPagamento'
And param idSession = response.idSession
And param idDominio = idDominio
And param codice = "R01"
And param riversamentoCumulativo = "0" 
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 302

# aspetto spedizione notifica 

* call sleep(1000)

* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

* call sleep(20000)

* call read('classpath:utils/govpay-op-spedizione-notifiche-appIO.feature')

# Controllo Eventi salvati nel Giornale

* call sleep(20000)

* def codiceFiscaleDebitore = pendenzaPut.soggettoPagatore.identificativo
* call read('classpath:utils/appio-verifica-getprofile.feature')

* call sleep(3000)

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param tipoEvento = 'getProfile'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#ignore",
	"ccp":"#ignore",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"componente": "API_BACKEND_IO",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getProfile",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/profiles/' + codiceFiscaleDebitore


Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param tipoEvento = 'submitMessageforUserWithFiscalCodeInBodyRicevuta'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#ignore",
	"ccp":"#ignore",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"componente": "API_BACKEND_IO",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "submitMessageforUserWithFiscalCodeInBodyRicevuta",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "201",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 201,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/messages'


@appio-ricevuta-2
Scenario: Notifica pagamento non avvenuto

# Svuoto Cache Simulatore AppIO
Given url appio_api_url
And path 'resetCacheAppIO'
When method get
Then status 200

# Configurazione AppIO

* set configurazione_patch[0].path = path_batch_appio
* set configurazione_patch[0].value = configurazione_batchAppIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then status 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.appIOBatch == configurazione_batchAppIO

* set configurazione_patch[0].path = path_avvisatura_appio
* set configurazione_patch[0].value = configurazione_avvisaturaAppIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then status 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.avvisaturaAppIO == configurazione_avvisaturaAppIO

# abilitazione invio notifica appIO nel tipoPendenzaDominio

Given url backofficeBasicBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, pagaTerzi: true, abilitato : true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set tipoPendenzaDominio_appIO.avvisaturaAppIO.apiKey = tipoPendenzaDominio_appIO_apiKey

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio_appIO
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaRinnovo
* set pendenzaPut.soggettoPagatore = debitoreCensitoAbilitatoAppIO

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' , UUID: '#notnull'}

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

# eseguo il pagamento

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
* set pagamentoPost.urlRitorno = "https://localhost/"


Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* configure followRedirects = false

Given url ndpsym_psp_url
And path '/eseguiPagamento'
And param idSession = response.idSession
And param idDominio = idDominio
And param codice = "R02"
And param riversamentoCumulativo = "0"
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 302

# aspetto spedizione notifica 

* call sleep(1000)

* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

* call sleep(20000)

* call read('classpath:utils/govpay-op-spedizione-notifiche-appIO.feature')

# Controllo Eventi salvati nel Giornale

* call sleep(20000)

* def codiceFiscaleDebitore = pendenzaPut.soggettoPagatore.identificativo
* call read('classpath:utils/appio-verifica-getprofile.feature')

* call sleep(3000)

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param tipoEvento = 'getProfile'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#ignore",
	"ccp":"#ignore",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"componente": "API_BACKEND_IO",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getProfile",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/profiles/' + codiceFiscaleDebitore


Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param tipoEvento = 'submitMessageforUserWithFiscalCodeInBodyRicevuta'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 0,
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

