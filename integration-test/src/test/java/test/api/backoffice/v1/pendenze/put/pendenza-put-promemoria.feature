Feature: Invio Promemoria pendenza via mail

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def tipoPendenzaPromemoria = 'PROMEMORIA'

Given url backofficeBaseurl
And path 'tipiPendenza', tipoPendenzaPromemoria
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Pendenza Promemoria' , codificaIUV: null, pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  portaleBackoffice: null,
  avvisaturaMail: {
	  promemoriaAvviso : {
	  	tipo: "freemarker",
	  	oggetto : "Pagamento Pendenza ",
	  	messaggio: "Pagamento Pendenza ",
	  	allegaPdf: false
	  }
  }
}
"""       
* set tipoPendenzaDominio.avvisaturaMail.promemoriaAvviso.oggetto = encodeBase64InputStream(read('msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))
* set tipoPendenzaDominio.avvisaturaMail.promemoriaAvviso.messaggio = encodeBase64InputStream(read('msg/tipoPendenza-promemoria-messaggio-freemarker.ftl'))   

Scenario: Pendenza caricata con invio Promemoria senza avviso di pagamento

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaPromemoria
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaPromemoria
* set pendenzaPut.soggettoPagatore.email = "pintori@link.it"

Given url backofficeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

* def responsePut = response

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'



Scenario: Pendenza caricata con invio Promemoria con avviso di pagamento

* set tipoPendenzaDominio.avvisaturaMail.promemoriaAvviso.allegaPdf = true

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaPromemoria
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaPromemoria
* set pendenzaPut.soggettoPagatore.email = "pintori@link.it"

Given url backofficeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

* def responsePut = response

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'




