Feature: Validazione sintattica stazioni

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediario.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediarioBasicAuth.json')
* def stazione = read('classpath:test/api/backoffice/intermediari/v1/put/msg/stazione.json')
* def idStazione = idIntermediario + '_01'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione
And headers basicAutenticationHeader
And request stazione
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| password | stazione.password | null |  'password' |
| password | stazione.password | '' |  'password' |
| password | stazione.password | loremIpsum |  'password' |
| abilitato | stazione.abilitato | null |  'abilitato' |
| abilitato | stazione.abilitato | 'zzz' |  'abilitato' |

Scenario: Identificativo stazione errato

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idIntermediario + '_999'
And headers basicAutenticationHeader
And request stazione
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
