Feature: Dettaglio ricevute

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')
* def applicazioneRequest = read('msg/applicazione_domini1_star.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della transazione

* def risposta = read('msg/<risposta>')

Given url ragioneriaBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento, <rpt>.datiVersamento.codiceContestoPagamento
And headers idA2ABasicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| applicazione | rpt | httpStatus | risposta |
| applicazione_domini1_star.json | rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | errore_notFound.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | errore_notFound.json |
| applicazione_domini1_star.json | rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_eseguito_ente.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 200 | transazione-get-singolo_noneseguito_ente.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 403 | errore_auth.json |
| applicazione_domini1_star.json | rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 403 | errore_auth.json |


Scenario: Ricerca transazioni BASIC filtrati per data e dominio1

Given url ragioneriaBaseurl
And path '/ricevute'
And param dataDa = dataInizio
And param dataA = dataFine
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
# And match response.risultati[0].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[1].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[2].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[3].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[4].iuv == rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[5].iuv == rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[0].iuv == rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[1].iuv == rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[8].iuv == rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[9].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[10].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.datiVersamento.identificativoUnivocoVersamento
# And match response.risultati[11].iuv == rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[2].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[3].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[4].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[5].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[6].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[7].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA.datiVersamento.identificativoUnivocoVersamento
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