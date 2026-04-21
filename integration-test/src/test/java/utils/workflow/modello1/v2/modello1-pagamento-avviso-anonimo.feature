Feature: Pagamento avviso precaricato

Background:

Scenario: Pagamento avviso precaricato anonimo

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

# Attivo il pagamento tramite il simulatore

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = 0
* call read('classpath:utils/psp-paGetPayment.feature')

* def idPagamentoAnonimo = iuv




