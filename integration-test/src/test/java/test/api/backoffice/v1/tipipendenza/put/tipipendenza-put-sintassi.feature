Feature: Validazione sintattica tipi pendenza

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
  portaleBackoffice: { abilitato : true },
  portalePagamento: { abilitato : true },
  avvisaturaMail: null,
  avvisaturaAppIO: null,
  visualizzazione: null,
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
* match response.dettaglio contains '<checkValue>'

Examples:
| field | value | checkValue |
| descrizione | null | descrizione | 
| descrizione | loremIpsum | descrizione |
| tipo | null | tipo |
| tipo | 'XXXX' | tipo |
| codificaIUV | '' | codificaIUV |
| codificaIUV | 'aaa' | codificaIUV |
| codificaIUV | '00000' | codificaIUV | 
| pagaTerzi | '' | pagaTerzi |
| pagaTerzi | 'si' | pagaTerzi | 
| portaleBackoffice.abilitato | "aaa" | abilitato |
| portaleBackoffice.abilitato | null | abilitato |
| portaleBackoffice.form | { "abilitato": true, "tipo": null, "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | form |
| portaleBackoffice.form | { "abilitato": true, "tipo": "angular2-json-schema-form", "definizione": null } | form |
| portaleBackoffice.trasformazione | { "tipo": "booo", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | trasformazione |
| portaleBackoffice.trasformazione | { "tipo": "freemarker", "definizione": null } | trasformazione |
| portalePagamento.abilitato | "aaa" | abilitato |
| portalePagamento.abilitato | null | abilitato |
| portalePagamento.form | { "abilitato": true, "tipo": null, "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | form |
| portalePagamento.form | { "abilitato": true, "tipo": "angular2-json-schema-form", "definizione": null } | form |
| portalePagamento.trasformazione | { "tipo": "booo", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | trasformazione |
| portalePagamento.trasformazione | { "tipo": "freemarker", "definizione": null } | trasformazione |

| avvisaturaMail.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": "aaaaa" } | allegaPdf |
| avvisaturaMail.promemoriaAvviso | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } | abilitato |
| avvisaturaMail.promemoriaAvviso | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } | abilitato |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "allegaPdf": "aaaaa", "soloEseguiti" : true }  | allegaPdf |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true, "soloEseguiti" : true } | abilitato |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true, "soloEseguiti" : true } | abilitato |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": "true", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true, "soloEseguiti" : "aaa" } | soloEseguiti |
| avvisaturaMail.promemoriaScadenza | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso" : 0  } | abilitato |
| avvisaturaMail.promemoriaScadenza | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare",  "preavviso" : 0  } | abilitato |
| avvisaturaMail.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "preavviso" : -1  }  | preavviso |

| avvisaturaAppIO.promemoriaAvviso | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare" } | abilitato |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare" } | abilitato |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : true } | abilitato |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : true } | abilitato |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": "true", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : "aaa" } | soloEseguiti |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": null, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso" : 0  } | abilitato |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso" : 0  } | abilitato |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "preavviso" : -1  }  | preavviso |


| tracciatoCsv | {tipo: "freemarker", "intestazione": "idA2A,idPendenza,idDominio", "richiesta": null, "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | tracciatoCsv |
| tracciatoCsv | {tipo: "freemarker", "intestazione": "idA2A,idPendenza,idDominio", "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": null } | tracciatoCsv |
| tracciatoCsv | {tipo: "freemarker", "intestazione": null, "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | tracciatoCsv |
| tracciatoCsv | {tipo: null, "intestazione": "idA2A,idPendenza,idDominio", "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | tracciatoCsv |


Scenario Outline: Sintassi errata nel campo (idTipoPendenza): <value>

* def idTipoPendenza = <value>


Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }

Examples:

| value |
| ' *&' |
| loremIpsum |


