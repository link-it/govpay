Feature: Dettaglio pagamenti utenza applicazione

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')
* def applicazioneRequest = read('msg/applicazione_disabilitato.json')
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
| applicazione_disabilitato.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |


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
| applicazione_disabilitato.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |



