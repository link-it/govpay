Feature: Pagamento ad iniziativa Ente

# Fornire l'esito del pagamento atteso:
# 		PAGAMENTO_ESEGUITO_SENZA_RPT ("R00"), 
# 		PAGAMENTO_ESEGUITO ("R01"), 
# 		PAGAMENTO_NON_ESEGUITO ("R02"), 
# 		PAGAMENTO_PARZIALMENTE_ESEGUITO ("R03"), 
# 		DECORRENZA_TERMINI ("R04"), 
# 		DECORRENZA_TERMINI_PARZIALE ("R05"), 
# 		PAGAMENTO_ESEGUITO_SENZA_RPT_CON_RT("R12"); 

Scenario: Pagamento ad iniziativa Ente dominio1

* def idPendenza = getCurrentTimeMillis()
* def pagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')

Given url pagamentoBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def idPagamento1 = response.id

Given url ndpsym_psp_url
And path '/eseguiPagamento'
And param idSession = response.idSession
And param idDominio = idDominio
And param codice = tipoRicevuta
And param riversamentoCumulativo = riversamentoCumulativo
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Pagamento ad iniziativa Ente dominio2

* def idPendenza = getCurrentTimeMillis()
* def pagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.idDominio = idDominio_2

Given url pagamentoBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def idPagamento2 = response.id

Given url ndpsym_psp_url
And path '/eseguiPagamento'
And param idSession = reponse.idSession
And param idDominio = idDominio_2
And param codice = tipoRicevuta
And param riversamentoCumulativo = riversamentoCumulativo
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 200