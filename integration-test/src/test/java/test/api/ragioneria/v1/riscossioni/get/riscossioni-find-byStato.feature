Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

Scenario: Controllo di sintassi sul valore del filtro per stato

Given url ragioneriaBaseurl
And path '/riscossioni'
And param stato = 'STATO_NON_VALIDO' 
And headers basicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'STATO_NON_VALIDO'

