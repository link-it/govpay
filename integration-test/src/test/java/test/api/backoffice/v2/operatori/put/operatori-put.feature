Feature: Modifica di un operatore (console-api v2)

# Corrispettivo v2 della parte di modifica di
# test/api/backoffice/v1/operatori/put/operatori-put.feature, comprese le
# modifiche di domini e tipiPendenza che in v1 erano scenari a se'.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def principal = 'OPERATORE_TEST_V2'
* def creaOperatore = call read('classpath:utils/api/v2/backoffice/crea-operatore.feature') { principal: '#(principal)' }

Scenario Outline: Modifica di un operatore (<campo>)

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.<campo> = <valore>
* remove corrente.principal

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then status 200
And match response.<campo> == <valore>

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<campo> == <valore>

Examples:
| campo | valore |
| nome | 'Nome modificato' |
| abilitato | false |
| abilitato | true |
| acl | [ { servizio: 'Pagamenti', autorizzazioni: [ 'R' ] } ] |

Scenario Outline: Modifica dei riferimenti di un operatore (<campo>)

# domini e tipiPendenza sono riferimenti, e in v2 la lettura non e' simmetrica
# alla scrittura: in scrittura e' significativo il solo identificativo, in
# lettura il server aggiunge l'etichetta, ragioneSociale per i domini e
# descrizione per i tipi pendenza. L'asserzione e' quindi sull'identificativo.

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.<campo> = <valore>
* remove corrente.principal

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then status 200
And match response.<campo>[*].<chiave> contains <atteso>
And match response.<campo>[0].<etichetta> == '#notnull'

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<campo>[*].<chiave> contains <atteso>

Examples:
| campo | valore | chiave | atteso | etichetta |
| domini | [ { idDominio: '12345678901' } ] | idDominio | '12345678901' | ragioneSociale |
| tipiPendenza | [ { idTipoPendenza: 'LIBERO' } ] | idTipoPendenza | 'LIBERO' | descrizione |

Scenario: Modifica dei domini con il carattere jolly

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response
* set corrente.domini = [ { idDominio: '*' } ]
* remove corrente.principal

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then status 200
And match response.domini[*].idDominio contains '*'

Scenario: Modifica con un dominio inesistente

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And header If-Match = etag
And request { nome: 'x', abilitato: true, domini: [ { idDominio: '99999999999' } ] }
When method put
Then assert responseStatus == 400 || responseStatus == 404

Scenario: Modifica senza If-Match

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And request { nome: 'x', abilitato: true }
When method put
Then status 428

Scenario: Modifica con If-Match non corrispondente

Given url consoleBaseurl
And path 'operatori', principal
And headers basicAutenticationHeader
And header If-Match = '"non-corrispondente"'
And request { nome: 'x', abilitato: true }
When method put
Then status 412

Scenario: Modifica di un operatore inesistente

Given url consoleBaseurl
And path 'operatori', 'OPERATORE_CHE_NON_ESISTE'
And headers basicAutenticationHeader
And header If-Match = '"qualsiasi"'
And request { nome: 'x', abilitato: true }
When method put
Then status 404

Scenario: Modifica senza autenticazione

Given url consoleBaseurl
And path 'operatori', principal
And request { nome: 'x', abilitato: true }
When method put
Then status 401
