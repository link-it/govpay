Feature: Caricamento tracciato CSV

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def freemarker_request = encodeBase64InputStream(read('msg/freemarker-request.ftl'))
* def freemarker_response = encodeBase64InputStream(read('msg/freemarker-response.ftl'))
* def response_csv_header = 


Given url backofficeBaseurl
And path 'configurazione', 
And headers basicAutenticationHeader
And request 
"""
[
	{
		op: "replace",
		path: "/tracciato_csv",
		value: {
			responseHeader: "idA2A,idPendenza,idDominio,tipoPendenza,numeroAvviso,pdfAvviso,tipoSoggettoPagatore,identificativoPagatore,anagraficaPagatore,emailPagatore,indirizzoPagatore,civicoPagatore,capPagatore,localitaPagatore,provinciaPagatore"
			freemarkerRequest: freemarker_request,
			freemarkerResponse: freemarker_response
		}
	}
]
"""
When method patch
Then status 200

* configure retry = { count: 10, interval: 1000 }

Scenario: Pagamento pendenza precaricata anonimo

* def idPendenza = getCurrentTimeMillis()
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And header Content-type = text/csv
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get