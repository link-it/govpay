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
  tracciatoCsv: null
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
* def checkValue = <value> != null ? <value> : '#notpresent'

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
And match response.valori.<field> == checkValue

Examples:
| field | value | 
| codificaIUV | null |
| codificaIUV | '090' |
| pagaTerzi | true |
| pagaTerzi | false |
| pagaTerzi | null |
| validazione | { "tipo": "angular2-json-schema-form", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| trasformazione | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| inoltro | idA2A |
| promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } |
| promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "allegaPdf": true } |
| promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "allegaPdf": false } |
| visualizzazione | "eyAidHlwZSI6ICJvYmplY3QiIH0=" |
| tracciatoCsv | { "tipo": "freemarker", "intestazione": "idA2A,idPendenza,idDominio", "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
