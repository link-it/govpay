Feature: Modifica di un ruolo (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/ruoli/put/ruoli-put.feature.
#
# Due differenze rispetto alla v1, che sono il motivo per cui questa feature non
# e' una traduzione riga per riga:
#  - la PUT sostituisce un ruolo che deve gia' esistere, non lo crea;
#  - richiede If-Match con l'ETag ottenuto da una GET, e senza quell'header la
#    richiesta e' rifiutata. Sono i due scenari finali.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idRuolo = 'RuoloTestV2'
* def creaRuolo = call read('classpath:utils/api/v2/backoffice/crea-ruolo.feature') { idRuolo: '#(idRuolo)' }

Scenario Outline: Modifica delle acl di un ruolo (<servizio>)

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And header If-Match = etag
And request { acl: [ { servizio: '<servizio>', autorizzazioni: [ 'R', 'W' ] } ] }
When method put
Then status 200
And match response.acl[0].servizio == '<servizio>'

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
When method get
Then status 200
And match response.acl[0].servizio == '<servizio>'
And match response.acl[0].autorizzazioni contains only [ 'R', 'W' ]

Examples:
| servizio |
| Anagrafica PagoPA |
| Anagrafica Creditore |
| Anagrafica Applicazioni |
| Anagrafica Ruoli |
| Pagamenti |
| Pendenze |
| Rendicontazioni e Incassi |
| Giornale degli Eventi |
| Configurazione e manutenzione |

Scenario: Modifica con piu' acl

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
When method get
Then status 200
* def etag = responseHeaders['ETag'][0]

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And header If-Match = etag
And request { acl: [ { servizio: 'Anagrafica PagoPA', autorizzazioni: [ 'R', 'W' ] }, { servizio: 'Anagrafica Creditore', autorizzazioni: [ 'W' ] } ] }
When method put
Then status 200
And match response.acl == '#[2]'

Scenario: Modifica senza If-Match

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And request { acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R' ] } ] }
When method put
Then status 428
And match response.status == 428

Scenario: Modifica con If-Match non corrispondente

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
And header If-Match = '"non-corrispondente"'
And request { acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R' ] } ] }
When method put
Then status 412
And match response.status == 412

Scenario: Modifica di un ruolo inesistente

Given url consoleBaseurl
And path 'ruoli', 'RuoloCheNonEsiste'
And headers basicAutenticationHeader
And header If-Match = '"qualsiasi"'
And request { acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R' ] } ] }
When method put
Then status 404

Scenario: Modifica senza autenticazione

Given url consoleBaseurl
And path 'ruoli', idRuolo
And request { acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R' ] } ] }
When method put
Then status 401
