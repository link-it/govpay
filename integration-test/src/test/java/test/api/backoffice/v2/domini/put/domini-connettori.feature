Feature: Connettori di un dominio (console-api v2)

# Corrispettivo v2 delle sei feature
# test/api/backoffice/v1/domini/put/domini-put-connettore*.feature, una per
# connettore. Come per gli intermediari, in v1 i connettori erano campi dentro
# il dominio e in v2 sono sotto-risorse tipizzate: le sei feature diventano una
# sola, parametrica.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '99999999997'
* def creaDominio = call read('classpath:utils/api/v2/backoffice/crea-dominio.feature') { idDominio: '#(idDominio)' }

Scenario Outline: Lettura del connettore <connettore>

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.abilitato == '#boolean'
And match responseHeaders['ETag'][0] == '#notnull'

Examples:
| connettore |
| mypivot |
| secim |
| govpay |
| hypersic-apk |
| maggioli-jppa |

Scenario: Lettura del connettore send

# send e' l'unico connettore del dominio senza il flag abilitato, ed e' una
# scelta dichiarata nello schema: il connettore e' considerato attivo appena
# configurato. Non ha nemmeno i campi di esportazione del tracciato che hanno
# gli altri canali di notifica.

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', 'send'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.abilitato == '#notpresent'
And match response.abilitaGDE == '#boolean'
And match response.auth == '#notnull'

Scenario Outline: Abilitazione e disabilitazione del connettore <connettore>

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.abilitato = true
* set corrente.url = 'http://localhost:8888/connettore-di-prova'

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', '<connettore>'
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then assert responseStatus == 200 || responseStatus == 204

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.abilitato == true
* def etag2 = responseHeaders['ETag'][0]
* def corrente2 = response
* set corrente2.abilitato = false

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', '<connettore>'
And headers basicAutenticationHeader
And header If-Match = etag2
And request corrente2
When method put
Then assert responseStatus == 200 || responseStatus == 204

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.abilitato == false

Examples:
| connettore |
| mypivot |
| secim |
| hypersic-apk |

Scenario: Modifica di un connettore senza If-Match

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', 'mypivot'
And headers basicAutenticationHeader
When method get
Then status 200
* def corrente = response

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', 'mypivot'
And headers basicAutenticationHeader
And request corrente
When method put
Then status 428

Scenario: Lettura di un connettore non previsto

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', 'connettore-che-non-esiste'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'domini', idDominio, 'connettori', 'mypivot'
When method get
Then status 401
