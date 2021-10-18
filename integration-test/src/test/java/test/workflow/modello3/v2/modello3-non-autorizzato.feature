Feature: Richiesta di verifica o attivazione autenticata, ma non autorizzata

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* set intermediario.principalPagoPa = 'xxx'

* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')

* def versionePagamento = 2

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def faultBean = 
"""
	{
		"faultCode":"PAA_SYSTEM_ERROR",
		"faultString":"Errore generico.",
		"id":"#(idDominio)",
		"description":'#("Autorizzazione fallita: principal fornito (" + ndpsym_user + ") non valido per l\'intermediario ("+ idIntermediario +").")',
		"serial":'##null',
		"originalFaultCode":'##null',
		"originalFaultString":'##null',
		"originalDescription":'##null'
	}
"""

Scenario: Verifica pagamento

* def numeroAvviso = '000000000000000000'
* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == faultBean

Scenario: Attiva pagamento

* def numeroAvviso = '000000000000000000'
* def iuv = '000000000000000'
* def ccp = '1'
* def importo = 10.01
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.faultBean == faultBean

