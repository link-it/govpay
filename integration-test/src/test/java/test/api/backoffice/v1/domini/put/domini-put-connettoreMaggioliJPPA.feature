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
| versioneCsv | '1.0' | '1.0' |
| emailIndirizzi | ['pec2@creditore.it'] | ['pec2@creditore.it'] |
| emailIndirizzi | ['pec2@creditore.it' , 'pec3@creditore.it' ] | ['pec2@creditore.it' , 'pec3@creditore.it' ] |
| emailSubject | '[Govpay] Export pagamenti Secim tipo pendenza #(codEntrataSegreteria)' | '[Govpay] Export pagamenti Secim tipo pendenza #(codEntrataSegreteria)' |
| tipiPendenza | [ '#(codEntrataSegreteria)' ] | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |
| tipiPendenza | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] | [{ 'idTipoPendenza' : '#(codEntrataSegreteria)' , 'descrizione' : 'Diritti e segreteria'}] |
| emailAllegato | false | false |
| url | 'http://prova.it' | 'http://prova.it' |
| versioneApi | 'SOAP v1' | 'SOAP v1' |
| auth | { username: 'usr', password: 'pwd' } | { username: 'usr', password: 'pwd' } |
| auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: 'JKS', ksPKeyPasswd: 'ksPKeyPasswd'	} |{ tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: 'JKS', ksPKeyPasswd: 'ksPKeyPasswd'	} |
| auth | { tipo: 'Server', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} | { tipo: 'Server', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |
| principal | 'username' | 'username' |

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
| versioneZip | versioneZip | null | 'versioneZip' |
| emailIndirizzi | emailIndirizzi | null | 'emailIndirizzi' |
| emailIndirizzi | emailIndirizzi | ['mail@errata@it'] | 'emailIndirizzi' |
| tipiPendenza | tipiPendenza | null | 'tipiPendenza' |
| tipiPendenza | tipiPendenza | [ '#(loremIpsum)' ] | 'idTipoPendenza' |
| emailAllegato | emailAllegato | null | 'emailAllegato' |
| principal | principal | null | 'principal' |
| principal | principal | [ '#(loremIpsum)' ] | 'principal' |

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
| versioneApi | versioneApi | 'xxxx' | 'versioneApi' |
| auth.tipo | auth | { } | 'tipo' |
| auth.username | auth | { username: null, password: 'pwd' } | 'username' |
| auth.password | auth | { username: 'usr', password: null } | 'password' |
| auth.tipo | auth | { tipo: null, ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} | 'tipo' |
| auth.tipo | auth | { tipo: 'xxx', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} | 'tipo' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: null, ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'ksLocation' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'ksPassword' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'tsLocation' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null, tsType: 'JKS', sslType: 'TLSv1.2'	}|  'tsPassword' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: null, sslType: 'TLSv1.2'	}|  'tsType' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'XXX', sslType: 'TLSv1.2'	}|  'tsType' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: null	}|  'sslType' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'XXX'	} |  'sslType' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: null, ksPKeyPasswd: 'ksPKeyPasswd'	} |  'ksType' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: 'XXX', ksPKeyPasswd: 'ksPKeyPasswd'	} |  'ksType' |
| auth.ksLocation | auth | { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	, ksType: 'JKS', ksPKeyPasswd:  null } |  'ksPKeyPasswd' |
| auth.ksLocation | auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} |  'tsLocation' |
| auth.ksLocation | auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null, tsType: 'JKS', sslType: 'TLSv1.2'	} |  'tsPassword' |
| auth.ksLocation | auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: null, sslType: 'TLSv1.2'	} |  'tsType' |
| auth.ksLocation | auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'XXX', sslType: 'TLSv1.2'	} |  'tsType' |
| auth.ksLocation | auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: null	} |  'sslType' |
| auth.ksLocation | auth | { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'XXX'	} |  'sslType' |
