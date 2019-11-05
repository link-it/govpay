Feature: Validazione sintattica domini

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def patch = 
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
  }
]
"""

Scenario Outline: Modifica della configurazione batch (<field>)


* set patch.value.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request batch
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
| allegatoPdf | false |
| allegatoPdf | true |
