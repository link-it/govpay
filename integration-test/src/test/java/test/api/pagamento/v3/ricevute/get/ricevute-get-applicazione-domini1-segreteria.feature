Feature: Dettaglio pagamenti utenza applicazione

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')
* def applicazioneRequest = read('msg/applicazione_domini1_segreteria.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})

@test1
Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della transazione [<rpt>] in formato json e pdf

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers idA2ABasicAutenticationHeader
And headers { 'Accept' : 'application/json' }
When method get
Then status <httpStatus>
And match response == risposta

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers idA2ABasicAutenticationHeader
And headers { 'Accept' : 'application/pdf' }
When method get
Then status <httpStatus>

Examples:
| applicazione | rpt | httpStatus | risposta |
| applicazione_domini1_segreteria.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_noneseguito_ente.json |


@test1
Scenario Outline: Ricerca pagamenti per idDominio/IUV utenza applicazione [<applicazione>] della transazione [<rpt>]

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | rpt | httpStatus | risposta |
| applicazione_domini1_segreteria.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 200 | lista_vuota.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 200 | lista_vuota.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json |


