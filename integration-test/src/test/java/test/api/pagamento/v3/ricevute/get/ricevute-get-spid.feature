Feature: Dettaglio pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

* configure cookies = null

@test1
Scenario Outline: Lettura dettaglio pagamento utente spid: [<idPagamento>] della transazione [<rpt>] in formato json

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers spidHeaders
And headers { 'Accept' : 'application/json' }
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| rpt | httpStatus | risposta | idPagamento |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json | idMessaggioRichiesta_Anonimo_INCORSO_DOM1_SEGRETERIA |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 |
| rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json | idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json | idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_noneseguito_ente.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 |

@test2
Scenario Outline: Lettura dettaglio pagamento utente spid versante: [<idPagamento>] della transazione [<rpt>] in formato pdf

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers spidHeaders
And headers { 'Accept' : 'application/pdf' }
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| rpt | httpStatus | risposta | idPagamento |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 |


@test3
Scenario Outline: Lettura dettaglio pagamento utente spid versante: [<idPagamento>] della transazione [<rpt>] in formato pdf

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers spidHeaders
And headers { 'Accept' : 'application/pdf' }
When method get
Then status <httpStatus>

Examples:
| rpt | httpStatus | idPagamento |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 |


Scenario: Verifica header accept

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

Given url pagamentiBaseurl
And headers spidHeaders
And path '/ricevute', rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.dominio.identificativoDominio, rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento, rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.codiceContestoPagamento
When method get
Then status 406

