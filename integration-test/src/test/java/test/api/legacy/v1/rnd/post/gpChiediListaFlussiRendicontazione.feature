Feature: GpRnd ChiediListaFlussiRendicontazione

Background: 

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
* def legacyGpAppBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})
* def legacyGpPrtBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPPrtService', versione: 'v1', autenticazione: 'basic'})
* def legacyGpRndBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPRndService', versione: 'v1', autenticazione: 'basic'})

Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione> per il dominio <dominio>.

* def applicazione = read('classpath:test/api/ragioneria/v2/flussiRendicontazione/get/msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def dataInizioListaFlussiRendicontazione = getDate(dataInizioFR)
* def dataFineListaFlussiRendicontazione = getDate(dataFineFR)
* def idDominioListaFlussiRendicontazione = <dominio>
* def gpChiediListaFlussiRendicontazioneRequest = read('classpath:test/api/legacy/v1/rnd/post/msg/gpChiediListaFlussiRendicontazioneRequest.xml')

Given url legacyGpRndBaseurl
And header SoapAction = 'gpChiediListaFlussiRendicontazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediListaFlussiRendicontazioneRequest
When method post
Then status 200

* xml resChiediFlussoRendicontazioni = response
* def numRisultatiCheck = $resChiediFlussoRendicontazioni count(/Envelope/Body/gpChiediListaFlussiRendicontazioneResponse//flussoRendicontazione)
* assert numRisultatiCheck >= <numRisultati>
* def flussi = $resChiediFlussoRendicontazioni /Envelope/Body/gpChiediListaFlussiRendicontazioneResponse/flussoRendicontazione
* match each flussi[*].codDominio == <dominio>

Examples:
| applicazione | numRisultati | dominio | 
| applicazione_star.json | 1 | idDominio |
| applicazione_dominio1e2.json | 1 | idDominio |
| applicazione_dominio1.json | 1 | idDominio |
| applicazione_star.json | 1 | idDominio_2 |
| applicazione_dominio1e2.json | 1 | idDominio_2 |
| applicazione_dominio2.json | 1 | idDominio_2 |


Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione> per il dominio <dominio>.

* def applicazione = read('classpath:test/api/ragioneria/v2/flussiRendicontazione/get/msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def dataInizioListaFlussiRendicontazione = getDate(dataInizioFR)
* def dataFineListaFlussiRendicontazione = getDate(dataFineFR)
* def idDominioListaFlussiRendicontazione = <dominio>
* def gpChiediListaFlussiRendicontazioneRequest = read('classpath:test/api/legacy/v1/rnd/post/msg/gpChiediListaFlussiRendicontazioneRequest.xml')

Given url legacyGpRndBaseurl
And header SoapAction = 'gpChiediListaFlussiRendicontazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediListaFlussiRendicontazioneRequest
When method post
Then status 200

* xml resChiediFlussoRendicontazioni = response
* def numRisultatiCheck = $resChiediFlussoRendicontazioni count(/Envelope/Body/gpChiediListaFlussiRendicontazioneResponse//flussoRendicontazione)
* assert numRisultatiCheck == <numRisultati>

Examples:
| applicazione | numRisultati | dominio | 
| applicazione_nonAuthDominio.json | 0 | idDominio |
| applicazione_nonAuthDominio.json | 0 | idDominio_2 |


Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione> per il dominio <dominio>.

* def applicazione = read('classpath:test/api/ragioneria/v2/flussiRendicontazione/get/msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def dataInizioListaFlussiRendicontazione = getDate(dataInizioFR)
* def dataFineListaFlussiRendicontazione = getDate(dataFineFR)
* def idDominioListaFlussiRendicontazione = <dominio>
* def gpChiediListaFlussiRendicontazioneRequest = read('classpath:test/api/legacy/v1/rnd/post/msg/gpChiediListaFlussiRendicontazioneRequest.xml')

Given url legacyGpRndBaseurl
And header SoapAction = 'gpChiediListaFlussiRendicontazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediListaFlussiRendicontazioneRequest
When method post
Then status 200

* xml resChiediFlussoRendicontazioni = response
* def numRisultatiCheck = $resChiediFlussoRendicontazioni count(/Envelope/Body/gpChiediListaFlussiRendicontazioneResponse//flussoRendicontazione)
* assert numRisultatiCheck == <numRisultati>

Examples:
| applicazione | numRisultati | dominio | 
| applicazione_dominio2.json | 0 | idDominio |
| applicazione_none.json | 0 | idDominio |
| applicazione_dominio1.json | 0 | idDominio_2 |
| applicazione_none.json | 0 | idDominio_2 |


Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione> per il dominio <dominio>.

* def applicazione = read('classpath:test/api/ragioneria/v2/flussiRendicontazione/get/msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def dataInizioListaFlussiRendicontazione = getDate(dataInizioFR)
* def dataFineListaFlussiRendicontazione = getDate(dataFineFR)
* def idDominioListaFlussiRendicontazione = <dominio>
* def gpChiediListaFlussiRendicontazioneRequest = read('classpath:test/api/legacy/v1/rnd/post/msg/gpChiediListaFlussiRendicontazioneRequest.xml')

Given url legacyGpRndBaseurl
And header SoapAction = 'gpChiediListaFlussiRendicontazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediListaFlussiRendicontazioneRequest
When method post
Then status 200

* xml resChiediFlussoRendicontazioni = response
* def numRisultatiCheck = $resChiediFlussoRendicontazioni count(/Envelope/Body/gpChiediListaFlussiRendicontazioneResponse//flussoRendicontazione)
* assert numRisultatiCheck == <numRisultati>

Examples:
| applicazione | numRisultati | dominio | 
| applicazione_dominio2.json | 0 | idDominio |
| applicazione_none.json | 0 | idDominio |
| applicazione_dominio1.json | 0 | idDominio_2 |
| applicazione_none.json | 0 | idDominio_2 |


Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione> per il dominio <dominio>.

* def applicazione = read('classpath:test/api/ragioneria/v2/flussiRendicontazione/get/msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def dataInizioListaFlussiRendicontazione = getDate(dataInizioFR)
* def dataFineListaFlussiRendicontazione = getDate(dataFineFR)
* def idDominioListaFlussiRendicontazione = <dominio>
* def gpChiediListaFlussiRendicontazioneRequest = read('classpath:test/api/legacy/v1/rnd/post/msg/gpChiediListaFlussiRendicontazioneRequest.xml')

Given url legacyGpRndBaseurl
And header SoapAction = 'gpChiediListaFlussiRendicontazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediListaFlussiRendicontazioneRequest
When method post
Then status 200

* xml resChiediFlussoRendicontazioni = response
* def codEsitoOperazione = $resChiediFlussoRendicontazioni /Envelope/Body/gpChiediListaFlussiRendicontazioneResponse/codEsitoOperazione
* assert codEsitoOperazione == <codEsitoOperazione>

Examples:
| applicazione | codEsitoOperazione | dominio | 
| applicazione_disabilitato.json | 'APP_001' | idDominio |
| applicazione_nonAuthServizio.json | 'APP_003' | idDominio |
| applicazione_disabilitato.json | 'APP_001' | idDominio_2 |
| applicazione_nonAuthServizio.json | 'APP_003' | idDominio_2 |


