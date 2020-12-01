Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})

Scenario: Verifica avviso precaricato anonimo

* def idPendenza = getCurrentTimeMillis()
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso


Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And header Accept = 'application/json'
When method get
Then status 200
And match response == 
"""
{ 
"stato":"NON_ESEGUITA",
"importo":100.99,
"idDominio":"#(idDominio)",
"numeroAvviso":"#(numeroAvviso)",
"dataValidita":"2900-12-31",
"dataScadenza":"2999-12-31",
"descrizione":"#notnull",
"tassonomiaAvviso":"Servizi erogati dal comune",
"qrcode":'#("PAGOPA|002|" + numeroAvviso + "|" + idDominio + "|10099")',
"barcode":"#notnull"
}
"""

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And header Accept = 'application/pdf'
When method get
Then status 403



Scenario: Verifica avviso generato dopo il caricamento di uno spontaneo anonimo

* def idPendenza = getCurrentTimeMillis()
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
# * def pendenza = read('classpath:test/api/pagamento/v2/pendenze/post/msg/pendenza-creata-anonimo.json')
* def idTipoPendenzaCOSAP = 'COSAP'
* def pendenzaCreataMSG = read('classpath:test/api/pagamento/v2/pendenze/post/msg/pendenza-creata-anonimo.json')

# Configurazione tipo pendenza
Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenzaCOSAP
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Canone Occupazione Spazi ed Aree Pubbliche' , codificaIUV: null, pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio =
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  abilitato: true,
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
        intestazione: "id,numeroAvviso,pdfAvviso,anagrafica,indirizzo,civico,localita,cap,provincia",
        richiesta: null,
          risposta: null
  }
}
"""
* set tipoPendenzaDominio.portaleBackoffice = null
* set tipoPendenzaDominio.portalePagamento.form.definizione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-form.json'))
* set tipoPendenzaDominio.portalePagamento.form.impaginazione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-impaginazione.json'))
* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-trasformazione.ftl'))
* set tipoPendenzaDominio.portalePagamento.validazione = encodeBase64InputStream(karate.readAsString('classpath:test/api/pagamento/v2/pendenze/post/msg/cosap/portale-validazione.json'))
* set tipoPendenzaDominio.avvisaturaMail = null
* set tipoPendenzaDominio.tracciatoCsv = null

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenzaCOSAP
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

* def numeroAvviso = response.numeroAvviso

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And header Accept = 'application/json'
When method get
Then status 200
And match response == 
"""
{ 
"stato":"NON_ESEGUITA",
"importo":100.01,
"idDominio":"#(idDominio)",
"numeroAvviso":"#(numeroAvviso)",
"dataValidita":"#notnull",
"dataScadenza":"#notnull",
"descrizione":"#notnull",
"tassonomiaAvviso":"Servizi erogati dal comune",
"qrcode":'#("PAGOPA|002|" + numeroAvviso + "|" + idDominio + "|10001")',
"barcode":"#notnull"
}
"""

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And header Accept = 'application/pdf'
When method get
Then status 200


