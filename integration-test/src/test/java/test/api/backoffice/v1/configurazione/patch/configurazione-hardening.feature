Feature: Configurazione hardening

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/hardening",
      "value":{
         "abilitato":true,
         "captcha":{
            "serverURL":"https://www.google.com/recaptcha/api/siteverify",
            "siteKey":"siteKey",
            "secretKey":"secretKey",
            "soglia":0.7,
            "parametro":"gRecaptchaResponse",
            "denyOnFail":true,
            "connectionTimeout":5000,
            "readTimeout":5000
         }
      }
   }
]
"""

Scenario Outline: Modifica della configurazione hardening (<field>)

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
And match response.hardening.<field> == checkValue

Examples:
| field | value | 
| abilitato | true |
| abilitato | false |
| captcha.serverURL | "http://myhost/" |
| captcha.siteKey | "siteKey" |
| captcha.secretKey | "secretKey" |
| captcha.soglia | 0 |
| captcha.soglia | 1 |
| captcha.soglia | 0.5 |
| captcha.parametro | "parametro" |
| captcha.denyOnFail | true |
| captcha.denyOnFail | false |
| captcha.connectionTimeout | 0 |
| captcha.connectionTimeout | 2000 |
| captcha.readTimeout | 0 |
| captcha.readTimeout | 2000 |

Scenario Outline: Errore sintassi della configurazione hardening (<field>)

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
| abilitato | abilitato | 'xxx' |
| abilitato | abilitato | null |
| captcha.serverURL | serverURL | loremIpsum |
| captcha.serverURL | serverURL | null |
| captcha.siteKey | siteKey | null |
| captcha.secretKey | secretKey | null |
| captcha.soglia | soglia | 2 |
| captcha.soglia | soglia | true |
| captcha.soglia | soglia | 'soglia' |
| captcha.soglia | soglia | null |
| captcha.parametro | parametro | null |
| captcha.parametro | parametro | '/&$ #' |
| captcha.connectionTimeout | connectionTimeout | null |
| captcha.connectionTimeout | connectionTimeout | true |
| captcha.connectionTimeout | connectionTimeout | 'aaa' |
| captcha.connectionTimeout | connectionTimeout | loremIpsum |
| captcha.readTimeout | readTimeout | null |
| captcha.readTimeout | readTimeout | true |
| captcha.readTimeout | readTimeout | 'aaa' |
| captcha.readTimeout | readTimeout | loremIpsum |



