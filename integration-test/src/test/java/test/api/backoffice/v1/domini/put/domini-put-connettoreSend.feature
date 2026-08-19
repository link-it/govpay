Feature: Censimento domini con connettore SEND

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-send.json')
* def dominioBasicAuth = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-send-basicAuth.json')
* def dominioClientAuth = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-send-clientAuth.json')
* def dominioServerAuth = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-send-serverAuth.json')
* def dominioHeaderAuth = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-send-headerAuth.json')
* def dominioApiKeyAuth = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-send-apiKeyAuth.json')
* def dominioOauth2Auth = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-send-oauth2Auth.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario: Aggiunta di un dominio con servizio SEND

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
And match response.servizioSend.url == dominio.servizioSend.url
And match response.servizioSend.subscriptionKey == dominio.servizioSend.subscriptionKey
And match response.servizioSend.abilitaGDE == dominio.servizioSend.abilitaGDE
And match response.servizioSend.auth == '#notpresent'

Scenario Outline: Modifica di un servizio SEND di un dominio (<field>)

* set dominio.servizioSend.<field> = <value>
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
And match response.servizioSend.<field> == checkValue

Examples:
| field           | value                    | retValue                 |
| url             | 'http://nuovaurl.it'     | 'http://nuovaurl.it'     |
| subscriptionKey | 'NUOVACHIAVE123'         | 'NUOVACHIAVE123'         |
| abilitaGDE      | true                     | true                     |
| abilitaGDE      | false                    | false                    |

Scenario: Modifica di un servizio SEND di un dominio con autenticazione basic

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioBasicAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioSend.auth == dominioBasicAuth.servizioSend.auth

Scenario: Modifica di un servizio SEND di un dominio con autenticazione client SSL

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioClientAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioSend.auth == dominioClientAuth.servizioSend.auth

Scenario: Modifica di un servizio SEND di un dominio con autenticazione server SSL

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioServerAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioSend.auth == dominioServerAuth.servizioSend.auth

Scenario: Modifica di un servizio SEND di un dominio con autenticazione header

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioHeaderAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioSend.auth == dominioHeaderAuth.servizioSend.auth

Scenario: Modifica di un servizio SEND di un dominio con autenticazione apiKey

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioApiKeyAuth
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioSend.auth == dominioApiKeyAuth.servizioSend.auth

Scenario: Modifica di un servizio SEND di un dominio con autenticazione oauth2

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioOauth2Auth
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioSend.auth == dominioOauth2Auth.servizioSend.auth

Scenario Outline: Configurazione servizio SEND di un dominio con <field> non valido

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field           | fieldRequest              | fieldValue  | fieldResponse     |
| ragioneSociale  | dominio.ragioneSociale    | null        | 'ragioneSociale'  |
| url             | dominio.servizioSend.url  | null        | 'url'             |
| url             | dominio.servizioSend.url  | 'xxxx'      | 'url'             |
| subscriptionKey | dominio.servizioSend.subscriptionKey | ''       | 'subscriptionKey' |
| subscriptionKey | dominio.servizioSend.subscriptionKey | loremIpsum | 'subscriptionKey' |
| abilitaGDE      | dominio.servizioSend.abilitaGDE | null  | 'abilitaGDE'      |

Scenario Outline: Configurazione servizio SEND di un dominio con autenticazione basic e <field> non valido

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioBasicAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field    | fieldRequest                            | fieldValue | fieldResponse |
| username | dominioBasicAuth.servizioSend.auth.username | null   | 'username'    |
| password | dominioBasicAuth.servizioSend.auth.password | null   | 'password'    |

