Feature: Ricerca per filtri sui metadati di paginazione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pathServizio = '/rpp'

Scenario: Ricerca tracciati senza filtri sui metadati di paginazione

Given url pendenzeBaseurl
And path pathServizio
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione true

Given url pendenzeBaseurl
And path pathServizio
And param metadatiPaginazione = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione false

Given url pendenzeBaseurl
And path pathServizio
And param metadatiPaginazione = false
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notpresent',
	numPagine: '#notpresent',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione true e risultatiPerPagina = 0

Given url pendenzeBaseurl
And path pathServizio
And param metadatiPaginazione = true
And param risultatiPerPagina = 0
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notpresent',
	risultatiPerPagina: 0,
	pagina: '#notnull',
	prossimiRisultati: '#notpresent',
	risultati: '#[0]'
}
"""

Scenario: Ricerca tracciati con maxRisultati true

Given url pendenzeBaseurl
And path pathServizio
And param maxRisultati = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con maxRisultati false

Given url pendenzeBaseurl
And path pathServizio
And param maxRisultati = false
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""