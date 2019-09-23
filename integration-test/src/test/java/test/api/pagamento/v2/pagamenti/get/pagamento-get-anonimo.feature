Feature: Dettaglio pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v2.feature')

Scenario Outline: Lettura dettaglio pagamento utente anonimo: [<idPagamento>]

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/pagamenti', <idPagamento>
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| idPagamento | httpStatus | risposta |
| idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente_anonimo.json |
| idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |

