Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})


Scenario: Filtro su divisione e direzione

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.pendenze[0].divisione = 'div1'
* set pagamentoPost.pendenze[0].direzione = 'dir1'

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request pagamentoPost
When method post
Then status 201

* def idSession = response.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.pendenze[0].divisione = 'div2'
* set pagamentoPost.pendenze[0].direzione = 'dir2'

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request pagamentoPost
When method post
Then status 201

* def idSession = response.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.pendenze[0].divisione = 'div1'

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request pagamentoPost
When method post
Then status 201

* def idSession = response.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
* set pagamentoPost.pendenze[0].direzione = 'dir2'

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request pagamentoPost
When method post
Then status 201

* def idSession = response.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def dataEnd = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri.

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param divisione = 'div1'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 4,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[4]'
}
"""

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param divisione = 'div2'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 2,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
"""

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param divisione = 'div3'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param direzione = 'dir1'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 2,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
"""

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param direzione = 'dir2'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 4,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[4]'
}
"""

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param direzione = 'dir3'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param divisione = 'div1'
And param direzione = 'dir1'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 2,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
"""

Given url backofficeBaseurl
And path '/riscossioni'
And param dataDa = dataStart 
And param dataA = dataEnd
And param divisione = 'div1'
And param direzione = 'dir2'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""