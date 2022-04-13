Feature: Censimento domini con connettore hyperSicAPKappa

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-hyperSicAPKappa.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario: Aggiunta di un dominio con servizio hyperSicAPKappa

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di un servizio hyperSicAPKappa di un dominio con connettore di tipo email (<field>)

* set dominio.servizioHyperSicAPKappa.<field> = <value>
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
And match response.servizioHyperSicAPKappa.<field> == checkValue

Examples:
| field | value | retValue | 
| abilitato | false | false |
| versioneCsv | '1.0' | '1.0' |
| emailIndirizzi | ['pec2@creditore.it'] | ['pec2@creditore.it'] |
| emailIndirizzi | ['pec2@creditore.it' , 'pec3@creditore.it' ] | ['pec2@creditore.it' , 'pec3@creditore.it' ] |
| emailSubject | '[Govpay] Export pagamenti Secim tipo pendenza #(codEntrataSegreteria)' | '[Govpay] Export pagamenti Secim tipo pendenza #(codEntrataSegreteria)' |
| tipiPendenza | [ '#(codEntrataSegreteria)' ] | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |
| tipiPendenza | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |
| emailAllegato | false | false |
| intervalloCreazioneTracciato | 1 | 1 |
| intervalloCreazioneTracciato | 12 | 12 |
| intervalloCreazioneTracciato | 1.2 | 1 |
| intervalloCreazioneTracciato | 1.7 | 1 |


Scenario Outline: Modifica di un connettore hyperSicAPKappa di un dominio con connettore di tipo file system (<field>)

* set dominio.servizioHyperSicAPKappa.tipoConnettore = 'FILESYSTEM'
* set dominio.servizioHyperSicAPKappa.fileSystemPath = '/tmp/'
* set dominio.servizioHyperSicAPKappa.<field> = <value>
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
And match response.servizioHyperSicAPKappa.<field> == checkValue

Examples:
| field | value | retValue | 
| abilitato | false | false |
| versioneCsv | '1.0' | '1.0' |
| fileSystemPath | '/var/' | '/var/' |
| tipiPendenza | [ '#(codEntrataSegreteria)' ] | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |
| tipiPendenza | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |
| intervalloCreazioneTracciato | 1 | 1 |
| intervalloCreazioneTracciato | 12 | 12 |


Scenario Outline: Modifica di un servizio hyperSicAPKappa di un dominio con connettore di tipo email <field> non valida

* set dominio.servizioHyperSicAPKappa.<fieldRequest> = <fieldValue>

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
| versioneCsv | fieldRequest | null | 'versioneCsv' |
| emailIndirizzi | emailIndirizzi | null | 'emailIndirizzi' |
| emailIndirizzi | emailIndirizzi | ['mail@errata@it'] | 'emailIndirizzi' |
| tipiPendenza | tipiPendenza | null | 'tipiPendenza' |
| tipiPendenza | tipiPendenza | [ '#(loremIpsum)' ] | 'idTipoPendenza' |
| emailAllegato | emailAllegato | null | 'emailAllegato' |
| intervalloCreazioneTracciato | intervalloCreazioneTracciato | null | 'intervalloCreazioneTracciato' |
| intervalloCreazioneTracciato | intervalloCreazioneTracciato | 0 | 'intervalloCreazioneTracciato' |


Scenario Outline: Modifica di un servizio hyperSicAPKappa di un dominio con connettore di tipo email <field> non valida

* set dominio.servizioHyperSicAPKappa.emailAllegato = false
* set dominio.servizioHyperSicAPKappa.<fieldRequest> = <fieldValue>

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


Scenario Outline: Modifica di un servizio hyperSicAPKappa di un dominio con connettore di tipo file system <field> non valida

* set dominio.servizioHyperSicAPKappa.tipoConnettore = 'FILESYSTEM'
* set dominio.servizioHyperSicAPKappa.fileSystemPath = '/tmp/'
* set dominio.servizioHyperSicAPKappa.<fieldRequest> = <fieldValue>

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
| versioneCsv | fieldRequest | null | 'versioneCsv' |
| fileSystemPath | fileSystemPath | null | 'fileSystemPath' |
| tipiPendenza | tipiPendenza | null | 'tipiPendenza' |
| tipiPendenza | tipiPendenza | [ '#(loremIpsum)' ] | 'idTipoPendenza' |
| intervalloCreazioneTracciato | intervalloCreazioneTracciato | null | 'intervalloCreazioneTracciato' |
| intervalloCreazioneTracciato | intervalloCreazioneTracciato | 0 | 'intervalloCreazioneTracciato' |

