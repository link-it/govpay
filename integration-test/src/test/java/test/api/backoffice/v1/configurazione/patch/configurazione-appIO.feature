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
    "path": "appIO",
    "value": null
  }
]
"""

* def configurazione_appIO = 
"""
{
	"url": "http://localhost/",
	"message": {
		"ttl": 1,
		"subject": "string",
		"body": "string"
	}
}
"""

Scenario Outline: Configurazione appIO: <path> = <value>


* set configurazione_appIO.<path> = <value>
* set configurazione_patch.value = configurazione_appIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then status 200

Given url backofficeBaseurl
And path 'configurazioni'
When method get
Then status 200
And match configurazioni.appIO == configurazione_appIO

Examples:
| path | value | 
| url | "bblablabla" |
| ttl | 1000 |
| ttl | null |
| subject | "blablabla" |
| body | "blablabla" |

 
