Feature: Ricerca riscossioni filtrate per tipo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
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

# Ho avviato due pagamenti. Verifico i filtri.

Given url ragioneriaBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'MBT'
And headers basicAutenticationHeader
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
And match response.risultati[0].tipo == 'MBT'
And match response.risultati[1].tipo == 'MBT'
And match response.risultati[2].tipo == 'MBT'
And match response.risultati[3].tipo == 'MBT'

Given url ragioneriaBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ENTRATA'
And headers basicAutenticationHeader
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
And match response.risultati[0].tipo == 'ENTRATA'
And match response.risultati[1].tipo == 'ENTRATA'
And match response.risultati[2].tipo == 'ENTRATA'
And match response.risultati[3].tipo == 'ENTRATA'

Given url ragioneriaBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ENTRATA','MBT'
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 8,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[8]'
}
"""
And match response.risultati[0].tipo == 'ENTRATA'
And match response.risultati[1].tipo == 'MBT'
And match response.risultati[2].tipo == 'ENTRATA'
And match response.risultati[3].tipo == 'MBT'
And match response.risultati[4].tipo == 'ENTRATA'
And match response.risultati[5].tipo == 'MBT'
And match response.risultati[6].tipo == 'ENTRATA'
And match response.risultati[7].tipo == 'MBT'


Given url ragioneriaBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ENTRATA','MBT','ALTRO_INTERMEDIARIO'
And headers basicAutenticationHeader
When method get
Then status 200

Given url ragioneriaBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ALTRO_INTERMEDIARIO'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Controllo di sintassi sul valore del filtro per tipo

Given url ragioneriaBaseurl
And path pathServizio
And param tipo = 'TIPO_NON_VALIDO' 
And headers basicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'TIPO_NON_VALIDO'






