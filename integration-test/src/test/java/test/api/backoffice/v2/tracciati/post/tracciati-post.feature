Feature: Caricamento di un tracciato di pendenze (console-api v2)

# Corrispettivo parziale delle dieci feature di
# test/api/backoffice/v1/tracciati/post/, che sono trentasei scenari e provano
# il caricamento CSV in ogni variante: avvisi, importi, tipo pendenza,
# tracciati grandi, json.
#
# Qui sono coperti i rifiuti, che non scrivono nulla: corpo assente, media type
# non ammesso, parametro idDominio mancante. Il caricamento valido non e'
# replicato: dipende dalla configurazione dei template CSV dell'ente e produce
# pendenze vere, e riprodurlo qui in modo fedele e' un lavoro a se' che vale la
# pena fare quando si decide se la testsuite deve caricare tracciati da entrambe
# le versioni delle API o solo da una.
#
# I tre tipi ammessi, dichiarati dal server nel messaggio del 415, sono
# multipart/form-data, application/json e text/csv.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '12345678901'

Scenario: Caricamento con un corpo JSON vuoto

# Karate non consente una POST senza corpo, quindi il caso e' un corpo vuoto,
# che il server rifiuta comunque.

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And header Content-Type = 'application/json'
And request { }
When method post
Then status 400

Scenario: Caricamento con un media type non ammesso

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And header Content-Type = 'application/xml'
And request '<tracciato/>'
When method post
Then status 415
And match response.detail contains 'Content-Type non supportato'

Scenario: Caricamento CSV senza il parametro idDominio

# Per il CSV il dominio e' un parametro di query, mentre per il JSON sta nel
# corpo e puo' variare riga per riga.

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And header Content-Type = 'text/csv'
And request 'idA2A,idPendenza\nERP,1\n'
When method post
Then status 400
And match response.detail contains 'idDominio'

Scenario: Caricamento senza autenticazione

Given url consoleBaseurl
And path 'pendenze', 'tracciati'
And header Content-Type = 'text/csv'
And param idDominio = idDominio
And request 'idA2A,idPendenza\nERP,1\n'
When method post
Then status 401
