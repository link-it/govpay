Feature: Ricerca pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def autenticationHeader = idA2ABasicAutenticationHeader
* def idA2APendenza = idA2A
* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }

* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* def idPendenzaI = idPendenza 
* def dataInizio = getDateTime()
* call sleep(1000)
* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza1 = idPendenza 
* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza2 = idPendenza 
* call sleep(1000)
* def dataFine = getDateTime()
* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* def idPendenzaF = idPendenza
 
Scenario: Ricerca pendenze operatore filtrato per intervallo di date

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio	
And param dataA = dataFine	
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[1].idPendenza == '#(""+ idPendenza1)'
And match response.risultati[0].idPendenza == '#(""+ idPendenza2)'
And match response == 
"""
{
	numRisultati: 2,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
"""

Scenario: Ricerca pendenze operatore filtrato per data inizio

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio	
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[2].idPendenza == '#(""+ idPendenza1)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza2)'
And match response.risultati[0].idPendenza == '#(""+ idPendenzaF)'
And match response == 
"""
{
	numRisultati: 3,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[3]'
}
"""


Scenario: Ricerca pendenze operatore filtrato per data fine

Given url backofficeBaseurl
And path '/pendenze'
And param dataA = dataFine	
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[2].idPendenza == '#(""+ idPendenzaI)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza1)'
And match response.risultati[0].idPendenza == '#(""+ idPendenza2)'


