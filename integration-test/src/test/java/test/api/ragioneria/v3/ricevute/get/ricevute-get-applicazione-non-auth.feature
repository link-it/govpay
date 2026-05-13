Feature: Dettaglio ricevute

Background:

* callonce read('classpath:utils/workflow/modellounico/v1/modellounico-bunch-pagamenti-v3.feature')
* def applicazioneRequest = read('msg/applicazione_nonAuth.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della transazione

* def risposta = read('msg/<risposta>')
* def idDominioDet = <rpt>.transferList.transfer[0].fiscalCodePA
* def iuvDet = <rpt>.creditorReferenceId
* def ccpDet = '3' + iuvDet

Given url ragioneriaBaseurl
And path '/ricevute', idDominioDet, iuvDet, ccpDet
And headers idA2ABasicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | rpt | httpStatus | risposta |
| applicazione_nonAuth.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |

Scenario: Ricerca pagamenti BASIC filtrati per data non autorizzato

Given url ragioneriaBaseurl
And path '/ricevute'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 403


