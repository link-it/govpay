Feature: Ricerca pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def autenticationHeader = idA2ABasicAutenticationHeader
* def idA2APendenza = idA2A
* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }



Scenario: Ricerca pendenze operatore filtrato per numero avviso esatto

* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* def numeroAvviso1 = numeroAvviso 
* def idPendenza1 = idPendenza 

Given url backofficeBaseurl
And path '/pendenze'
And param iuv = numeroAvviso1	
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza1)'
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

Scenario: Ricerca pendenze operatore filtrato per numero avviso parziale

* def dataInizio = getDateTime()
* call sleep(1000)
* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* def numeroAvviso1 = numeroAvviso 
* def idPendenza1 = idPendenza 
* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* call sleep(1000)
* def dataFine = getDateTime()

* def numeroAvviso1 = numeroAvviso 
* def idPendenza1 = idPendenza 

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio	
And param dataA = dataFine
And param iuv = tail(numeroAvviso,5)
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].idPendenza == '#(""+ idPendenza1)'

Scenario: Ricerca pendenze operatore filtrato per iuv esatto

* def tipoRicevuta = "R00"
* def cumulativo = "0"
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento-spontaneo.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param iuv = iuv	
And param idDominio = idDominio
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""


Scenario: Ricerca pendenze operatore filtrato per iuv parziale

* def tipoRicevuta = "R00"
* def cumulativo = "0"
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento-spontaneo.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param iuv = iuv	
And param idDominio = idDominio	
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
