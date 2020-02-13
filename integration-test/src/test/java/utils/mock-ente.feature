Feature: stateful mock server

Background:

* def pagamentiPath = '/paServiceImpl'
* def pendenzaSconosciuta = {stato :'SCONOSCIUTA'}
* def versamenti = {}
* def notificheAttivazione = {}
* def notificheTerminazione = {}
* def notificheAttivazioneByIdSession = {}
* def notificheTerminazioneByIdSession = {}


* def pagoPaPath = '/pagopa'
* def pagoPaResponseCode = {}
* def pagoPaResponseMessage = {}

* def recaptchaPath = '/recaptcha'


# Servizi per il caricamento dati
Scenario: pathMatches(pagamentiPath+'/v1/avvisi/{idDominio}/{iuv}') && methodIs('post')
  * eval versamenti[pathParams.idDominio + pathParams.iuv] = request

Scenario: pathMatches(pagamentiPath+'/v1/pendenze/{idA2A}/{idPendenza}') && methodIs('put')
  * eval versamenti[pathParams.idA2A + pathParams.idPendenza] = request    
  
Scenario: pathMatches(pagamentiPath+'/v2/avvisi/{idDominio}/{numeroAvviso}') && methodIs('post')
  * eval versamenti[pathParams.idDominio + pathParams.numeroAvviso] = request  
  
# API Verifica per numero Avviso

Scenario: pathMatches(pagamentiPath+'/v1/avvisi/{idDominio}/{iuv}') && methodIs('get')
  * eval pendenza = versamenti[pathParams.idDominio + pathParams.iuv] == null ? pendenzaSconosciuta : versamenti[pathParams.idDominio + pathParams.iuv] 
  * def response = pendenza
  
Scenario: pathMatches(pagamentiPath+'/v2/avvisi/{idDominio}/{numeroAvviso}') && methodIs('get')
  * eval versamenti[pathParams.idDominio + pathParams.numeroAvviso] = request

# API Verifica per numero Pendenza

Scenario: pathMatches(pagamentiPath+'/v1/pendenze/{idA2A}/{idPendenza}') && methodIs('get')
  * eval pendenza = versamenti[pathParams.idA2A + pathParams.idPendenza] == null ? pendenzaSconosciuta : versamenti[pathParams.idA2A + pathParams.idPendenza] 
  * def response = pendenza
  
# API Inoltro pendenza modello 4 al verticale

Scenario: pathMatches(pagamentiPath+'/v1/pendenze/{idDominio}/{idTipoPendenza}') && methodIs('post') && paramValue('idUnitaOperativa') != null
  * def responseStatus = 200 
  * eval pendenza = versamenti[request.idA2A + request.idPendenza] == null ? pendenzaSconosciuta : versamenti[request.idA2A + request.idPendenza] 
  * eval pendenza.idUnitaOperativa = paramValue('idUnitaOperativa')
  * def response = pendenza
  
Scenario: pathMatches(pagamentiPath+'/v1/pendenze/{idDominio}/{idTipoPendenza}') && methodIs('post')
  * def responseStatus = 200 
  * eval pendenza = versamenti[request.idA2A + request.idPendenza] == null ? pendenzaSconosciuta : versamenti[request.idA2A + request.idPendenza] 
  * def response = pendenza
  
# Tipo Pendenza diverso da quello richiesto
Scenario: pathMatches(pagamentiPath+'/vTP/pendenze/{idDominio}/{idTipoPendenza}') && methodIs('post')
	* def responseStatus = 200 
  * eval pendenza = versamenti[request.idA2A + request.idPendenza] == null ? pendenzaSconosciuta : versamenti[request.idA2A + request.idPendenza] 
  * eval pendenza.idTipoPendenza = 'CODENTRATA'
  * def response = pendenza

# Errore interno
Scenario: pathMatches(pagamentiPath+'/vERROR/pendenze/{idDominio}/{idTipoPendenza}') && methodIs('post')
	* def responseStatus = 500  
	* def response =  
"""	
<?xml version = '1.0' encoding = 'UTF-8'?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV = "http://schemas.xmlsoap.org/soap/envelope/"
   xmlns:xsi = "http://www.w3.org/1999/XMLSchema-instance"
   xmlns:xsd = "http://www.w3.org/1999/XMLSchema">
   <SOAP-ENV:Body>
      <SOAP-ENV:Fault>
         <faultcode xsi:type = "xsd:string">SOAP-ENV:Client</faultcode>
         <faultstring xsi:type = "xsd:string">Failed to locate method</faultstring>
      </SOAP-ENV:Fault>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
"""

