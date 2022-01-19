Feature: Accesso ai flussi di rendicontazione per idFlusso e dataOraFlusso

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-idFlussoNonUnivoco-v2.feature')

Scenario: Ricerca rendicontazioni da applicazione applicazione_dominio1.

* def applicazione = read('msg/applicazione_dominio1e2.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url ragioneriaBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizioFR
And param dataA = dataFineFR
When method get
Then status 200
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

* def findResponse = response
* def idDominio = findResponse.risultati[0].dominio.idDominio
* def idFlusso = findResponse.risultati[0].idFlusso
* def dataFlusso = findResponse.risultati[0].dataFlusso

Given url ragioneriaBaseurl
And path 'flussiRendicontazione', idDominio, idFlusso, dataFlusso 
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso
And match response.rendicontazioni == '#[6]'

Given url ragioneriaBaseurl
And path 'flussiRendicontazione', idDominio, idFlusso 
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso
And match response.rendicontazioni == '#[6]'


* def idDominio = findResponse.risultati[1].dominio.idDominio
* def idFlusso = findResponse.risultati[1].idFlusso
* def dataFlusso = findResponse.risultati[1].dataFlusso

Given url ragioneriaBaseurl
And path 'flussiRendicontazione', idDominio, idFlusso, dataFlusso 
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso
And match response.rendicontazioni == '#[4]'

Given url ragioneriaBaseurl
And path 'flussiRendicontazione', idDominio, idFlusso 
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso
And match response.rendicontazioni == '#[4]'

Scenario: Test per bug conteggio risultati backoffice

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers gpAdminBasicAutenticationHeader
And param dataDa = dataInizioFR
And param dataA = dataFineFR
When method get
Then status 200
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
