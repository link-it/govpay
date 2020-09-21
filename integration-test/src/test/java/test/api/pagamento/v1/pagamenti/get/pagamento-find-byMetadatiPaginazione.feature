Feature: Ricerca per filtri sui metadati di paginazione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pathServizio = '/pagamenti'

Scenario: Ricerca tracciati senza filtri sui metadati di paginazione

Given url pagamentiBaseurl
And path pathServizio
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati : '#ignore',
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione true

Given url pagamentiBaseurl
And path pathServizio
And param metadatiPaginazione = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati : '#ignore',
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione false

Given url pagamentiBaseurl
And path pathServizio
And param metadatiPaginazione = false
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati : '#notpresent',
	numRisultati: '#notpresent',
	numPagine: '#notpresent',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione true e risultatiPerPagina = 0

Given url pagamentiBaseurl
And path pathServizio
And param metadatiPaginazione = true
And param risultatiPerPagina = 0
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati : '#notnull',
	numRisultati: '#notnull',
	numPagine: '#notpresent',
	risultatiPerPagina: 0,
	pagina: '#notnull',
	prossimiRisultati: '#notpresent',
	risultati: '#[0]'
}
"""

Scenario: Ricerca tracciati con maxRisultati true

Given url pagamentiBaseurl
And path pathServizio
And param maxRisultati = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati : '#notnull',
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con maxRisultati false

Given url pagamentiBaseurl
And path pathServizio
And param maxRisultati = false
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati : '#notpresent',
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""