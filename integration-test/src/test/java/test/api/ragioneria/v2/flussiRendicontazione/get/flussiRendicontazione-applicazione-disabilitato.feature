Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-v2.feature')
* def applicazioneRequest = read('msg/applicazione_disabilitato.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
* def rendicontazioneSchema = read('msg/rendicontazione.json')

* callonce sleep(10000)

Scenario Outline: Verifico che la find restituisca tutti e sole le riconciliazioni caricate dal verticale <applicazione>.

Given url ragioneriaBaseurl
And path '/flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403

Examples:
| applicazione |
| applicazione_disabilitato.json |
