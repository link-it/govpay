Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-v2.feature')
* def applicazioneRequest = read('msg/applicazione_nonAuthServizio.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
* def pathServizio = '/flussiRendicontazione'
* def rendicontazioneSchema = read('msg/rendicontazione.json')

Scenario Outline: Verifico che la find restituisca tutti e sole le riconciliazioni caricate dal verticale <applicazione>.

Given url ragioneriaBaseurl
And path '/flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403

Examples:
| applicazione |
| applicazione_nonAuthServizio.json |


Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idFlusso>]

Given url ragioneriaBaseurl
And path '/flussiRendicontazione', <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idFlusso | httpStatus | risposta |
| applicazione_nonAuthServizio.json | idflusso_dom1_1 | 403 | errore_auth.json |
| applicazione_nonAuthServizio.json | idflusso_dom2_1 | 403 | errore_auth.json |