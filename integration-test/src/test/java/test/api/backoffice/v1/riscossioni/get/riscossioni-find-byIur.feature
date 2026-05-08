Feature: Ricerca riscossioni filtrate per tipo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

* def pathServizio = '/riscossioni'

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* configure followRedirects = false
* def esitoVerifyPayment = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')

Scenario: Filtro per iur

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()

* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = 'true'
* def idSession = idCart

* call read('classpath:utils/psp-paGetPaymentV2.feature')

# Verifico la notifica di attivazione

* call read('classpath:utils/pa-notifica-attivazione.feature')

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

* def dataEnd = getDateTime()

* def iur1 = response.rt.receiptId
* def iur2 = response.rt.receiptId

# Ho avviato il pagamento. Verifico i filtri.

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param iur = iur2
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And assert response.numRisultati >= 1
And match response.risultati[0].iur == iur1

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param iur = iur1
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And assert response.numRisultati >= 1
And match response.risultati[0].iur == iur2

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param iur = 'XXX'
And headers gpAdminBasicAutenticationHeader
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
