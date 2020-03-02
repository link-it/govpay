Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/v1/pendenze/bunch-pendenze.feature')

Scenario Outline: Lettura dettaglio operatore [<operatore>] della pendenza [<numeroAvviso>]

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
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/pendenze', <idA2A>, <idPendenza>
And headers spidHeadersRossi
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| operatore | idA2A | idPendenza | httpStatus | risposta |
| operatore_star_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_star.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |

Scenario Outline: Lettura dettaglio operatore [<operatore>] della pendenza [<numeroAvviso>]

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
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/pendenze/byAvviso', <identificativoDominio>, <numeroAvviso>
And headers spidHeadersRossi
When method get
Then status <httpStatus>
And match response == risposta


Examples:
| operatore | identificativoDominio | numeroAvviso | httpStatus | risposta |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_star_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_star.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1_star.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_domini1e2_segreteria.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_nonAuth.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio_2 | numeroAvviso_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| operatore_disabilitato.json | idDominio | numeroAvviso_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 403 | errore_auth.json |



