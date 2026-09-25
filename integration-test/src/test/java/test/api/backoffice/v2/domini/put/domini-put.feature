Feature: Modifica di un dominio (console-api v2)

# Corrispettivo v2 della parte di modifica di
# test/api/backoffice/v1/domini/put/dominio-put.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '99999999997'
* def creaDominio = call read('classpath:utils/api/v2/backoffice/crea-dominio.feature') { idDominio: '#(idDominio)' }

Scenario Outline: Modifica di un dominio (<campo>)

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.<campo> = <valore>
* remove corrente.idDominio

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then status 200
And match response.<campo> == <valore>

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<campo> == <valore>

Examples:
| campo | valore |
| ragioneSociale | 'Ragione sociale modificata' |
| abilitato | false |
| abilitato | true |
| cap | '00100' |
| email | 'prova@ente.it' |
| scaricaFr | true |
| scaricaFr | false |

Scenario: Modifica senza If-Match

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
* def corrente = response
* remove corrente.idDominio

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request corrente
When method put
Then status 428

Scenario: Modifica con If-Match non corrispondente

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
* def corrente = response
* remove corrente.idDominio

Given url consoleBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And header If-Match = '"non-corrispondente"'
And request corrente
When method put
Then status 412

Scenario: Modifica di un dominio inesistente

Given url consoleBaseurl
And path 'domini', '99999999990'
And headers basicAutenticationHeader
And header If-Match = '"qualsiasi"'
And request { ragioneSociale: 'x', abilitato: true, scaricaFr: false }
When method put
Then status 404

Scenario: Modifica senza autenticazione

Given url consoleBaseurl
And path 'domini', idDominio
And request { ragioneSociale: 'x', abilitato: true, scaricaFr: false }
When method put
Then status 401
