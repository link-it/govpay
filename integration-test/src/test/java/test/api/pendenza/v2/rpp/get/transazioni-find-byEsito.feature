Feature: Ricerca richieste di pagamento pendenza filtrate per esito

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* configure followRedirects = false
* def esitoVerifyPayment = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')

Scenario: Filtro su data

* def dataRptStart = getDateTime()

* def idPendenza = getCurrentTimeMillis()
* def idPendenza1 = idPendenza

* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

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

* def dataRtEnd2 = getDateTime()

# Fine verifiche. Verifico i filtri

Given url pendenzeBaseurl
And path '/rpp'
And param esitoPagamento = 'ESEGUITO' 
And param idPendenza = idPendenza1
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza1)'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'


Scenario: Controllo di sintassi sul valore del filtro per esito
Given url pendenzeBaseurl
And path '/rpp'
And param esitoPagamento = 'ESITO_NON_VALIDO' 
And headers basicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'ESITO_NON_VALIDO'

