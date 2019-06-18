Feature: Validazione sintattica tipoPendenza

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
	"form": null
	"validazione": null,
	"trasformazione": null,
	"inoltro": null,
	"promemoria": null
}
"""

Scenario Outline: <field> non valida

* set tipoPendenza.<field> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', 'test'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>

Examples:
| field | fieldValue | fieldName | 
| codificaIUV | '' | 'codificaIUV' |
| codificaIUV | 'aaa' | 'codificaIUV' |
| codificaIUV | '00000' | 'codificaIUV' |
| pagaTerzi |  '' | 'pagaTerzi' |
| pagaTerzi |  'si' | 'pagaTerzi' |
| form | { "tipo": "angular2-json-schema-form", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| form | { "tipo": null, "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| form | { "tipo": "angular2-json-schema-form", "definizione": null } |
| trasformazione | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| trasformazione | { "tipo": "booo", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| trasformazione | { "tipo": "freemarker", "definizione": null } |
| promemoria | { "oggetto": null, "messaggio": "Devi pagare", "allegaAvviso": null } |
| promemoria | { "oggetto": "Promemoria pagamento", "messaggio": null, "allegaAvviso": null } |
| promemoria | { "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaAvviso": "aaaaa" } |
| validazione | { "type": "object" } |
