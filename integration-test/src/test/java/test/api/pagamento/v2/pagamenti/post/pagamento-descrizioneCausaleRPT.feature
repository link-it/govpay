Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Pagamento pendenza precaricata con causaleRPT custom

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenza.voci[0].descrizioneCausaleRPT = 'Causale RPT Custom'

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def rispostaGetPendenza = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')
Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method get
Then status 200
And match response == rispostaGetPendenza
And match response.voci[0].descrizioneCausaleRPT == pendenza.voci[0].descrizioneCausaleRPT

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')
* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "RSSMRA30A01H501I",
  "anagrafica": "Mario Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-1234567"
}
"""

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
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].causaleVersamento contains pendenza.voci[0].descrizioneCausaleRPT

Scenario: Pagamento spontaneo con causaleRPT custom

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "RSSMRA30A01H501I",
  "anagrafica": "Mario Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-1234567"
}
"""
* set pagamentoPost.pendenze[0].voci[0].descrizioneCausaleRPT = 'Causale RPT Custom'
* set pagamentoPost.pendenze[0].voci[1].descrizioneCausaleRPT = 'Causale RPT Custom per Bollo'

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].causaleVersamento contains pagamentoPost.pendenze[0].voci[0].descrizioneCausaleRPT
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[1].causaleVersamento contains pagamentoPost.pendenze[0].voci[1].descrizioneCausaleRPT



