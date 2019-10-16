Feature: Validazione sintattica richieste pagamento

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "RSSMRA30A01H501I",
  "anagrafica": "Mario Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-1234567"
}
"""
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'public'})

Scenario: Recaptcha v2 ok

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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/pagamenti'
And param gRecaptchaResponse = 'v2ok'
And request pagamentoPost
When method post
Then status 201

Scenario: Recaptcha v3 ok

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
				"serverURL": "#(recaptcha_api_url + '/v3/true/0.8')",
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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/pagamenti'
And param gRecaptchaResponse = 'v3ok'
And request pagamentoPost
When method post
Then status 201

Scenario: Recaptcha v3 fail: low score

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
				"serverURL": "#(recaptcha_api_url + '/v3/true/0.6')",
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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/pagamenti'
And param gRecaptchaResponse = 'v3fail'
And request pagamentoPost
When method post
Then status 403

Scenario: Recaptcha fail: gRecaptchaResponse non fornito

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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/pagamenti'
And request pagamentoPost
When method post
Then status 403


Scenario: Recaptcha fail: gRecaptchaResponse non valido

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
				"serverURL": "#(recaptcha_api_url + '/v2/false')",
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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/pagamenti'
And param gRecaptchaResponse = 'xxx'
And request pagamentoPost
When method post
Then status 403


Scenario: Recaptcha fail: google service not available

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
				"serverURL": "http://unknown",
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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/pagamenti'
And param gRecaptchaResponse = 'xxx'
And request pagamentoPost
When method post
Then status 403


Scenario: Recaptcha denyOnFail: google service not available, denyOnFail=false

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
				"serverURL": "http://unknown",
				"siteKey": "siteKey",
				"secretKey": "secretKey",
				"soglia": 0.7,
				"parametro": "gRecaptchaResponse",
				"denyOnFail": false,
				"connectionTimeout": 5000,
				"readTimeout": 5000
			}
		}
  }
]
"""
When method patch
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pagamentiBaseurl
And path '/pagamenti'
And param gRecaptchaResponse = 'denyOnFail'
And request pagamentoPost
When method post
Then status 201
