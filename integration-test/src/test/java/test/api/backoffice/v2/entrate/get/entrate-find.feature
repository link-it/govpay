Feature: Ricerca entrate con i filtri (console-api v2)

# Non ha un corrispettivo diretto in v1, dove i filtri sulle entrate non erano
# sotto test: la v2 li dichiara nell'OpenAPI, idEntrata e descrizione, ed
# entrambi fanno match parziale.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Ricerca per idEntrata, match parziale

Given url consoleBaseurl
And path 'entrate'
And param idEntrata = 'SEGRETERIA'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 0]'
And match each response.results[*].idEntrata contains 'SEGRETERIA'

Scenario: Ricerca per descrizione, match parziale

Given url consoleBaseurl
And path 'entrate'
And param descrizione = 'segreteria'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 0]'

Scenario: Ricerca con un filtro che non seleziona nulla

Given url consoleBaseurl
And path 'entrate'
And param idEntrata = 'NESSUNA_ENTRATA_COSI'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'
