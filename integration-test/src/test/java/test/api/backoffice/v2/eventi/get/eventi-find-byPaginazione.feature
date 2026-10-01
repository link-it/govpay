Feature: Paginazione degli eventi del giornale (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/eventi/get/
# eventi-find-byMetadatiPaginazione.feature. L'involucro cambia del tutto: la v1
# aveva risultati/numRisultati/numPagine/maxRisultati governati da
# metadatiPaginazione e maxRisultati, la v2 ha results/pagination e due modalita'
# di paginazione alternative, per pagina e a cursore.
#
# Gli eventi hanno due vincoli propri, assenti sulle altre liste, entrambi a
# difesa di una tabella che cresce a milioni di righe: il tetto di limit e' 100
# e non 200, e il conteggio totale e' ammesso solo su un intervallo temporale
# che non superi le 24 ore.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Paginazione per pagina

# Qui si verificano i metadati e il fatto che la pagina richiesta sia servita,
# non che le due pagine siano disgiunte: il giornale cresce mentre il test gira
# - le chiamate di questo stesso scenario producono eventi - e con ordinamento
# per data decrescente ogni evento nuovo in testa fa scorrere di una posizione
# tutta la finestra. Una pagina 2 presa un istante dopo la pagina 1 contiene
# percio' legittimamente qualcosa che prima stava nella 1.
# La garanzia di non sovrapposizione e' della paginazione a cursore, che la
# ottiene proprio perche' non conta le posizioni: e' verificata piu' sotto.

Given url consoleBaseurl
And path 'eventi'
And param limit = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination == { page: 1, limit: 2, hasNextPage: '#boolean' }
And match response.results == '#[2]'

Given url consoleBaseurl
And path 'eventi'
And param limit = 2
And param page = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.page == 2
And match response.results == '#[2]'

Scenario: Conteggio totale su un intervallo ammesso

# Senza dataDa/dataA l'intervallo e' implicitamente le ultime 24 ore, che la
# regola ammette: il conteggio e' quindi disponibile anche senza passarli.

Given url consoleBaseurl
And path 'eventi'
And param total = true
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'
And match response.pagination.totalPages == '#number'

Scenario: Conteggio totale rifiutato su un intervallo troppo ampio

Given url consoleBaseurl
And path 'eventi'
And param dataDa = '2026-01-01T00:00:00Z'
And param dataA = '2026-12-31T00:00:00Z'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 400
And match response.detail contains '24 ore'

Scenario: Tetto di limit a 100

Given url consoleBaseurl
And path 'eventi'
And param limit = 100
And headers basicAutenticationHeader
When method get
Then status 200

Given url consoleBaseurl
And path 'eventi'
And param limit = 101
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Paginazione a cursore

# Modalita' alternativa a quella per pagina: si attiva con ?cursor, la risposta
# porta nextCursor al posto di pagination.

Given url consoleBaseurl
And path 'eventi'
And param cursor = ''
And param limit = 2
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination == '#notpresent'
And match response.nextCursor == '#string'
* def cursore = response.nextCursor
* def primaPagina = $response.results[*].id

Given url consoleBaseurl
And path 'eventi'
And param cursor = cursore
And param limit = 2
And headers basicAutenticationHeader
When method get
Then status 200
* def secondaPagina = $response.results[*].id
* def comuni = karate.filter(secondaPagina, function(x){ return primaPagina.contains(x) })
And match comuni == '#[0]'

Scenario: Cursore e paginazione per pagina sono alternative

Given url consoleBaseurl
And path 'eventi'
And param cursor = ''
And param page = 2
And headers basicAutenticationHeader
When method get
Then status 400
And match response.detail contains 'mutuamente esclusivi'

Given url consoleBaseurl
And path 'eventi'
And param cursor = ''
And param total = true
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Cursore malformato

Given url consoleBaseurl
And path 'eventi'
And param cursor = 'non-un-cursore'
And headers basicAutenticationHeader
When method get
Then status 400
And match response.detail contains 'Cursor'
