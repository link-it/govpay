Feature: RPT con iban di appoggio

Background: 

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* configure followRedirects = false
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

Scenario: Iban appoggio in tributo precaricato iniziativa ente

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')

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
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale

Scenario: Iban appoggio in tributo non precaricato iniziativa ente

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')

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
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale

Scenario: Iban appoggio in tributo spontaneo iniziativa ente

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo.json')

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
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale

Scenario: Iban appoggio in pendenza precaricato iniziativa ente

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenza.voci[0].codEntrata = null
* set pendenza.voci[0].ibanAccredito = ibanAccredito
* set pendenza.voci[0].ibanAppoggio = ibanAccreditoPostale
* set pendenza.voci[0].tipoContabilita = "ALTRO"
* set pendenza.voci[0].codiceContabilita = "CodiceContabilita"

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')

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
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale


Scenario: Iban appoggio in pendenza non precaricato iniziativa ente

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.voci[0].codEntrata = null
* set pendenzaPut.voci[0].ibanAccredito = ibanAccredito
* set pendenzaPut.voci[0].ibanAppoggio = ibanAccreditoPostale
* set pendenzaPut.voci[0].tipoContabilita = "ALTRO"
* set pendenzaPut.voci[0].codiceContabilita = "CodiceContabilita"
* call read('classpath:utils/pa-prepara-avviso.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )


* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')

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
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale

Scenario: Iban appoggio in pendenza spontaneo iniziativa ente

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo.json')
* set pagamentoPost.pendenze[0].voci[1].ibanAppoggio = ibanAccreditoPostale

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
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale

Scenario: Iban appoggio in tributo precaricato iniziativa psp

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.esitoVerificaRPT.datiPagamentoPA.ibanAccredito == ibanAccredito
* match response.esitoVerificaRPT.datiPagamentoPA.bicAccredito == bicAccredito

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati.ibanAccredito == ibanAccredito
* match response.dati.bicAccredito == bicAccredito

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale


Scenario: Iban appoggio in tributo non precaricato iniziativa psp

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.esitoVerificaRPT.datiPagamentoPA.ibanAccredito == ibanAccredito
* match response.esitoVerificaRPT.datiPagamentoPA.bicAccredito == bicAccredito

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati.ibanAccredito == ibanAccredito
* match response.dati.bicAccredito == bicAccredito

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale

Scenario: Iban appoggio in pendenza precaricato iniziativa psp

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenza.voci[0].codEntrata = null
* set pendenza.voci[0].ibanAccredito = ibanAccredito
* set pendenza.voci[0].ibanAppoggio = ibanAccreditoPostale
* set pendenza.voci[0].tipoContabilita = "ALTRO"
* set pendenza.voci[0].codiceContabilita = "CodiceContabilita"

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.esitoVerificaRPT.datiPagamentoPA.ibanAccredito == ibanAccredito
* match response.esitoVerificaRPT.datiPagamentoPA.bicAccredito == bicAccredito

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati.ibanAccredito == ibanAccredito
* match response.dati.bicAccredito == bicAccredito

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale

Scenario: Iban appoggio in pendenza non precaricato iniziativa psp

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.voci[0].codEntrata = null
* set pendenzaPut.voci[0].ibanAccredito = ibanAccredito
* set pendenzaPut.voci[0].ibanAppoggio = ibanAccreditoPostale
* set pendenzaPut.voci[0].tipoContabilita = "ALTRO"
* set pendenzaPut.voci[0].codiceContabilita = "CodiceContabilita"
* call read('classpath:utils/pa-prepara-avviso.feature')

* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.esitoVerificaRPT.datiPagamentoPA.ibanAccredito == ibanAccredito
* match response.esitoVerificaRPT.datiPagamentoPA.bicAccredito == bicAccredito

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati.ibanAccredito == ibanAccredito
* match response.dati.bicAccredito == bicAccredito

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAccredito == ibanAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAccredito == bicAccredito
* match response.rpt.datiVersamento.datiSingoloVersamento[0].ibanAppoggio == ibanAccreditoPostale
* match response.rpt.datiVersamento.datiSingoloVersamento[0].bicAppoggio == bicAccreditoPostale