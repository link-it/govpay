Feature: Catalogo delle operazioni (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/operazioni/get/operazioni-find.feature, per la parte di
# elenco.
#
# Il modello e' cambiato. In v1 /operazioni elencava gli identificativi delle
# operazioni e l'esecuzione si chiedeva con una GET sull'identificativo. In v2
# /operazioni e' un catalogo con nome, descrizione, stato di abilitazione,
# frequenza schedulata e riferimento all'ultima esecuzione, e non e' paginato:
# la risposta e' un array. L'esecuzione e' una risorsa a se', trattata in
# post/operazioni-esecuzioni.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Lista delle operazioni disponibili

Given url consoleBaseurl
And path 'operazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == '#[_ > 0]'
And match each response contains { id: '#string', nome: '#string', abilitata: '#boolean' }

Scenario: Il catalogo contiene le operazioni dei batch esterni

Given url consoleBaseurl
And path 'operazioni'
And headers basicAutenticationHeader
When method get
Then status 200
* def identificativi = $response[*].id
And match identificativi contains 'RESET_CACHE'

Scenario: Lista senza autenticazione

Given url consoleBaseurl
And path 'operazioni'
When method get
Then status 401
