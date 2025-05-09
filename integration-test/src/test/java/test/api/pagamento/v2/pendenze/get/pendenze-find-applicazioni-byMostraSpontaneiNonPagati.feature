Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/v2/pendenze/bunch-pendenze.feature')

Scenario: Ricerca pendenze applicazione autorizzata filtrati per data escludendo gli spontanei non pagati

* def applicazione = read('msg/applicazione_auth.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201
* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And param mostraSpontaneiNonPagati = false
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
