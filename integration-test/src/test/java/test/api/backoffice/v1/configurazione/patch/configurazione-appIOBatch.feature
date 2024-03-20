Feature: Validazione sintattica appIOBatch

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

* def configurazione_patch = 
"""
[
  {
    "op": "REPLACE",
    "path": "/appIOBatch",
    "value": null
  }
]
"""

* def configurazione_appIO = 
"""
{
	"abilitato": true, 
	"url": "http://localhost/",
  "timeToLive": 10000
}
"""

Scenario Outline: Configurazione appIOBatch: <path> = <value>


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
And match response.appIOBatch.<path> == checkValue

Examples:
| path | value | 
| abilitato | false |
| url | "bblablabla" |
| timeToLive | 3601 |
| timeToLive | 604799 |
| timeToLive | null |


Scenario Outline: Errore sintassi della configurazione appIOBatch (<field>)

* def checkValue = <value> != null ? <value> : '#notpresent'
* set configurazione_appIO.<path> = <value>
* set configurazione_patch[0].value = configurazione_appIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '<fieldName>'

Examples:
| field | fieldName | value | 
| abilitato | abilitato | "aaaa" |
| abilitato | abilitato | null |
| url | url | loremIpsum |
| url | url | 'true ciao' |
| url | url | null |
| timeToLive | timeToLive | 3599 |
| timeToLive | timeToLive | 604801 |


Scenario Outline: Errore sintassi della configurazione appIOBatch (<field>)

* def checkValue = <value> != null ? <value> : '#notpresent'
* set configurazione_appIO.<path> = <value>
* set configurazione_patch[0].value = configurazione_appIO

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_patch
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <field>

Examples:
| path | value | field |
| auth | { } | 'tipo' |
| auth | { username: null, password: 'pwd' } | 'username' |
| auth | { username: 'usr', password: null } | 'password' |
| auth | { tipo: null, ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} | 'tipo' |
| auth | { tipo: 'xxx', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	, tsType: 'JKS', sslType: 'TLSv1.2'} | 'tipo' |
| auth | { tipo: 'Client', ksLocation: null, ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'ksLocation' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'ksPassword' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'tsLocation' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null, tsType: 'JKS', sslType: 'TLSv1.2'	} |  'tsPassword' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: null, sslType: 'TLSv1.2'	} |  'tsType' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'XXX', sslType: 'TLSv1.2'	} |  'tsType' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: null	} |  'sslType' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'XXX'	} |  'sslType' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: null, ksPKeyPasswd: 'ksPKeyPasswd'	} |  'ksType' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: 'XXX', ksPKeyPasswd: 'ksPKeyPasswd'	} |  'ksType' |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	, ksType: 'JKS', ksPKeyPasswd:  null } |  'ksPKeyPasswd' |
| auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'tsLocation' |
| auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null, tsType: 'JKS', sslType: 'TLSv1.2'	} |  'tsPassword' |
| auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: null, sslType: 'TLSv1.2'	} |  'tsType' |
| auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'XXX', sslType: 'TLSv1.2'	} |  'tsType' |
| auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: null	} |  'sslType' |
| auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'XXX'	} |  'sslType' |
| auth | { headerName: null, headerValue: 'govpay' } | 'headerName' |
| auth | { headerName: 'X-GOVPAY-auth', headerValue: null } | 'headerValue' |
| auth | { apiId: null, apiKey: 'govpay' } | 'apiId' |
| auth | { apiId: 'X-GOVPAY-auth', apiKey: null } | 'apiKey' |
| auth | { clientId: 'GOVPAY', clientSecret: null, urlTokenEndpoint: 'http://localhost:8080/token' } | 'clientSecret' |
| auth | { clientId: null, clientSecret: 'govpay', urlTokenEndpoint: 'http://localhost:8080/token' } | 'clientId' |
| auth | { clientId: 'GOVPAY', clientSecret: 'govpay', urlTokenEndpoint: null } | 'urlTokenEndpoint' |






 
