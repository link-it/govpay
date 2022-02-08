Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def generaIuv = false
* def gpCaricaVersamentoRequest = read('msg/gpCaricaVersamentoRequest-multivoce_bollo.xml')
* def legacyBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})
* def gpCaricaVersamentoResponse = read('msg/gpCaricaVersamentoResponse-multivoce.xml')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Caricamento pendenza multivoce

Given url legacyBaseurl
And header SoapAction = 'gpCaricaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaVersamentoRequest
When method post
Then status 200
And match response == gpCaricaVersamentoResponse

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[3]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.numeroAvviso == '#notpresent'