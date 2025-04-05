Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v3/ragioneria/bunch-riconciliazioni-v2.feature')
* def applicazioneRequest = read('msg/applicazione_star.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def pathServizio = '/flussiRendicontazione'
* def rendicontazioneSchema = read('msg/rendicontazione.json')

Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione>.

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
| applicazione_star.json | 2 |

Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione>.

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


Scenario: Ricerca flussiRendicontazione con iuv impostato 

Given url ragioneriaBaseurl
And path pathServizio
And param iuv = '1_2_3_4'
And headers idA2ABasicAutenticationHeader
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

Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione>.

Given url ragioneriaBaseurl
And path pathServizio
And headers idA2ABasicAutenticationHeader
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
| applicazione | numRisultati | 
| applicazione_star.json | 0 |


Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idDominio> <idFlusso>]

Given url ragioneriaBaseurl
And path '/flussiRendicontazione', <idDominio>, <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')
And match each response.rendicontazioni[*] == read('msg/rendicontazione.json')

Examples:
| applicazione | idDominio | idFlusso | httpStatus | risposta |
| applicazione_star.json | idDominio | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idDominio_2 | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |






