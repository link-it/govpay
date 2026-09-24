Feature: Modifica di un intermediario (console-api v2)

# Corrispettivo v2 della parte di modifica di
# test/api/backoffice/v1/intermediari/put/intermediari-put.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idIntermediario = '99999999998'
* def creaIntermediario = call read('classpath:utils/api/v2/backoffice/crea-intermediario.feature') { idIntermediario: '#(idIntermediario)' }

Scenario Outline: Modifica di un intermediario (<campo>)

Given url consoleBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.<campo> = <valore>
* remove corrente.idIntermediario

Given url consoleBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then status 200
And match response.<campo> == <valore>

Given url consoleBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<campo> == <valore>

Examples:
| campo | valore |
| denominazione | 'Denominazione modificata' |
| abilitato | false |
| abilitato | true |
| principalPagoPa | 'ndpsym' |

Scenario: Modifica senza If-Match

Given url consoleBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request { denominazione: 'x', principalPagoPa: 'p', abilitato: true }
When method put
Then status 428

Scenario: Modifica con If-Match non corrispondente

Given url consoleBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And header If-Match = '"non-corrispondente"'
And request { denominazione: 'x', principalPagoPa: 'p', abilitato: true }
When method put
Then status 412

Scenario: Modifica di un intermediario inesistente

Given url consoleBaseurl
And path 'intermediari', '99999999990'
And headers basicAutenticationHeader
And header If-Match = '"qualsiasi"'
And request { denominazione: 'x', principalPagoPa: 'p', abilitato: true }
When method put
Then status 404

Scenario: Modifica senza autenticazione

Given url consoleBaseurl
And path 'intermediari', idIntermediario
And request { denominazione: 'x', principalPagoPa: 'p', abilitato: true }
When method put
Then status 401
