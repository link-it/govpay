Feature: GpRnd ChiediFlussoRendicontazione

Background: 

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
* def legacyGpAppBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})
* def legacyGpPrtBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPPrtService', versione: 'v1', autenticazione: 'basic'})
* def legacyGpRndBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPRndService', versione: 'v1', autenticazione: 'basic'})


Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idFlusso>]

* def applicazione = read('classpath:test/api/ragioneria/v2/flussiRendicontazione/get/msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idFlusso = <idFlusso>
* def annoRiferimentoFlusso = <annoRiferimentoFlusso>
* def gpChiediFlussoRendicontazioneRequest = read('classpath:test/api/legacy/v1/rnd/post/msg/gpChiediFlussoRendicontazioneRequest.xml')

Given url legacyGpRndBaseurl
And header SoapAction = 'gpChiediFlussoRendicontazione'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediFlussoRendicontazioneRequest
When method post
Then status 200

* xml resChiediFlussoRendicontazioni = response
* def codEsitoOperazione = /Envelope/Body/gpChiediFlussoRendicontazioneResponse/codEsitoOperazione
* assert codEsitoOperazione == <codEsitoOperazione>


Examples:
| applicazione | idFlusso | codEsitoOperazione | annoRiferimentoFlusso | 
| applicazione_star.json | idflusso_dom1_1 | 'OK' | annoRif_flusso_dom1_1 |
| applicazione_star.json | idflusso_dom2_1 | 'OK' | annoRif_flusso_dom2_1 |
| applicazione_dominio1e2.json | idflusso_dom1_1 | 'OK' | annoRif_flusso_dom1_1 |
| applicazione_dominio1e2.json | idflusso_dom2_1 | 'OK' | annoRif_flusso_dom2_1 |
| applicazione_dominio1.json | idflusso_dom1_1 | 'OK' | annoRif_flusso_dom1_1 |
| applicazione_dominio1.json | idflusso_dom2_1 | 'RND_001' | annoRif_flusso_dom2_1 |
| applicazione_dominio2.json | idflusso_dom1_1 | 'RND_001' | annoRif_flusso_dom1_1 |
| applicazione_dominio2.json | idflusso_dom2_1 | 'OK' | annoRif_flusso_dom2_1 |
| applicazione_none.json | idflusso_dom1_1 | 'RND_001' | annoRif_flusso_dom1_1 |
| applicazione_none.json | idflusso_dom2_1 | 'RND_001' | annoRif_flusso_dom2_1 |
| applicazione_nonAuthServizio.json | idflusso_dom1_1 | 'APP_003' | annoRif_flusso_dom1_1 |
| applicazione_nonAuthServizio.json | idflusso_dom2_1 | 'APP_003' | annoRif_flusso_dom2_1 |

