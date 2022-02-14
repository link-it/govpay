Feature: Ricerca per filtri su idFlusso

Background:

* def pathServizio = '/flussiRendicontazione'

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione>.

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url ragioneriaBaseurl
And path pathServizio
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizioFR
And param dataA = dataFineFR
And param idFlusso = idflusso_dom1_1
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
| applicazione | numRisultati | 
| applicazione_star.json | 1 |
| applicazione_dominio1.json | 1 |
| applicazione_dominio2.json | 0 |
| applicazione_nonAuthDominio.json | 0 |
