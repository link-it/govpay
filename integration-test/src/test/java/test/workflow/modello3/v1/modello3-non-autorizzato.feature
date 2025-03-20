Feature: Richiesta di verifica o attivazione autenticata, ma non autorizzata

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* set intermediario.principalPagoPa = 'xxx'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def faultBean = 
"""
	{
		"faultCode":"PAA_SYSTEM_ERROR",
		"faultString":"Errore generico.",
		"id":"#(idDominio)",
		"description":'#("Autorizzazione fallita: principal fornito (" + ndpsym_user + ") non valido per l\'intermediario ("+ idIntermediario +").")',
		"serial":'##null'
	}
"""

Scenario: Verifica pagamento

* def numeroAvviso = '000000000000000000'
* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == faultBean

Scenario: Attiva pagamento

* def numeroAvviso = '000000000000000000'
* def iuv = '000000000000000'
* def ccp = '1'
* def importo = 10.01
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean
