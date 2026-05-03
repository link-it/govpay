Feature: Controllo autorizzazione applicazione lettura dettaglio ricevute

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

* def spidHeadersVerdi = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 
* def soggettoVersanteVerdi = { tipo: 'F', identificativo: 'VRDGPP65B03A112N', anagrafica: 'Giuseppe Verdi' }

* def spidHeaders = spidHeadersVerdi
* def soggettoVersante = soggettoVersanteVerdi

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* configure followRedirects = false
* def esitoVerifyPayment = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')

Scenario: Ricerca transazioni BASIC filtrati per data e dominio1

* def applicazione = read('classpath:test/api/backoffice/v1/pendenze/get/msg/applicazione_domini1_star.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)

# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria

* set pendenzaPut.idDominio = idDominioPagamento
* set pendenzaPut.idTipoPendenza = codTipoPendenzaPagamento
* set pendenzaPut.voci[0].codEntrata = codEntrataPagamento

* def idA2A2BasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A2, password: pwdA2A2 } )

Given url backofficeBaseurl
And path '/pendenze', idA2A2, idPendenza
And headers idA2A2BasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def responsePut = response

Given url backofficeBaseurl
And path '/pendenze', idA2A2, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = pendenzaPut.importo

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

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')
* def attivazioneExpected = read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')
* set attivazioneExpected.idA2A = idA2A2
* match response == attivazioneExpected

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp = ccp_numero_avviso
* def terminazioneExpected = read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')
* set terminazioneExpected.idA2A = idA2A2
* match response == terminazioneExpected

* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = ccp
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione.creditorReferenceId

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url ragioneriaBaseurl
And path '/ricevute'
And param dataDa = dataInizio
And param dataA = dataFine
And headers basicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.creditorReferenceId
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

* def rpt = response.risultati[0]


Given url ragioneriaBaseurl
And path '/ricevute', rpt.dominio.idDominio, rpt.iuv, rpt.idRicevuta
And headers idA2ABasicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status 200
