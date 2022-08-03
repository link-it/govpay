Feature: Validazione sintattica filtri di ricerca

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gdeBaseurl = govpay_url + '/govpay/backend/api/gde'
* def nomeAPI = '/eventi'

Scenario: Validazione sintattica filtri per data

# No filtri

Given url gdeBaseurl
And path nomeAPI
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""

# Filtro DataDa formato Date

Given url gdeBaseurl
And path nomeAPI
And param dataDa = '2020-01-01'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'Parse attempt failed for value [2020-01-01]'

# Filtro DataA formato Date

Given url gdeBaseurl
And path nomeAPI
And param dataA = '2020-01-01'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'Parse attempt failed for value [2020-01-01]'

# Filtro DataDa formato DateTime

Given url gdeBaseurl
And path nomeAPI
And param dataDa = '2020-01-01T00:00:00.000'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""

# Filtro DataA formato DateTime

Given url gdeBaseurl
And path nomeAPI
And param dataA = '2020-01-01T23:59:59.999'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""

# Filtro DataDa formato DateTime

Given url gdeBaseurl
And path nomeAPI
And param dataDa = '2020-01-01T25:00:00.000'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'Parse attempt failed for value [2020-01-01T25:00:00.000]'

# Filtro DataA formato DateTime

Given url gdeBaseurl
And path nomeAPI
And param dataA = '2020-01-01T25:59:59.999'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'Parse attempt failed for value [2020-01-01T25:59:59.999]'

# Filtro DataDa formato non valido

* def dataDaNonValida = '2020-01-01TTT:00:00.000'
* def dataDaParamName = 'dataDa'

Given url gdeBaseurl
And path nomeAPI
And param dataDa = dataDaNonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'Parse attempt failed for value [2020-01-01TTT:00:00.000]'

# Filtro DataA formato DateTime

* def dataANonValida = '2020-01-01TTT:59:59.999'
* def dataAParamName = 'dataA'

Given url gdeBaseurl
And path nomeAPI
And param dataA = dataANonValida
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'Parse attempt failed for value [2020-01-01TTT:59:59.999]'

Scenario: Validazione sintattica filtri per severita

# No filtri

Given url gdeBaseurl
And path nomeAPI
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""

# Filtro SeveritaDa

Given url gdeBaseurl
And path nomeAPI
And param severitaDa = 0
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""

# Filtro SeveritaA

Given url gdeBaseurl
And path nomeAPI
And param severitaA = 0
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""


# Filtro SeveritaDa e SeveritaA

Given url gdeBaseurl
And path nomeAPI
And param severitaDa = 0
And param severitaA = 0
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	_embedded: '#ignore',
	_links: '#ignore',
	page: {
		size: 25,
		totalElements: '#number',
		totalPages: '#number',
		number: 0
	}
}
"""


# Filtro SeveritaDa formato non valido

* def severitaDa = 'XXX'
* def severitaA = 'XXX'

Given url gdeBaseurl
And path nomeAPI
And param severitaDa = severitaDa
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'java.lang.NumberFormatException: For input string: "'+severitaDa+'"'

# Filtro SeveritaA formato non valido

Given url gdeBaseurl
And path nomeAPI
And param severitaA = severitaA
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'java.lang.NumberFormatException: For input string: "'+severitaA+'"'


# Filtro SeveritaDa formato non valido

* def severitaDa2 = '-1'
* def severitaA2 = '-1'

Given url gdeBaseurl
And path nomeAPI
And param severitaDa = severitaDa2
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'findEventi.severitaDa: Il parametro severitaDa deve contenere un valore >= 0.'

# Filtro SeveritaA formato non valido

Given url gdeBaseurl
And path nomeAPI
And param severitaA = severitaA2
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400

* match response == 
"""
{
	title: 'Bad Request',
	status: 400,
	detail: '#notnull'
}
"""
* match response.detail contains 'findEventi.severitaA: Il parametro severitaA deve contenere un valore >= 0.'


