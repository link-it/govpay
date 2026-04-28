Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

* def esitoVerifyPayment = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')

Scenario: Filtro su divisione e direzione

* def dataStart = getDateTime()


# Pendenza 1

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* set pendenzaPut.divisione = 'div1'
* set pendenzaPut.direzione = 'dir1'

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
* set pendenzaPut.divisione = 'div2'
* set pendenzaPut.direzione = 'dir2'

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
* set pendenzaPut.divisione = 'div1'

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
* set pendenzaPut.direzione = 'dir2'

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
