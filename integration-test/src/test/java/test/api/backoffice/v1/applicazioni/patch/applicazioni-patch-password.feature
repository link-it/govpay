Feature: Censimento applicazioni con definizione della password per l'autenticazione HTTP-Basic

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario: Aggiunta di una applicazione, test login, patch del campo password e login con le nuove credenziali

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 200

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/password",
      "value": null
   }
]
"""

* def nuovaPasswordA2A = 'Password2'
* set patchRequest[0].value = nuovaPasswordA2A

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 401

* def nuovoIdA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: nuovaPasswordA2A } )

Given url backofficeBaseurl
And path 'profilo'
And headers nuovoIdA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 200


Scenario: Reset della password tramite patch

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201

* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A , password: pwdA2A } )

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 200

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/password",
      "value": null
   }
]
"""

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200
And match response.password == false

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'profilo'
And headers idA2ABasicAutenticationHeader
When method get
Then assert responseStatus == 401


Scenario Outline: Controllo sintassi password operazione patch

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/password",
      "value": null
   }
]
"""

* set patchRequest[0].value = <value>

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>

Examples:
| value | fieldName |
| loremIpsum | 'password' |
| 'ABC' | 'password' |
| '123' | 'password' |
| 'abc   123' | 'password' |



