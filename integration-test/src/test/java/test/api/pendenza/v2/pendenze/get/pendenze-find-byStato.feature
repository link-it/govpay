Feature: Ricerca pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario: Controllo di sintassi sul valore del filtro per stato

Given url pendenzeBaseurl
And path '/pendenze'
And param stato = 'STATO_NON_VALIDO' 
And headers basicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'STATO_NON_VALIDO'



Scenario: Controllo di sintassi sul valore del filtro per stato

Given url pendenzeBaseurl
And path '/pendenze'
And param stato = 'INCASSATA' 
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#notnull',
	risultati: '#[]'
}
"""
And match response.risultati[*].stato contains 'INCASSATA'