Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni.feature')
* def errore_auth = read('msg/errore_auth.json')

* def applicazioneRequest = read('msg/applicazione_auth.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della riconciliazione [<idRiconciliazione>]

Given url ragioneriaBaseurl
And path '/incassi', idDominio, <idRiconciliazione>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == <risposta>

Examples:
| applicazione | idRiconciliazione | httpStatus | risposta |
| applicazione_auth.json | idRiconciliazioneSin_DOM1_A2A | 200 | riconciliazioneSin_DOM1_A2A |
| applicazione_auth.json | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth |
| applicazione_auth.json | idRiconciliazioneCum_DOM1_A2A | 200 | riconciliazioneCum_DOM1_A2A |
| applicazione_auth.json | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth |

Scenario: Verifico che la find restituisca tutti e sole le riconciliazioni caricate dal verticale

Given url ragioneriaBaseurl
And path '/incassi'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
When method get
Then status 200
And match response.risultati[0].idIncasso == idRiconciliazioneCum_DOM1_A2A
And match response.risultati[1].idIncasso == idRiconciliazioneSin_DOM1_A2A
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