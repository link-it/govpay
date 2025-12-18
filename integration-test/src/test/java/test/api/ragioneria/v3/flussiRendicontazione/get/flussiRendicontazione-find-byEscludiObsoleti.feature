Feature: Ricerca per filtro escludiObsoleti

Background: 

* def pathServizio = '/flussiRendicontazione'
* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

* callonce sleep(10000)

Scenario: Ricerca flussiRendicontazione senza filtri

Given url ragioneriaBaseurl
And path pathServizio
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""

Scenario: Ricerca flussiRendicontazione senza filtri

Given url ragioneriaBaseurl
And path pathServizio
And param escludiObsoleti = true
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""