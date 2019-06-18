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
  tipo: "dovuta",
  codificaIUV: "030",
  pagaTerzi: true,
  form: null,
  trasformazione: null,
  validazione: null  
}
"""          
* set tipoPendenza.form = encodeBase64(read('msg/tipoPendenza-dovuta-form.json'))
* set tipoPendenza.trasformazione = encodeBase64(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenza.validazione = read('msg/tipoPendenza-dovuta-validazione-form.json')

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
| tipo | 'spontanea' |
| tipo | 'dovuta' |
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
| promemoria | { "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaAvviso": true } |
| promemoria | { "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaAvviso": false } |
| promemoria | null |

