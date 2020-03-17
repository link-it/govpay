Feature: Validazione sintattica filtri di ricerca

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def nomeAPI = '/quadrature/rendicontazioni'

Scenario: Validazione sintattica filtri per data

# No filtri

Given url backofficeBaseurl
And path nomeAPI
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataOraFlussoDa formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoDa = '2020-01-01'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataOraFlussoA formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoA = '2020-01-01'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataOraFlussoDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoDa = '2020-01-01T00:00:00.000'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataOraFlussoA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoA = '2020-01-01T23:59:59.999'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataOraFlussoDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoDa = '2020-01-01T25:00:00.000'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataOraFlussoA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoA = '2020-01-01T25:59:59.999'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataOraFlussoDa formato non valido

* def dataDaNonValida = '2020-01-01TTT:00:00.000'
* def dataDaParamName = 'dataOraFlussoDa'

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoDa = dataDaNonValida
And param gruppi = 'ID_FLUSSO'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataDaNonValida + '] per il parametro [' + dataDaParamName + '] non e\' valido.'

# Filtro dataOraFlussoA formato DateTime

* def dataANonValida = '2020-01-01TTT:59:59.999'
* def dataAParamName = 'dataOraFlussoA'

Given url backofficeBaseurl
And path nomeAPI
And param dataOraFlussoA = dataANonValida
And param gruppi = 'ID_FLUSSO'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataANonValida + '] per il parametro [' + dataAParamName + '] non e\' valido.'


# Filtro dataRendicontazioneDa formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneDa = '2020-01-01'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataRendicontazioneA formato Date

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneA = '2020-01-01'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataRendicontazioneDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneDa = '2020-01-01T00:00:00.000'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataRendicontazioneA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneA = '2020-01-01T23:59:59.999'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataRendicontazioneDa formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneDa = '2020-01-01T25:00:00.000'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataRendicontazioneA formato DateTime

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneA = '2020-01-01T25:59:59.999'
And param gruppi = 'ID_FLUSSO'
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

# Filtro dataRendicontazioneDa formato non valido

* def dataDaNonValida = '2020-01-01TTT:00:00.000'
* def dataDaParamName = 'dataRendicontazioneDa'

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneDa = dataDaNonValida
And param gruppi = 'ID_FLUSSO'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataDaNonValida + '] per il parametro [' + dataDaParamName + '] non e\' valido.'

# Filtro dataRendicontazioneA formato DateTime

* def dataANonValida = '2020-01-01TTT:59:59.999'
* def dataAParamName = 'dataRendicontazioneA'

Given url backofficeBaseurl
And path nomeAPI
And param dataRendicontazioneA = dataANonValida
And param gruppi = 'ID_FLUSSO'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il formato della data indicata [' + dataANonValida + '] per il parametro [' + dataAParamName + '] non e\' valido.'


