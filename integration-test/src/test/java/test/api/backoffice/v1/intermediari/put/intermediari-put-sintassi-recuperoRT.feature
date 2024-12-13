Feature: Validazione sintattica intermediari

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-recuperoRT.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioBasicAuth-recuperoRT.json')
* def intermediarioClientAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioClientAuth-recuperoRT.json')
* def intermediarioServerAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioServerAuth-recuperoRT.json')
* def intermediarioHeaderAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioHeaderAuth-recuperoRT.json')
* def intermediarioApiKeyAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioApiKeyAuth-recuperoRT.json')
* def intermediarioOauth2ClientCredentialsAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioOauth2ClientCredentialsAuth-recuperoRT.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| denominazione | intermediario.denominazione | null |  'denominazione' |
| principalPagoPa | intermediario.principalPagoPa | null |  'principalPagoPa' |
| urlRPT | intermediario.servizioPagoPaRecuperoRT.urlRPT | 'htttttttp://aaa.it' |  'urlRPT' |
| abilitato | intermediario.abilitato | 'boh' |  'abilitato' |
| abilitato | intermediario.abilitato | null |  'abilitato' |
| subscriptionKey | intermediario.servizioPagoPaRecuperoRT.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediario.servizioPagoPaRecuperoRT.subscriptionKey | loremIpsum |  'subscriptionKey' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBasicAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| username | intermediarioBasicAuth.servizioPagoPaRecuperoRT.auth.username | null |  'username' |
| password | intermediarioBasicAuth.servizioPagoPaRecuperoRT.auth.password | null |  'password' |
| subscriptionKey | intermediarioBasicAuth.servizioPagoPaRecuperoRT.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioBasicAuth.servizioPagoPaRecuperoRT.subscriptionKey | loremIpsum |  'subscriptionKey' |
| denominazione | intermediarioBasicAuth.denominazione | null |  'denominazione' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioClientAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| tipo | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.tipo | null |  'tipo' |
| tipo | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.tipo | 'XXXX' |  'tipo' |
| ksLocation | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.ksLocation | null |  'ksLocation' |
| ksPassword | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.ksPassword | null |  'ksPassword' |
| tsLocation | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.tsLocation | null |  'tsLocation' |
| tsPassword | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.tsPassword | null |  'tsPassword' |
| tsType | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.tsType | null |  'tsType' |
| tsType | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.tsType | 'XXX' |  'tsType' |
| sslType | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.sslType | null |  'sslType' |
| sslType | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.sslType | 'XXX' |  'sslType' |
| ksType | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.ksType | null |  'ksType' |
| ksType | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.ksType | 'XXX' |  'ksType' |
| ksPKeyPasswd | intermediarioClientAuth.servizioPagoPaRecuperoRT.auth.ksPKeyPasswd | null |  'ksPKeyPasswd' |
| subscriptionKey | intermediarioClientAuth.servizioPagoPaRecuperoRT.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioClientAuth.servizioPagoPaRecuperoRT.subscriptionKey | loremIpsum |  'subscriptionKey' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioServerAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| tipo | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.tipo | null |  'tipo' |
| tipo | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.tipo | 'XXXX' |  'tipo' |
| tsLocation | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.tsLocation | null |  'tsLocation' |
| tsPassword | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.tsPassword | null |  'tsPassword' |
| tsType | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.tsType | null |  'tsType' |
| tsType | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.tsType | 'XXX' |  'tsType' |
| sslType | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.sslType | null |  'sslType' |
| sslType | intermediarioServerAuth.servizioPagoPaRecuperoRT.auth.sslType | 'XXX' |  'sslType' |
| subscriptionKey | intermediarioServerAuth.servizioPagoPaRecuperoRT.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioServerAuth.servizioPagoPaRecuperoRT.subscriptionKey | loremIpsum |  'subscriptionKey' |


Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioHeaderAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| headerName | intermediarioHeaderAuth.servizioPagoPaRecuperoRT.auth.headerName | null |  'headerName' |
| headerValue | intermediarioHeaderAuth.servizioPagoPaRecuperoRT.auth.headerValue | null |  'headerValue' |
| subscriptionKey | intermediarioHeaderAuth.servizioPagoPaRecuperoRT.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioHeaderAuth.servizioPagoPaRecuperoRT.subscriptionKey | loremIpsum |  'subscriptionKey' |
| denominazione | intermediarioHeaderAuth.denominazione | null |  'denominazione' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioApiKeyAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| headerName | intermediarioApiKeyAuth.servizioPagoPaRecuperoRT.auth.apiId | null |  'apiId' |
| headerValue | intermediarioApiKeyAuth.servizioPagoPaRecuperoRT.auth.apiKey | null |  'apiKey' |
| subscriptionKey | intermediarioApiKeyAuth.servizioPagoPaRecuperoRT.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioApiKeyAuth.servizioPagoPaRecuperoRT.subscriptionKey | loremIpsum |  'subscriptionKey' |
| denominazione | intermediarioApiKeyAuth.denominazione | null |  'denominazione' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioOauth2ClientCredentialsAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| clientId | intermediarioOauth2ClientCredentialsAuth.servizioPagoPaRecuperoRT.auth.clientId | null |  'clientId' |
| clientSecret | intermediarioOauth2ClientCredentialsAuth.servizioPagoPaRecuperoRT.auth.clientSecret | null |  'clientSecret' |
| urlTokenEndpoint | intermediarioOauth2ClientCredentialsAuth.servizioPagoPaRecuperoRT.auth.urlTokenEndpoint | null |  'urlTokenEndpoint' |
| subscriptionKey | intermediarioOauth2ClientCredentialsAuth.servizioPagoPaRecuperoRT.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioOauth2ClientCredentialsAuth.servizioPagoPaRecuperoRT.subscriptionKey | loremIpsum |  'subscriptionKey' |
| denominazione | intermediarioOauth2ClientCredentialsAuth.denominazione | null |  'denominazione' |


