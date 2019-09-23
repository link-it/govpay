Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v3.feature')

Scenario Outline: Lettura dettaglio operatore [<operatore>] della riconciliazione [<idRiconciliazione>]

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
And path 'incassi', <idDominio>, <idRiconciliazione>
And headers spidHeadersRossi
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| operatore | idDominio | idRiconciliazione | httpStatus | risposta |
| operatore_disabilitato.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 403 | errore_auth.json |
| operatore_domini1.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 403 | errore_auth.json |
| operatore_domini1.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 403 | errore_auth.json |
| operatore_domini1.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 403 | errore_auth.json |
| operatore_domini1.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 403 | errore_auth.json |
| operatore_domini2.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth.json |
| operatore_domini2.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth.json |
| operatore_domini2.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth.json |
| operatore_domini2.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth.json |
| operatore_none.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth.json |
| operatore_none.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth.json |
| operatore_none.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth.json |
| operatore_none.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 403 | errore_auth.json |