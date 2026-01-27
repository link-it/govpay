Feature: Ricerca richieste di pagamento pendenza filtrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* call read('classpath:utils/nodo-svuota-coda-rpt.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Scenario: Filtro su data

* def dataRptStart = getDateTime()

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso1 = response.numeroAvviso
* def iuv1 = getIuvFromNumeroAvviso(numeroAvviso1)
* def ccp1 = getCurrentTimeMillis()
* def importo1 = pendenzaPut.importo
* def dataRptEnd1 = getDateTime()

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso2 = response.numeroAvviso
* def iuv2 = getIuvFromNumeroAvviso(numeroAvviso2)
* def ccp2 = getCurrentTimeMillis()
* def importo2 = pendenzaPut.importo
* def dataRptEnd2 = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri.

Given url pendenzeBaseurl
And path '/rpp'
And param dataRptDa = dataRptStart
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

# Attivo i pagamenti tramite il simulatore

Given url ndpsym_psp_url
And path 'attiva'
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso1
And param ccp = ccp1
And param importo = importo1
And param tipoRicevuta = 'R01'
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = '0'
When method get
Then assert responseStatus == 200

* def numeroAvviso = numeroAvviso1
* def iuv = iuv1
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def dataRtEnd1 = getDateTime()

Given url ndpsym_psp_url
And path 'attiva'
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso2
And param ccp = ccp2
And param importo = importo2
And param tipoRicevuta = 'R00'
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = '0'
When method get
Then assert responseStatus == 200

* def numeroAvviso = numeroAvviso2
* def iuv = iuv2
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def dataRtEnd2 = getDateTime()

# Fine verifiche. Verifico i filtri

Given url pendenzeBaseurl
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

Given url pendenzeBaseurl
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

Given url pendenzeBaseurl
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

Given url pendenzeBaseurl
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

Given url pendenzeBaseurl
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

Given url pendenzeBaseurl
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
