Feature: Lettura dettaglio flusso rendicontazione  

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
* def rendicontazioneSchema = read('msg/rendicontazione.json')

* def applicazioneRequest = read('msg/applicazione_dominio2.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idFlusso>]

Given url ragioneriaBaseurl
And path '/flussiRendicontazione', <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idFlusso | httpStatus | risposta |
| applicazione_dominio2.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |

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
| applicazione_dominio2.json | 1 |