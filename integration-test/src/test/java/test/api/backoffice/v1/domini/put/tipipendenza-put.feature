Feature: Censimento tipiPendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def tipoPendenza = 
"""
{
	"codificaIUV": "999",  
	"pagaTerzi": false,
	"abilitato": true,
	"form": null,
	"validazione": null,
	"trasformazione": null,
	"inoltro": null,
	"promemoriaAvviso": null,
	"promemoriaRicevuta": null,
  "visualizzazione": null,
  "tracciatoCsv": null
}
"""

Given url backofficeBaseurl
And path 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
And request 
"""
{
  descrizione: "Sanzione codice della strada",
  tipo: "dovuto",
  codificaIUV: "030",
  pagaTerzi: true,
  form: null,
  trasformazione: null,
  validazione: null,
  visualizzazione: null,
  tracciatoCsv: null,
  appIO: null
}
"""  
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario: Aggiunta di un tipoPendenza

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una tipoPendenza (<field>)

* set tipoPendenza.<field> = <value>

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.valori.<field> == <checkValue>

Examples:
| field | value | checkValue |
| codificaIUV | null | '#notpresent' |
| codificaIUV | '090' | '090' |
| pagaTerzi | true | true |
| pagaTerzi | false | false |
| pagaTerzi | null | '#notpresent' |
| validazione | { "tipo": "angular2-json-schema-form", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | { "tipo": "angular2-json-schema-form", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| validazione | null | '#notpresent' |
| trasformazione | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| trasformazione | null | '#notpresent' |
| inoltro | idA2A | idA2A |
| inoltro | null | '#notpresent' |
| promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| promemoriaAvviso | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } |
| promemoriaAvviso | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null } | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| promemoriaAvviso | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null } | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null, "allegaPdf" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : '#notpresent' } |
| promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : true } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : true } |
| promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| promemoriaRicevuta | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } |
| promemoriaRicevuta | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null } | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| promemoriaRicevuta | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null } | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null, "allegaPdf" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : '#notpresent' } |
| promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : true } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : true } |
| visualizzazione | null | '#notpresent' |
| tracciatoCsv | null | '#notpresent'	|
| appIO | null | { "abilitato": false }	|
| appIO | { "abilitato": false } | { "abilitato": false }	|
| appIO | { "abilitato": true } | { "abilitato": true }	|
| appIO | { "abilitato": true , "apiKey" : null } | { "abilitato": true , "apiKey" : '#notpresent' }	|
| appIO | { "abilitato": true , "apiKey" : 'ABC...' } | { "abilitato": true , "apiKey" : 'ABC...' }	|
| appIO | { "abilitato": true , "apiKey" : 'ABC...' , "message" : { "tipo": "freemarker", "subject" : "aaa", "body" : "aaa" } } | { "abilitato": true , "apiKey" : 'ABC...' , "message" :  { "tipo": "freemarker", "subject": "aaa", "body": "aaa" } }	|


