Feature: Riconciliazione pagamento rendicontato senza rpt

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario: Riconciliazione pagamento rendicontato senza rpt

* def tipoRicevuta = "R00"
* def riversamentoCumulativo = "true"

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* call read('classpath:utils/v2/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Given url ndpsym_psp_url 
And path 'attiva' 
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = riversamentoCumulativo
When method get
Then assert responseStatus == 200

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789'}
When method put
Then status 202

* def riconciliazioneLocation = responseHeaders['Location'][0]

# Attesa batch elaborazione rendicontazioni
* call sleep(10000)

Given url ragioneriaBaseurl
And path riconciliazioneLocation
And headers basicAutenticationHeader
When method get
Then status 200
And match response == read('msg/riconciliazione-singola-senza-rpt.json')

