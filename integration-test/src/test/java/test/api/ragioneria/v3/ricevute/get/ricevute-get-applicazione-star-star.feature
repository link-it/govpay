Feature: Dettaglio ricevute

Background:

* callonce read('classpath:utils/workflow/modellounico/v1/modellounico-bunch-pagamenti-v3.feature')
* def applicazioneRequest = read('msg/applicazione_star_star.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della transazione

* def risposta = read('msg/<risposta>')
* def idDominioDet = <rpt>.transferList.transfer[0].fiscalCodePA
* def iuvDet = <rpt>.creditorReferenceId
* def ccpDet = '3' + iuvDet

Given url ragioneriaBaseurl
And path '/ricevute', idDominioDet, iuvDet, ccpDet
And headers idA2ABasicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | rpt | httpStatus | risposta |
| applicazione_star_star.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 404 | errore_notFound.json |
| applicazione_star_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | errore_notFound.json |


Scenario: Ricerca transazioni BASIC filtrati per data

Given url ragioneriaBaseurl
And path '/ricevute'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
# And match response.risultati[0].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2.creditorReferenceId
# And match response.risultati[1].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A.creditorReferenceId
And match response.risultati[0].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.creditorReferenceId
And match response.risultati[1].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.creditorReferenceId
# And match response.risultati[4].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE.creditorReferenceId
And match response.risultati[2].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.creditorReferenceId
# And match response.risultati[6].iuv == rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA.creditorReferenceId
And match response.risultati[3].iuv == rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA.creditorReferenceId
# And match response.risultati[8].iuv == rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE.creditorReferenceId
And match response.risultati[4].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.creditorReferenceId
And match response.risultati[5].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.creditorReferenceId
And match response.risultati[6].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.creditorReferenceId
# And match response.risultati[12].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2.creditorReferenceId
# And match response.risultati[13].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A.creditorReferenceId
# And match response.risultati[14].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA.creditorReferenceId
And match response.risultati[7].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.creditorReferenceId
And match response.risultati[8].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.creditorReferenceId
And match response.risultati[9].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.creditorReferenceId
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