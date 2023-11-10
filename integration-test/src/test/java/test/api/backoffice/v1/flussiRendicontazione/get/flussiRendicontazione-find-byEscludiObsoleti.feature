Feature: Ricerca per filtro escludiObsoleti

Background: 

* def pathServizio = '/flussiRendicontazione'

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')

Scenario: Ricerca flussiRendicontazione senza filtri

Given url backofficeBaseurl
And path pathServizio
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati: '#notnull',
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""

Scenario: Ricerca flussiRendicontazione senza filtri

Given url backofficeBaseurl
And path pathServizio
And param escludiObsoleti = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati: '#notnull',
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""