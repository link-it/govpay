Feature: Trasformazione pendenza

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')

Given url backofficeBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  form: { 
  	tipo: "angular2-json-schema-form",
  	definizione: null
  },
  trasformazione: {
  	tipo: "freemarker",
  	definizione: null
  },
  validazione: null
}
"""          
* set tipoPendenzaDominio.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenzaDominio.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-uo-freemarker.ftl'))
* set tipoPendenzaDominio.validazione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-validazione-form.json.payload'))

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario: Pendenza da form con valorizzazione dell'UO

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And param idUnitaOperativa = idUnitaOperativa
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 201


Given url backofficeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response.unitaOperativa.idUnita == idUnitaOperativa

Scenario: Pendenza da form con valorizzazione dell'UO da operatore non autorizzato

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa)' ] } ],
  tipiPendenza: ['*'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true
}
"""
When method put

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurlSpid = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBaseurlSpid
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And param idUnitaOperativa = idUnitaOperativa2
And headers operatoreSpidAutenticationHeader
And request 
"""
{
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 403


Scenario: Pendenza da form con valorizzazione dell'UO inesistente da operatore

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa)' ] } ],
  tipiPendenza: ['*'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true
}
"""
When method put

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurlSpid = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBaseurlSpid
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And param idUnitaOperativa = '00000000000'
And headers operatoreSpidAutenticationHeader
And request 
"""
{
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 403



