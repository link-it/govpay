Feature: Lista tipipendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

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
* set tipoPendenzaDominio.portaleBackoffice.validazione = encodeBase64InputStream(karate.readAsString('msg/tipoPendenza-dovuta-validazione-form.json'))
* set tipoPendenzaDominio.portalePagamento.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenzaDominio.portalePagamento.validazione = encodeBase64InputStream(karate.readAsString('msg/tipoPendenza-dovuta-validazione-form.json'))

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['#(codLibero)', '#(tipoPendenzaRinnovo)', '#(codSpontaneo)'],
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

Scenario: Filtro per tipo, abilitazione e form

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
And param form = true
And param abilitato = true
And param tipo = 'dovuto'
When method get
Then status 200
And match response.numRisultati == 1
And match response.risultati[0].idTipoPendenza == tipoPendenzaRinnovo

Scenario: Verifica filtro abilitato

* set tipoPendenzaDominio.abilitato = false

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
And param form = true
And param abilitato = true
And param tipo = 'dovuto'
When method get
Then status 200
And match response.numRisultati == 0

Scenario: Verifica filtro form

* set tipoPendenzaDominio.portaleBackoffice.form = null

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
And param form = true
And param abilitato = true
And param tipo = 'dovuto'
When method get
Then status 200
And match response.numRisultati == 0

Scenario: Verifica filtro autorizzazione

* set tipoPendenzaDominio.portaleBackoffice.form = null

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
And param form = true
And param abilitato = true
And param tipo = 'dovuto'
When method get
Then status 200
And match response.numRisultati == 0

Scenario: Verifica filtro abilitato

* set tipoPendenzaDominio.portaleBackoffice.form = null

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
And param abilitato = true
When method get
Then status 200
And match response.numRisultati == 3

Scenario: Lettura TipiPendenza associati ad un dominio non esistente

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'domini', idDominioNonCensito, 'tipiPendenza'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 


Scenario: Lettura di un TipoPendenza associato ad un dominio non esistente

* def idDominioNonCensito = '11221122331'
* def idTipoPendenzaNonCensito = 'AZNEDNEP_OPIT'

Given url backofficeBaseurl
And path 'domini', idDominioNonCensito, 'tipiPendenza' , idTipoPendenzaNonCensito
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 

Scenario: Lettura di un TipoPendenza inesistente associato ad un dominio

* def idTipoPendenzaNonCensito = 'AZNEDNEP_OPIT'

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza' , idTipoPendenzaNonCensito
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Tipo Pendenza "+idTipoPendenzaNonCensito+" non censito in Anagrafica per il Dominio "+idDominio+"")' 




