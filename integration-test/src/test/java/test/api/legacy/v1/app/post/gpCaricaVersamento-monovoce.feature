Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def legacyBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Caricamento avviso senza numeroAvviso con voce riferita

* def generaIuv = false
* def gpCaricaVersamentoRequest = read('msg/gpCaricaVersamentoRequest-monovoce_riferimento.xml')
* def gpCaricaVersamentoResponse = read('msg/gpCaricaVersamentoResponse.xml')

Given url legacyBaseurl
And header SoapAction = 'gpCaricaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaVersamentoRequest
When method post
Then status 200
And match response == gpCaricaVersamentoResponse

* xml res = response

Given url pendenzeBaseurl
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

Scenario: Caricamento avviso senza numeroAvviso con voce definita

* def generaIuv = false
* def gpCaricaVersamentoRequest = read('msg/gpCaricaVersamentoRequest-monovoce_definito.xml')
* def gpCaricaVersamentoResponse = read('msg/gpCaricaVersamentoResponse.xml')

Given url legacyBaseurl
And header SoapAction = 'gpCaricaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaVersamentoRequest
When method post
Then status 200
And match response == gpCaricaVersamentoResponse

* xml res = response

Given url pendenzeBaseurl
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


Scenario: Caricamento avviso senza numeroAvviso con MBT

* def generaIuv = false
* def gpCaricaVersamentoRequest = read('msg/gpCaricaVersamentoRequest-monovoce_bollo.xml')
* def gpCaricaVersamentoResponse = read('msg/gpCaricaVersamentoResponse.xml')

Given url legacyBaseurl
And header SoapAction = 'gpCaricaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaVersamentoRequest
When method post
Then status 200
And match response == gpCaricaVersamentoResponse

* xml res = response

Given url pendenzeBaseurl
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


