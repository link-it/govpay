Feature: Pagamento di una pendenza multibeneficiario con connettore SEND e data di validita' decorsa

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_dominio5.feature')

* configure followRedirects = false

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

# NB: le fixture che confrontano gli importi contengono placeholder su sendFeeEurocent e
# pendenzaPut, e vanno lette al punto d'uso: una read() nel Background congelerebbe il valore
# risolto al primo match, portandolo nello scenario successivo.
* def pathEsitoVerifyPayment = 'classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json'
* def pathEsitoGetPayment = 'classpath:test/workflow/send/v1/msg/getPayment-response-ok-multibeneficiario.json'

# Configurazione del connettore SEND sul dominio principale della pendenza

# NB: SendClient accoda '/delivery/v2.3/price/{paTaxId}/{noticeCode}' alla url del connettore,
# quindi qui va configurata solo la base. abilitaGDE=true perche' il test rilegge la risposta
# SEND dagli eventi.
* set dominio.servizioSend = { "url": '#(ndpsym_url + "/pagopa/rs")', "auth": { "headerName": "x-api-key", "headerValue": "test-api-key" }, "abilitaGDE": true }

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Configurazione dell'applicazione per l'aggiornamento della pendenza al gestionale dell'ente
# (necessaria perche' la data di validita' decorsa forza una riacquisizione della pendenza)

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Caricamento pendenza multibeneficiario con SEND e data di validita' decorsa, pagamento e verifica ricevuta

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* remove pendenzaPut.sendAbilitato
* call read('classpath:utils/pa-prepara-avviso.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'NON_ESEGUITA'
And match response.importo == pendenzaPut.importo
And match response.voci == '#[2]'
And match response.voci[0].importo == pendenzaPut.voci[0].importo
And match response.voci[1].importo == pendenzaPut.voci[1].importo

# Verifico il pagamento (la pendenza ha dataValidita decorsa: la verifica forza una
# riacquisizione della pendenza dal gestionale dell'ente prima di generare la RPT).
# E' in questo momento, e non al caricamento, che GovPay interroga SEND: l'attualizzazione
# avviene in VersamentoUtils.acquisisciVersamento, non nel PUT della pendenza.

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerifica = response
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Recupero dal giornale eventi interno la risposta del servizio SEND, per calcolare gli
# importi attesi comprensivi delle spese di notifica. Ci si attende una sola interrogazione,
# quella innescata dalla riacquisizione.

* call read('classpath:utils/send-recupera-fee.feature')
* match numEventiSend == 1
* def addEuro = function(a,b){ return Number((a+b).toFixed(2)) }

# Aggiorno gli importi attesi (solo sulla voce dell'ente proprietario della pendenza,
# idDominio_5 non deve essere impattato dalla commissione SEND) da qui in poi utilizzati
# dalle fixture per verificare la ricevuta. Va fatto prima di validare la risposta della
# verifica, che confronta gli importi con pendenzaPut.

* set pendenzaPut.importo = addEuro(pendenzaPut.importo, sendFeeEuro)
* set pendenzaPut.voci[0].importo = addEuro(pendenzaPut.voci[0].importo, sendFeeEuro)
* def importo = pendenzaPut.importo

* match rispostaVerifica == read(pathEsitoVerifyPayment)

# Attivo il pagamento

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == read(pathEsitoGetPayment)

# Verifico la notifica di attivazione: gli importi devono essere comprensivi della
# commissione SEND sulla sola voce dell'ente proprietario della pendenza

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/send/v1/msg/notifica-attivazione-multibeneficiario-send.json')

# Verifico la notifica di terminazione (ricevuta)

* call sleep(10000)

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp = ccp_numero_avviso
* match response == read('classpath:test/workflow/send/v1/msg/notifica-terminazione-eseguito-multibeneficiario-send.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

Scenario: Pagamento non eseguito con attualizzazione della commissione SEND, seguito da pagamento eseguito

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* def pendenzaPutBase = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* def addEuro = function(a,b){ return Number((a+b).toFixed(2)) }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* remove pendenzaPut.sendAbilitato
# Caricamento pendenza nel servizio ENTE
* call read('classpath:utils/pa-prepara-avviso.feature')

# Azzero la cache dei prezzi del simulatore (non tocca RPT/rendicontazioni) per partire da
# uno stato noto: al caricamento della pendenza SEND non e' stato interrogato.

Given url ndpsym_url + '/pagopa/rs/delivery/v2.3'
And path 'reset'
When method get
Then assert responseStatus == 200

# Verifico il pagamento: la riacquisizione della pendenza (dataValidita decorsa) interroga
# SEND prima di generare la RPT

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerifica = response
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Recupero la commissione SEND attualizzata: e' la prima e unica interrogazione. Calcolo gli
# importi attesi ripartendo sempre dai valori originali della pendenza (mai sommando sopra un
# importo gia' attualizzato in precedenza)

* call read('classpath:utils/send-recupera-fee.feature')
* match numEventiSend == 1
* set pendenzaPut.importo = addEuro(pendenzaPutBase.importo, sendFeeEuro)
* set pendenzaPut.voci[0].importo = addEuro(pendenzaPutBase.voci[0].importo, sendFeeEuro)
* def importo = pendenzaPut.importo

* match rispostaVerifica == read(pathEsitoVerifyPayment)

# Attivo il pagamento con esito NON eseguito (NON_ESEGUITO_SANP_24, "R22")

* def riversamentoCumulativo = 'true'
* def tipoRicevuta = "R22"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == read(pathEsitoGetPayment)

# Verifico la notifica di attivazione: gli importi devono gia' riflettere la commissione
# SEND attualizzata, anche se il pagamento non e' andato a buon fine

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/send/v1/msg/notifica-attivazione-multibeneficiario-send.json')

# Verifico che la pendenza non sia stata pagata, ma che l'importo sia stato attualizzato

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'NON_ESEGUITA'
* match response.voci[0].stato == 'Non eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RPT_ACCETTATA_NODO'

# Ritento il pagamento, questa volta con esito positivo

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerificaRetry = response
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Recupero di nuovo la commissione SEND: la riacquisizione avviene ancora (dataValidita resta
# decorsa). Se il tempo trascorso dal tentativo precedente e' entro la retention configurata
# (it.govpay.client.send.retention) viene riutilizzato il valore gia' attualizzato, altrimenti
# c'e' una nuova interrogazione: non si asserisce il numero, gli importi attesi vengono in
# entrambi i casi ricalcolati dai valori originali della pendenza.

* call read('classpath:utils/send-recupera-fee.feature')
* print 'Numero di interrogazioni SEND dopo il secondo tentativo di pagamento:', numEventiSend
* set pendenzaPut.importo = addEuro(pendenzaPutBase.importo, sendFeeEuro)
* set pendenzaPut.voci[0].importo = addEuro(pendenzaPutBase.voci[0].importo, sendFeeEuro)
* def importo = pendenzaPut.importo

* match rispostaVerificaRetry == read(pathEsitoVerifyPayment)

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == read(pathEsitoGetPayment)

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/send/v1/msg/notifica-attivazione-multibeneficiario-send.json')

# Verifico la notifica di terminazione (ricevuta)

* call sleep(10000)

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp = ccp_numero_avviso
* match response == read('classpath:test/workflow/send/v1/msg/notifica-terminazione-eseguito-multibeneficiario-send.json')

# Verifico lo stato finale della pendenza: due RPP, la prima annullata dal tentativo non
# eseguito, la seconda accettata con la ricevuta del pagamento eseguito

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[2]'
* match response.rpp[0].stato == 'RPT_ANNULLATA'
* match response.rpp[0].rt == '#notpresent'
* match response.rpp[1].stato == 'RT_ACCETTATA_PA'
* match response.rpp[1].rt == '#notnull'
