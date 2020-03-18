Feature: Validazione sintattica filtri di ricerca

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def nomeAPI = '/rpp'

Scenario: Validazione sintattica filtri per data

# No filtri

Given url backofficeBaseurl
And path nomeAPI
And headers gpAdminBasicAutenticationHeader
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

# Date RPT

# Filtro dataRptDa formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataRptDa = '2020-01-01'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRptA formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataRptA = '2020-01-01'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRptDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRptDa = '2020-01-01T00:00:00.000'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRptA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRptA = '2020-01-01T23:59:59.999'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRptDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRptDa = '2020-01-01T25:00:00.000'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRptA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRptA = '2020-01-01T25:59:59.999'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRptDa formato non valido

* def dataDaNonValida = '2020-01-01TTT:00:00.000'
* def dataDaParamName = 'dataRptDa'

Given url backofficeBaseurl
And path nomeAPI
And param dataRptDa = dataDaNonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataDaNonValida + '] per il parametro [' + dataDaParamName + '] non e\' valido.'

# Filtro dataRptA formato DateTime

* def dataANonValida = '2020-01-01TTT:59:59.999'
* def dataAParamName = 'dataRptA'

Given url backofficeBaseurl
And path nomeAPI
And param dataRptA = dataANonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataANonValida + '] per il parametro [' + dataAParamName + '] non e\' valido.'

# date RT

# Filtro dataRtDa formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataRtDa = '2020-01-01'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRtA formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataRtA = '2020-01-01'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRtDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRtDa = '2020-01-01T00:00:00.000'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRtA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRtA = '2020-01-01T23:59:59.999'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRtDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRtDa = '2020-01-01T25:00:00.000'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRtA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRtA = '2020-01-01T25:59:59.999'
And headers gpAdminBasicAutenticationHeader
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

# Filtro dataRtDa formato non valido

* def dataDaNonValida = '2020-01-01TTT:00:00.000'
* def dataDaParamName = 'dataRtDa'

Given url backofficeBaseurl
And path nomeAPI
And param dataRtDa = dataDaNonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataDaNonValida + '] per il parametro [' + dataDaParamName + '] non e\' valido.'

# Filtro dataRtA formato DateTime

* def dataANonValida = '2020-01-01TTT:59:59.999'
* def dataAParamName = 'dataRtA'

Given url backofficeBaseurl
And path nomeAPI
And param dataRtA = dataANonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataANonValida + '] per il parametro [' + dataAParamName + '] non e\' valido.'


