Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )


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

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

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

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

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

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

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

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def dataRptEnd1 = getDateTime()

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

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
