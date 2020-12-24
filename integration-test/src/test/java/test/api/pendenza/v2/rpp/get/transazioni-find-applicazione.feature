Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v4.feature')

Scenario Outline: Ricerca pendenze applicazione star/star filtrati per data

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/<applicazione>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/rpp'
And param dataRptDa = dataInizio
And param dataRptA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[1].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[2].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[3].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[4].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[5].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[6].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[7].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA
And match response.risultati[8].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE
And match response.risultati[9].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO
And match response.risultati[10].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[11].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[12].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[13].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[14].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[15].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[16].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[17].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[18].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[19].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA 
And match response.risultati[20].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Anonimo_INCORSO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 21,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[21]'
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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/rpp'
And param dataRptDa = dataInizio
And param dataRptA = dataFine
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
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/rpp'
And param dataRptDa = dataInizio
And param dataRptA = dataFine
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
