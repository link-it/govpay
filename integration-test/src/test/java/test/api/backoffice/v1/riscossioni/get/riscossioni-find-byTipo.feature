Feature: Ricerca riscossioni filtrate per tipo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

* def pathServizio = '/riscossioni'

* def esitoVerifyPayment = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')

Scenario: Filtro su divisione e direzione

* def dataStart = getDateTime()

# Pendenza 1

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
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

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

# Pendenza 2

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
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

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

# Pendenza 3

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
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

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

# Pendenza 4

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
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

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')

* def dataEnd = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri.

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'MBT'
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
And match response.risultati[0].tipo == 'MBT'
And match response.risultati[1].tipo == 'MBT'
And match response.risultati[2].tipo == 'MBT'
And match response.risultati[3].tipo == 'MBT'

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ENTRATA'
And headers gpAdminBasicAutenticationHeader
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

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ENTRATA','MBT'
And headers gpAdminBasicAutenticationHeader
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

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ENTRATA','MBT','ALTRO_INTERMEDIARIO'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path pathServizio
And param dataDa = dataStart 
And param dataA = dataEnd
And param tipo = 'ALTRO_INTERMEDIARIO'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200

Scenario: Controllo di sintassi sul valore del filtro per tipo

Given url backofficeBaseurl
And path pathServizio
And param tipo = 'TIPO_NON_VALIDO' 
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'TIPO_NON_VALIDO'






