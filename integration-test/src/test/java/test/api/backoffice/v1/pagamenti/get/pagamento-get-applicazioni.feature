Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')

Scenario Outline: Lettura dettaglio applicazione <applicazione> [<idPagamento>]

* def applicazione = read('msg/<applicazione>')
* def risposta = read('msg/<risposta>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti', <idPagamento>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta	

Examples:
| applicazione | idPagamento | httpStatus | risposta |
| applicazione_none_none.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 200 | pagamento-get-singolo_rifiutato_ente.json |
| applicazione_none_none.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_none_none.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_none_none.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_none_none.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_none_none.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_none_none.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_none_none.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_disabilitato.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 200 | pagamento-get-singolo_rifiutato_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 200 | pagamento-get-singolo_rifiutato_ente.json |
| applicazione_domini1_star.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_domini1_star.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 200 | pagamento-get-singolo_rifiutato_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_domini1e2_segreteria.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_nonAuth.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_star_star.json | idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO | 200 | pagamento-get-singolo_rifiutato_ente.json |
| applicazione_star_star.json | idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_incorso_ente.json |
| applicazione_star_star.json | idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_star_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | pagamento-get-singolo_noneseguito_ente.json |
| applicazione_star_star.json | idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | pagamento-get-singolo_noneseguito_ente.json |
