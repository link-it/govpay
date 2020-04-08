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
  portaleBackoffice: { abilitato : true },
  portalePagamento: { abilitato : true },
  avvisaturaMail: null,
  avvisaturaAppIO: null,
  visualizzazione: null,
  tracciatoCsv: null 
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
And match response.dettaglio contains '<checkValue>'

Examples:
| field | fieldValue | checkValue |
| codificaIUV | '' | codificaIUV |
| codificaIUV | 'aaa' | codificaIUV |
| codificaIUV | '00000' | codificaIUV | 
| pagaTerzi | '' | pagaTerzi |
| pagaTerzi | 'si' | pagaTerzi | 
| portaleBackoffice.abilitato | "aaa" | abilitato |
| portaleBackoffice.form | { "abilitato": true, "tipo": null, "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | form |
| portaleBackoffice.form | { "abilitato": true, "tipo": "angular2-json-schema-form", "definizione": null } | form |
| portaleBackoffice.trasformazione | { "tipo": "booo", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | trasformazione |
| portaleBackoffice.trasformazione | { "tipo": "freemarker", "definizione": null } | trasformazione |
| portalePagamento.abilitato | "aaa" | abilitato |
| portalePagamento.form | { "abilitato": true, "tipo": null, "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | form |
| portalePagamento.form | { "abilitato": true, "tipo": "angular2-json-schema-form", "definizione": null } | form |
| portalePagamento.trasformazione | { "tipo": "booo", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | trasformazione |
| portalePagamento.trasformazione | { "tipo": "freemarker", "definizione": null } | trasformazione |

| avvisaturaMail.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": "aaaaa" } | allegaPdf |
| avvisaturaMail.promemoriaAvviso | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } | abilitato |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "allegaPdf": "aaaaa", "soloEseguiti" : true }  | allegaPdf |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true, "soloEseguiti" : true } | abilitato |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": "true", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true, "soloEseguiti" : "aaa" } | soloEseguiti |
| avvisaturaMail.promemoriaScadenza | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare",  "preavviso" : 0  } | abilitato |
| avvisaturaMail.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "preavviso" : -1  }  | preavviso |

| avvisaturaAppIO.promemoriaAvviso | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare" } | abilitato |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : true } | abilitato |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": "true", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : "aaa" } | soloEseguiti |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": "aaa", "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso" : 0  } | abilitato |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento eseguito", "messaggio": "Hai pagato", "preavviso" : -1  }  | preavviso |

| tracciatoCsv | {tipo: "freemarker", "intestazione": "idA2A,idPendenza,idDominio", "richiesta": null, "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | tracciatoCsv |
| tracciatoCsv | {tipo: "freemarker", "intestazione": "idA2A,idPendenza,idDominio", "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": null } | tracciatoCsv |
| tracciatoCsv | {tipo: "freemarker", "intestazione": null, "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | tracciatoCsv |
| tracciatoCsv | {tipo: null, "intestazione": "idA2A,idPendenza,idDominio", "richiesta": "eyAidHlwZSI6ICJvYmplY3QiIH0=", "risposta": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | tracciatoCsv |

