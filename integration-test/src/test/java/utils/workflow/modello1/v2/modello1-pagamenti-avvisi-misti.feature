Feature: Pagamento ad iniziativa Ente

# Fornire l'esito del pagamento atteso:
# 		PAGAMENTO_ESEGUITO_SENZA_RPT ("R00"),
# 		PAGAMENTO_ESEGUITO ("R01"),
# 		PAGAMENTO_NON_ESEGUITO ("R02"),
# 		PAGAMENTO_PARZIALMENTE_ESEGUITO ("R03"),
# 		DECORRENZA_TERMINI ("R04"),
# 		DECORRENZA_TERMINI_PARZIALE ("R05"),
# 		PAGAMENTO_ESEGUITO_SENZA_RPT_CON_RT("R12");

* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Pagamento ad iniziativa Ente dominio1

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def idPagamento1 = iuv

# Attivo il pagamento tramite il simulatore

* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'
* def versionePagamento = 2

Given url ndpsym_psp_url
And path 'attiva'
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = riversamentoCumulativo
And param versione = versionePagamento
When method get
Then assert responseStatus == 200

Scenario: Pagamento ad iniziativa Ente dominio2

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idDominio = idDominio_2

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def idPagamento2 = iuv

# Attivo il pagamento tramite il simulatore

* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'
* def versionePagamento = 2

Given url ndpsym_psp_url
And path 'attiva'
And param codDominio = idDominio_2
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = riversamentoCumulativo
And param versione = versionePagamento
When method get
Then assert responseStatus == 200


