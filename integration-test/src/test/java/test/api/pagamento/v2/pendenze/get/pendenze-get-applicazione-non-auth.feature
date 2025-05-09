Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/v2/pendenze/bunch-pendenze.feature')
* def applicazioneRequest = read('msg/applicazione_nonAuth.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della pendenza [<idPendenza>]

* def risposta = read('msg/<risposta>')

Given url pendenzeBaseurl
And path '/pendenze', <idA2A>, <idPendenza>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | idA2A | idPendenza | httpStatus | risposta |
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


Scenario: Ricerca pendenze applicazione filtrati per data, senza autorizzazione

Given url pendenzeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 403
And match response == 
"""
{ 
	categoria: 'AUTORIZZAZIONE', 
	codice: '#notnull', 
	descrizione: 'Operazione non autorizzata', 
	dettaglio: '#notnull' 
}
"""

