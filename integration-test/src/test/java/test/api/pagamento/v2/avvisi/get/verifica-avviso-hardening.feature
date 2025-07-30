Feature: Verifica avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Given url backofficeBaseurl
And path 'configurazioni'
And headers gpAdminBasicAutenticationHeader 
And request 
"""
[
  {
    "op": "REPLACE",
    "path": "/hardening",
    "value": {
			"abilitato": true,
			"captcha": {
				"serverURL": "#(recaptcha_api_url + '/v2/true')",
				"siteKey": "siteKey",
				"secretKey": "secretKey",
				"soglia": 0.7,
				"parametro": "gRecaptchaResponse",
				"denyOnFail": true,
				"connectionTimeout": 5000,
				"readTimeout": 5000
			}
		}
  }
]
"""
When method patch
Then status 200


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

* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()
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

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param gRecaptchaResponse = 'v2ok'
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG

* def numeroAvviso = response.numeroAvviso
* def uuidPendenza = response.UUID

# * configure cookies = null

Scenario: Verifica avviso ok con recaptcha

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And param gRecaptchaResponse = 'v2ok'
And header Accept = 'application/json'
When method get
Then status 200

Scenario: Verifica avviso ok con uuid

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And param UUID = uuidPendenza
And param gRecaptchaResponse = 'v2ok'
And header Accept = 'application/json'
When method get
Then status 200

Scenario: Verifica avviso ok con recaptcha ma uuid non valido

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And param UUID = '123456'
And param gRecaptchaResponse = 'v2ok'
And header Accept = 'application/json'
When method get
Then status 200

# verifiche sul pdf

@pdf1
Scenario: Verifica avviso ok con recaptcha

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And param gRecaptchaResponse = 'v2ok'
And header Accept = 'application/pdf'
When method get
Then status 200

@pdf2
Scenario: Verifica avviso ok con uuid

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And param UUID = uuidPendenza
And param gRecaptchaResponse = 'v2ok'
And header Accept = 'application/pdf'
When method get
Then status 200

@pdf3
Scenario: Verifica avviso ok con recaptcha ma uuid non valido

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And param UUID = '123456'
And param gRecaptchaResponse = 'v2ok'
And header Accept = 'application/pdf'
When method get
Then status 200

# verifiche casi non autorizzato

Scenario: Verifica avviso senza UUID

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And header Accept = 'application/json'
When method get
Then status 403

Scenario: Verifica avviso con UUID errato

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And param UUID = '123456'
And header Accept = 'application/json'
When method get
Then status 403
