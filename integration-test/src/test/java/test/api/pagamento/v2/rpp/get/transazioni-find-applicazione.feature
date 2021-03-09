Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

Scenario: Ricerca transazioni BASIC filtrati per data

* def applicazione = read('msg/applicazione_auth.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path 'rpp'
And param dataRptDa = dataInizio
And param dataRptA = dataFine
And headers basicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[1].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[2].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[3].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[4].rpt.identificativoMessaggioRichiesta == idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response == 
"""
{
	numRisultati: 5,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[5]'
}
"""

Scenario: Ricerca pagamenti BASIC filtrati per data non autorizzato

* def applicazione = read('msg/applicazione_nonAuth.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path 'rpp'
And param dataRptDa = dataInizio
And param dataRptA = dataFine
And headers basicAutenticationHeader
When method get
Then status 403

Scenario: Ricerca pagamenti BASIC filtrati per data disabilitato

* def applicazione = read('msg/applicazione_disabilitato.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path 'pagamenti'
And param dataRptDa = dataInizio
And param dataRptA = dataFine
And headers basicAutenticationHeader
When method get
Then status 403

