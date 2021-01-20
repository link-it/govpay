Feature: Ricerca richieste di pagamento pendenza filtrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* call read('classpath:utils/nodo-svuota-coda-rpt.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario: Filtro su data

* def dataRptStart = getDateTime()

* def idPendenza = getCurrentTimeMillis()

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt1 = response 
* def dataRptEnd1 = getDateTime()

* def idPendenza = getCurrentTimeMillis()

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt2 = response 
* def dataRptEnd2 = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri.

Given url pagamentiBaseurl
And path '/rpp'
And param dataRptDa = dataRptStart 
And headers basicAutenticationHeader
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

Given url pagamentiBaseurl
And path '/rpp'
And param dataRptDa = dataRptStart 
And param dataRptA = dataRptEnd1
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

Given url pagamentiBaseurl
And path '/rpp'
And param dataRptDa = dataRptStart 
And param dataRptA = dataRptEnd2
And headers basicAutenticationHeader
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

Given url pagamentiBaseurl
And path '/rpp'
And param dataRtDa = dataRptStart 
And headers basicAutenticationHeader
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

# Fine verifiche. Completo i pagamenti

 
* def idSession = responseRpt1.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def dataRtEnd1 = getDateTime()

* def idSession = responseRpt2.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R00'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def dataRtEnd2 = getDateTime()

# Fine verifiche. Verifico i filtri

Given url pagamentiBaseurl
And path '/rpp'
And param dataRptDa = dataRptStart 
And headers basicAutenticationHeader
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

Given url pagamentiBaseurl
And path '/rpp'
And param dataRptDa = dataRptStart 
And param dataRptA = dataRptEnd1
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

Given url pagamentiBaseurl
And path '/rpp'
And param dataRptDa = dataRptStart 
And param dataRptA = dataRptEnd2
And headers basicAutenticationHeader
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

Given url pagamentiBaseurl
And path '/rpp'
And param dataRtDa = dataRptStart 
And headers basicAutenticationHeader
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

Given url pagamentiBaseurl
And path '/rpp'
And param dataRtDa = dataRptStart 
And param dataRtA = dataRtEnd1 
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

Given url pagamentiBaseurl
And path '/rpp'
And param dataRtDa = dataRptStart 
And param dataRtA = dataRtEnd2 
And headers basicAutenticationHeader
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
