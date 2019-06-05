Feature: stateful mock server

Background:
* def versamenti = {}
* def notificheAttivazione = {}
* def notificheTerminazione = {}
* def notificheAttivazioneByIdSession = {}
* def notificheTerminazioneByIdSession = {}
* def pagamentiPath = '/paServiceImpl'
* def pendenzaSconosciuta = {stato :'SCONOSCIUTA'}

# Servizi per il caricamento dati
Scenario: pathMatches(pagamentiPath+'/v1/avvisi/{idDominio}/{iuv}') && methodIs('post')
  * eval versamenti[pathParams.idDominio + pathParams.iuv] = request

Scenario: pathMatches(pagamentiPath+'/v1/pendenze/{idA2A}/{idPendenza}') && methodIs('post')
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
<SOAP-ENV:Fault xmlns="">
   <faultcode>SOAP-ENV:Server</faultcode>
   <faultstring>Internal server error</faultstring>
   <detail>Bla bla bla bla bla bla</detail> 
</SOAP-ENV:Fault>
"""
  
Scenario:
	* def responseStatus = 404

    



