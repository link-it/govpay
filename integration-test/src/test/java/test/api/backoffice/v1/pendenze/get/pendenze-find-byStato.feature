Feature: Ricerca pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Ricerca pendenze operatore filtrato per stato non eseguito

* call sleep(500)
* def dataInizio = getDateTime()
* call sleep(500)
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataScadenza = '1999-12-31'
* call read('classpath:utils/pa-carica-avviso.feature')
* call sleep(500)
* def dataFine = getDateTime()

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine	
And param stato = 'NON_ESEGUITA'	
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""

