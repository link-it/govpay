Feature: Caricamento tracciato CSV

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def patchValue = 
"""
{
	tipo : "freemarker", 
	intestazione: "idA2A,idPendenza,idDominio,tipoPendenza,numeroAvviso,pdfAvviso,tipoSoggettoPagatore,identificativoPagatore,anagraficaPagatore,indirizzoPagatore,civicoPagatore,capPagatore,localitaPagatore,provinciaPagatore,nazionePagatore,emailPagatore,cellularePagatore,errore",	
  richiesta: null,
  risposta: null
}
"""

* configure retry = { count: 5, interval: 5000 }

* def importo = 122.5
* def importo_voce = 61.25

Scenario: Caricamento di un tracciato in formato CSV valido senza indicare il tipoPendenza in URL e utilizzo template di sistema

* set patchValue.richiesta = encodeBase64InputStream(read('msg/freemarker-request.ftl'))
* set patchValue.risposta = encodeBase64InputStream(read('msg/freemarker-response.ftl'))

Given url backofficeBaseurl
And path '/configurazioni' 
And headers basicAutenticationHeader
And request 
"""
[
	{
		op: "REPLACE",
		path: "/tracciatoCsv",
		value: #(patchValue)
	}
]
"""
When method patch
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio
And headers { 'Content-Type' : 'text/csv' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

* call sleep(1000)

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response.descrizioneStato == '' 
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'operazioni'
And headers basicAutenticationHeader
When method get
Then status 200
Then match response.risultati[0].richiesta.idTipoPendenza == codEntrataSegreteria
Then match response.risultati[0].richiesta.datiAllegati == { "foo": "baz", "bar": true }

Scenario: Caricamento di un tracciato in formato CSV valido indicando il tipoPendenza in URL e utilizzo template del tipoPendenza

* set patchValue.richiesta = encodeBase64InputStream(read('msg/freemarker-request.ftl'))
* set patchValue.risposta = encodeBase64InputStream(read('msg/freemarker-response.ftl'))

Given url backofficeBaseurl
And path '/configurazioni' 
And headers basicAutenticationHeader
And request 
"""
[
	{
		op: "REPLACE",
		path: "/tracciatoCsv",
		value: #(patchValue)
	}
]
"""
When method patch
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configurazione Tipo Pendenza con definizione custom del template di trasformazione definito per non utilizzare quello di sistema

* def idTipoPendenza = 'SCDS'
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
  validazione: null,
  visualizzazione: null,
  tracciatoCsv: {
  	tipo: "freemarker",
  	intestazione: #(patchValue.intestazione),
  	richiesta: null,
	  risposta: null
  }
}
"""          
* set tipoPendenza.form.definizione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenza.trasformazione.definizione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenza.validazione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-validazione-form.json'))
* set tipoPendenza.visualizzazione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-visualizzazione.json.payload'))
* set tipoPendenza.tracciatoCsv.richiesta = encodeBase64InputStream(read('msg/freemarker-request-tipopendenza.ftl'))
* set tipoPendenza.tracciatoCsv.risposta = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tracciato-csv-freemarker-response.ftl'))

Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configurazione Tipo Pendenza Dominio con template di trasformazione vuoto per utilizzare quello del parent

* def tipoPendenzaDominio = 
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
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", idTipoPendenza);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, idTipoPendenza
And headers { 'Content-Type' : 'text/csv' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

* call sleep(1000)

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response.descrizioneStato == '' 
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'operazioni'
And headers basicAutenticationHeader
When method get
Then status 200
Then match response.risultati[0].richiesta.idTipoPendenza == idTipoPendenza
Then match response.risultati[0].richiesta.datiAllegati == { "controlloTemplate": "Ridefinito Tipo Pendenza", "datiAllegatiCSV" : { "foo": "baz", "bar": true } }

Scenario: Caricamento di un tracciato in formato CSV valido indicando il tipoPendenza in URL e utilizzo template del sistema

* set patchValue.richiesta = encodeBase64InputStream(read('msg/freemarker-request.ftl'))
* set patchValue.risposta = encodeBase64InputStream(read('msg/freemarker-response.ftl'))

Given url backofficeBaseurl
And path '/configurazioni' 
And headers basicAutenticationHeader
And request 
"""
[
	{
		op: "REPLACE",
		path: "/tracciatoCsv",
		value: #(patchValue)
	}
]
"""
When method patch
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configurazione Tipo Pendenza con definizione vuota del template di trasformazione per utilizzare quello di sistema

* def idTipoPendenza = 'SCDS'
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
  validazione: null,
  visualizzazione: null,
  tracciatoCsv: null
}
"""          
* set tipoPendenza.form.definizione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenza.trasformazione.definizione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenza.validazione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-validazione-form.json'))
* set tipoPendenza.visualizzazione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-visualizzazione.json.payload'))

Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configurazione Tipo Pendenza Dominio con template di trasformazione vuoto per utilizzare quello del parent

* def tipoPendenzaDominio = 
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
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", idTipoPendenza);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, idTipoPendenza
And headers { 'Content-Type' : 'text/csv' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

* call sleep(1000)

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response.descrizioneStato == '' 
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'operazioni'
And headers basicAutenticationHeader
When method get
Then status 200
Then match response.risultati[0].richiesta.idTipoPendenza == idTipoPendenza
Then match response.risultati[0].richiesta.datiAllegati == { "foo": "baz", "bar": true }


Scenario: Caricamento di un tracciato in formato CSV valido indicando il tipoPendenza in URL e utilizzo template del tipoPendenzaDominio

* set patchValue.richiesta = encodeBase64InputStream(read('msg/freemarker-request.ftl'))
* set patchValue.risposta = encodeBase64InputStream(read('msg/freemarker-response.ftl'))

Given url backofficeBaseurl
And path '/configurazioni' 
And headers basicAutenticationHeader
And request 
"""
[
	{
		op: "REPLACE",
		path: "/tracciatoCsv",
		value: #(patchValue)
	}
]
"""
When method patch
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configurazione Tipo Pendenza con definizione null del template di trasformazione

* def idTipoPendenza = 'SCDS'
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
  validazione: null,
  visualizzazione: null,
  tracciatoCsv: null
}
"""          
* set tipoPendenza.form.definizione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenza.trasformazione.definizione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenza.validazione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-validazione-form.json'))
* set tipoPendenza.visualizzazione = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tipoPendenza-dovuta-visualizzazione.json.payload'))

Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configurazione Tipo Pendenza Dominio con template di trasformazione ridefinito

* def tipoPendenzaDominio = 
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
  "tracciatoCsv": {
  	tipo: "freemarker",
  	intestazione: #(patchValue.intestazione),
  	richiesta: null,
	  risposta: null
  }
}
"""

* set tipoPendenzaDominio.tracciatoCsv.richiesta = encodeBase64InputStream(read('msg/freemarker-request-tipopendenzadominio.ftl'))
* set tipoPendenzaDominio.tracciatoCsv.risposta = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/tipipendenza/put/msg/tracciato-csv-freemarker-response.ftl'))

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", idTipoPendenza);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, idTipoPendenza
And headers { 'Content-Type' : 'text/csv' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

* call sleep(1000)

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response.descrizioneStato == '' 
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'operazioni'
And headers basicAutenticationHeader
When method get
Then status 200
Then match response.risultati[0].richiesta.idTipoPendenza == idTipoPendenza
Then match response.risultati[0].richiesta.datiAllegati == { "controlloTemplate": "Ridefinito Tipo Pendenza Dominio", "datiAllegatiCSV" : { "foo": "baz", "bar": true } }