# API Notifica
    
Scenario: pathMatches(pagamentiPath+'/v1/pagamenti/{idDominio}/{iuv}') && methodIs('post')
  * def idDominio = request.rpt.dominio.identificativoDominio
  * def iuv = request.rpt.datiVersamento.identificativoUnivocoVersamento
  * def ccp = request.rpt.datiVersamento.codiceContestoPagamento == 'n/a' ? 'n_a' : request.rpt.datiVersamento.codiceContestoPagamento
  * def repo = request.rt == null ? notificheAttivazione : notificheTerminazione
  * def repoByIdSession = request.rt == null ? notificheAttivazioneByIdSession : notificheTerminazioneByIdSession
  * def responseStatus = repo[idDominio+iuv+ccp] == null ? 200: 201
  * eval repo[idDominio+iuv+ccp] = request    
  * eval repoByIdSession[paramValue('idSession')] = request   
    
Scenario: pathMatches(pagamentiPath+'/notificaAttivazione/{idDominio}/{iuv}/{ccp}') && methodIs('get')
  * def responseStatus = notificheAttivazione[pathParams.idDominio+pathParams.iuv+pathParams.ccp] == null ? 404: 200
  * def response = notificheAttivazione[pathParams.idDominio+pathParams.iuv+pathParams.ccp]
    
Scenario: pathMatches(pagamentiPath+'/notificaTerminazione/{idDominio}/{iuv}/{ccp}') && methodIs('get')
  * def responseStatus = notificheTerminazione[pathParams.idDominio+pathParams.iuv+pathParams.ccp] == null ? 404: 200
  * def response = notificheTerminazione[pathParams.idDominio+pathParams.iuv+pathParams.ccp]
    
Scenario: pathMatches(pagamentiPath+'/notificaAttivazioneByIdSession/{idSession}') && methodIs('get')
  * def responseStatus = notificheAttivazioneByIdSession[pathParams.idSession] == null ? 404: 200
  * def response = notificheAttivazioneByIdSession[pathParams.idSession]
    
Scenario: pathMatches(pagamentiPath+'/notificaTerminazioneByIdSession/{idSession}') && methodIs('get')
  * def responseStatus = notificheTerminazioneByIdSession[pathParams.idSession] == null ? 404: 200
  * def response = notificheTerminazioneByIdSession[pathParams.idSession]    
    
Scenario: pathMatches(pagamentiPath+'/v1/versamenti') && methodIs('get')
  * def response = versamenti  
  
Scenario: pathMatches(pagamentiPath+'/vERROR/avvisi/{idDominio}/{iuv}') 
	* def responseStatus = 500  
	* def response =  
"""	
<?xml version = '1.0' encoding = 'UTF-8'?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV = "http://schemas.xmlsoap.org/soap/envelope/"
   xmlns:xsi = "http://www.w3.org/1999/XMLSchema-instance"
   xmlns:xsd = "http://www.w3.org/1999/XMLSchema">
   <SOAP-ENV:Body>
      <SOAP-ENV:Fault>
         <faultcode xsi:type = "xsd:string">SOAP-ENV:Client</faultcode>
         <faultstring xsi:type = "xsd:string">Failed to locate method</faultstring>
      </SOAP-ENV:Fault>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
"""
  
Scenario: pathMatches(pagoPaPath+'/setResponse/{status}') && methodIs('post')
	* def pagoPaResponseMessage = request
	* def pagoPaResponseCode = pathParams.status

Scenario: pathMatches(pagoPaPath+'/PagamentiTelematiciRPTservice') && methodIs('post')
	* def responseStatus = pagoPaResponseCode  
	* def response = pagoPaResponseMessage  
  
Scenario: pathMatches(recaptchaPath+'/v2/{success}') 
	* def responseStatus = 200  
	* def response =  
"""	
{
  "success": '#(pathParams.success)',
  "challenge_ts": "2000-12-31T23:59:00+0000",
  "hostname": "https://hostname.org/"
}
"""

Scenario: pathMatches(recaptchaPath+'/v3/{success}/{score}') 
	* def responseStatus = 200  
	* def response =  
"""	
{
  "success": '#(pathParams.success)',
  "score": '#(pathParams.score)',
  "action": 'login',
  "challenge_ts": "2000-12-31T23:59:00+0000",
  "hostname": "https://hostname.org/"
}
"""

Scenario:
	* def responseStatus = 404
  * def response = "PATH NON PREVISTO DAL MOCK"

    



