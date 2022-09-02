Feature: Censimento domini con connettore NetPay

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-netpay.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario: Aggiunta di un dominio con servizio NetPay

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di un servizio NetPay di un dominio (<field>)

* set dominio.servizioNetPay.<field> = <value>
* def checkValue = <retValue> != null ? <retValue> : '#notpresent'

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioNetPay.<field> == checkValue

Examples:
| field | value | retValue | 
| abilitato | false | false |
| url | 'http://prova.it' | 'http://prova.it' |
| versioneApi | 'SOAP v1' | 'SOAP v1' |
| url | 'http://prova.it' | 'http://prova.it' |
| principal | 'username' | 'username' |
| username | 'username' | 'username' |
| password | 'username' | 'username' |
| ruolo | 'username' | 'username' |
| company | 'username' | 'username' |

Scenario Outline: Modifica di un servizio NetPay di un dominio <field> non valida

* set dominio.servizioNetPay.<fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| abilitato | abilitato | null | 'abilitato' |
| versioneApi | versioneApi | 'xxxx' | 'versioneApi' |
| url | url | null | 'url' |
| url | url | 'xxxx' | 'url' |
| principal | principal | null | 'principal' |
| principal | principal | [ '#(loremIpsum)' ] | 'principal' |
| username | username | null | 'username' |
| username | username | [ '#(loremIpsum)' ] | 'username' |
| password | password | null | 'password' |
| password | password | [ '#(loremIpsum)' ] | 'password' |
| ruolo | ruolo | null | 'ruolo' |
| ruolo | ruolo | [ '#(loremIpsum)' ] | 'ruolo' |
| company | company | null | 'company' |
| company | company | [ '#(loremIpsum)' ] | 'company' |








