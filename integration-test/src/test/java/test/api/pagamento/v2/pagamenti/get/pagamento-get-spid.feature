Feature: Dettaglio pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')



Scenario Outline: Lettura dettaglio pagamento utente spid: [<idPagamento>]

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/pagamenti', <idPagamento>
And headers spidHeaders
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| idPagamento | httpStatus | risposta |
| idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |

