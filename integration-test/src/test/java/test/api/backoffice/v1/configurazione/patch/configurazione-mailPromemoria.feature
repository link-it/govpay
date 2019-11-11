Feature: Configurazione mailPromemoria

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
    "path": "/mailPromemoria",
    "value": {
				"tipo": "freemarker",
				"oggetto": "..base64 freemarker..",
				"messaggio": "..base64 freemarker..",
				"allegaPdf": true
		}
  }
]
"""

Scenario Outline: Modifica della configurazione mailPromemoria (<field>)


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
And match response.mailPromemoria.<field> == checkValue

Examples:
| field | value | 
| tipo | "freemarker" |
| oggetto | "aaa" |
| messaggio | "aaa" |
| allegaPdf | false |
| allegaPdf | true |

Scenario Outline: Errore sintassi della configurazione mailPromemoria (<field>)

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
| tipo | tipo | null |
| tipo | tipo | "aaa" |
| oggetto | oggetto | null |
| messaggio | messaggio | null |
| allegaPdf | allegaPdf | null |
| allegaPdf | allegaPdf | "aaa" |

