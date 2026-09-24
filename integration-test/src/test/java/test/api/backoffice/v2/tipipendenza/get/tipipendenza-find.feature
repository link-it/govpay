Feature: Ricerca tipi pendenza con i filtri (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/tipipendenza/get/tipipendenza-find.feature e
# tipipendenza-find-byNonAssociati.feature.
#
# Il filtro che in v1 si chiamava nonAssociati era un booleano da usare insieme
# a idDominio; in v2 nonAssociati e' il codice fiscale del dominio, undici
# cifre, e vale da solo.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idTipoPendenza = 'TIPO_TEST_V2'
* def creaTipo = call read('classpath:utils/api/v2/backoffice/crea-tipo-pendenza.feature') { idTipoPendenza: '#(idTipoPendenza)' }

Scenario: Ricerca per idTipoPendenza, match parziale

Given url consoleBaseurl
And path 'tipiPendenza'
And param idTipoPendenza = 'LIBER'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 0]'
And match each response.results[*].idTipoPendenza contains 'LIBER'

Scenario: Ricerca per descrizione, match parziale

Given url consoleBaseurl
And path 'tipiPendenza'
And param descrizione = 'libera'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 0]'

Scenario: Ricerca per abilitato

Given url consoleBaseurl
And path 'tipiPendenza'
And param abilitato = true
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].abilitato == true

Scenario: Ricerca dei tipi pendenza non associati a un dominio

# Il tipo creato qui sopra non e' associato ad alcun dominio, quindi deve
# comparire; i tipi associati al dominio non devono comparire.

Given url consoleBaseurl
And path 'tipiPendenza'
And param nonAssociati = '12345678901'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results[*].idTipoPendenza contains idTipoPendenza

Given url consoleBaseurl
And path 'domini', '12345678901', 'tipiPendenza'
And headers basicAutenticationHeader
When method get
Then status 200
* def associatiAlDominio = $response.results[*].idTipoPendenza

Given url consoleBaseurl
And path 'tipiPendenza'
And param nonAssociati = '12345678901'
And param limit = 100
And headers basicAutenticationHeader
When method get
Then status 200
* def nonAssociatiAlDominio = $response.results[*].idTipoPendenza
And match nonAssociatiAlDominio !contains associatiAlDominio

Scenario: Ricerca con nonAssociati di formato errato

# Lo schema vuole undici cifre.

Given url consoleBaseurl
And path 'tipiPendenza'
And param nonAssociati = 'non-un-codice-fiscale'
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Ricerca con un filtro che non seleziona nulla

Given url consoleBaseurl
And path 'tipiPendenza'
And param idTipoPendenza = 'NESSUN_TIPO_COSI'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'
