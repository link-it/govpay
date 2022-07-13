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
	  promemoriaRicevuta : {
	  	tipo: "freemarker",
	  	oggetto : "Pagamento Pendenza ",
	  	messaggio: "Pagamento Pendenza ",
	  	allegaPdf: false
	  }
  }
}
"""       

* set tipoPendenzaDominio.avvisaturaMail.promemoriaRicevuta.oggetto = encodeBase64InputStream(read('classpath:configurazione/v1/msg/notifica-oggetto-freemarker.ftl'))
* set tipoPendenzaDominio.avvisaturaMail.promemoriaRicevuta.messaggio = encodeBase64InputStream(read('classpath:configurazione/v1/msg/notifica-messaggio-freemarker.ftl'))   

* def tipoRicevuta = "R00"
* def riversamentoCumulativo = "true"

Scenario: Pendenza caricata con invio Notifica di quietanza di pagamento

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
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

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

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Given url ndpsym_psp_url 
And path 'attiva' 
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = riversamentoCumulativo
When method get
Then assert responseStatus == 200

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('classpath:test/api/backoffice/v1/riconciliazioni/post/msg/riconciliazione-singola-senza-rpt.json')

Given url backofficeBaseurl
And path '/flussiRendicontazione', response.idFlusso
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

# aspetto spedizione notifica 

* call sleep(1000)

* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

* call sleep(20000)



Scenario: Pendenza caricata con invio Notifica di quietanza di pagamento e allegato PDF

* set tipoPendenzaDominio.avvisaturaMail.promemoriaRicevuta.allegaPdf = true

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
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

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

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Given url ndpsym_psp_url 
And path 'attiva' 
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = riversamentoCumulativo
When method get
Then assert responseStatus == 200

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('classpath:test/api/backoffice/v1/riconciliazioni/post/msg/riconciliazione-singola-senza-rpt.json')

Given url backofficeBaseurl
And path '/flussiRendicontazione', response.idFlusso
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

# aspetto spedizione notifica 

* call sleep(1000)

* call read('classpath:utils/govpay-op-gestione-promemoria.feature')

* call sleep(20000)




