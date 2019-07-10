Feature: Censimento tipiPendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def tipoPendenza = 
"""
{
  descrizione: "Sanzione codice della strada",
  tipo: "dovuto",
  codificaIUV: "030",
  pagaTerzi: true,
  form: { 
  	tipo: "angular2-json-schema-form",
  	definizione: null
  },
  trasformazione: {
  	tipo: "freemarker",
  	definizione: null
  },
  validazione: null;
}
"""          
* set tipoPendenza.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenza.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenza.validazione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-validazione-form.json'))

Scenario: Aggiunta di un tipoPendenza

Given url backofficeBaseurl
And path 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una tipoPendenza (<field>)

* set tipoPendenza.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| descrizione | 'Nuova descrizione' |
| tipo | 'spontaneo' |
| tipo | 'dovuto' |
| codificaIUV | null |
| codificaIUV | '090' |
| pagaTerzi | true |
| pagaTerzi | false |
| validazione | { "tipo": "angular2-json-schema-form", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| validazione | null |
| trasformazione | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| trasformazione | null |
| inoltro | idA2A |
| inoltro | null |
| promemoriaAvviso | { "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| promemoriaAvviso | { "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } |
| promemoriaAvviso | null |
| promemoriaRicevuta | { "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "allegaPdf": true } |
| promemoriaRicevuta | { "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "allegaPdf": false } |
| promemoriaRicevuta | null |

