Feature: Ricerca transazioni

Background:

* callonce read('classpath:utils/workflow/modellounico/v1/modellounico-bunch-pagamenti-v3.feature')

Scenario: Ricerca transazioni BASIC filtrati per data

* def applicazione = read('classpath:test/api/backoffice/v1/pendenze/get/msg/applicazione_star_star.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url backofficeBaseurl
And path 'rpp'
And param dataRptDa = dataInizio
And param dataRtA = dataFine
And headers basicAutenticationHeader
When method get
Then status 200
# And match response.risultati[0].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2
# And match response.risultati[1].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
# And match response.risultati[2].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
# And match response.risultati[3].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
# And match response.risultati[4].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE
# And match response.risultati[5].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE
# And match response.risultati[6].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA
# And match response.risultati[7].rpt.creditorReferenceId == idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA
# And match response.risultati[8].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE
# And match response.risultati[9].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
# And match response.risultati[10].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
# And match response.risultati[11].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE
# And match response.risultati[12].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
# And match response.risultati[13].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
# And match response.risultati[14].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA
# And match response.risultati[15].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
# And match response.risultati[16].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
# And match response.risultati[17].rpt.creditorReferenceId == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA
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

* def cod_dominio_sel = response.risultati[0].rpt.transferList.transfer[0].fiscalCodePA
* def iuv_sel = response.risultati[0].rpt.creditorReferenceId
* def ccp_sel = '3' + iuv_sel

* def applicazione = read('classpath:test/api/backoffice/v1/pendenze/get/msg/applicazione_domini2_segreteria.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url backofficeBaseurl
And path 'rpp', cod_dominio_sel, iuv_sel, ccp_sel
And headers basicAutenticationHeader
When method get
Then status 403


