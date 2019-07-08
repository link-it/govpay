Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/ragioneria/bunch-riconciliazioni-v2.feature')
* def rendicontazioneSchema = read('msg/rendicontazione.json')

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idFlusso>]

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/flussiRendicontazione', <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idFlusso | httpStatus | risposta |
| applicazione_star.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom2_1 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| applicazione_nonAuthDominio.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuthDominio.json | idflusso_dom2_1 | 403 | errore_auth.json |
| applicazione_nonAuthServizio.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuthServizio.json | idflusso_dom2_1 | 403 | errore_auth.json |