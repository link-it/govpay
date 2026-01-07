Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-v2.feature')
* def applicazioneRequest = read('msg/applicazione_nonAuthDominio.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
* def pathServizio = '/flussiRendicontazione'
* def rendicontazioneSchema = read('msg/rendicontazione.json')

* callonce sleep(10000)

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
| applicazione_nonAuthDominio.json | 0 |

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
| applicazione_nonAuthDominio.json | 0 |

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
| applicazione_nonAuthDominio.json | 0 |

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idFlusso>]

Given url ragioneriaBaseurl
And path '/flussiRendicontazione', <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idFlusso | httpStatus | risposta |
| applicazione_nonAuthDominio.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuthDominio.json | idflusso_dom2_1 | 403 | errore_auth.json |


