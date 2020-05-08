Feature: Lista tipipendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBasicBaseurl
And path 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Pendenza spontaneo' , codificaIUV: null, pagaTerzi: false}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: false,
  portaleBackoffice: null,
  portalePagamento: { 
  	abilitato : true, 
  	form: { 
	  	tipo: "angular2-json-schema-form",
	  	definizione: null
	  },
	  trasformazione: {
	  	tipo: "freemarker",
	  	definizione: null
	  },
	  validazione: null
  },
  avvisaturaMail: null,
  avvisaturaAppIO: null,
  abilitato: true,
  visualizzazione: null
}
"""          
* set tipoPendenzaDominio.portalePagamento.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-form.json.payload'))
* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-freemarker.ftl'))
* set tipoPendenzaDominio.portalePagamento.validazione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-validazione-form.json.payload'))
* set tipoPendenzaDominio.visualizzazione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-visualizzazione.json.payload'))

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
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

* def pagamentiSpidBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.risultati[*].idTipoPendenza contains codSpontaneo

Scenario: Verifica filtro abilitato

* set tipoPendenzaDominio.abilitato = false

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiSpidBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.risultati[*].idTipoPendenza !contains codSpontaneo

Scenario: Verifica filtro form

* set tipoPendenzaDominio.portalePagamento.form = null

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiSpidBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.risultati[*].idTipoPendenza !contains codSpontaneo

Scenario: Verifica filtro autorizzazione

* set tipoPendenzaDominio.portalePagamento.form = null

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiSpidBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.risultati[*].idTipoPendenza !contains codSpontaneo


Scenario: Verifica filtro tipo

* set tipoPendenzaDominio.portalePagamento.form = null

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiSpidBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.risultati[*].idTipoPendenza !contains codSpontaneo

Scenario: Verifica filtro tipo

* set tipoPendenzaDominio.portalePagamento.form = null

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pagamentiSpidBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiSpidBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.risultati[*].idTipoPendenza !contains codSpontaneo

