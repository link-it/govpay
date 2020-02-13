Feature: Validazione sintattica entrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def tipoPendenza = 
"""
{
  descrizione: "Sanzione codice della strada",
  tipo: "dovuto",
  codificaIUV: "030",
  pagaTerzi: true,
  form: null,
  trasformazione: null,
  validazione: null,
  promemoriaAvviso: null,
  promemoriaRicevuta: null,
  tracciatoCsv: null 
}
"""          

          
Scenario Outline: Sintassi errata nel campo (<field>)

* set tipoPendenza.<field> = <value>

Given url backofficeBaseurl
And path 'tipiPendenza', 'Multa'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '<field>'

Examples:
| field | value |
| descrizione | null |  
| descrizione | loremIpsum |
| tipo | null | 
| tipo | 'XXXX' | 
| codificaIUV | '' | 
| codificaIUV | 'aaa' | 
| codificaIUV | '00000' | 
| pagaTerzi | '' | 
| pagaTerzi | 'si' | 
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


Scenario: Sintassi errata nel campo (idTipoEntrata)

Given url backofficeBaseurl
And path 'tipiPendenza', ' *&'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
