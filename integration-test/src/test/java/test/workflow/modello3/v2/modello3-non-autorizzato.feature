Feature: Richiesta di verifica o attivazione autenticata, ma non autorizzata

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* set intermediario.principalPagoPa = 'xxx'

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')
* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')

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

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 2

* call read('classpath:utils/nodo-config-dominio-put.feature')

* call read('classpath:utils/nodo-config-stazione-put.feature')

* def numeroAvviso = '000000000000000000'
* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == faultBean

# ripristino dominio e stazione

* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* call read('classpath:utils/nodo-config-dominio-put.feature')

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')

* call read('classpath:utils/nodo-config-stazione-put.feature')

Scenario: Attiva pagamento

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 2

* call read('classpath:utils/nodo-config-dominio-put.feature')

* call read('classpath:utils/nodo-config-stazione-put.feature')

* def numeroAvviso = '000000000000000000'
* def iuv = '000000000000000'
* def ccp = '1'
* def importo = 10.01
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean

# ripristino dominio e stazione

* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* call read('classpath:utils/nodo-config-dominio-put.feature')

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')

* call read('classpath:utils/nodo-config-stazione-put.feature')
