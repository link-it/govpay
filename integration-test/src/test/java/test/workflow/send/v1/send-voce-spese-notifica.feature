Feature: Prima voce a importo 0 per le spese di notifica SEND

# Issue #913. In una pendenza sendAbilitato la prima voce puo' avere importo 0: e' la voce
# dell'ente creditore su cui GovPay accredita le spese di notifica attualizzate, con il metadato
# NOTIFICATION_FEE. Se l'interrogazione di SEND fallisce si usa l'importo base
# it.govpay.send.speseNotifica.importoBase; senza importo base la pendenza non e' pagabile.
# Il valore configurato nell'istanza sotto test si passa a Karate con -Dsend.importoBase=2.00.
# Gli scenari negativi coprono i controlli al caricamento: voce a 0 non in prima posizione
# (VER_042), di un altro ente (VER_043), dominio senza connettore SEND e senza importo base
# (VER_044). Senza
# sendAbilitato una voce a importo 0 resta rifiutata dalla validazione sintattica, come prima.
# L'applicazione e' senza connettore di verifica, come in send-verifica-senza-connettore-verifica.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_dominio5.feature')

* configure followRedirects = false

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def addEuro = function(a,b){ return Number((a+b).toFixed(2)) }

* def pathEsitoVerifyPayment = 'classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json'
* def pathEsitoGetPayment = 'classpath:test/workflow/send/v1/msg/getPayment-response-ok-multibeneficiario.json'

* set dominio.servizioSend = { "url": '#(ndpsym_url + "/pagopa/rs")', "auth": { "headerName": "x-api-key", "headerValue": "test-api-key" }, "abilitaGDE": true }

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* remove applicazione.servizioIntegrazione

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

# Pendenza con la prima voce, dell'ente creditore, a importo 0: il totale e' la sola voce di idDominio_5
* def pendenzaConVoceSpese =
"""
function(idPendenza) {
	var p = karate.read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json');
	p.voci[0].importo = 0;
	p.importo = p.voci[1].importo;
	return p;
}
"""


Scenario: Prima voce a importo 0: le spese SEND attualizzate vanno sulla prima voce

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call pendenzaConVoceSpese idPendenza
* def importoPendenza = pendenzaPut.importo

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerifica = response
* def ccp = response.ccp

* call read('classpath:utils/send-recupera-fee.feature')
* match numEventiSend == 1

* set pendenzaPut.importo = addEuro(importoPendenza, sendFeeEuro)
* set pendenzaPut.voci[0].importo = sendFeeEuro
* def importo = pendenzaPut.importo
* match rispostaVerifica == read(pathEsitoVerifyPayment)

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'false'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == read(pathEsitoGetPayment)

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { importo: '#? _ == importoPendenza', sendAbilitato: true, sendImportoTotale: '#? _ == sendFeeEuro' }
And match response.voci[0].importo == 0


Scenario: Prima voce a importo 0 e SEND in errore: importo base, oppure pendenza non pagabile

# Il connettore SEND resta abilitato, ma punta a una risorsa inesistente: l'interrogazione fallisce

* set dominio.servizioSend.url = ndpsym_url + '/pagopa/rs/inesistente'

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call pendenzaConVoceSpese idPendenza
* def importoPendenza = pendenzaPut.importo

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerifica = response

# Con l'importo base la verifica riesce e lo aggiunge al totale; senza, la pendenza non e' pagabile

* def esitoAtteso = sendImportoBase == null ? 'PAA_SYSTEM_ERROR' : 'OK'
* def esitoVerifica = rispostaVerifica.faultBean ? rispostaVerifica.faultBean.faultCode : rispostaVerifica.esitoVerifica.outcome
* match esitoVerifica == esitoAtteso
* def importoAtteso = sendImportoBase == null ? null : addEuro(importoPendenza, sendImportoBase)
* def importoVerifica = sendImportoBase == null ? null : rispostaVerifica.esitoVerifica.paymentList.paymentOptionDescription.amount
* match importoVerifica == '#? _ == importoAtteso'

# L'importo base non viene salvato: il tentativo successivo interroga di nuovo SEND

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { importo: '#? _ == importoPendenza', sendAbilitato: true, sendImportoTotale: '##null' }


Scenario: Voce a importo 0 non in prima posizione: rifiutata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.voci[1].importo = 0
* set pendenzaPut.importo = pendenzaPut.voci[0].importo

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_042' }


Scenario: Prima voce a importo 0 di un altro ente creditore: rifiutata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* def voceEnte = pendenzaPut.voci[0]
* def voceAltroEnte = pendenzaPut.voci[1]
* set voceAltroEnte.importo = 0
* set pendenzaPut.voci = [ '#(voceAltroEnte)', '#(voceEnte)' ]
* set pendenzaPut.importo = voceEnte.importo

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_043' }
And match response.dettaglio contains idDominio_5


Scenario: Prima voce a importo 0 su un dominio senza connettore SEND: importo base, oppure rifiutata

* remove dominio.servizioSend

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call pendenzaConVoceSpese idPendenza
* def importoPendenza = pendenzaPut.importo

# Senza importo base la pendenza e' rifiutata (VER_044); con l'importo base e' accettata e lo riceve in verifica

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then match responseStatus == (sendImportoBase == null ? 422 : 201)

* def codiceAtteso = sendImportoBase == null ? 'VER_044' : null
* def codice = sendImportoBase == null ? response.codice : null
* match codice == codiceAtteso

* def numeroAvviso = sendImportoBase == null ? null : response.numeroAvviso
* def iuv = sendImportoBase == null ? null : getIuvFromNumeroAvviso(numeroAvviso)
* def verifica = function(){ return sendImportoBase == null ? null : karate.call('classpath:utils/psp-paVerifyPaymentNotice.feature').response }
* def rispostaVerifica = verifica()
* def importoVerifica = sendImportoBase == null ? null : rispostaVerifica.esitoVerifica.paymentList.paymentOptionDescription.amount
* def importoAtteso = sendImportoBase == null ? null : addEuro(importoPendenza, sendImportoBase)
* match importoVerifica == '#? _ == importoAtteso'


Scenario: Voce a importo 0 senza sendAbilitato: rifiutata come prima

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* remove pendenzaPut.sendAbilitato
* set pendenzaPut.voci[0].importo = 0
* set pendenzaPut.importo = pendenzaPut.voci[1].importo

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI' }
