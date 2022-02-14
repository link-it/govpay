Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v3/ragioneria/bunch-riconciliazioni-v2.feature')
* def rendicontazioneSchema = read('msg/rendicontazione.json')

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idDominio> <idFlusso>]

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/flussiRendicontazione', <idDominio>, <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')
And match each response.rendicontazioni[*] == read('msg/rendicontazione.json')

Examples:
| applicazione | idDominio | idFlusso | httpStatus | risposta |
| applicazione_star.json | idDominio | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idDominio_2 | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idDominio | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio2.json | idDominio_2 | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |


Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idDominio> <idFlusso>]

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/flussiRendicontazione',<idDominio>, <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idDominio | idFlusso | httpStatus | risposta |
| applicazione_dominio1.json | idDominio_2 | idflusso_dom2_1 | 403 | errore_auth.json |
| applicazione_dominio2.json | idDominio | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuthDominio.json | idDominio | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuthDominio.json | idDominio_2 | idflusso_dom2_1 | 403 | errore_auth.json |
| applicazione_nonAuthServizio.json | idDominio | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuthServizio.json | idDominio_2 | idflusso_dom2_1 | 403 | errore_auth.json |

