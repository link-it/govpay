Feature: Controllo sintassi causale versamento RPT

Background: 

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* configure followRedirects = false
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )


Scenario: Iban appoggio in tributo spontaneo iniziativa ente

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers basicAutenticationHeader
When method get
Then status 200	

* def iuv = response.rpp[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def causale = "/RFB/" + iuv + "/99.99/TXT/Diritti e segreteria"

* match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].causaleVersamento == causale