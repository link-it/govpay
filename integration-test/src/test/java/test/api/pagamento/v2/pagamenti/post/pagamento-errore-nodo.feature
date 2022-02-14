Feature: Pagamento con Nodo in errore

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* configure followRedirects = false
* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	"denominazione": "Soggetto Intermediario",
	"principalPagoPa": '#(ndpsym_user)',
	"servizioPagoPa": {
		"urlRPT": '#(pagopa_api_url + "/PagamentiTelematiciRPTservice")'
	},
	"abilitato": true
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario: Nodo dei pagamenti in errore

Given url pagopa_api_url + "/setResponse/500"
And request 
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
When method post
Then assert responseStatus == 200

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 502
And match response == { id: '#notnull', location: '#notnull', categoria : 'PAGOPA', codice : 'PAA_NODO_INDISPONIBILE', descrizione: '#notnull', dettaglio: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/', response.id
And headers basicAutenticationHeader
When method get
Then status 200
And match response.stato == 'FALLITO'

Given url pagamentiBaseurl
And path '/rpp'
And param idPendenza = idPendenza
And param idA2A = pendenza.idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza)'
And match response.risultati[0].rt == '#notpresent'
And match response.risultati[0].stato == 'RPT_ERRORE_INVIO_A_NODO'

* callonce read('classpath:utils/govpay-op-acquisisci-verifica-transazioni-pendenti.feature')

Given url pagamentiBaseurl
And path '/rpp'
And param idPendenza = idPendenza
And param idA2A = pendenza.idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza)'
And match response.risultati[0].rt == '#notpresent'
And match response.risultati[0].stato == 'RPT_ERRORE_INVIO_A_NODO'

Scenario: Nodo dei pagamenti in errore 2

* def intermediario = read('classpath:configurazione/v1/msg/intermediario.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers gpAdminBasicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenza.importo = 999.01
* set pendenza.voci[0].importo = 999.01

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 502
And match response == { id: '#notnull', location: '#notnull', categoria : 'PAGOPA', codice : 'PAA_NODO_INDISPONIBILE', descrizione: '#notnull' , dettaglio: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/', response.id
And headers basicAutenticationHeader
When method get
Then status 200
And match response.stato == 'FALLITO'

Given url pagamentiBaseurl
And path '/rpp'
And param idPendenza = idPendenza
And param idA2A = pendenza.idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza)'
And match response.risultati[0].rt == '#notpresent'
And match response.risultati[0].stato == 'RPT_ATTIVATA'

* callonce read('classpath:utils/govpay-op-acquisisci-verifica-transazioni-pendenti.feature')

Given url pagamentiBaseurl
And path '/rpp'
And param idPendenza = idPendenza
And param idA2A = pendenza.idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza)'
And match response.risultati[0].rt == '#notpresent'
And match response.risultati[0].stato == 'RPT_ERRORE_INVIO_A_NODO'

