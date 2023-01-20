Feature: Ricerca pagamenti per idDominio/IUV utenza cittadino

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')


@test1
Scenario Outline: Lettura dettaglio pagamento utente spid: [<idPagamento>] transazioni in corso

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento
And headers spidHeaders
When method get
Then status <httpStatus>
And match response == risposta
And match response.numRisultati == <numRisultati>


Examples:
| rpt | httpStatus | risposta | idPagamento | numRisultati |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 200 | lista_vuota.json | idMessaggioRichiesta_Anonimo_INCORSO_DOM1_SEGRETERIA | 0 |
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 200 | lista_vuota.json | idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO | 0 |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 200 | lista_vuota.json | idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE | 0 |

@test1b
Scenario Outline: Lettura dettaglio pagamento utente spid: [<idPagamento>] transazioni completate

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento
And headers spidHeaders
When method get
Then status <httpStatus>
And match response == risposta
And match response.numRisultati == <numRisultati>
And match response.risultati[0].esito == <esito>

Examples:
| rpt | httpStatus | risposta | idPagamento | numRisultati | esito |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 1 | 'ESEGUITO' |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 1 | 'NON_ESEGUITO' |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 1 | 'NON_ESEGUITO' |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 1 | 'NON_ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 1 | 'ESEGUITO' |
| rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 1 | 'NON_ESEGUITO' |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA | 1 | 'ESEGUITO' |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 1 | 'NON_ESEGUITO' |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 1 | 'ESEGUITO' |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 1 | 'NON_ESEGUITO' |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 1 | 'ESEGUITO' |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 1 | 'ESEGUITO' |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 1 | 'NON_ESEGUITO' |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 1 | 'NON_ESEGUITO' |

@test2
Scenario Outline: Lettura dettaglio pagamento utente spid versante: [<idPagamento>]

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento
And headers spidHeaders
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| rpt | httpStatus | risposta | idPagamento |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 404 | notFound.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 404 | notFound.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 404 | notFound.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json | idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 404 | notFound.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json | idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 |


@test3
Scenario Outline: Lettura dettaglio pagamento utente spid versante: [<idPagamento>]

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento
And param visualizzaSoggettoDebitore = true
And headers spidHeaders
When method get
Then status <httpStatus>

Examples:
| rpt | httpStatus | risposta | idPagamento | numRisultati | esito |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 1 | 'ESEGUITO' |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 1 | 'NON_ESEGUITO' |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 1 | 'NON_ESEGUITO' |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 1 | 'NON_ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 1 | 'ESEGUITO' |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json | idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 1 | 'ESEGUITO' |