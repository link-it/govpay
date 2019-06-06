Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/pendenze/v1/bunch-pendenze.feature')

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della pendenza [<idPendenza>]

* def applicazione = read('msg/<applicazione>')
* def risposta = read('msg/<risposta>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pendenze', <idA2A>, <idPendenza>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | idA2A | idPendenza | httpStatus | risposta |
| applicazione_star_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_star_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |



