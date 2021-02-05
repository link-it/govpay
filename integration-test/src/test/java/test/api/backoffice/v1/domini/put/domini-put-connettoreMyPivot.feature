Feature: Censimento domini con connettore mypivot

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-mypivot.json')

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


Scenario Outline: Modifica di un connettore mypivot di un dominio con connettore di tipo web service (<field>)

* set dominio.servizioMyPivot.tipoConnettore = 'WEBSERVICE'
* set dominio.servizioMyPivot.webServiceUrl = 'http://localhost:8080/connettoreMyPivot'
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
| tipiPendenza | null |
| tipiPendenza | [ { 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'} ] |
| webServiceAuth | { username: 'usr', password: 'pwd' } |
| webServiceAuth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} | 
| webServiceAuth | { tipo: 'Server', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} | 


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
| codiceIPA | fieldRequest | null | 'codiceIPA' |
| versioneCsv | fieldRequest | null | 'versioneCsv' |
| emailIndirizzo | emailIndirizzo | null | 'emailIndirizzo' |


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
| codiceIPA | fieldRequest | null | 'codiceIPA' |
| versioneCsv | fieldRequest | null | 'versioneCsv' |
| fileSystemPath | fileSystemPath | null | 'fileSystemPath' |


Scenario Outline: Modifica di un servizio mypivot di un dominio con connettore di tipo web service <field> non valida

* set dominio.servizioMyPivot.tipoConnettore = 'WEBSERVICE'
* set dominio.servizioMyPivot.webServiceUrl = 'http://localhost:8080/connettoreMyPivot'
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
| codiceIPA | fieldRequest | null | 'codiceIPA' |
| versioneCsv | fieldRequest | null | 'versioneCsv' |
| webServiceUrl | webServiceUrl | null | 'webServiceUrl' |
| webServiceUrl | webServiceUrl | 'xxxx' | 'URL' |
| webServiceAuth | webServiceAuth | { } | 'tipo' |
| webServiceAuth | webServiceAuth | { username: null, password: 'pwd' } | 'username' |
| webServiceAuth | webServiceAuth | { username: 'usr', password: null } | 'password' |
| webServiceAuth | webServiceAuth | { tipo: null, ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} | 'tipo' |
| webServiceAuth | webServiceAuth | { tipo: 'xxx', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} | 'tipo' |
| webServiceAuth | webServiceAuth | { tipo: 'Client', ksLocation: null, ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} |  'ksLocation' |
| webServiceAuth | webServiceAuth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} |  'ksPassword' |
| webServiceAuth | webServiceAuth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd'	} |  'tsLocation' |
| webServiceAuth | webServiceAuth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null	} |  'tsPassword' |
| webServiceAuth | webServiceAuth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd'	} |  'tsLocation' |
| webServiceAuth | webServiceAuth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null	} |  'tsPassword' |


