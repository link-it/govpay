Feature: Verifica che le primitive dismesse delle API pagoPA restituiscano un SOAP fault

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def pagoPABaseurl = getGovPayApiBaseUrl({api: 'pagopa'})
* def pagopaBasicAutenticationHeader = getBasicAuthenticationHeader( { username: 'ndpsym', password: 'password' } )

# Valori placeholder usati come segnaposto nei template SOAP. Le primitive dismesse
# rifiutano sempre la richiesta a livello di logica applicativa, quindi il contenuto
# semantico della richiesta e' irrilevante: e' sufficiente che lo XML rispetti lo
# schema CXF.
* def iuv = '00100000000000001'
* def numeroAvviso = '300100000000000001'


Scenario: paaVerificaRPT (SANP 2.3 - dismessa) restituisce SOAP fault PAA_SYSTEM_ERROR

* def soapRequest = read('classpath:test/api/pagopa/msg/paaVerificaRPTReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers { 'Content-Type' : 'application/xml' }
And request soapRequest
When method post
Then status 200
And match response /Envelope/Body//paaVerificaRPTRisposta/fault/faultCode == 'PAA_SYSTEM_ERROR'
And match response /Envelope/Body//paaVerificaRPTRisposta/fault/description contains 'Operazione non disponibile'


Scenario: paaAttivaRPT (SANP 2.3 - dismessa) restituisce SOAP fault PAA_SYSTEM_ERROR

* def soapRequest = read('classpath:test/api/pagopa/msg/paaAttivaRPTReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers { 'Content-Type' : 'application/xml' }
And request soapRequest
When method post
Then status 200
And match response /Envelope/Body//paaAttivaRPTRisposta/fault/faultCode == 'PAA_SYSTEM_ERROR'
And match response /Envelope/Body//paaAttivaRPTRisposta/fault/description contains 'Operazione non disponibile'


Scenario: paaInviaRT (SANP 2.3 - dismessa) sul servizio PagamentiTelematiciCCPservice restituisce SOAP fault PAA_SYSTEM_ERROR

* def soapRequest = read('classpath:test/api/pagopa/msg/paaInviaRTReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciCCPservice'
And headers pagopaBasicAutenticationHeader
And headers { 'Content-Type' : 'application/xml' }
And request soapRequest
When method post
Then status 200
And match response /Envelope/Body//paaInviaRTRisposta/fault/faultCode == 'PAA_SYSTEM_ERROR'
And match response /Envelope/Body//paaInviaRTRisposta/fault/description contains 'Operazione non disponibile'


Scenario: paaInviaRT (SANP 2.3 - dismessa) sul servizio PagamentiTelematiciRTservice restituisce SOAP fault PAA_SYSTEM_ERROR

* def soapRequest = read('classpath:test/api/pagopa/msg/paaInviaRTReq.xml')

Given url pagoPABaseurl
And path '/PagamentiTelematiciRTservice'
And headers pagopaBasicAutenticationHeader
And headers { 'Content-Type' : 'application/xml' }
And request soapRequest
When method post
Then status 200
And match response /Envelope/Body//paaInviaRTRisposta/fault/faultCode == 'PAA_SYSTEM_ERROR'
And match response /Envelope/Body//paaInviaRTRisposta/fault/description contains 'Non implementato'
