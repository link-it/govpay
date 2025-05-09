Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')
* def applicazioneRequest = read('classpath:test/api/backoffice/v1/pendenze/get/msg/applicazione_domini2_segreteria.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario: Ricerca transazioni BASIC filtrati per data e dominio2 segreteria

Given url ragioneriaBaseurl
And path '/ricevute'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[1].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[2].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[3].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[4].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[5].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[0].iuv == rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[1].iuv == rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[6].iuv == rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[7].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[8].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[9].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[2].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[3].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[4].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[5].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[6].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[7].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
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



