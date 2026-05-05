Feature: Ricerca riscossioni filtrate per tipo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

* def pathServizio = '/riscossioni'

Scenario: Filtro per tipo

* def dataStart = getDateTime()

# Pendenza 1

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_bollo.json')

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

# Attivo il pagamento 
* call read('classpath:utils/psp-paGetPaymentV2.feature')

# Verifico la notifica di attivazione

* call read('classpath:utils/pa-notifica-attivazione.feature')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

* def iur1 = response.rt.receiptId

# Pendenza 2

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_bollo.json')

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
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_bollo.json')

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
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_bollo.json')

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
	numRisultati: 8,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[8]'
}
"""
And match response.risultati[0].tipo == 'ENTRATA'
And match response.risultati[1].tipo == 'ENTRATA'
And match response.risultati[2].tipo == 'ENTRATA'
And match response.risultati[3].tipo == 'ENTRATA'
And match response.risultati[4].tipo == 'ENTRATA'
And match response.risultati[5].tipo == 'ENTRATA'
And match response.risultati[6].tipo == 'ENTRATA'
And match response.risultati[7].tipo == 'ENTRATA'

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
	numRisultati: 12,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[12]'
}
"""
And match response.risultati[0].tipo == 'ENTRATA'
And match response.risultati[1].tipo == 'ENTRATA'
And match response.risultati[2].tipo == 'MBT'
And match response.risultati[3].tipo == 'ENTRATA'
And match response.risultati[4].tipo == 'ENTRATA'
And match response.risultati[5].tipo == 'MBT'
And match response.risultati[6].tipo == 'ENTRATA'
And match response.risultati[7].tipo == 'ENTRATA'
And match response.risultati[8].tipo == 'MBT'
And match response.risultati[9].tipo == 'ENTRATA'
And match response.risultati[10].tipo == 'ENTRATA'
And match response.risultati[11].tipo == 'MBT'

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
