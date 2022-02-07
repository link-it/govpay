Feature: Ricerca delle riconciliazioni filtrate per sct

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v3.feature')

Scenario: Riconciliazioni da applicazione autorizzata per dominio_1 e dominio_2

* def applicazione = read('msg/applicazione_dominio1e2.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/incassi'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
And param sct = 'SCT0123456789'
When method get
Then status 200
And match response.risultati[0].idIncasso == idRiconciliazioneCum_DOM2_A2A2
And match response.risultati[1].idIncasso == idRiconciliazioneCum_DOM2_A2A
And match response.risultati[2].idIncasso == idRiconciliazioneSin_DOM2_A2A2
And match response.risultati[3].idIncasso == idRiconciliazioneSin_DOM2_A2A
And match response.risultati[4].idIncasso == idRiconciliazioneCum_DOM1_A2A2
And match response.risultati[5].idIncasso == idRiconciliazioneCum_DOM1_A2A
And match response.risultati[6].idIncasso == idRiconciliazioneSin_DOM1_A2A2
And match response.risultati[7].idIncasso == idRiconciliazioneSin_DOM1_A2A
And match response ==
"""
{
	maxRisultati: '#number',
	numRisultati: 8,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[8]'
}
""" 

Given url ragioneriaBaseurl
And path '/incassi'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
And param sct = 'SCT01'
When method get
Then status 200
And match response.risultati[0].idIncasso == idRiconciliazioneCum_DOM2_A2A2
And match response.risultati[1].idIncasso == idRiconciliazioneCum_DOM2_A2A
And match response.risultati[2].idIncasso == idRiconciliazioneSin_DOM2_A2A2
And match response.risultati[3].idIncasso == idRiconciliazioneSin_DOM2_A2A
And match response.risultati[4].idIncasso == idRiconciliazioneCum_DOM1_A2A2
And match response.risultati[5].idIncasso == idRiconciliazioneCum_DOM1_A2A
And match response.risultati[6].idIncasso == idRiconciliazioneSin_DOM1_A2A2
And match response.risultati[7].idIncasso == idRiconciliazioneSin_DOM1_A2A
And match response ==
"""
{
	maxRisultati: '#number',
	numRisultati: 8,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[8]'
}
""" 

Given url ragioneriaBaseurl
And path '/incassi'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
And param sct = 'SCTXXX'
When method get
Then status 200
And match response ==
"""
{
	maxRisultati: '#number',
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
""" 

