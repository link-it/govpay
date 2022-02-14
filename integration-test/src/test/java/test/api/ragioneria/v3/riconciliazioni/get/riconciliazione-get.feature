Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v3/ragioneria/bunch-riconciliazioni.feature')
* def errore_auth = read('msg/errore_auth.json')

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della riconciliazione [<idRiconciliazione>]

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, <idRiconciliazione>
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
| applicazione_nonAuthDominio.json | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth |
| applicazione_nonAuthDominio.json | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth |
| applicazione_nonAuthDominio.json | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth |
| applicazione_nonAuthDominio.json | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth |
| applicazione_nonAuthServizio.json | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth |
| applicazione_nonAuthServizio.json | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth |
| applicazione_nonAuthServizio.json | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth |
| applicazione_nonAuthServizio.json | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth |
| applicazione_disabilitato.json | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth |
| applicazione_disabilitato.json | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth |
| applicazione_disabilitato.json | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth |
| applicazione_disabilitato.json | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth |

Scenario Outline: Controllo sintassi idRiconciliazione

* def idRiconciliazione = <value>
* def applicazione = read('msg/applicazione_auth.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers idA2ABasicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>

Examples:
| value |  fieldName | 
| 'importo null' | 'id' |
| '123456789012345678901234567890123456' | 'id' |

