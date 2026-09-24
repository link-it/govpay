Feature: Esecuzione di una operazione (console-api v2)

# Corrispettivo v2 della parte di esecuzione di
# test/api/backoffice/v1/operazioni/get/operazioni-find.feature, dove le
# operazioni si eseguivano con una GET sull'identificativo.
#
# In v2 chiedere una esecuzione e' una POST su una sotto-risorsa, e la risposta
# e' 202 con la rappresentazione dell'esecuzione: identificativo, stato e date.
#
# L'operazione esercitata e' RESET_CACHE, che e' interna e senza effetti sui
# dati: eseguire qui i batch veri, come faceva la v1 con chiusuraRptScadute o
# elaborazioneRiconciliazioni, significherebbe far partire i batch esterni.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idOperazione = 'RESET_CACHE'

Scenario: Richiesta di esecuzione

Given url consoleBaseurl
And path 'operazioni', idOperazione, 'esecuzioni'
And headers basicAutenticationHeader
And request { }
When method post
Then status 202
And match response.idOperazione == idOperazione
And match response.idEsecuzione == '#notnull'
And match response.stato == '#notnull'
And match response.dataInizio == '#notnull'

Scenario: Richiesta di esecuzione di una operazione inesistente

Given url consoleBaseurl
And path 'operazioni', 'OPERAZIONE_CHE_NON_ESISTE', 'esecuzioni'
And headers basicAutenticationHeader
And request { }
When method post
Then status 404

Scenario: Richiesta di esecuzione senza autenticazione

Given url consoleBaseurl
And path 'operazioni', idOperazione, 'esecuzioni'
And request { }
When method post
Then status 401
