Feature: Attivazione o Verifica RPT con data scadenza decorsa

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'
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

Scenario: Attivazione RPT scaduta precaricata

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean

Scenario: Attivazione RPT scaduta non precaricato

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean


Scenario: Verifica RPT scaduta precaricata

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == faultBean


Scenario: Verifica RPT scaduta non precaricato

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == faultBean

Scenario: Attivazione RPT scaduta non precaricato

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso-scaduto.feature')
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean

Scenario: Verifica RPT scaduta non precaricato

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso-scaduto.feature')

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == faultBean
