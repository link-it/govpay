Feature: Ricerca riscossioni filtrate per tipo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
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

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

* def dataEnd = getDateTime()

* def iur1 = response.rt.datiPagamento.datiSingoloPagamento[0].identificativoUnivocoRiscossione
* def iur2 = response.rt.datiPagamento.datiSingoloPagamento[1].identificativoUnivocoRiscossione

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
