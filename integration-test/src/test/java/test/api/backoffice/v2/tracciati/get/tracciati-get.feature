Feature: Dettaglio di un tracciato di pendenze (console-api v2)

# In v2 il tracciato ha quattro sotto-risorse: richiesta, esito, stampe e
# operazioni. Qui si verificano il 404 sul tracciato inesistente e il 404 sulle
# sotto-risorse di un tracciato inesistente: il dettaglio di un tracciato vero
# richiede di averne caricato uno, e il caricamento e' trattato in
# post/tracciati-post.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idInesistente = '999999999'

Scenario: Lettura di un tracciato inesistente

Given url consoleBaseurl
And path 'pendenze', 'tracciati', idInesistente
And headers basicAutenticationHeader
When method get
Then status 404

Scenario Outline: Lettura della sotto-risorsa <sottorisorsa> di un tracciato inesistente

Given url consoleBaseurl
And path 'pendenze', 'tracciati', idInesistente, '<sottorisorsa>'
And headers basicAutenticationHeader
When method get
Then status 404

Examples:
| sottorisorsa |
| richiesta |
| esito |
| operazioni |

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'pendenze', 'tracciati', idInesistente
When method get
Then status 401
