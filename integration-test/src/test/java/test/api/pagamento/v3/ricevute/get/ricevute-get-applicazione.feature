Feature: Dettaglio pagamenti utenza applicazione

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

@test1
Scenario Outline: Lettura dettaglio pagamento utenza applicazione

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})
* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers idA2ABasicAutenticationHeader
And headers { 'Accept' : 'application/json' }
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| rpt | httpStatus | risposta |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json |
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
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_noneseguito_ente.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |

@test2
Scenario Outline: Lettura ricevute pagamento utenza applicazione formato pdf

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers idA2ABasicAutenticationHeader
And headers { 'Accept' : 'application/pdf' }
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


Scenario: Verifica header accept

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})

Given url pagamentiBaseurl
And headers idA2ABasicAutenticationHeader
And path '/ricevute', rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.dominio.identificativoDominio, rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento, rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.codiceContestoPagamento
When method get
Then status 406

