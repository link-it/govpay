Feature: Modifica delle impostazioni (console-api v2)

# Corrispettivo v2 della parte di modifica delle feature di
# test/api/backoffice/v1/configurazione/, che usavano la PATCH su
# /configurazioni. In v2 ogni area si modifica per conto suo, e come tutte le
# risorse della v2 richiede If-Match con l'ETag ottenuto da una GET.
#
# Gli scenari lasciano le impostazioni come le hanno trovate: leggono, scrivono
# il valore letto e verificano l'esito. Cambiare per davvero la configurazione
# di posta o del giornale eventi avrebbe effetti sul resto della testsuite.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario Outline: Riscrittura dell'area <area> con il suo stesso contenuto

Given url consoleBaseurl
And path <percorso>
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]
* def corrente = response

Given url consoleBaseurl
And path <percorso>
And headers basicAutenticationHeader
And header If-Match = etag
And request corrente
When method put
Then assert responseStatus == 200 || responseStatus == 204

# hardening non e' fra le aree riscritte, ed e' una scelta obbligata: la sua
# rappresentazione non regge il giro. La GET restituisce captcha vuoto, la PUT
# di quel corpo lo normalizza a zeri, e da quel momento la GET restituisce
# soglia 0.0, che la PUT rifiuta perche' il minimo e' 0.1. La risorsa resta in
# uno stato da cui non si puo' piu' scrivere, e un test che la lasciasse cosi'
# romperebbe l'ambiente per tutti.
Examples:
| area | percorso |
| tracciati CSV | 'impostazioni', 'tracciati-csv' |
| server App IO | 'impostazioni', 'app-io', 'server' |

Scenario Outline: Modifica dell'area <area> senza If-Match

# Il corpo e' quello corrente, valido: la precondizione si verifica su una
# richiesta per il resto legittima.

Given url consoleBaseurl
And path <percorso>
And headers basicAutenticationHeader
When method get
Then status 200
* def corrente = response

Given url consoleBaseurl
And path <percorso>
And headers basicAutenticationHeader
And request corrente
When method put
Then status 428

Examples:
| area | percorso |
| tracciati CSV | 'impostazioni', 'tracciati-csv' |
| servizio GDE | 'impostazioni', 'servizioGDE' |
| server di posta | 'impostazioni', 'mail', 'server' |

Scenario: Modifica con If-Match non corrispondente

# Il corpo dev'essere valido: la validazione del corpo precede il controllo
# della precondizione, e con un corpo incompleto la risposta sarebbe 400
# invece di 412.

Given url consoleBaseurl
And path 'impostazioni', 'tracciati-csv'
And headers basicAutenticationHeader
When method get
Then status 200
* def corrente = response

Given url consoleBaseurl
And path 'impostazioni', 'tracciati-csv'
And headers basicAutenticationHeader
And header If-Match = '"non-corrispondente"'
And request corrente
When method put
Then status 412

Scenario: Modifica senza autenticazione

Given url consoleBaseurl
And path 'impostazioni', 'tracciati-csv'
And request { tipo: 'freemarker' }
When method put
Then status 401
