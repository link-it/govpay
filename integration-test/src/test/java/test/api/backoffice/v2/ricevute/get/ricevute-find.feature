Feature: Ricerca delle ricevute (console-api v2)

# Corrispettivo v2 della parte di lettura di
# test/api/backoffice/v1/ricevute/post/ricevute-post-modellounico.feature, che
# eseguiva un pagamento completo e poi leggeva /ricevute e /rpp/.../rt.
#
# Qui si verifica la sola lettura, sulle ricevute gia' presenti nell'ambiente:
# il pagamento e' esercitato dalle feature di workflow, e ripeterlo qui
# vorrebbe dire provare due volte la stessa cosa. Le ricevute in v2 sono una
# risorsa di sola lettura, con rpt e rt come sotto-risorse.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Elenco paginato delle ricevute

Given url consoleBaseurl
And path 'ricevute'
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	results: '#[]',
	pagination: { page: '#number', limit: '#number', hasNextPage: '#boolean' }
}
"""

Scenario: Elenco con total

Given url consoleBaseurl
And path 'ricevute'
And param total = true
And headers basicAutenticationHeader
When method get
Then status 200
And match response.pagination.totalResults == '#number'

Scenario: Filtro per dominio

Given url consoleBaseurl
And path 'ricevute'
And param idDominio = '12345678901'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].idDominio == '12345678901'

Scenario: Filtro per iuv

Given url consoleBaseurl
And path 'ricevute'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[1]'
* def iuv = response.results[0].iuv

Given url consoleBaseurl
And path 'ricevute'
And param iuv = iuv
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].iuv == iuv

Scenario: Filtro che non seleziona nulla

Given url consoleBaseurl
And path 'ricevute'
And param idDominio = '99999999999'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'

Scenario: Cursore malformato

# Le ricevute ammettono anche la paginazione a cursore, alternativa a quella per
# pagina. Il cursore e' opaco per il chiamante, e uno malformato viene rifiutato.

Given url consoleBaseurl
And path 'ricevute'
And param cursor = 'non-un-cursore'
And headers basicAutenticationHeader
When method get
Then status 400
And match response.detail contains 'Cursor'

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'ricevute'
When method get
Then status 401
