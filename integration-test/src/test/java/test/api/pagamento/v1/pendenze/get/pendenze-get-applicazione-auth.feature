Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/v1/pendenze/bunch-pendenze.feature')
* def applicazioneRequest = read('msg/applicazione_auth.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})

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


Scenario: Ricerca pendenze applicazione autorizzata filtrati per data


Given url pendenzeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[4].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[6].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
And match response.risultati[7].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
And match response.risultati[8].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
And match response.risultati[9].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[10].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
And match response.risultati[11].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[12].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[13].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[14].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
And match response.risultati[15].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'


And match response == 
"""
{
	numRisultati: 16,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[16]'
}
"""