Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/api/v1/pendenze/bunch-pendenze.feature')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')

Scenario: Ricerca pendenze applicazione star/star filtrati per data

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
And param mostraSpontaneiNonPagati = true	
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[4].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[6].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[7].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[8].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
And match response.risultati[9].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
And match response.risultati[10].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
And match response.risultati[11].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[12].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[13].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[14].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
And match response.risultati[15].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[16].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[17].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[18].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
And match response.risultati[19].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 20,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[20]'
}
"""

Scenario: Ricerca pagamenti applicazione dominio1/star filtrati per data

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
And param mostraSpontaneiNonPagati = true	
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[4].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[6].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
And match response.risultati[7].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
And match response.risultati[8].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
And match response.risultati[9].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
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

Scenario: Ricerca pagamenti applicazione dominio1/segreteria filtrati per data

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
And param mostraSpontaneiNonPagati = true	
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
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 4,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[4]'
}
"""

Scenario: Ricerca pagamenti applicazione dominio2/segreteria filtrati per data

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_domini2_segreteria.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And param mostraSpontaneiNonPagati = true	
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
#And match response.risultati[0].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
#And match response.risultati[1].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
#And match response.risultati[2].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
#And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""

Scenario: Ricerca pagamenti applicazione non autorizzato al servizio

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_nonAuth.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
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
Scenario: Ricerca pagamenti operatore non abilitato

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_disabilitato.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
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


Scenario: Ricerca pagamenti applicazione dominio1 con uo 1 e 2 e dominio2 filtrati per data

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_domini1e2_uo1e2.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And param mostraSpontaneiNonPagati = true	
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
#And match response.risultati[0].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
#And match response.risultati[1].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
#And match response.risultati[].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
#And match response.risultati[2].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
#And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A)'
And match response == 
"""
{
	numRisultati: 4,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[4]'
}
"""


