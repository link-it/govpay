Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')

Scenario Outline: Lettura dettaglio operatore <operatore> [<idPagamento>]

* def operatore = read('msg/<operatore>')
* def risposta = read('msg/<risposta>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

Given url backofficeBaseurl
And path '/pagamenti', <idPagamento>
And headers spidHeaders
When method get
Then status <httpStatus>
And match response == risposta	

Examples:
| operatore | idPagamento | httpStatus | risposta |
| operatore_none_none.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_none_none.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| operatore_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 200 | pagamento-get-singolo_rifiutato_ente.json |
| operatore_domini1_star.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1_star.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_domini1e2_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_domini1e2_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| operatore_star_star.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| operatore_star_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_star_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_star_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_star_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_star_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_star_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_star_star.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_star_star.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 200 | pagamento-get-singolo_rifiutato_ente.json |
| operatore_star_star.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_incorso_ente.json |
| operatore_star_star.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_star_star.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_star_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_star_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| operatore_star_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| operatore_star_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
