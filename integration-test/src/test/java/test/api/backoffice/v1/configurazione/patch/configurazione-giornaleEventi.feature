Feature: Configurazione giornaleEventi

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def patchRequest = 
"""
[
  {
    "op": "REPLACE",
    "path": "/giornaleEventi",
    "value": {
    	"interfacce": {
				"apiEnte": {
					"letture": {
						"log": "sempre",
						"dump": "sempre"
					},
					"scritture": {
						"log": "sempre",
						"dump": "sempre"
					}
				},
				"apiPagamento": {
					"letture": {
						"log": "sempre",
						"dump": "sempre"
					},
					"scritture": {
						"log": "sempre",
						"dump": "sempre"
					}
				},
				"apiRagioneria": {
					"letture": {
						"log": "sempre",
						"dump": "sempre"
					},
					"scritture": {
						"log": "sempre",
						"dump": "sempre"
					}
				},
				"apiBackoffice": {
					"letture": {
						"log": "sempre",
						"dump": "sempre"
					},
					"scritture": {
						"log": "sempre",
						"dump": "sempre"
					}
				},
				"apiPagoPA": {
					"letture": {
						"log": "sempre",
						"dump": "sempre"
					},
					"scritture": {
						"log": "sempre",
						"dump": "sempre"
					}
				},
				"apiPendenze": {
					"letture": {
						"log": "sempre",
						"dump": "sempre"
					},
					"scritture": {
						"log": "sempre",
						"dump": "sempre"
					}
				}
			}
		}
  }
]
"""

Scenario Outline: Modifica della configurazione giornaleEventi (<field>)


* set patchRequest[0].value.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.giornaleEventi.<field> == checkValue

Examples:
| field | value | 
| interfacce.apiEnte.letture.log | "sempre" |
| interfacce.apiEnte.letture.log | "mai" |
| interfacce.apiEnte.letture.log | "solo errore" |
| interfacce.apiEnte.letture.dump | "sempre" |
| interfacce.apiEnte.letture.dump | "mai" |
| interfacce.apiEnte.letture.dump | "solo errore" |
| interfacce.apiEnte.scritture.log | "sempre" |
| interfacce.apiEnte.scritture.log | "mai" |
| interfacce.apiEnte.scritture.log | "solo errore" |
| interfacce.apiEnte.scritture.dump | "sempre" |
| interfacce.apiEnte.scritture.dump | "mai" |
| interfacce.apiEnte.scritture.dump | "solo errore" |
| interfacce.apiPagamento.letture.log | "sempre" |
| interfacce.apiPagamento.letture.log | "mai" |
| interfacce.apiPagamento.letture.log | "solo errore" |
| interfacce.apiPagamento.letture.dump | "sempre" |
| interfacce.apiPagamento.letture.dump | "mai" |
| interfacce.apiPagamento.letture.dump | "solo errore" |
| interfacce.apiPagamento.scritture.log | "sempre" |
| interfacce.apiPagamento.scritture.log | "mai" |
| interfacce.apiPagamento.scritture.log | "solo errore" |
| interfacce.apiPagamento.scritture.dump | "sempre" |
| interfacce.apiPagamento.scritture.dump | "mai" |
| interfacce.apiPagamento.scritture.dump | "solo errore" |
| interfacce.apiRagioneria.letture.log | "sempre" |
| interfacce.apiRagioneria.letture.log | "mai" |
| interfacce.apiRagioneria.letture.log | "solo errore" |
| interfacce.apiRagioneria.letture.dump | "sempre" |
| interfacce.apiRagioneria.letture.dump | "mai" |
| interfacce.apiRagioneria.letture.dump | "solo errore" |
| interfacce.apiRagioneria.scritture.log | "sempre" |
| interfacce.apiRagioneria.scritture.log | "mai" |
| interfacce.apiRagioneria.scritture.log | "solo errore" |
| interfacce.apiRagioneria.scritture.dump | "sempre" |
| interfacce.apiRagioneria.scritture.dump | "mai" |
| interfacce.apiRagioneria.scritture.dump | "solo errore" |
| interfacce.apiPendenze.letture.log | "sempre" |
| interfacce.apiPendenze.letture.log | "mai" |
| interfacce.apiPendenze.letture.log | "solo errore" |
| interfacce.apiPendenze.letture.dump | "sempre" |
| interfacce.apiPendenze.letture.dump | "mai" |
| interfacce.apiPendenze.letture.dump | "solo errore" |
| interfacce.apiPendenze.scritture.log | "sempre" |
| interfacce.apiPendenze.scritture.log | "mai" |
| interfacce.apiPendenze.scritture.log | "solo errore" |
| interfacce.apiPendenze.scritture.dump | "sempre" |
| interfacce.apiPendenze.scritture.dump | "mai" |
| interfacce.apiPendenze.scritture.dump | "solo errore" |
| interfacce.apiPagoPA.letture.log | "sempre" |
| interfacce.apiPagoPA.letture.log | "mai" |
| interfacce.apiPagoPA.letture.log | "solo errore" |
| interfacce.apiPagoPA.letture.dump | "sempre" |
| interfacce.apiPagoPA.letture.dump | "mai" |
| interfacce.apiPagoPA.letture.dump | "solo errore" |
| interfacce.apiPagoPA.scritture.log | "sempre" |
| interfacce.apiPagoPA.scritture.log | "mai" |
| interfacce.apiPagoPA.scritture.log | "solo errore" |
| interfacce.apiPagoPA.scritture.dump | "sempre" |
| interfacce.apiPagoPA.scritture.dump | "mai" |
| interfacce.apiPagoPA.scritture.dump | "solo errore" |


Scenario Outline: Errore sintassi della configurazione giornaleEventi (<field>)

* set patchRequest[0].value.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '<fieldName>'

Examples:
| field | fieldName | value | 
| interfacce.apiEnte.letture.log | log | 'xxxx' |
| interfacce.apiEnte.letture.dump | dump | 'xxxx' |
| interfacce.apiEnte.letture | letture | null |
| interfacce.apiEnte.scritture.log | log | 'xxxx' |
| interfacce.apiEnte.scritture.dump | dump | 'xxxx' |
| interfacce.apiEnte.scritture | scritture | null |
| interfacce.apiPagamento.letture.log | log | 'xxxx' |
| interfacce.apiPagamento.letture.dump | dump | 'xxxx' |
| interfacce.apiPagamento.letture | letture | null |
| interfacce.apiPagamento.scritture.log | log | 'xxxx' |
| interfacce.apiPagamento.scritture.dump | dump | 'xxxx' |
| interfacce.apiPagamento.scritture | scritture | null |
| interfacce.apiRagioneria.letture.log | log | 'xxxx' |
| interfacce.apiRagioneria.letture.dump | dump | 'xxxx' |
| interfacce.apiRagioneria.letture | letture | null |
| interfacce.apiRagioneria.scritture.log | log | 'xxxx' |
| interfacce.apiRagioneria.scritture.dump | dump | 'xxxx' |
| interfacce.apiRagioneria.scritture | scritture | null |
| interfacce.apiBackoffice.letture.log | log | 'xxxx' |
| interfacce.apiBackoffice.letture.dump | dump | 'xxxx' |
| interfacce.apiBackoffice.letture | letture | null |
| interfacce.apiBackoffice.scritture.log | log | 'xxxx' |
| interfacce.apiBackoffice.scritture.dump | dump | 'xxxx' |
| interfacce.apiBackoffice.scritture | scritture | null |
| interfacce.apiPagoPA.letture.log | log | 'xxxx' |
| interfacce.apiPagoPA.letture.dump | dump | 'xxxx' |
| interfacce.apiPagoPA.letture | letture | null |
| interfacce.apiPagoPA.scritture.log | log | 'xxxx' |
| interfacce.apiPagoPA.scritture.dump | dump | 'xxxx' |
| interfacce.apiPagoPA.scritture | scritture | null |
| interfacce.apiPendenze.letture.log | log | 'xxxx' |
| interfacce.apiPendenze.letture.dump | dump | 'xxxx' |
| interfacce.apiPendenze.letture | letture | null |
| interfacce.apiPendenze.scritture.log | log | 'xxxx' |
| interfacce.apiPendenze.scritture.dump | dump | 'xxxx' |
| interfacce.apiPendenze.scritture | scritture | null |
| interfacce.prova | prova | 'xxxx' |



