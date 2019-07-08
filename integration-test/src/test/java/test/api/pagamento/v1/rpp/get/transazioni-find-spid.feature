Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v3.feature')

Scenario: Ricerca transazioni SPID filtrati per data

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})
Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/rpp'
And param dataDa = dataInizio
And param dataA = dataFine
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE
And match response.risultati[1].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO
And match response.risultati[2].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[3].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[4].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[5].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[6].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[7].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[8].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[9].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[10].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[11].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 12,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[12]'
}
"""

Scenario: Ricerca transazioni SPID filtrati per data, numero risultati e pagina

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/rpp'
And param dataDa = dataInizio
And param dataA = dataFine
And param risultatiPerPagina = 5
And param pagina = 3
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[1].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 12,
	numPagine: 3,
	risultatiPerPagina: 5,
	pagina: 3,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
"""

Scenario: Ricerca transazioni SPID filtrati per data e esito = 'IN_CORSO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/rpp'
And param dataDa = dataInizio
And param dataA = dataFine
And param esito = 'IN_CORSO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE
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


Scenario: Ricerca transazioni SPID filtrati per data e esito = 'RIFIUTATO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/rpp'
And param dataDa = dataInizio
And param dataA = dataFine
And param esito = 'RIFIUTATO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO
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

Scenario: Ricerca transazioni SPID filtrati per data e esito = 'ESEGUITO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/rpp'
And param dataDa = dataInizio
And param dataA = dataFine
And param esito = 'ESEGUITO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[1].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[2].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[3].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[4].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[5].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 6,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[6]'
}
"""

Scenario: Ricerca transazioni SPID filtrati per data e stato = 'NON_ESEGUITO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/rpp'
And param dataDa = dataInizio
And param dataA = dataFine
And param esito = 'NON_ESEGUITO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[1].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[2].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[3].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA
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


