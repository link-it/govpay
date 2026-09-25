Feature: Dettaglio di una pendenza (console-api v2)

# Corrispettivo v2 della parte di lettura delle feature di
# test/api/backoffice/v1/pendenze/, comprese le sotto-risorse: l'avviso, le
# informazioni per il debitore e le ricevute della pendenza.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Given url consoleBaseurl
And path 'pendenze'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def pendenza = response.results[0]

Scenario: Lettura di una pendenza

Given url consoleBaseurl
And path 'pendenze', pendenza.idA2A, pendenza.idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idA2A == pendenza.idA2A
And match response.idPendenza == pendenza.idPendenza
And match response.stato == '#notnull'
And match response.dominio.idDominio == '#notnull'

Scenario: Lettura dell'avviso della pendenza

Given url consoleBaseurl
And path 'pendenze', pendenza.idA2A, pendenza.idPendenza, 'avviso'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Lettura delle informazioni per il debitore

Given url consoleBaseurl
And path 'pendenze', pendenza.idA2A, pendenza.idPendenza, 'informazioniDebitore'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Lettura delle ricevute della pendenza

Given url consoleBaseurl
And path 'pendenze', pendenza.idA2A, pendenza.idPendenza, 'ricevute'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Lettura di una pendenza inesistente

Given url consoleBaseurl
And path 'pendenze', pendenza.idA2A, 'PENDENZA_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura con una applicazione inesistente

Given url consoleBaseurl
And path 'pendenze', 'APPLICAZIONE_CHE_NON_ESISTE', pendenza.idPendenza
And headers basicAutenticationHeader
When method get
Then status 404

Scenario Outline: Lettura della sotto-risorsa <sottorisorsa> di una pendenza inesistente

Given url consoleBaseurl
And path 'pendenze', pendenza.idA2A, 'PENDENZA_CHE_NON_ESISTE', '<sottorisorsa>'
And headers basicAutenticationHeader
When method get
Then status 404

Examples:
| sottorisorsa |
| avviso |
| informazioniDebitore |
| ricevute |

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'pendenze', pendenza.idA2A, pendenza.idPendenza
When method get
Then status 401
