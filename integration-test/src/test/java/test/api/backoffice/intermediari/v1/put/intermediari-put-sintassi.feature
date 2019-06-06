Feature: Validazione sintattica intermediari

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediario.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediarioBasicAuth.json')
* def intermediarioClientAuth = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediarioClientAuth.json')
* def intermediarioServerAuth = read('classpath:test/api/backoffice/intermediari/v1/put/msg/intermediarioServerAuth.json')

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
| urlRPT | intermediario.servizioPagoPa.urlRPT | 'htttttttp://aaa.it' |  'urlRPT' |
| abilitato | intermediario.abilitato | 'boh' |  'abilitato' |
| abilitato | intermediario.abilitato | null |  'abilitato' |

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
| username | intermediarioBasicAuth.servizioPagoPa.auth.username | null |  'username' |
| password | intermediarioBasicAuth.servizioPagoPa.auth.password | null |  'password' |

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
| tipo | intermediarioClientAuth.servizioPagoPa.auth.tipo | null |  'tipo' |
| tipo | intermediarioClientAuth.servizioPagoPa.auth.tipo | 'XXXX' |  'tipo' |
| ksLocation | intermediarioClientAuth.servizioPagoPa.auth.ksLocation | null |  'ksLocation' |
| ksPassword | intermediarioClientAuth.servizioPagoPa.auth.ksPassword | null |  'ksPassword' |
| tsLocation | intermediarioClientAuth.servizioPagoPa.auth.tsLocation | null |  'tsLocation' |
| tsPassword | intermediarioClientAuth.servizioPagoPa.auth.tsPassword | null |  'tsPassword' |

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
| tipo | intermediarioServerAuth.servizioPagoPa.auth.tipo | null |  'tipo' |
| tipo | intermediarioServerAuth.servizioPagoPa.auth.tipo | 'XXXX' |  'tipo' |
| tsLocation | intermediarioServerAuth.servizioPagoPa.auth.tsLocation | null |  'tsLocation' |
| tsPassword | intermediarioServerAuth.servizioPagoPa.auth.tsPassword | null |  'tsPassword' |
