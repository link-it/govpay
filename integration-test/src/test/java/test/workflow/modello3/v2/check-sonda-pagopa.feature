Feature: Verifica chiamata di check attivita di pagoPA

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* configure followRedirects = false

* def versionePagamento = 2

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')

* def faultBean = 
"""
	{
		"faultCode":"PAA_PAGAMENTO_SCONOSCIUTO",
		"faultString":"Pagamento in attesa risulta sconosciuto all’Ente Creditore.",
		"id":"#(idDominio)"
	}
"""

Scenario: Verifica sonda pagoPA

* def numeroAvviso = '000000000000000000'
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = 0

# Configurazione dell'applicazione

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == faultBean


Scenario: Verifica sonda pagoPA con dominio non intermediato

# Questo scenario verifica che il check Stand-In restituisca sempre PAA_PAGAMENTO_SCONOSCIUTO
# anche quando il dominio (EC) non e' tra quelli intermediati dalla stazione.
# Prima della fix, veniva restituito PAA_ID_DOMINIO_ERRATO che Stand-In interpretava come disservizio.
# Effettuiamo la chiamata SOAP direttamente a GovPay per bypassare il simulatore pagoPA.

* def numeroAvviso = '000000000000000000'

# Uso un codice fiscale di un dominio inesistente
* def idDominioNonIntermediato = '99999999999'

* def pagoPABaseurl = getGovPayApiBaseUrl({api: 'pagopa'})
* def pagopaBasicAutenticationHeader = getBasicAuthenticationHeader( { username: 'ndpsym', password: 'password' } )

* def paVerifyPaymentNoticeReq =
"""
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <ns6:paVerifyPaymentNoticeReq xmlns:ns6="http://pagopa-api.pagopa.gov.it/pa/paForNode.xsd">
            <idPA>#(idDominioNonIntermediato)</idPA>
            <idBrokerPA>#(idIntermediario)</idBrokerPA>
            <idStation>#(idStazione)</idStation>
            <qrCode>
                <fiscalCode>#(idDominioNonIntermediato)</fiscalCode>
                <noticeNumber>#(numeroAvviso)</noticeNumber>
            </qrCode>
        </ns6:paVerifyPaymentNoticeReq>
    </soap:Body>
</soap:Envelope>
"""

# Verifico il pagamento con dominio non intermediato chiamando direttamente GovPay
Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paVerifyPaymentNoticeReq
When method post
Then status 200
And match response contains 'PAA_PAGAMENTO_SCONOSCIUTO'
And match response contains 'Pagamento in attesa risulta sconosciuto'
