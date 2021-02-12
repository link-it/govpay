Feature: Censimento domini con connettore mypivot

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-mypivot.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario: Aggiunta di un dominio con servizio mypivot

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di un servizio mypivot di un dominio con connettore di tipo email (<field>)

* set dominio.servizioMyPivot.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

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
And match response.servizioMyPivot.<field> == checkValue

Examples:
| field | value | 
| abilitato | false|
| codiceIPA | 'IPA' |
| versioneCsv | '1.0' |
| emailIndirizzo | 'pec2@creditore.it' |
| emailServer.host | "smtp.myhost.it" |
| emailServer.port | 10 |
| emailServer.username | "xxxx" |
| emailServer.password | "xxxx" |
| emailServer.from | "from@xxx.org" |
| emailServer.readTimeout | 0 |
| emailServer.connectionTimeout | 0 |
| tipiPendenza | null |
| tipiPendenza | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |



Scenario Outline: Modifica di un connettore mypivot di un dominio con connettore di tipo file system (<field>)

* set dominio.servizioMyPivot.tipoConnettore = 'FILESYSTEM'
* set dominio.servizioMyPivot.fileSystemPath = '/tmp/'
* set dominio.servizioMyPivot.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

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
And match response.servizioMyPivot.<field> == checkValue

Examples:
| field | value | 
| abilitato | false|
| codiceIPA | 'IPA' |
| versioneCsv | '1.0' |
| fileSystemPath | '/var/' |
| tipiPendenza | null |
| tipiPendenza | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |


Scenario Outline: Modifica di un servizio mypivot di un dominio con connettore di tipo email <field> non valida

* set dominio.servizioMyPivot.<fieldRequest> = <fieldValue>

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
| codiceIPA | codiceIPA | null | 'codiceIPA' |
| versioneCsv | versioneCsv | null | 'versioneCsv' |
| emailIndirizzo | emailIndirizzo | null | 'emailIndirizzo' |
| emailServer | emailServer | 123 | 'emailServer' |
| emailServer | emailServer | "a" | 'emailServer' |
| emailServer.host | emailServer.host | loremIpsum | 'host' |
| emailServer.host | emailServer.host | 't rue' | 'host' |
| emailServer.host | emailServer.host | null | 'host' |
| emailServer.port | emailServer.port | null | 'port' |
| emailServer.port | emailServer.port | "aaa" | 'port' |
| emailServer.username | emailServer.username | null | 'username' |
| emailServer.username | emailServer.username | loremIpsum | 'username' |
| emailServer.password | emailServer.password | null | 'password' |
| emailServer.password | emailServer.password | loremIpsum | 'password' |
| emailServer.from | emailServer.from | null | 'from' |
| emailServer.from | emailServer.from | loremIpsum | 'from' |
| emailServer.readTimeout | emailServer.readTimeout | null | 'readTimeout' |
| emailServer.readTimeout | emailServer.readTimeout | "aaa" | 'readTimeout' |
| emailServer.connectionTimeout | emailServer.connectionTimeout | null | 'connectionTimeout' |
| emailServer.connectionTimeout | emailServer.connectionTimeout | "aaa" | 'connectionTimeout' |


Scenario Outline: Modifica di un servizio mypivot di un dominio con connettore di tipo file system <field> non valida

* set dominio.servizioMyPivot.tipoConnettore = 'FILESYSTEM'
* set dominio.servizioMyPivot.fileSystemPath = '/tmp/'
* set dominio.servizioMyPivot.<fieldRequest> = <fieldValue>

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
| codiceIPA | codiceIPA | null | 'codiceIPA' |
| versioneCsv | versioneCsv | null | 'versioneCsv' |
| fileSystemPath | fileSystemPath | null | 'fileSystemPath' |


