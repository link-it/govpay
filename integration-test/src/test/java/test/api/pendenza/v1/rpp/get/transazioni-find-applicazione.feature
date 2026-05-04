Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modellounico/v1/modellounico-bunch-pagamenti-v4.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario Outline: Ricerca pendenze applicazione star/star filtrati per data

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/<applicazione>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/rpp'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
#And match response.risultati[0].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[0].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[1].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[2].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA
# And match response.risultati[3].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_A2A2
And match response.risultati[3].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE
And match response.risultati[4].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO
#And match response.risultati[6].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[5].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[6].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE
# And match response.risultati[9].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[7].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[8].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA
And match response.risultati[9].rpt.creditorReferenceId == idMessaggioRichiesta_Anonimo_INCORSO_DOM1_SEGRETERIA
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

Examples:
| applicazione |
| applicazione_star_star.json |

Scenario: Ricerca pagamenti applicazione non autorizzato al servizio

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_nonAuth.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/rpp'
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

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/rpp'
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
