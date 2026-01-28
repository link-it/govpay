Feature: Configurazione giornaleEventi - servizioGDE

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def patchRequest =
"""
[
  {
    "op": "REPLACE",
    "path": "/servizioGDE",
    "value": {
    	"abilitato": false
    }
  }
]
"""

Scenario: Configurazione servizioGDE disabilitato

* set patchRequest[0].value = { "abilitato": false }

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioGDE.abilitato == false

Scenario: Configurazione servizioGDE abilitato con URL

* set patchRequest[0].value = { "abilitato": true, "url": "https://gde.pagopa.it/api/v1" }

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioGDE.abilitato == true
And match response.servizioGDE.url == "https://gde.pagopa.it/api/v1"

Scenario: Configurazione servizioGDE con autenticazione HTTPBasic

* set patchRequest[0].value =
"""
{
	"abilitato": true,
	"url": "https://gde.pagopa.it/api/v1",
	"auth": {
		"username": "gdeuser",
		"password": "gdepass"
	}
}
"""

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioGDE.abilitato == true
And match response.servizioGDE.url == "https://gde.pagopa.it/api/v1"
And match response.servizioGDE.auth.username == "gdeuser"

Scenario: Configurazione servizioGDE con autenticazione HTTPHeader

* set patchRequest[0].value =
"""
{
	"abilitato": true,
	"url": "https://gde.pagopa.it/api/v1",
	"auth": {
		"headerName": "X-API-KEY",
		"headerValue": "secret-key-123"
	}
}
"""

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioGDE.abilitato == true
And match response.servizioGDE.auth.headerName == "X-API-KEY"

Scenario: Errore sintassi - servizioGDE abilitato senza URL

* set patchRequest[0].value = { "abilitato": true }

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'url'

Scenario: Errore sintassi - URL non valido

* set patchRequest[0].value = { "abilitato": true, "url": "htttttttp://aaa.it" }

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'url'

Scenario: Errore sintassi - campo abilitato mancante

* set patchRequest[0].value = { "url": "https://gde.pagopa.it/api/v1" }

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'abilitato'
