Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni.feature')

Scenario: Verifico che la find restituisca tutti e sole le riconciliazioni caricate dal verticale

* def applicazione = read('msg/applicazione_auth.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riconciliazioni'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
When method get
Then status 200
And match response.risultati[0].idRiconciliazione == idRiconciliazioneCum_DOM1_A2A
And match response.risultati[1].idRiconciliazione == idRiconciliazioneSin_DOM1_A2A
And match response ==
"""
{
	numRisultati: 2,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
""" 

Scenario: Senza autorizzazioni per dominio

* def applicazione = read('msg/applicazione_nonAuthDominio.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riconciliazioni'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
When method get
Then status 200
And match response ==
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
""" 

Scenario: Applicazione disabilitata

* def applicazione = read('msg/applicazione_disabilitato.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riconciliazioni'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403

Scenario: Applicazione non autorizzata al servizio

* def applicazione = read('msg/applicazione_nonAuthServizio.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riconciliazioni'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403

