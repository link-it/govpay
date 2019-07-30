Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
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

Given url backofficeBaseurl
And path '/flussiRendicontazione', <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idFlusso | httpStatus | risposta |
| applicazione_star.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom2_1 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| applicazione_none.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_none.json | idflusso_dom2_1 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuth.json | idflusso_dom2_1 | 403 | errore_auth.json |

Scenario Outline: Lettura dettaglio operatore [<operatore>] del flusso di rendicontazione [<idFlusso>]

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request read('msg/<operatore>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/flussiRendicontazione', <idFlusso>
And headers spidHeadersRossi
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| operatore | idFlusso | httpStatus | risposta |
| operatore_star.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| operatore_star.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| operatore_domini1e2.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| operatore_domini1e2.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| operatore_domini1.json | idflusso_dom1_1 | 200 | flussoRendicontazioni.json |
| operatore_domini1.json | idflusso_dom2_1 | 403 | errore_auth.json |
| operatore_domini2.json | idflusso_dom1_1 | 403 | errore_auth.json |
| operatore_domini2.json | idflusso_dom2_1 | 200 | flussoRendicontazioni.json |
| operatore_none.json | idflusso_dom1_1 | 403 | errore_auth.json |
| operatore_none.json | idflusso_dom2_1 | 403 | errore_auth.json |
| operatore_nonAuth.json | idflusso_dom1_1 | 403 | errore_auth.json |
| operatore_nonAuth.json | idflusso_dom2_1 | 403 | errore_auth.json |