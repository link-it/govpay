Feature: Invio Promemoria pendenza via mail

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def tipoPendenzaPromemoria = 'PROMEMORIA'

Given url backofficeBaseurl
And path 'tipiPendenza', tipoPendenzaPromemoria
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Pendenza Promemoria' , codificaIUV: null, pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  portaleBackoffice: null,
  avvisaturaMail: {
	  promemoriaAvviso : {
	  	abilitato: true,
	  	tipo: "freemarker",
	  	oggetto : "Pagamento Pendenza ",
	  	messaggio: "Pagamento Pendenza ",
	  	allegaPdf: false
	  },
	  promemoriaRicevuta : {
	  	abilitato: true,
	  	allegaPdf: true
	  }
  }
}
"""       
* set tipoPendenzaDominio.avvisaturaMail.promemoriaAvviso.oggetto = encodeBase64InputStream(read('msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))
* set tipoPendenzaDominio.avvisaturaMail.promemoriaAvviso.messaggio = encodeBase64InputStream(read('msg/tipoPendenza-promemoria-messaggio-freemarker.ftl'))   

* set tipoPendenzaDominio.avvisaturaMail.promemoriaAvviso.allegaPdf = true

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaPromemoria
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Imposto mail server

* def patchRequest = 
"""
[
  {
    "op": "REPLACE",
    "path": "/mailBatch",
    "value": {
			"abilitato":true,
		   "mailserver":{
		      "host":"localhost",
		      "port":1025,
		      "username":"govpay",
		      "password":"123456",
		      "from":"noreply.govpay@link.it",
		      "readTimeout":120000,
		      "connectionTimeout":10000,
		      "sslConfig":{
		         "abilitato":false
		      },
		      "startTls":false
		   }	
		}
  }
]
"""

Given url backofficeBaseurl
And path 'configurazioni'
And headers gpAdminBasicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201


* callonce read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"
* def versionePagamento = 2

* configure followRedirects = false
* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')

Scenario: Pagamento pendenza caricata con invio Promemoria con avviso di pagamento

# svuota cache messaggi
* call read('classpath:utils/smtpsym-cancella-messaggi.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaPromemoria
* set pendenzaPut.soggettoPagatore.email = "pintori@link.it"

Given url backofficeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def responsePut = response

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

# sleep
* call sleep(60000)

# elaborazione promemoria
* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

# sleep
* call sleep(60000)

# elaborazione promemoria
* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

# spedizione promemoria
* call read('classpath:utils/govpay-op-spedizione-promemoria.feature')

# svuota cache messaggi
* call read('classpath:utils/smtpsym-leggi-messaggi.feature')
* assert response.items.length > 0
And match response.items[0].Content.Headers.To[0] == pendenzaPut.soggettoPagatore.email

# svuota cache messaggi
* call read('classpath:utils/smtpsym-cancella-messaggi.feature')

# pagamento
* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
# * match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
#	* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
# * match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')

* def dataRptEnd2 = getDateTime()

* configure retry = { count: 25, interval: 20000 }

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
And retry until response.numRisultati == 1
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

# sleep
* call sleep(60000)

# elaborazione promemoria
* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

# sleep
* call sleep(60000)

# elaborazione promemoria
* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

# spedizione promemoria
* call read('classpath:utils/govpay-op-spedizione-promemoria.feature')

# svuota cache messaggi
* call read('classpath:utils/smtpsym-leggi-messaggi.feature')
* assert response.items.length > 0
And match response.items[0].Content.Headers.To[0] == pendenzaPut.soggettoPagatore.email

# svuota cache messaggi
* call read('classpath:utils/smtpsym-cancella-messaggi.feature')


