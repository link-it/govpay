Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def pagoPABaseurl = getGovPayApiBaseUrl({api: 'pagopa'})
* def pagopaBasicAutenticationHeader = getBasicAuthenticationHeader( { username: 'ndpsym', password: 'password' } )

Scenario: Verifica avviso e attivazione

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo
* def causale = pendenzaPut.causale

* def paVerifyPaymentNoticeReq = read('classpath:test/api/pagopa/msg/paVerifyPaymentNoticeReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paVerifyPaymentNoticeReq
When method post
Then status 200

* def paGetPaymentReq = read('classpath:test/api/pagopa/msg/paGetPaymentReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paGetPaymentReq
When method post
Then status 200

Scenario: Verifica avviso e attivazione con due date

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo
* def causale = pendenzaPut.causale

* def paVerifyPaymentNoticeReq = read('classpath:test/api/pagopa/msg/paVerifyPaymentNoticeReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paVerifyPaymentNoticeReq
When method post
Then status 200

* def paGetPaymentReq = read('classpath:test/api/pagopa/msg/paGetPaymentReq-dueDate.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paGetPaymentReq
When method post
Then status 200

Scenario: Verifica avviso, attivazione e ricevuta

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo
* def causale = pendenzaPut.causale

* def paVerifyPaymentNoticeReq = read('classpath:test/api/pagopa/msg/paVerifyPaymentNoticeReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paVerifyPaymentNoticeReq
When method post
Then status 200

* def paGetPaymentReq = read('classpath:test/api/pagopa/msg/paGetPaymentReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paGetPaymentReq
When method post
Then status 200

* def receiptId = iuv + '0123'	

* def paSendRTReq = read('classpath:test/api/pagopa/msg/paSendRTReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paSendRTReq
When method post
Then status 200



