Feature: Creazione Tracciato GovPay

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')
* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-govpay.json')

* set dominio.servizioGovPay.tipoConnettore = 'EMAIL'
* set dominio.servizioGovPay.emailIndirizzi = ['pintori@link.it']
* set dominio.servizioGovPay.emailAllegato = false
 * set dominio.servizioGovPay.downloadBaseUrl = "http://localhost:8080/govpay/backend/api/backoffice/rs/form/v1/tracciatiNotificaPagamenti"
* set dominio.servizioGovPay.versioneZip = '1.0'
* set dominio.servizioGovPay.contenuti = ['RPP', 'SINTESI_PAGAMENTI', 'SINTESI_FLUSSI_RENDICONTAZIONE', 'FLUSSI_RENDICONTAZIONE']
* set dominio.servizioGovPay.tipiPendenza =  [ '*' ]

* def patchRequest = 
"""
[
  {
    "op": "REPLACE",
    "path": "/mailBatch",
    "value": {
			"abilitato": true,
			"mailserver": {
				"host": "smtp.link.it",
				"port": "25",
				"username": "govcloud",
				"password": "G65trw%$3we",
				"from": "govcloud@link.it",
				"readTimeout": 180000,
				"connectionTimeout": 20000,
				"sslConfig": {
					"abilitato": false
				},
				"startTls" : false
			}
		}
  }
]
"""

Scenario: Configurazione Dominio per spedizione tracciati govpay via rest e invocazione delle operazioni di creazione e spedizione tracciato

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* call read('classpath:utils/govpay-op-elaborazione-tracciati-notifica-pagamenti.feature')

# * call sleep(30000)

* call read('classpath:utils/govpay-op-spedizione-tracciati-notifica-pagamenti.feature')

* call sleep(60000)