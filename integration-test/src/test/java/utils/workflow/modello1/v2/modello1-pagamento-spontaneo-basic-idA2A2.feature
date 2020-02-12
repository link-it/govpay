Feature: Pagamento ad iniziativa Ente

# Impostare tipoRicevuta:
# 		PAGAMENTO_ESEGUITO_SENZA_RPT ("R00"), 
# 		PAGAMENTO_ESEGUITO ("R01"), 
# 		PAGAMENTO_NON_ESEGUITO ("R02"), 
# 		PAGAMENTO_PARZIALMENTE_ESEGUITO ("R03"), 
# 		DECORRENZA_TERMINI ("R04"), 
# 		DECORRENZA_TERMINI_PARZIALE ("R05"), 
# 		PAGAMENTO_ESEGUITO_SENZA_RPT_CON_RT("R12"); 

# Impostare cumulativo: 0 o 1

# Impostare idDominioPagamento

# Impostare codEntrataPagamento

Scenario: Pagamento ad iniziativa Ente

* def idPendenza = getCurrentTimeMillis()
* def pagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idDominio = idDominioPagamento
* set pagamentoPost.pendenze[0].idTipoPendenza = codTipoPendenzaPagamento
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codEntrataPagamento
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A2, password: pwdA2A2  } )
* set pagamentoPost.soggettoVersante = soggettoVersante 

Given url pagamentoBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* configure followRedirects = false
* def idSession = response.idSession
* def idPagamento = response.id

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominioPagamento
And param codice = tipoRicevuta
And param riversamento = cumulativo
And headers spidHeaders
When method get
Then status 302
And match responseHeaders.Location == '#notnull'

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')


