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
	"form": null,
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
And match response.dettaglio contains '<field>'

Examples:
| field | fieldValue |  
| codificaIUV | '' | 
| codificaIUV | 'aaa' | 
| codificaIUV | '00000' |
| pagaTerzi |  '' | 
| pagaTerzi |  'si' |
| form | { "tipo": null, "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | 
| form | { "tipo": "angular2-json-schema-form", "definizione": null } |
| trasformazione | { "tipo": "booo", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| trasformazione | { "tipo": "freemarker", "definizione": null } |
| promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": "aaaaa" } |
| promemoriaAvviso | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| promemoriaAvviso | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "allegaPdf": "aaaaa" }  |
| promemoriaRicevuta | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| promemoriaRicevuta | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| tracciatoCsv | {tipo: "freemarker", "intestazione": "idA2A,idPendenza,idDominio", "richiesta": null, "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| tracciatoCsv | {tipo: "freemarker", "intestazione": "idA2A,idPendenza,idDominio", "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": null } |
| tracciatoCsv | {tipo: "freemarker", "intestazione": null, "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| tracciatoCsv | {tipo: null, "intestazione": "idA2A,idPendenza,idDominio", "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
