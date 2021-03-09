Feature: Accesso ai flussi di rendicontazione per idFlusso e dataOraFlusso

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')

Scenario: Ricerca rendicontazioni da applicazione applicazione_dominio1.

* def applicazione = read('msg/applicazione_dominio1.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizioFR
And param dataA = dataFineFR
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

* def idFlusso = response.risultati[0].idFlusso
* def dataFlusso = response.risultati[0].dataFlusso

Given url backofficeBaseurl
And path 'flussiRendicontazione', idFlusso, dataFlusso 
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso

Scenario: Ricerca rendicontazioni da operatore operatore_domini1.

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def operatore_request = read('msg/operatore_domini1.json')

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request operatore_request
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers spidHeadersRossi
And param dataDa = dataInizioFR
And param dataA = dataFineFR
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

* def idFlusso = response.risultati[0].idFlusso
* def dataFlusso = response.risultati[0].dataFlusso

Given url backofficeBaseurl
And path 'flussiRendicontazione', idFlusso, dataFlusso
And headers spidHeadersRossi
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso


