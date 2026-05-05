Feature: Ricerca riscossioni filtrate per IUR

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

* def pathServizio = '/riscossioni'

Scenario: Filtro per IUR

* def dataStart = getDateTime()

# Pendenza 1

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = 'true'
* def idSession = idCart

* call read('classpath:utils/psp-paGetPaymentV2.feature')

# Verifico la notifica di attivazione

* call read('classpath:utils/pa-notifica-attivazione.feature')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

* def iur1 = response.rt.receiptId

# Pendenza 2

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = 'true'
* def idSession = idCart

* call read('classpath:utils/psp-paGetPaymentV2.feature')

# Verifico la notifica di attivazione

* call read('classpath:utils/pa-notifica-attivazione.feature')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

# Pendenza 3

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = 'true'
* def idSession = idCart

* call read('classpath:utils/psp-paGetPaymentV2.feature')

# Verifico la notifica di attivazione

* call read('classpath:utils/pa-notifica-attivazione.feature')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

# Pendenza 4

* call read('classpath:utils/pa-notifica-terminazione.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = 'true'
* def idSession = idCart

* call read('classpath:utils/psp-paGetPaymentV2.feature')

# Verifico la notifica di attivazione

* call read('classpath:utils/pa-notifica-attivazione.feature')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

* call sleep(3000)

* def dataEnd = getDateTime()

* def iur4 = response.rt.receiptId

# Ho avviato due pagamenti. Verifico i filtri.

Given url ragioneriaBaseurl
And path pathServizio
And param dataDa = dataStart
And param dataA = dataEnd
And param iur = iur1
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
And match response.risultati[0].iur == iur1

Given url ragioneriaBaseurl
And path pathServizio
And param dataDa = dataStart
And param dataA = dataEnd
And param iur = 'XXX'
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
