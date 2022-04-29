Feature: Ricerca delle riconciliazioni filtrate per id flusso rendicontazione

Background:

 callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

Scenario: Riconciliazioni da applicazione autorizzata per dominio_1 e dominio_2

* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v2/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

* def idRiconciliazione = getCurrentTimeMillis()
* def idFlussoRendicontazione = estraiIdFlussoDallaCausale(causale)

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers idA2ABasicAutenticationHeader
And request { idFlussoRendicontazione: '#(idFlussoRendicontazione)', importo: '#(importo)' , sct : 'SCT0123456789'}
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(5000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def idRiconciliazioneCum_DOM1_A2A = response.id
* def riconciliazioneCum_DOM1_A2A = response

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

* def applicazione = read('msg/applicazione_dominio1e2.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/incassi'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
And param idFlusso = riconciliazioneCum_DOM1_A2A.idFlussoRendicontazione
When method get
Then status 200
And match response.risultati[0].idIncasso == riconciliazioneCum_DOM1_A2A.idFlussoRendicontazione
And match response.risultati[0].idFlusso == riconciliazioneCum_DOM1_A2A.idFlussoRendicontazione
And match response ==
"""
{
	maxRisultati: '#number',
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
""" 

Scenario: Ricerca riconciliazioni con un idFlusso non presente

Given url backofficeBaseurl
And path '/incassi'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
And param idFlusso = 'XXX'
When method get
Then status 200
And match response ==
"""
{
	maxRisultati: '#number',
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
""" 

