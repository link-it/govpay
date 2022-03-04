Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/v2/pendenze/bunch-pendenze.feature')

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
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/pendenze', <idA2A>, <idPendenza>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | idA2A | idPendenza | httpStatus | risposta |
| applicazione_auth.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_auth.json | idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| applicazione_auth.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_auth.json | idA2A2 | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 | 403 | errore_auth.json |
| applicazione_auth.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
| applicazione_auth.json | idA2A2 | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | pendenza-get-dettaglio.json |
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



Scenario Outline: Lettura dettaglio pendenza con dati allegati

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_bollo.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

* set pendenzaPut.datiAllegati = <datiAllegati>

* def applicazione = read('msg/applicazione_auth.json')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def dataPutStart = getDateTime()

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def dataPutEnd = getDateTime()

Given url pendenzeBaseurl
And path '/pendenze'
And param idA2A = idA2A 
And param dataDa = dataPutStart 
And param dataA = dataPutEnd
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].datiAllegati == <datiAllegati>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.datiAllegati == <datiAllegati>

Examples:
| datiAllegati |
| "datoAllegato" |
| 10 |
| [ "datoAllegato1" , "datoAllegato2" ] |
| [ 10 , 20 ] |
| [ datoAllegato1: 10 , datoAllegato2: 20 ] |


