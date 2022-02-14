Feature: Ricerca riscossioni filtrate per tipo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* def pathServizio = '/riscossioni'

Scenario: Filtro su divisione e direzione

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

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

* def iur1 = response.rt.datiPagamento.datiSingoloPagamento[0].identificativoUnivocoRiscossione

# Ho avviato due pagamenti. Verifico i filtri.

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param iur = iur1
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
And match response.risultati[0].iur == iur1
And match response.risultati[1].iur == iur1

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param iur = 'XXX'
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