Scenario Outline: Configurazione servizio SEND di un dominio con autenticazione client SSL e <field> non valido

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioClientAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field        | fieldRequest                                  | fieldValue | fieldResponse  |
| tipo         | dominioClientAuth.servizioSend.auth.tipo         | null   | 'tipo'         |
| tipo         | dominioClientAuth.servizioSend.auth.tipo         | 'XXXX' | 'tipo'         |
| ksLocation   | dominioClientAuth.servizioSend.auth.ksLocation   | null   | 'ksLocation'   |
| ksPassword   | dominioClientAuth.servizioSend.auth.ksPassword   | null   | 'ksPassword'   |
| tsLocation   | dominioClientAuth.servizioSend.auth.tsLocation   | null   | 'tsLocation'   |
| tsPassword   | dominioClientAuth.servizioSend.auth.tsPassword   | null   | 'tsPassword'   |
| tsType       | dominioClientAuth.servizioSend.auth.tsType       | null   | 'tsType'       |
| tsType       | dominioClientAuth.servizioSend.auth.tsType       | 'XXX'  | 'tsType'       |
| sslType      | dominioClientAuth.servizioSend.auth.sslType      | null   | 'sslType'      |
| sslType      | dominioClientAuth.servizioSend.auth.sslType      | 'XXX'  | 'sslType'      |
| ksType       | dominioClientAuth.servizioSend.auth.ksType       | null   | 'ksType'       |
| ksType       | dominioClientAuth.servizioSend.auth.ksType       | 'XXX'  | 'ksType'       |
| ksPKeyPasswd | dominioClientAuth.servizioSend.auth.ksPKeyPasswd | null   | 'ksPKeyPasswd' |

Scenario Outline: Configurazione servizio SEND di un dominio con autenticazione server SSL e <field> non valido

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioServerAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field      | fieldRequest                                | fieldValue | fieldResponse |
| tipo       | dominioServerAuth.servizioSend.auth.tipo       | null   | 'tipo'        |
| tipo       | dominioServerAuth.servizioSend.auth.tipo       | 'XXXX' | 'tipo'        |
| tsLocation | dominioServerAuth.servizioSend.auth.tsLocation | null   | 'tsLocation'  |
| tsPassword | dominioServerAuth.servizioSend.auth.tsPassword | null   | 'tsPassword'  |
| tsType     | dominioServerAuth.servizioSend.auth.tsType     | null   | 'tsType'      |
| tsType     | dominioServerAuth.servizioSend.auth.tsType     | 'XXX'  | 'tsType'      |
| sslType    | dominioServerAuth.servizioSend.auth.sslType    | null   | 'sslType'     |
| sslType    | dominioServerAuth.servizioSend.auth.sslType    | 'XXX'  | 'sslType'     |

Scenario Outline: Configurazione servizio SEND di un dominio con autenticazione header e <field> non valido

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioHeaderAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field       | fieldRequest                                  | fieldValue | fieldResponse |
| headerName  | dominioHeaderAuth.servizioSend.auth.headerName   | null   | 'headerName'  |
| headerValue | dominioHeaderAuth.servizioSend.auth.headerValue  | null   | 'headerValue' |

Scenario Outline: Configurazione servizio SEND di un dominio con autenticazione apiKey e <field> non valido

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioApiKeyAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field  | fieldRequest                          | fieldValue | fieldResponse |
| apiId  | dominioApiKeyAuth.servizioSend.auth.apiId  | null  | 'apiId'       |
| apiKey | dominioApiKeyAuth.servizioSend.auth.apiKey | null  | 'apiKey'      |

Scenario Outline: Configurazione servizio SEND di un dominio con autenticazione oauth2 e <field> non valido

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioOauth2Auth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field            | fieldRequest                                          | fieldValue | fieldResponse      |
| clientId         | dominioOauth2Auth.servizioSend.auth.clientId          | null   | 'clientId'         |
| clientSecret     | dominioOauth2Auth.servizioSend.auth.clientSecret      | null   | 'clientSecret'     |
| urlTokenEndpoint | dominioOauth2Auth.servizioSend.auth.urlTokenEndpoint  | null   | 'urlTokenEndpoint' |
