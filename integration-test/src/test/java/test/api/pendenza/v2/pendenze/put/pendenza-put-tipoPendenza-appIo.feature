Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
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
* def tipoPendenzaDominio_appIO_apiKeyErrata = 'API_KEY_ERRATA'
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
		"promemoriaRicevuta": null,
		"promemoriaScadenza": null
  }
}
"""

* set configurazione_avvisaturaAppIO.promemoriaAvviso.oggetto = encodeBase64InputStream(read('classpath:configurazione/v1/msg/appio-subject-freemarker.ftl'))
* set configurazione_avvisaturaAppIO.promemoriaAvviso.messaggio = encodeBase64InputStream(read('classpath:configurazione/v1/msg/appio-body-freemarker.ftl'))

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

Scenario: Caricamento pendenza dovuta con spedizione notifica AppIO, ApiKey non valida.

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
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set tipoPendenzaDominio_appIO.avvisaturaAppIO.apiKey = tipoPendenzaDominio_appIO_apiKeyErrata

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio_appIO
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaRinnovo
* set pendenzaPut.soggettoPagatore = debitoreNoAuthAppIO

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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

# Controllo Eventi salvati nel Giornale

* call sleep(3000)

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
	"esito": "FAIL",
	"sottotipoEsito": "401",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 401,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/profiles/' + codiceFiscaleDebitore

Scenario: Caricamento pendenza dovuta con spedizione notifica AppIO, Debitore non registrato su AppIO

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
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
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
* set pendenzaPut.soggettoPagatore = debitoreNonCensitoAppIO

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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

# Controllo Eventi salvati nel Giornale

* call sleep(3000)

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
	"esito": "KO",
	"sottotipoEsito": "404",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 404,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/profiles/' + codiceFiscaleDebitore

Scenario: Caricamento pendenza dovuta con spedizione notifica AppIO, Debitore registrato su AppIO ma non abilitato all'invio della notifica

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
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
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
* set pendenzaPut.soggettoPagatore = debitoreCensitoNonAbilitatoAppIO

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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

# Controllo Eventi salvati nel Giornale

* call sleep(3000)

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
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/profiles/' + codiceFiscaleDebitore

Scenario: Caricamento pendenza dovuta con spedizione notifica AppIO, Debitore registrato su AppIO abilitato all'invio della notifica

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
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
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
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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

# Controllo Eventi salvati nel Giornale

* call sleep(3000)

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
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
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
And param tipoEvento = 'submitMessageforUserWithFiscalCodeInBody'
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
	"tipoEvento": "submitMessageforUserWithFiscalCodeInBody",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "201",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 201,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/messages'


Scenario: Caricamento pendenza dovuta con spedizione notifica AppIO, Debitore registrato su AppIO abilitato all'invio della notifica, Messaggio malformato

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
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
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
* set pendenzaPut.soggettoPagatore = debitoreCensitoAppIOErrore400

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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

# Controllo Eventi salvati nel Giornale

* call sleep(3000)

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
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
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
And param tipoEvento = 'submitMessageforUserWithFiscalCodeInBody'
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
	"tipoEvento": "submitMessageforUserWithFiscalCodeInBody",
	"sottotipoEvento": "##null",
	"esito": "FAIL",
	"sottotipoEsito": "400",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 400,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/messages'

Scenario: Caricamento pendenza dovuta con spedizione notifica AppIO, Debitore registrato su AppIO abilitato all'invio della notifica, Errore interno AppIO

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
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
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
* set pendenzaPut.soggettoPagatore = debitoreCensitoAppIOErrore500

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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

# Controllo Eventi salvati nel Giornale

* call sleep(3000)

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
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
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
And param tipoEvento = 'submitMessageforUserWithFiscalCodeInBody'
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
	"tipoEvento": "submitMessageforUserWithFiscalCodeInBody",
	"sottotipoEvento": "##null",
	"esito": "FAIL",
	"sottotipoEsito": "500",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "#notnull",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 500,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == appio_api_url + '/messages'


Scenario: Caricamento pendenza dovuta con spedizione notifica AppIO abilitata specificando pero' il parametro che disabilita l'invio per la pendenza caricata

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
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
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
* set pendenzaPut.soggettoPagatore = debitoreCensitoAppIOErrore500

# Caricamento Pendenza e Controllo caricamento

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And param data_avvisatura = 'MAI'
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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

# Controllo Eventi salvati nel Giornale

* call sleep(3000)

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
	numRisultati: 0,
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: []
}
"""

