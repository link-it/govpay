Feature: Ricerca transazioni

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v3.feature')

Scenario: Ricerca transazioni BASIC filtrati per data

* def applicazione = read('classpath:test/api/backoffice/v1/pendenze/get/msg/applicazione_star_star.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url backofficeBaseurl
And path 'rpp'
And param dataRptDa = dataInizio
And param dataRtA = dataFine
And headers basicAutenticationHeader
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
And match response.risultati[8].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[9].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
And match response.risultati[10].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[11].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[12].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[13].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[14].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[15].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
And match response.risultati[16].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[17].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA
And match response == 
"""
{
	numRisultati: 18,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[18]'
}
"""

* def cod_dominio_sel = response.risultati[0].rpt.dominio.identificativoDominio
* def iuv_sel = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccp_sel = response.risultati[0].rpt.datiVersamento.codiceContestoPagamento

* def applicazione = read('classpath:test/api/backoffice/v1/pendenze/get/msg/applicazione_domini2_segreteria.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url backofficeBaseurl
And path 'rpp', cod_dominio_sel, iuv_sel, ccp_sel
And headers basicAutenticationHeader
When method get
Then status 403


