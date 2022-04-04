Feature: Aggiornamento avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def versionePagamento = 2


Scenario: Caricamento idDominio disabilitato

* set dominio.abilitato = false

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'DOM_001',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Dominio (" + idDominio + ") disabilitato")'
}
"""

Scenario: Pagamento pendenza modello 3 di un idDominio disabilitato

* set dominio.abilitato = true

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

* set dominio.abilitato = false

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')


Scenario: Pagamento pendenza modello 1 di un idDominio disabilitato

* set dominio.abilitato = true

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

* set dominio.abilitato = false

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
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
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'DOM_001', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }

