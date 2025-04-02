Feature: Ricerca e lettura di flussi di rendicontazione con autorizzazione per UO 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-rendicontazioni.feature')
* def applicazioneRequest = read('msg/applicazione_none.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
* def pathServizio = '/flussiRendicontazione'
* def rendicontazioneSchema = read('msg/rendicontazione.json')

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idFlusso>]

Given url ragioneriaBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
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

Given url ragioneriaBaseurl
And path '/flussiRendicontazione', <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idFlusso | httpStatus | risposta | numRisultati | 
| applicazione_none.json | idflusso_dom1 | 403 | errore_auth.json | 0 |
| applicazione_none.json | idflusso_dom1_uo | 403 | errore_auth.json | 0 |
| applicazione_none.json | idflusso_dom1_uo2 | 403 | errore_auth.json | 0 |
| applicazione_none.json | idflusso_dom2_uo | 403 | errore_auth.json | 0 |
| applicazione_none.json | idflusso_dom2 | 403 | errore_auth.json | 0 |

