Feature: Validazione sintattica filtri di ricerca

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def nomeAPI = '/rpp'

Scenario: Validazione sintattica filtri per data

# No filtri

Given url pagamentiBaseurl
And path nomeAPI
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataDa formato Date

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptDa = '2020-01-01'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataA formato Date

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptA = '2020-01-01'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataDa formato DateTime

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptDa = '2020-01-01T00:00:00.000'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataA formato DateTime

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptA = '2020-01-01T23:59:59.999'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataDa formato DateTime

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptDa = '2020-01-01T25:00:00.000'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataA formato DateTime

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptA = '2020-01-01T25:59:59.999'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataDa formato non valido

* def dataRptDaNonValida = '2020-01-01TTT:00:00.000'
* def dataRptDaParamName = 'dataRptDa'

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptDa = dataRptDaNonValida
And headers basicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataRptDaNonValida + '] per il parametro [' + dataRptDaParamName + '] non e\' valido.'

# Filtro DataA formato DateTime

* def dataRptANonValida = '2020-01-01TTT:59:59.999'
* def dataRptAParamName = 'dataRptA'

Given url pagamentiBaseurl
And path nomeAPI
And param dataRptA = dataRptANonValida
And headers basicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataRptANonValida + '] per il parametro [' + dataRptAParamName + '] non e\' valido.'


