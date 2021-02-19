Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')

Scenario: Lettura dettaglio applicazione_star_star filtrati data e per idDominio

* def applicazione = read('msg/applicazione_star_star.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param idDominio = idDominio
And headers idA2ABasicAutenticationHeader
When method get
And match response.risultati[7].id == idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
And match response.risultati[6].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
And match response.risultati[5].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[4].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[3].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[2].id == idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
And match response.risultati[1].id == idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
And match response.risultati[0].id == idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
Then status 200
And match response == 
"""
{
	numRisultati: 8,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[8]'
}
"""

Scenario: Lettura dettaglio applicazione_domini1e2_segreteria filtrati per data

* def applicazione = read('msg/applicazione_domini1e2_segreteria.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param idDominio = idDominio
And headers idA2ABasicAutenticationHeader
When method get
And match response.risultati[6].id == idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
And match response.risultati[5].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
And match response.risultati[4].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[3].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[2].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[1].id == idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
And match response.risultati[0].id == idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
Then status 200
And match response == 
"""
{
	numRisultati: 7,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[7]'
}
"""

Scenario: Lettura dettaglio applicazione_domini1e2_segreteria filtrati per data idA2A e per idDominio

* def applicazione = read('msg/applicazione_domini1e2_segreteria.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param idDominio = idDominio
And param idA2A = idA2A
And headers idA2ABasicAutenticationHeader
When method get
# And match response.risultati[6].id == idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
# And match response.risultati[5].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
And match response.risultati[1].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
# And match response.risultati[3].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[0].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
# And match response.risultati[1].id == idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
# And match response.risultati[0].id == idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
Then status 200
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


Scenario: Lettura dettaglio applicazione_domini1e2_segreteria filtrati per data e idA2A

* def applicazione = read('msg/applicazione_domini1e2_segreteria.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param idA2A = idA2A
And headers idA2ABasicAutenticationHeader
When method get
# And match response.risultati[6].id == idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
# And match response.risultati[5].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
And match response.risultati[1].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
# And match response.risultati[3].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[0].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
# And match response.risultati[1].id == idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
# And match response.risultati[0].id == idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
Then status 200
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


Scenario: Lettura dettaglio applicazione_domini1e2_segreteria filtrati per data e idPendenza

* def applicazione = read('msg/applicazione_domini1e2_segreteria.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param idA2A = idA2A
And headers idA2ABasicAutenticationHeader
When method get
And match response.risultati[1].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[0].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
Then status 200
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

* def risposta = read('msg/pagamento-get-singolo_eseguito_ente.json')

Given url backofficeBaseurl
And path '/pagamenti', idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == risposta	

* def idPendenzaParam = response.rpp[0].pendenza.idPendenza
* def iuvParam = response.rpp[0].pendenza.iuvPagamento

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param idPendenza = idPendenzaParam
And headers idA2ABasicAutenticationHeader
When method get
And match response.risultati[0].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param iuv = iuvParam
And headers idA2ABasicAutenticationHeader
When method get
And match response.risultati[0].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param iuv = tail(iuvParam,5)
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
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



