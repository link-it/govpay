Feature: Validazione sintattica filtri di ricerca per stato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def nomeAPI = '/incassi'

Scenario: Validazione sintattica filtri per stato

# No filtri

Given url backofficeBaseurl
And path nomeAPI
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati: '#number',
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro stato = 'IN_ELABORAZIONE'

Given url backofficeBaseurl
And path nomeAPI
And param stato = 'IN_ELABORAZIONE'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati: '#number',
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro stato = 'ACQUISITO'

Given url backofficeBaseurl
And path nomeAPI
And param stato = 'ACQUISITO'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati: '#number',
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro stato = 'ERRORE'

Given url backofficeBaseurl
And path nomeAPI
And param stato = 'ERRORE'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	maxRisultati: '#number',
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro stato  non valido

* def dataDaNonValida = 'STATO_NON_VALIDO'
* def dataDaParamName = 'stato'

Given url backofficeBaseurl
And path nomeAPI
And param dataDa = dataDaNonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'STATO_NON_VALIDO'
