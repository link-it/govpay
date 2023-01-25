Feature: Ricerca pagamenti per idDominio/IUV utenza applicazione

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})

@test1
Scenario Outline: Ricerca pagamenti per idDominio/IUV utenza applicazione [<applicazione>] della transazione [<rpt>]

* def risposta = read('msg/<risposta>')
* def applicazione = read('msg/<applicazione>')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | rpt | httpStatus | risposta |
| applicazione_star_star.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json |
| applicazione_star_star.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json |
| applicazione_domini1_star.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json |
| applicazione_domini1e2_segreteria.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| applicazione_domini1e2_segreteria.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | lista_ricevute.json |
| applicazione_nonAuth.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
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
