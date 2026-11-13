Feature: Attualizzazione della commissione SEND con il NAV notificato (sendNav)

# Issue #912. Quando l'avviso notificato su SEND viene sostituito da una pendenza diversa,
# sendNav indica il NAV notificato: l'attualizzazione interroga SEND con quel NAV al posto
# del numero avviso della pendenza. Gli scenari negativi coprono i controlli sul campo:
# sendAbilitato obbligatorio, NAV diverso dal numero avviso della pendenza stessa. Il NAV di
# una pendenza GovPay ancora da pagare e' accettato: annullarla e' onere dell'ente.
# L'applicazione e' senza connettore di verifica, come in send-verifica-senza-connettore-verifica:
# con la data di validita' decorsa GovPay interroga SEND senza passare dal gestionale.

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

# NAV di un avviso notificato su SEND ma non gestito da questa installazione di GovPay:
# il prefisso 3 e la parte variabile da currentTimeMillis lo tengono lontano dagli avvisi generati.
* def navEsterno = function(){ var s = '' + getCurrentTimeMillis(); while (s.length < 17) s = '0' + s; return '3' + s.substring(s.length - 17) }

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


Scenario: NAV notificato esterno: SEND interrogato con sendNav e importo attualizzato

* def idPendenza = getCurrentTimeMillis()
* def sendNav = navEsterno()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* def pendenzaPutBase = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.sendNav = sendNav

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

# L'evento e' registrato sullo IUV della pendenza, ma la richiesta a SEND porta il NAV notificato

* call read('classpath:utils/send-recupera-fee.feature')
* match numEventiSend == 1
* match ultimoEventoSend.parametriRichiesta.url contains '/delivery/v2.3/price/' + idDominio + '/' + sendNav
* match ultimoEventoSend.parametriRichiesta.url !contains numeroAvviso

* set pendenzaPut.importo = addEuro(pendenzaPutBase.importo, sendFeeEuro)
* set pendenzaPut.voci[0].importo = addEuro(pendenzaPutBase.voci[0].importo, sendFeeEuro)
* def importo = pendenzaPut.importo
* remove pendenzaPut.sendNav

* match rispostaVerifica == read(pathEsitoVerifyPayment)

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { sendAbilitato: true, sendNav: '#(sendNav)', sendImportoTotale: '#? _ == sendFeeEuro' }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response contains { sendAbilitato: true, sendNav: '#(sendNav)' }


Scenario: NAV notificato di una pendenza GovPay annullata: accettato e usato per SEND

* def idPendenzaA = getCurrentTimeMillis()
* def idPendenza = idPendenzaA
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenzaA
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def navA = response.numeroAvviso

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenzaA
And headers basicAutenticationHeader
And request [ { "op":"REPLACE","path":"/stato","value":"ANNULLATA"}, {"op":"REPLACE","path":"/descrizioneStato","value":"Sostituita"} ]
When method patch
Then status 200

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.sendNav = navA

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.esitoVerifica.outcome == 'OK'

* call read('classpath:utils/send-recupera-fee.feature')
* match ultimoEventoSend.parametriRichiesta.url contains '/delivery/v2.3/price/' + idDominio + '/' + navA


Scenario: NAV notificato di una pendenza GovPay ancora da pagare: accettato

* def idPendenzaA = getCurrentTimeMillis()
* def idPendenza = idPendenzaA
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenzaA
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def navA = response.numeroAvviso

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.sendNav = navA

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.sendNav == navA


Scenario: NAV notificato uguale al numero avviso della pendenza stessa: rifiutato in aggiornamento

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* set pendenzaPut.sendNav = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_041' }


Scenario: NAV notificato senza sendAbilitato: rifiutato dalla validazione sintattica

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* remove pendenzaPut.sendAbilitato
* set pendenzaPut.sendNav = navEsterno()

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI' }
And match response.dettaglio contains 'sendNav'


Scenario: NAV notificato con formato non valido: rifiutato dalla validazione sintattica

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.sendNav = '30100000000000012'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI' }
And match response.dettaglio contains 'sendNav'


Scenario: Aggiornamento senza sendNav: resta il NAV notificato gia' salvato

* def idPendenza = getCurrentTimeMillis()
* def sendNav = navEsterno()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.sendNav = sendNav

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* remove pendenzaPut.sendNav

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.sendNav == sendNav
