Feature: Ricerca per filtri sui metadati di paginazione

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pathServizio = '/pendenze/tracciati'

Scenario: Ricerca tracciati senza filtri sui metadati di paginazione

Given url backofficeBaseurl
And path pathServizio
And headers gpAdminBasicAutenticationHeader
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
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione true

Given url backofficeBaseurl
And path pathServizio
And param metadatiPaginazione = true
And headers gpAdminBasicAutenticationHeader
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
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con metadatiPaginazione false

Given url backofficeBaseurl
And path pathServizio
And param metadatiPaginazione = false
And headers gpAdminBasicAutenticationHeader
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

Given url backofficeBaseurl
And path pathServizio
And param metadatiPaginazione = true
And param risultatiPerPagina = 0
And headers gpAdminBasicAutenticationHeader
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

Given url backofficeBaseurl
And path pathServizio
And param maxRisultati = true
And headers gpAdminBasicAutenticationHeader
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
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""

Scenario: Ricerca tracciati con maxRisultati false

Given url backofficeBaseurl
And path pathServizio
And param maxRisultati = false
And headers gpAdminBasicAutenticationHeader
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
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""


