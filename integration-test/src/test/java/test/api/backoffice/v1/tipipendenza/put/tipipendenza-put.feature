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
  codificaIUV: "030",
  pagaTerzi: true,
  portaleBackoffice: {
  	abilitato: true,
  	form: { 
	  	tipo: "angular2-json-schema-form",
	  	definizione: null
	  },
	  validazione: null,
	  trasformazione: {
	  	tipo: "freemarker",
	  	definizione: null
	  },
	  inoltro: null
  },
  portalePagamento: {
  	abilitato: true,
  	form: { 
	  	tipo: "angular2-json-schema-form",
	  	definizione: null,
	  	impaginazione: null
	  },
	  validazione: null,
	  trasformazione: {
	  	tipo: "freemarker",
	  	definizione: null
	  },
	  inoltro: null
  },
  visualizzazione: null,
  tracciatoCsv: {
  	tipo: "freemarker",
  	intestazione: "idA2A,idPendenza,idDominio",
  	richiesta: null,
	  risposta: null
  }
}
"""          
* set tipoPendenza.portaleBackoffice.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenza.portaleBackoffice.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenza.portaleBackoffice.validazione = encodeBase64InputStream(karate.readAsString('msg/tipoPendenza-dovuta-validazione-form.json'))

* set tipoPendenza.portalePagamento.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenza.portalePagamento.form.impaginazione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenza.portalePagamento.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenza.portalePagamento.validazione = encodeBase64InputStream(karate.readAsString('msg/tipoPendenza-dovuta-validazione-form.json'))

* set tipoPendenza.visualizzazione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-visualizzazione.json.payload'))
* set tipoPendenza.tracciatoCsv.richiesta = encodeBase64InputStream(read('msg/tracciato-csv-freemarker-request.ftl'))
* set tipoPendenza.tracciatoCsv.risposta = encodeBase64InputStream(read('msg/tracciato-csv-freemarker-response.ftl'))

Scenario: Aggiunta di un tipoPendenza

Given url backofficeBaseurl
And path 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una tipoPendenza (<field>)

* set tipoPendenza.<field> = <value>

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
And match response.<field> == <checkValue>

Examples:
| field | value | checkValue |
| descrizione | 'Nuova descrizione' | 'Nuova descrizione' |
| codificaIUV | null | '#notpresent' |
| codificaIUV | '090' | '090' |
| pagaTerzi | true | true |
| pagaTerzi | false | false |
| portaleBackoffice.abilitato | false | false |
| portaleBackoffice.abilitato | true | true |
| portaleBackoffice.validazione | "eyAidHlwZSI6ICJvYmplY3QiIH0=" | "eyAidHlwZSI6ICJvYmplY3QiIH0=" |
| portaleBackoffice.validazione | null | '#notpresent' |
| portaleBackoffice.trasformazione | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| portaleBackoffice.trasformazione | null | '#notpresent' |
| portaleBackoffice.inoltro | idA2A | idA2A |
| portaleBackoffice.inoltro | null | '#notpresent' |
| portalePagamento.abilitato | false | false |
| portalePagamento.abilitato | true | true |
| portalePagamento.validazione | "eyAidHlwZSI6ICJvYmplY3QiIH0=" | "eyAidHlwZSI6ICJvYmplY3QiIH0=" |
| portalePagamento.validazione | null | '#notpresent' |
| portalePagamento.trasformazione | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } | { "tipo": "freemarker", "definizione": "eyAidHlwZSI6ICJvYmplY3QiIH0=" } |
| portalePagamento.trasformazione | null | '#notpresent' |
| portalePagamento.inoltro | idA2A | idA2A |
| portalePagamento.inoltro | null | '#notpresent' |
| avvisaturaMail.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true } |
| avvisaturaMail.promemoriaAvviso | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false } |
| avvisaturaMail.promemoriaAvviso | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null } | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| avvisaturaMail.promemoriaAvviso | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null } | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| avvisaturaMail.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null, "allegaPdf" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent', "allegaPdf" : '#notpresent' } |
| avvisaturaMail.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : '#notpresent' } |
| avvisaturaMail.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : true } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : true } |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true , "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": true , "soloEseguiti" : '#notpresent'} |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false , "soloEseguiti" : null} | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "allegaPdf": false , "soloEseguiti" : '#notpresent'} |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null , "soloEseguiti" : null} | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' , "soloEseguiti" : '#notpresent'} |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null, "allegaPdf" : null , "soloEseguiti" : null} | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "allegaPdf" : '#notpresent' , "soloEseguiti" : '#notpresent'} |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null, "allegaPdf" : null , "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent', "allegaPdf" : '#notpresent' , "soloEseguiti" : '#notpresent'} |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : null , "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : '#notpresent' , "soloEseguiti" : '#notpresent'} |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : true , "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : true , "soloEseguiti" : '#notpresent'} |
| avvisaturaMail.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "allegaPdf" : true , "soloEseguiti" : true} | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "allegaPdf" : true , "soloEseguiti" : true } |
| avvisaturaMail.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso": 0 } | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare",  "preavviso": 0 } |
| avvisaturaMail.promemoriaScadenza | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso": 0 } | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso": 0 } |
| avvisaturaMail.promemoriaScadenza | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null, "preavviso" : null } | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "preavviso" : '#notpresent' } |
| avvisaturaMail.promemoriaScadenza | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null, "preavviso" : null } | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "preavviso" : '#notpresent' } |
| avvisaturaMail.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null, "preavviso" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent', "preavviso" : '#notpresent' } |
| avvisaturaMail.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "preavviso" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "preavviso" : '#notpresent' } |
| avvisaturaMail.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "preavviso" : 0 } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "preavviso" : 0 } |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare" } | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare"} |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare" } | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare" } |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null } | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent' } |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null } | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent' } |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null } | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent' } |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa" } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa" } |
| avvisaturaAppIO.promemoriaAvviso | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa" } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa"} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : '#notpresent'} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : null} | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "soloEseguiti" : '#notpresent'} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null, "soloEseguiti" : null} | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "soloEseguiti" : '#notpresent'} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null, "soloEseguiti" : null} | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "soloEseguiti" : '#notpresent'} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null, "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent', "soloEseguiti" : '#notpresent'} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "soloEseguiti" : '#notpresent'} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "soloEseguiti" : null} | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "soloEseguiti" : '#notpresent'} |
| avvisaturaAppIO.promemoriaRicevuta | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "soloEseguiti" : true} | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "soloEseguiti" : true } |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso": 0 } | { "abilitato": true, "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare",  "preavviso": 0 } |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso": 0 } | { "abilitato": false,  "tipo": "freemarker", "oggetto": "Promemoria pagamento", "messaggio": "Devi pagare", "preavviso": 0 } |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": false, "tipo": null, "oggetto": null, "messaggio": null, "preavviso" : null } | { "abilitato": false, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "preavviso" : '#notpresent' } |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": true, "tipo": null, "oggetto": null, "messaggio": null, "preavviso" : null } | { "abilitato": true, "tipo": '#notpresent', "oggetto": '#notpresent', "messaggio": '#notpresent', "preavviso" : '#notpresent' } |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": null, "preavviso" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": "aaa", "messaggio": '#notpresent', "preavviso" : '#notpresent' } |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "preavviso" : null } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "preavviso" : '#notpresent' } |
| avvisaturaAppIO.promemoriaScadenza | { "abilitato": true, "tipo": "freemarker", "oggetto": null, "messaggio": "aaa", "preavviso" : 0 } | { "abilitato": true, "tipo": "freemarker", "oggetto": '#notpresent', "messaggio": "aaa", "preavviso" : 0 } |
| visualizzazione | null | '#notpresent' |
| tracciatoCsv | null | '#notpresent'	 |


Scenario: Configurazione di due tipiPendenza con idTipoPendenza del secondo che e' una sottostringa del primo idTipoPendenza	 

* def idComune = getCurrentTimeMillis()
* def idTipoPendenza1 = 'PROVA_' + idComune
* def idTipoPendenza2 = 'OVA_' + idComune


Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza1
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza2
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201


Scenario: Autorizzazioni alla creazione dei tipi pendenza

* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['*'],
  tipiPendenza: ['#(codEntrataSegreteria)'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] }, 	{ servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true	
}
"""

* def idTipoPendenzaNonCensita = 'XXXX'

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'tipiPendenza', idTipoPendenzaNonCensita
And headers operatoreSpidAutenticationHeader
And request tipoPendenza
When method put
Then status 403
* match response == { categoria: 'AUTORIZZAZIONE', codice: '403000', descrizione: 'Operazione non autorizzata', dettaglio: '#notnull' }





