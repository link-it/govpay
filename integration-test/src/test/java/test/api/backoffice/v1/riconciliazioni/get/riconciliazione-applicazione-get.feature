Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v3.feature')

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

Given url backofficeBaseurl
And path '/incassi', <idDominio>, <idRiconciliazione>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idDominio | idRiconciliazione | httpStatus | risposta |
| applicazione_disabilitato.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 403 | errore_auth.json |
| applicazione_dominio1.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 200 | riconciliazione-singola-response.json |
| applicazione_dominio1.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 200 | riconciliazione-singola-response.json |
| applicazione_dominio1.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 200 | riconciliazione-cumulativa-response.json |
| applicazione_dominio1.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 200 | riconciliazione-cumulativa-response.json |
| applicazione_dominio1.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 403 | errore_auth.json |
| applicazione_dominio1.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 403 | errore_auth.json |
| applicazione_dominio1.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 403 | errore_auth.json |
| applicazione_dominio1.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 403 | errore_auth.json |
| applicazione_dominio2.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth.json |
| applicazione_dominio2.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth.json |
| applicazione_dominio2.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth.json |
| applicazione_dominio2.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth.json |
| applicazione_dominio2.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 200 | riconciliazione-singola-response.json |
| applicazione_dominio2.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 200 | riconciliazione-singola-response.json |
| applicazione_dominio2.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 200 | riconciliazione-cumulativa-response.json |
| applicazione_dominio2.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 200 | riconciliazione-cumulativa-response.json |
| applicazione_dominio1e2.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 200 | riconciliazione-singola-response.json |
| applicazione_dominio1e2.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 200 | riconciliazione-singola-response.json |
| applicazione_dominio1e2.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 200 | riconciliazione-cumulativa-response.json |
| applicazione_dominio1e2.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 200 | riconciliazione-cumulativa-response.json |
| applicazione_dominio1e2.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 200 | riconciliazione-singola-response.json |
| applicazione_dominio1e2.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 200 | riconciliazione-singola-response.json |
| applicazione_dominio1e2.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 200 | riconciliazione-cumulativa-response.json |
| applicazione_dominio1e2.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 200 | riconciliazione-cumulativa-response.json |
| applicazione_star.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 200 | riconciliazione-singola-response.json |
| applicazione_star.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 200 | riconciliazione-singola-response.json |
| applicazione_star.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 200 | riconciliazione-cumulativa-response.json |
| applicazione_star.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 200 | riconciliazione-cumulativa-response.json |
| applicazione_star.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 200 | riconciliazione-singola-response.json |
| applicazione_star.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 200 | riconciliazione-singola-response.json |
| applicazione_star.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 200 | riconciliazione-cumulativa-response.json |
| applicazione_star.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 200 | riconciliazione-cumulativa-response.json |
| applicazione_none.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 403 | errore_auth.json |
| applicazione_none.json | idDominio | idRiconciliazioneSin_DOM1_A2A2 | 403 | errore_auth.json |
| applicazione_none.json | idDominio | idRiconciliazioneCum_DOM1_A2A | 403 | errore_auth.json |
| applicazione_none.json | idDominio | idRiconciliazioneCum_DOM1_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idDominio_2 | idRiconciliazioneSin_DOM2_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idDominio_2 | idRiconciliazioneCum_DOM2_A2A2 | 403 | errore_auth.json |
