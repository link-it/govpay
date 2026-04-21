Feature: Pagamento ad iniziativa Ente

# Impostare tipoRicevuta:
# 		PAGAMENTO_ESEGUITO_SENZA_RPT ("R00"),
# 		PAGAMENTO_ESEGUITO ("R01"),
# 		PAGAMENTO_NON_ESEGUITO ("R02"),
# 		PAGAMENTO_PARZIALMENTE_ESEGUITO ("R03"),
# 		DECORRENZA_TERMINI ("R04"),
# 		DECORRENZA_TERMINI_PARZIALE ("R05"),
# 		PAGAMENTO_ESEGUITO_SENZA_RPT_CON_RT("R12");

# Fornire pendenzaPut

Scenario: Pagamento ad iniziativa Ente

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

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

# Attivo il pagamento tramite il simulatore

* def riversamentoCumulativo = cumulativo
* call read('classpath:utils/psp-paGetPayment.feature')




