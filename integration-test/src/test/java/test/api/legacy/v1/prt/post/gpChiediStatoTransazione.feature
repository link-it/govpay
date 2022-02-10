Feature: GpPrt ChiediStatoTransazione

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def legacyGpAppBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})
* def legacyGpPrtBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPPrtService', versione: 'v1', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Avvio Transazione pagamento tramite il riferimento codDominio/iuv

# Carico Pendenza

* def generaIuv = false
* def idPendenza = getCurrentTimeMillis()
* def gpCaricaVersamentoRequest = read('classpath:test/api/legacy/v1/app/post/msg/gpCaricaVersamentoRequest-monovoce_riferimento.xml')
* def gpCaricaVersamentoResponse = read('classpath:test/api/legacy/v1/app/post/msg/gpCaricaVersamentoResponse.xml')

Given url legacyGpAppBaseurl
And header SoapAction = 'gpCaricaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaVersamentoRequest
When method post
Then status 200
And match response == gpCaricaVersamentoResponse

* xml res = response

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* def iuvToCheck = getIuvFromNumeroAvviso(response.numeroAvviso)
* match iuvToCheck == $res /Envelope/Body/gpCaricaVersamentoResponse/iuvGenerato/iuv

# Avvio Pagamento

* def iuv = $res /Envelope/Body/gpCaricaVersamentoResponse/iuvGenerato/iuv
* def gpAvviaTransazionePagamentoRequest = read('classpath:test/api/legacy/v1/prt/post/msg/gpAvviaTransazionePagamentoRequest-riferimento.xml')
* def gpAvviaTransazionePagamentoResponse = read('classpath:test/api/legacy/v1/prt/post/msg/gpAvviaTransazionePagamentoResponse-ok.xml')

Given url legacyGpPrtBaseurl
And header SoapAction = 'gpAvviaTransazionePagamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpAvviaTransazionePagamentoRequest
When method post
Then status 200
And match response == gpAvviaTransazionePagamentoResponse

* xml resAvviaPagamento = response

* def iuvToCheck2 = $resAvviaPagamento /Envelope/Body/gpAvviaTransazionePagamentoResponse/rifTransazione/iuv
* match iuvToCheck2 == iuv

* def ccp = $resAvviaPagamento /Envelope/Body/gpAvviaTransazionePagamentoResponse/rifTransazione/ccp

* def gpChiediStatoTransazioneRequest = read('classpath:test/api/legacy/v1/prt/post/msg/gpChiediStatoTransazioneRequest.xml')
* def gpChiediStatoTransazioneResponse = read('classpath:test/api/legacy/v1/prt/post/msg/gpChiediStatoTransazioneResponse-inCorso.xml')

Given url legacyGpPrtBaseurl
And header SoapAction = 'gpChiediStatoTransazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediStatoTransazioneRequest
When method post
Then status 200
And match response == gpChiediStatoTransazioneResponse

* def gpChiediStatoVersamentoRequest = read('classpath:test/api/legacy/v1/app/post/msg/gpChiediStatoVersamentoRequest.xml')
* def gpChiediStatoVersamentoResponse = read('classpath:test/api/legacy/v1/app/post/msg/gpChiediStatoVersamentoResponse-inCorso.xml')

Given url legacyGpAppBaseurl
And header SoapAction = 'gpChiediStatoVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediStatoVersamentoRequest
When method post
Then status 200
And match response == gpChiediStatoVersamentoResponse

# Completo Pagamento
* configure followRedirects = false
* def idSession = $resAvviaPagamento /Envelope/Body/gpAvviaTransazionePagamentoResponse/pspSessionId

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamentoCumulativo = '0'
And headers basicAutenticationHeader
When method get
Then status 302

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')


* def gpChiediStatoTransazioneRequest = read('classpath:test/api/legacy/v1/prt/post/msg/gpChiediStatoTransazioneRequest.xml')
* def gpChiediStatoTransazioneResponse = read('classpath:test/api/legacy/v1/prt/post/msg/gpChiediStatoTransazioneResponse-eseguito.xml')

Given url legacyGpPrtBaseurl
And header SoapAction = 'gpChiediStatoTransazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediStatoTransazioneRequest
When method post
Then status 200
And match response == gpChiediStatoTransazioneResponse

* def gpChiediStatoVersamentoRequest = read('classpath:test/api/legacy/v1/app/post/msg/gpChiediStatoVersamentoRequest.xml')
* def gpChiediStatoVersamentoResponse = read('classpath:test/api/legacy/v1/app/post/msg/gpChiediStatoVersamentoResponse-eseguito.xml')

Given url legacyGpAppBaseurl
And header SoapAction = 'gpChiediStatoVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediStatoVersamentoRequest
When method post
Then status 200
And match response == gpChiediStatoVersamentoResponse






