Feature: Modifica di un tipo pendenza (console-api v2)

# Corrispettivo v2 della parte di modifica di
# test/api/backoffice/v1/tipipendenza/put/tipipendenza-put.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idTipoPendenza = 'TIPO_TEST_V2'
* def creaTipo = call read('classpath:utils/api/v2/backoffice/crea-tipo-pendenza.feature') { idTipoPendenza: '#(idTipoPendenza)' }

Scenario Outline: Modifica di un tipo pendenza (<campo>)

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.<campo> = <valore>
* remove corrente.idTipoPendenza

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then status 200
And match response.<campo> == <valore>

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<campo> == <valore>

Examples:
| campo | valore |
| descrizione | 'Descrizione modificata' |
| abilitato | false |
| abilitato | true |
| pagaTerzi | true |
| pagaTerzi | false |

Scenario: Modifica senza If-Match

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request { descrizione: 'x' }
When method put
Then status 428

Scenario: Modifica con If-Match non corrispondente

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And header If-Match = '"non-corrispondente"'
And request { descrizione: 'x' }
When method put
Then status 412

Scenario: Modifica di un tipo pendenza inesistente

Given url consoleBaseurl
And path 'tipiPendenza', 'TIPO_CHE_NON_ESISTE'
And headers basicAutenticationHeader
And header If-Match = '"qualsiasi"'
And request { descrizione: 'x' }
When method put
Then status 404

Scenario: Modifica senza autenticazione

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And request { descrizione: 'x' }
When method put
Then status 401
