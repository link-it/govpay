Feature: Attivazione o Verifica RPT con data scadenza decorsa

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')

* def faultBean =
"""
{
	faultCode: "PAA_PAGAMENTO_SCADUTO",
	faultString: '#notnull',
	id: "#(idDominio)",
	description: '#notnull',
	serial: '##null'
}
"""
* def esitoAttivaRPT = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')
* configure followRedirects = false

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Scenario: Attivazione RPT pendenza precaricata con data validita decorsa

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataValidita = '1999-12-31'

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoAttivaRPT

Scenario: Attivazione RPT pendenza precaricata con data validita decorsa e aggiornata tramite le API di verifica

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataValidita = '1999-12-31'

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

* set pendenzaPut.importo = pendenzaPut.importo + 10
* set pendenzaPut.voci[0].importo = pendenzaPut.voci[0].importo + 10
# * set pendenzaPut.causale = pendenzaPut.causale + ' con importo maggiorato' 
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoAttivaRPT


Scenario: Attivazione RPT pendenza precaricata con data validita decorsa e aggiornata tramite le API di verifica, ma con applicazione che non ha diritti sulla pendenza

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataValidita = '1999-12-31'

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

* set pendenzaPut.importo = pendenzaPut.importo + 10
* set pendenzaPut.voci[0].importo = pendenzaPut.voci[0].importo + 10
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo

# Ripristino gli importi originali per il check finale dell'esitoAttiva che restituira' la pendenza con gli importi non aggiornati.
* set pendenzaPut.importo = pendenzaPut.importo - 10
* set pendenzaPut.voci[0].importo = pendenzaPut.voci[0].importo - 10

# modifica diritti applicazione gestore

* def applicazione = read('classpath:test/api/pendenza/v1/pendenze/get/msg/applicazione_auth.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

* set applicazione.tipiPendenza = []

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoAttivaRPT

