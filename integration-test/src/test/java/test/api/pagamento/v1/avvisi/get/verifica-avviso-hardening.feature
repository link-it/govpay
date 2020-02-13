Feature: Pagamento avviso precaricato

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

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'public'})

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

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
