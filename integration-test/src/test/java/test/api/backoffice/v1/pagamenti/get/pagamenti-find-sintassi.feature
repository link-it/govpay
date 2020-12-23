Feature: Validazione sintattica filtri di ricerca

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def nomeAPI = '/pagamenti'

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
	maxRisultati: '#number',
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""

# Filtro DataDa formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataDa = '2020-01-01'
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

# Filtro DataA formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataA = '2020-01-01'
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

# Filtro DataDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataDa = '2020-01-01T00:00:00.000'
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

# Filtro DataA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataA = '2020-01-01T23:59:59.999'
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

# Filtro DataDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataDa = '2020-01-01T25:00:00.000'
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

# Filtro DataA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataA = '2020-01-01T25:59:59.999'
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

# Filtro DataDa formato non valido

* def dataDaNonValida = '2020-01-01TTT:00:00.000'
* def dataDaParamName = 'dataDa'

Given url backofficeBaseurl
And path nomeAPI
And param dataDa = dataDaNonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataDaNonValida + '] per il parametro [' + dataDaParamName + '] non e\' valido.'

# Filtro DataA formato DateTime

* def dataANonValida = '2020-01-01TTT:59:59.999'
* def dataAParamName = 'dataA'

Given url backofficeBaseurl
And path nomeAPI
And param dataA = dataANonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataANonValida + '] per il parametro [' + dataAParamName + '] non e\' valido.'


# Filtro SeveritaDa formato non valido

* def severitaDa = 'XXX'
* def severitaA = 'XXX'

Given url backofficeBaseurl
And path nomeAPI
And param severitaDa = severitaDa
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il valore indicato per il parametro [severitaDa] non e\' valido: il valore fornito [' + severitaDa + '] non e\' un intero.'

# Filtro SeveritaA formato non valido

Given url backofficeBaseurl
And path nomeAPI
And param severitaA = severitaA
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il valore indicato per il parametro [severitaA] non e\' valido: il valore fornito [' + severitaA + '] non e\' un intero.'




