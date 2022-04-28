Feature: Dettaglio pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

@test1
Scenario Outline: Lettura dettaglio pagamento utente anonimo

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/rpp', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| rpt | httpStatus | risposta |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_noneseguito_ente.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_noneseguito_ente.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |

@test2
Scenario Outline: Lettura ricevute pagamento utente anonimo

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Given url pagamentiBaseurl
And path '/rpp', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento, 'rt'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>

Examples:
| rpt | httpStatus |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 403 |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 |
| rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 403 |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 |


@test3
Scenario Outline: Lettura ricevute pagamento utente anonimo formato pdf

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Given url pagamentiBaseurl
And path '/rpp', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento, 'rt'
And headers idA2ABasicAutenticationHeader
And headers { 'Accept' : 'application/pdf' }
And param visualizzaSoggettoDebitore = true
When method get
Then status <httpStatus>

Examples:
| rpt | httpStatus |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 |
| rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 |


