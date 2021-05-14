Feature: Creazione Tracciato GovPay

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-govpay.json')

* set dominio.servizioGovPay.versioneCsv = "3.0"
* set dominio.servizioGovPay.tipoConnettore = 'REST'
* set dominio.servizioGovPay.url = 'http://localhost:8888/enteRendicontazioni'
* set dominio.servizioGovPay.versioneApi = 'REST v1'
* set dominio.servizioGovPay.contenuti = ['RPP', 'SINTESI_PAGAMENTI', 'SINTESI_FLUSSI_RENDICONTAZIONE', 'FLUSSI_RENDICONTAZIONE']
* set dominio.servizioGovPay.tipiPendenza =  [ '#(codEntrataSegreteria)' ]

Scenario: Configurazione Dominio per spedizione tracciati govpay via rest e invocazione delle operazioni di creazione e spedizione tracciato

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