Feature: Pagamento eseguito ad iniziativa PSP

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* configure followRedirects = false

* def esitoVerifyPayment = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Scenario: Pagamento eseguito dovuto precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo

# Configurazione dell'applicazione

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

# NON_ESEGUITO_SANP_24("R22")

* def riversamentoCumulativo = 'true'
* def tipoRicevuta = "R22"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'NON_ESEGUITA'
* match response.voci[0].stato == 'Non eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RPT_ACCETTATA_NODO'

