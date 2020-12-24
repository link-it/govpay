Feature: Lista tipipendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeBasicBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
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
  abilitato: true
}
"""          
* set tipoPendenzaDominio.portaleBackoffice.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenzaDominio.portaleBackoffice.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenzaDominio.portaleBackoffice.validazione = read('msg/tipoPendenza-dovuta-validazione-form.json')
* set tipoPendenzaDominio.portalePagamento.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenzaDominio.portalePagamento.validazione = read('msg/tipoPendenza-dovuta-validazione-form.json')

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBasicBaseurl
And path 'domini', idDominio_2, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['*'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true
}
"""

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario: Filtro per associati=true

Given url backofficeSpidBaseurl
And path 'domini'
And headers operatoreSpidAutenticationHeader
And param associati = true
When method get
Then status 200
And match response.numRisultati == 1

Scenario: Filtro per associati=true e form = true

Given url backofficeSpidBaseurl
And path 'domini'
And headers operatoreSpidAutenticationHeader
And param associati = true
And param form = true
When method get
Then status 200
And match response.numRisultati == 1

Scenario: Operatore con diritti sugli enti creditori e parametro associati  = false

* set operatore.acl = 
"""
[ 
	{ 
		servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] 
	},
	{ 
		servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] 
	}
]
"""

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'domini'
And headers operatoreSpidAutenticationHeader
And param associati = false
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }

Scenario: Operatore con diritti sugli enti creditori e parametro associati non impostato

* set operatore.acl = 
"""
[ 
	{ 
		servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] 
	},
	{ 
		servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] 
	}
]
"""

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'domini'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.numRisultati == 1


