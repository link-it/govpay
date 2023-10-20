Feature: Acquisizione di una RT non attivata o verificata

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* configure followRedirects = false

* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')

* def ndpsym_standin_url = ndpsym_url + '/pagopa/rs/standin'

Scenario: Pagamento eseguito dovuto precaricato con verifica

#			@QueryParam(value = "codPsp") String idPsp, opt
#			@QueryParam(value = "numeroAvviso") String numeroAvviso, obb
# 		@QueryParam(value = "ccp") String ccp, obb
#			@QueryParam(value = "idDominio") String idDominio, obb
#			@QueryParam(value = "importo") String importo, obb 
#			@QueryParam(value = "tipoRicevuta") String tipoRicevuta, obb
#			@QueryParam(value = "ibanAccredito") String ibanAccredito, obb
#			@QueryParam(value = "riversamentoCumulativo")@DefaultValue(value = "true") boolean riversamentoCumulativo, opt 
#			@QueryParam(value = "versione") @DefaultValue(value = "2")  Integer versione obb

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def ccp = getCurrentTimeMillis()
* def importo = 100.99
* def tipoRicevuta = "R23"
* def ibanAccreditoRT = ibanAccredito
* def versionePagamento = 2

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

Given url ndpsym_standin_url 
And path 'generaRicevuta' 
And param idDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccreditoRT
And param versione = versionePagamento
When method get
Then assert responseStatus == 200

Scenario: Pagamento eseguito dovuto precaricato con verifica, RT con importo diverso

* def versionePagamento = 2

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 


* def tipoRicevuta = "R01"
* def ibanAccreditoRT = ibanAccredito
* def importoRicevuta = 200.99

Given url ndpsym_standin_url 
And path 'generaRicevutaImportoDifferente' 
And param idDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccreditoRT
And param versione = versionePagamento
And param importoRicevuta = importoRicevuta
When method get
Then assert responseStatus == 200
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito-rt-importo-modificato.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ANOMALA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
# * match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'
