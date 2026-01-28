Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni.feature')
* def errore_auth = read('msg/errore_auth.json')

* def applicazioneRequest = read('msg/applicazione_disabilitato.json')
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
| applicazione_disabilitato.json | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth |
| applicazione_disabilitato.json | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth |
| applicazione_disabilitato.json | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth |
| applicazione_disabilitato.json | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth |

Scenario: Applicazione disabilitata

Given url ragioneriaBaseurl
And path '/incassi'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403