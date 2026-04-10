Feature: Censimento domini con connettore maggioli JPPA

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-maggioliJPPA.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario: Aggiunta di un dominio con servizio govpay

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario: Salvataggio URL e credenziali con abilitato false

* def dominioDisabilitato = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-maggioliJPPA.json')
* set dominioDisabilitato.servizioMaggioliJPPA.abilitato = false
* set dominioDisabilitato.servizioMaggioliJPPA.url = 'http://test.disabilitato.it'
* set dominioDisabilitato.servizioMaggioliJPPA.auth = { username: 'user_disabled', password: 'pwd_disabled' }

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioDisabilitato
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioMaggioliJPPA.abilitato == false
And match response.servizioMaggioliJPPA.url == 'http://test.disabilitato.it'
And match response.servizioMaggioliJPPA.auth == { username: 'user_disabled', password: 'pwd_disabled' }

Scenario Outline: Modifica di un servizio govpay di un dominio con connettore di tipo email (<field>)

* set dominio.servizioMaggioliJPPA.<field> = <value>
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
And match response.servizioMaggioliJPPA.<field> == checkValue

Examples:
| field | value | retValue |
| abilitato | false | false |
| inviaTracciatoEsito | true | true |
| inviaTracciatoEsito | false | false |
| fileSystemPath | '/tmp/nuovoPath' | '/tmp/nuovoPath' |
| emailIndirizzi | ['pec2@creditore.it'] | ['pec2@creditore.it'] |
| emailIndirizzi | ['pec2@creditore.it' , 'pec3@creditore.it' ] | ['pec2@creditore.it' , 'pec3@creditore.it' ] |
| emailSubject | '[Govpay] Export pagamenti MaggioliJPPA' | '[Govpay] Export pagamenti MaggioliJPPA' |
| emailAllegato | false | false |
| downloadBaseUrl | 'http://download.it/tracciati' | 'http://download.it/tracciati' |
| url | 'http://nuovaurl.it' | 'http://nuovaurl.it' |
| auth | { username: 'usr2', password: 'pwd2' } | { username: 'usr2', password: 'pwd2' } |

Scenario Outline: Modifica di un servizio govpay di un dominio con connettore di tipo email <field> non valida

* set dominio.servizioMaggioliJPPA.<fieldRequest> = <fieldValue>

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
| inviaTracciatoEsito | inviaTracciatoEsito | null | 'inviaTracciatoEsito' |
| fileSystemPath | fileSystemPath | null | 'fileSystemPath' |
| emailIndirizzi | emailIndirizzi | null | 'emailIndirizzi' |
| emailIndirizzi | emailIndirizzi | ['mail@errata@it'] | 'emailIndirizzi' |
| emailAllegato | emailAllegato | null | 'emailAllegato' |

Scenario Outline: Modifica di un servizio govpay di un dominio con connettore di tipo email <field> non valida

* set dominio.servizioMaggioliJPPA.emailAllegato = false
* set dominio.servizioMaggioliJPPA.<fieldRequest> = <fieldValue>

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
| downloadBaseUrl | downloadBaseUrl | null | 'downloadBaseUrl' |
| downloadBaseUrl | downloadBaseUrl | 'xxxx' | 'downloadBaseUrl' |


Scenario Outline: Modifica di un servizio govpay di un dominio con connettore di tipo email <field> non valida

* set dominio.servizioMaggioliJPPA.<fieldRequest> = <fieldValue>

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
| url | url | null | 'url' |
| url | url | 'xxxx' | 'url' |
| auth | auth | null | 'auth' |
| auth | auth | { } | 'username' |
| auth.username | auth | { username: null, password: 'pwd' } | 'username' |
| auth.username | auth | { password: 'pwd' } | 'username' |
| auth.password | auth | { username: 'usr', password: null } | 'password' |
| auth.password | auth | { username: 'usr' } | 'password' |
