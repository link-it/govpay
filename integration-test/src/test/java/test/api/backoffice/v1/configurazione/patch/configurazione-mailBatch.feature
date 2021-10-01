Feature: Configurazione mailBatch

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def string71 = '1GLqJdabGYFpRi4RbM8gWlnpCzVvMyeKC2qoCYkqfvTyGZ1eovAxsFqpGfVqzzXXjCfMsKi'
* def string17 = 'LS2wIWYPN0QPsgTbX'
* def string36 = 'VTnniDMiQ2ngyoDMBnfzeGUPKTbhx2U7fMO1'
* def patchRequest = 
"""
[
  {
    "op": "REPLACE",
    "path": "/mailBatch",
    "value": {
			"abilitato": true,
			"mailserver": {
				"host": "smtp.entecreditore.it",
				"port": "25",
				"username": "usrpagopa",
				"password": "changeme",
				"from": "pagopa@entecreditore.it",
				"readTimeout": 120000,
				"connectionTimeout": 10000,
				"sslConfig": {
					"abilitato": true,
					"type" : "SSL",
					"hostnameVerifier" : true,
					"keystore" : {
						"type" : "JKS",
						"location": "/pathToFile/keystore.jks",
						"password": "password"
					},
					"truststore" : {
						"type" : "JKS",
						"location": "/pathToFile/truststore.jks",
						"password": "password"
					}
				},
				"startTls" : false
			}
		}
  }
]
"""

Scenario Outline: Modifica della configurazione mailBatch (<field>)


* set patchRequest[0].value.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.mailBatch.<field> == checkValue

Examples:
| field | value | 
| abilitato | true |
| abilitato | false |
| mailserver.host | "smtp.myhost.it" |
| mailserver.port | 10 |
| mailserver.username | "xxxx" |
| mailserver.password | "xxxx" |
| mailserver.from | "from@xxx.org" |
| mailserver.readTimeout | 0 |
| mailserver.connectionTimeout | 0 |
| mailserver.sslConfig.abilitato | true |
| mailserver.sslConfig.abilitato | false |
| mailserver.sslConfig.type | 'SSL' |
| mailserver.sslConfig.type | 'SSLv3' |
| mailserver.sslConfig.type | 'TLS' |
| mailserver.sslConfig.type | 'TLSv1' |
| mailserver.sslConfig.type | 'TLSv1.1' |
| mailserver.sslConfig.type | 'TLSv1.2' |
| mailserver.sslConfig.keystore.type | "JKS" |
| mailserver.sslConfig.truststore.type | "JKS" |
| mailserver.startTls | true |
| mailserver.startTls | false |

Scenario Outline: Errore sintassi della configurazione mailBatch (<field>)

* set patchRequest[0].value.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '<fieldName>'

Examples:
| field | fieldName | value | 
| abilitato | abilitato | "aaaa" |
| abilitato | abilitato | null |
| mailserver | mailserver | 123 |
| mailserver | mailserver | "a" |
| mailserver.host | host | loremIpsum |
| mailserver.host | host | 't rue' |
| mailserver.host | host | null |
| mailserver.port | port | null |
| mailserver.port | port | "aaa" |
| mailserver.username | username | null |
| mailserver.password | password | null |
| mailserver.from | from | null |
| mailserver.from | from | loremIpsum |
| mailserver.readTimeout | readTimeout | null |
| mailserver.readTimeout | readTimeout | "aaa" |
| mailserver.connectionTimeout | connectionTimeout | null |
| mailserver.connectionTimeout | connectionTimeout | "aaa" |
| mailserver.sslConfig.abilitato | abilitato | "aaaa" |
| mailserver.sslConfig.abilitato | abilitato | null |
| mailserver.sslConfig.type | type | "aaaa" |
| mailserver.sslConfig.type | type | null |
| mailserver.sslConfig.hostnameVerifier | hostnameVerifier | "aaaa" |
| mailserver.sslConfig.hostnameVerifier | hostnameVerifier | null |
| mailserver.sslConfig.keystore.type | type | "aaaa" |
| mailserver.sslConfig.keystore.type | type | null |
| mailserver.sslConfig.keystore.location | location | null |
| mailserver.sslConfig.keystore.password | password | loremIpsum |
| mailserver.sslConfig.keystore.managementAlgorithm | managementAlgorithm | loremIpsum |
| mailserver.sslConfig.truststore.type | type | "aaaa" |
| mailserver.sslConfig.truststore.type | type | null |
| mailserver.sslConfig.truststore.location | location | null |
| mailserver.sslConfig.truststore.password | password | loremIpsum |
| mailserver.sslConfig.truststore.managementAlgorithm | managementAlgorithm | loremIpsum |
| mailserver.startTls | startTls | "aaaa" |
| mailserver.startTls | startTls | null |

