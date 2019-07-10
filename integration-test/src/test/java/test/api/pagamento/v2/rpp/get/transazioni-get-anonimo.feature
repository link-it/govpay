Feature: Dettaglio pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

Scenario Outline: Lettura dettaglio pagamento utente anonimo

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/rpp', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| rpt | httpStatus | risposta |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_incorso_ente_anonimo.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |