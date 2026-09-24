Feature: Esecuzioni di una operazione (console-api v2)

# Non ha corrispettivo in v1: le esecuzioni non erano una risorsa, l'unica cosa
# osservabile era l'esito della richiesta di esecuzione.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idOperazione = 'RESET_CACHE'

Scenario: Elenco paginato delle esecuzioni

Given url consoleBaseurl
And path 'operazioni', idOperazione, 'esecuzioni'
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

Scenario: Elenco delle esecuzioni di una operazione inesistente

Given url consoleBaseurl
And path 'operazioni', 'OPERAZIONE_CHE_NON_ESISTE', 'esecuzioni'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Dettaglio di una esecuzione inesistente

Given url consoleBaseurl
And path 'operazioni', idOperazione, 'esecuzioni', '999999999'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'operazioni', idOperazione, 'esecuzioni'
When method get
Then status 401
