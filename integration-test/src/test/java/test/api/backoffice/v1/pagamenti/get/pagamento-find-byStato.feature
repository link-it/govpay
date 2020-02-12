Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Controllo di sintassi sul valore del filtro per stato

Given url backofficeBaseurl
And path '/pagamenti'
And param stato = 'STATO_NON_VALIDO' 
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'STATO_NON_VALIDO'

