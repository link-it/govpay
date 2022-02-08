Feature: Caricamento Iuv

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def legacyBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Caricamento pendenza multivoce

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio,applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def gpCaricaIuvRequest = read('msg/gpCaricaIuvRequest.xml')
* def gpCaricaIuvResponse = read('msg/gpCaricaIuvResponse.xml')

Given url legacyBaseurl
And header SoapAction = 'gpCaricaIuv'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaIuvRequest
When method post
Then status 200
And match response == gpCaricaIuvResponse