Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')

Scenario Outline: Lettura dettaglio applicazione <applicazione> filtrati per data

* def applicazione = read('msg/<applicazione>')
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
And headers idA2ABasicAutenticationHeader
When method get
And match response.risultati[15].id == idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
And match response.risultati[14].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
And match response.risultati[13].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[12].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[11].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[10].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[9].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[8].id == idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[7].id == idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
And match response.risultati[6].id == idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
And match response.risultati[5].id == idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
And match response.risultati[4].id == idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[3].id == idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[2].id == idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[1].id == idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[0].id == idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
Then status 200
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

Examples:
| applicazione |
| applicazione_star_star.json |
| applicazione_domini1e2_segreteria.json |
| applicazione_domini1_segreteria.json |
| applicazione_domini1_star.json |
| applicazione_none_none.json |

Scenario: Ricerca pagamenti applicazione non autorizzato al servizio

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def applicazione = read('msg/applicazione_nonAuth.json')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403

Scenario: Ricerca pagamenti applicazione non abilitato

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def applicazione = read('msg/applicazione_disabilitato.json')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403


Scenario: Ricerca pagamenti applicazione non censito

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: 'xxxx', password: 'password' } )

Given url backofficeBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
When method get
Then status 401

