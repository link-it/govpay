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
* configure followRedirects = false

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Scenario: Attivazione RPT scaduta precaricata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo

* def riversamentoCumulativo = 'true'
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.faultBean == faultBean

Scenario: Attivazione RPT scaduta non precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo

* def riversamentoCumulativo = 'true'
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.faultBean == faultBean


Scenario: Verifica RPT scaduta precaricata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == faultBean


Scenario: Verifica RPT scaduta non precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == faultBean

Scenario: Attivazione RPT scaduta non precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso-scaduto.feature')
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo

* def riversamentoCumulativo = 'true'
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.faultBean == faultBean

Scenario: Verifica RPT scaduta non precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso-scaduto.feature')

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == faultBean
