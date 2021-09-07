Feature: Pagamenti spontanei definizione del parametro opzionale codiceConvenzione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

@test1
Scenario: Pagamento spontaneo anonimo con codiceConvenzione = CCOK-REDIRECT

* def codiceConvenzione = 'CCOK-REDIRECT'

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

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
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
And param codiceConvenzione = codiceConvenzione
And request pagamentoPost
When method post
Then status 403
And match response == { categoria : 'AUTORIZZAZIONE', codice : '403000', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.descrizione == 'Operazione non autorizzata'
And match response.dettaglio == 'Il richiedente non è autorizzato ad indicare un codice convenzione per il pagamento'

