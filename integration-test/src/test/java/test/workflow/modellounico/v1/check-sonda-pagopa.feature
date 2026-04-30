Feature: Verifica chiamata di check attivita di pagoPA

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* configure followRedirects = false

* def stazioneNdpSymPut = read('classpath:test/workflow/modellounico/v1/msg/stazione.json')

Scenario: Verifica sonda pagoPA

# Il numeroAvviso '000000000000000000' e' il pattern della sonda pagoPA (Stand-In check).
# GovPay deve riconoscerlo e rispondere con PAA_PAGAMENTO_SCONOSCIUTO senza prima
# verificare il dominio (fix Issue #848).
# Effettuiamo la chiamata SOAP direttamente a GovPay per bypassare il simulatore pagoPA
# che farebbe un proprio check del dominio prima di inoltrare la richiesta.

* def numeroAvviso = '000000000000000000'

* def pagoPABaseurl = getGovPayApiBaseUrl({api: 'pagopa'})
* def pagopaBasicAutenticationHeader = getBasicAuthenticationHeader( { username: 'ndpsym', password: 'password' } )

* def paVerifyPaymentNoticeReq =
"""
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <ns6:paVerifyPaymentNoticeReq xmlns:ns6="http://pagopa-api.pagopa.gov.it/pa/paForNode.xsd">
            <idPA>#(idDominio)</idPA>
            <idBrokerPA>#(idIntermediario)</idBrokerPA>
            <idStation>#(idStazione)</idStation>
            <qrCode>
                <fiscalCode>#(idDominio)</fiscalCode>
                <noticeNumber>#(numeroAvviso)</noticeNumber>
            </qrCode>
        </ns6:paVerifyPaymentNoticeReq>
    </soap:Body>
</soap:Envelope>
"""

# Verifico il pagamento sonda chiamando direttamente GovPay
Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers {'Content-Type' : 'application/xml'}
And request paVerifyPaymentNoticeReq
When method post
Then status 200
And match response contains 'PAA_PAGAMENTO_SCONOSCIUTO'
And match response contains 'Pagamento in attesa risulta sconosciuto'


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
