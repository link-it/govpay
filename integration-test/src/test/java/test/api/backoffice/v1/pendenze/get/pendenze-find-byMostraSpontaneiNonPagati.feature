Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/v1/pendenze/bunch-pendenze.feature')

Scenario: Ricerca pendenze applicazione star/star filtrati per data senza impostare il filtro sui versamenti spontanei non pagati

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_star_star.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[4].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[6].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
#And match response.risultati[7].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
And match response.risultati[7].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[11].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[8].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[9].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[10].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[15].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[11].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[12].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[13].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
#And match response.risultati[15].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 14,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[14]'
}
"""

Scenario: Ricerca pendenze applicazione star/star filtrati per data escludendo spontanei non pagati

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_star_star.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And param mostraSpontaneiNonPagati = false	
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[4].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[6].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
#And match response.risultati[7].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
And match response.risultati[7].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[11].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[8].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[9].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[10].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[15].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[11].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[12].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[13].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
#And match response.risultati[15].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 14,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[14]'
}
"""

Scenario: Ricerca pagamenti applicazione dominio1/star filtrati per data escludendo spontanei non pagati

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_domini1_star.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And param mostraSpontaneiNonPagati = false	
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[4].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
#And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
And match response.risultati[5].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[9].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[6].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[11].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[7].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[8].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[9].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
#And match response.risultati[11].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 10,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[10]'
}
"""

Scenario: Ricerca pagamenti applicazione dominio1/segreteria filtrati per data escludendo spontanei non pagati 

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_domini1_segreteria.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And param mostraSpontaneiNonPagati = false	
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
#And match response.risultati[1].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
#And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 2,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
"""

