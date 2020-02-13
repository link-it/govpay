Feature: Configurazione tracciatoCsv

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/tracciatoCsv",
      "value": {
        "tipo": "freemarker", 
   			"intestazione": "headers",	
        "richiesta": "..base64 freemarker..",
        "risposta": "..base64 freemarker.."
      }
   }
]
"""

Scenario Outline: Modifica della configurazione tracciatoCsv (<field>)

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
And match response.tracciatoCsv.<field> == checkValue

Examples:
| field | value | 
| tipo | "freemarker" |
| intestazione | "intestazione" |
| richiesta | "richiesta" |
| risposta | "risposta" |

Scenario Outline: Errore sintassi della configurazione tracciatoCsv (<field>)

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
| intestazione | intestazione | null |
| richiesta | richiesta | null |
| risposta | risposta | null |



