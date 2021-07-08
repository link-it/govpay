Feature: Ricerca per filtri sui metadati di paginazione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pathServizio = '/riconciliazioni'

Scenario: Ricerca riconciliazioni senza filtri sui metadati di paginazione

Given url ragioneriaBaseurl
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

Scenario: Ricerca riconciliazioni con metadatiPaginazione true

Given url ragioneriaBaseurl
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

Scenario: Ricerca riconciliazioni con metadatiPaginazione false

Given url ragioneriaBaseurl
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

Scenario: Ricerca riconciliazioni con metadatiPaginazione true e risultatiPerPagina = 0

Given url ragioneriaBaseurl
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

Scenario: Ricerca riconciliazioni con maxRisultati true

Given url ragioneriaBaseurl
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

Scenario: Ricerca riconciliazioni con maxRisultati false

Given url ragioneriaBaseurl
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