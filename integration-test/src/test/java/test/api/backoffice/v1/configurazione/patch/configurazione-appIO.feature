Feature: Validazione sintattica domini

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* def configurazione_patch = 
"""
[
  {
    "op": "REPLACE",
    "path": "/appIO",
    "value": null
  }
]
"""

* def configurazione_appIO = 
"""
{
	"abilitato": true, 
	"url": "http://localhost/",
	"message": {
		"timeToLive": 3600,
		"tipo": "freemarker",
		"subject": "string",
		"body": "string"
	}
}
"""

Scenario Outline: Configurazione appIO: <path> = <value>


* def checkValue = <value> != null ? <value> : '#notpresent'
* set configurazione_appIO.<path> = <value>
* set configurazione_patch[0].value = configurazione_appIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then status 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.appIO.<path> == checkValue

Examples:
| path | value | 
| abilitato | false |
| url | "bblablabla" |
| message.timeToLive | 3601 |
| message.timeToLive | 604799 |
| message.timeToLive | null |
| message.tipo | "freemarker" |
| message.subject | "blablabla" |
| message.body | "blablabla" |

 
