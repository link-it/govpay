Feature: Modifica di una entrata (console-api v2)

# Corrispettivo v2 della parte di modifica di
# test/api/backoffice/v1/entrate/put/entrate-put.feature. Come per i ruoli, la
# PUT della v2 sostituisce una entrata che deve gia' esistere e richiede
# If-Match con l'ETag ottenuto da una GET.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idEntrata = 'ENTRATA_TEST_V2'
* def creaEntrata = call read('classpath:utils/api/v2/backoffice/crea-entrata.feature') { idEntrata: '#(idEntrata)' }

Scenario Outline: Modifica di una entrata (<campo>)

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.<campo> = <valore>
* remove corrente.idEntrata

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then status 200
And match response.<campo> == <valore>

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<campo> == <valore>

Examples:
| campo | valore |
| descrizione | 'Descrizione modificata' |
| tipoContabilita | 'CAPITOLO' |
| codiceContabilita | 'CONTAB_MODIFICATO' |
| tipoContabilita | 'ALTRO' |

Scenario: Modifica senza If-Match

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
And request { descrizione: 'x', tipoContabilita: 'ALTRO', codiceContabilita: 'C' }
When method put
Then status 428

Scenario: Modifica con If-Match non corrispondente

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
And header If-Match = '"non-corrispondente"'
And request { descrizione: 'x', tipoContabilita: 'ALTRO', codiceContabilita: 'C' }
When method put
Then status 412

Scenario: Modifica di una entrata inesistente

Given url consoleBaseurl
And path 'entrate', 'ENTRATA_CHE_NON_ESISTE'
And headers basicAutenticationHeader
And header If-Match = '"qualsiasi"'
And request { descrizione: 'x', tipoContabilita: 'ALTRO', codiceContabilita: 'C' }
When method put
Then status 404

Scenario: Modifica con tipoContabilita fuori dall'enumerazione

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
And header If-Match = etag
And request { descrizione: 'x', tipoContabilita: 'NON_ESISTE', codiceContabilita: 'C' }
When method put
Then status 400

Scenario: Modifica senza autenticazione

Given url consoleBaseurl
And path 'entrate', idEntrata
And request { descrizione: 'x', tipoContabilita: 'ALTRO', codiceContabilita: 'C' }
When method put
Then status 401
