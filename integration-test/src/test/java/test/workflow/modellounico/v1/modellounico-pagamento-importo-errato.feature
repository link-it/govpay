Feature: Attivazione RPT con importo errato

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* def esitoAttivaRPT = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')
* def esitoVerificaRPT = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* configure followRedirects = false

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Scenario: Attivazione RPT con importo errato dovuto precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo + 10

# Attivo il pagamento 

* def riversamentoCumulativo = 'true'
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoAttivaRPT

Scenario: Attivazione RPT con importo errato dovuto non precaricato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo + 10

# Attivo il pagamento 

* def riversamentoCumulativo = 'true'
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoAttivaRPT
