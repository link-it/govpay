Feature: Ricerca per filtri sui metadati di paginazione

Background: 

* def pathServizio = '/flussiRendicontazione'

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')

Scenario: Ricerca flussiRendicontazione con iuv impostato 

Given url backofficeBaseurl
And path pathServizio
And param iuv = '1_2_3_4'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '##null',
	risultati: '#[]'
}
"""


Scenario Outline: Ricerca rendicontazioni da operatore <operatore>.

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request read('msg/<operatore>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path pathServizio
And headers spidHeadersRossi
And param dataDa = dataInizioFR
And param dataA = dataFineFR
And param iuv = '1_2_3_4'
When method get
Then status 200
And match response ==
"""
{
	numRisultati: <numRisultati>,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[<numRisultati>]'
}
""" 

Examples:
| operatore | numRisultati | 
| operatore_star.json | 0 |
| operatore_domini1e2.json | 0 |
| operatore_domini1.json | 0 |
| operatore_domini2.json | 0 |
| operatore_none.json | 0 |



