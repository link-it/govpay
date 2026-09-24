Feature: Connettori pagoPA di un intermediario (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/intermediari/put/intermediari-put-nuoviConnettori.feature
# e intermediari-put-recuperoRT.feature.
#
# In v1 i connettori erano campi dentro l'intermediario e si scrivevano con la
# stessa PUT; in v2 sono sei sotto-risorse tipizzate, ciascuna con la propria
# GET e PUT, piu' una sotto-risorsa per le credenziali. E' il motivo per cui le
# due feature della v1 qui diventano una sola, parametrica sul connettore.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idIntermediario = '99999999998'
* def creaIntermediario = call read('classpath:utils/api/v2/backoffice/crea-intermediario.feature') { idIntermediario: '#(idIntermediario)' }

Scenario Outline: Lettura del connettore <connettore>

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.abilitato == '#boolean'

Examples:
| connettore |
| pagopa |
| pagopa-aca |
| pagopa-gpd |
| pagopa-fr |
| pagopa-backoffice-ec |
| pagopa-recupero-rt |

Scenario Outline: Abilitazione e disabilitazione del connettore <connettore>

# Anche i connettori sono protetti da If-Match: la PUT senza header e' rifiutata
# con 428, come per la risorsa padre.

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', '<connettore>'
And headers basicAutenticationHeader
And header If-Match = etag
And request { abilitato: true, url: 'http://localhost:8888/connettore-di-prova', auth: { tipoAutenticazione: 'NONE' } }
When method put
Then assert responseStatus == 200 || responseStatus == 204

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.abilitato == true
* def etag2 = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', '<connettore>'
And headers basicAutenticationHeader
And header If-Match = etag2
# auth resta obbligatorio anche per disabilitare: il corpo e' una sostituzione
# completa, non una modifica del solo campo abilitato.
And request { abilitato: false, url: 'http://localhost:8888/connettore-di-prova', auth: { tipoAutenticazione: 'NONE' } }
When method put
Then assert responseStatus == 200 || responseStatus == 204

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', '<connettore>'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.abilitato == false

Examples:
| connettore |
| pagopa-aca |
| pagopa-gpd |
| pagopa-fr |

Scenario: Lettura di un connettore non previsto

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', 'connettore-che-non-esiste'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura di un connettore di un intermediario inesistente

Given url consoleBaseurl
And path 'intermediari', '99999999990', 'connettori', 'pagopa'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Modifica del connettore senza If-Match

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', 'pagopa-aca'
And headers basicAutenticationHeader
And request { abilitato: false, url: 'http://localhost:8888/x', auth: { tipoAutenticazione: 'NONE' } }
When method put
Then status 428

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'intermediari', idIntermediario, 'connettori', 'pagopa'
When method get
Then status 401
