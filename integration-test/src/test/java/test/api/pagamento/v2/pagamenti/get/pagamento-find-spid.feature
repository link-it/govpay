Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v2.feature')

Scenario: Ricerca pagamenti SPID filtrati per data

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].id == idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
And match response.risultati[1].id == idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
And match response.risultati[2].id == idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[3].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[4].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[5].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[6].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[7].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[8].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 9,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[9]'
}
"""

Scenario: Ricerca pagamenti SPID filtrati per data e numero risultati

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param risultatiPerPagina = 4
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].id == idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
And match response.risultati[1].id == idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
And match response.risultati[2].id == idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[3].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response == 
"""
{
	numRisultati: 9,
	numPagine: 3,
	risultatiPerPagina: 4,
	pagina: 1,
	prossimiRisultati: '#notnull',
	risultati: '#[4]'
}
"""

Scenario: Ricerca pagamenti SPID filtrati per data, numero risultati e pagina

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param risultatiPerPagina = 4
And param pagina = 3
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 9,
	numPagine: 3,
	risultatiPerPagina: 4,
	pagina: 3,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

Scenario: Ricerca pagamenti SPID filtrati per data e stato = 'ESEGUITO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param stato = 'ESEGUITO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[1].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[2].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[3].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
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


Scenario: Ricerca pagamenti SPID filtrati per data e stato = 'NON_ESEGUITO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param stato = 'NON_ESEGUITO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].id == idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[1].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[2].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 3,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[3]'
}
"""

Scenario: Ricerca pagamenti SPID filtrati per data e stato = 'IN_CORSO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param stato = 'IN_CORSO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].id == idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
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

Scenario: Ricerca pagamenti SPID filtrati per data e stato = 'RIFIUTATO'

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get
Then status 200

Given url pagamentiBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And param stato = 'FALLITO'
And headers spidHeadersVerdi
When method get
Then status 200
And match response.risultati[0].id == idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
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


