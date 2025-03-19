Feature: Pagamento Avviso precaricato per un dominio a cui viene modificata la stazione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idStazione2 = idIntermediario + '_02'

Scenario: Pagamento modello 3 avviso precaricato per un dominio a cui viene modificata la stazione

* def gpBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And headers basicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status 200
And match response == 
"""
{ 
	stato: 'NON_ESEGUITA',
	importo: 100.99,
	idDominio: '#(idDominio)',
	numeroAvviso: '#(numeroAvviso)',
	dataValidita: '2900-12-31',
	dataScadenza: '2999-12-31',
	descrizione: '#notnull',
	tassonomiaAvviso: 'Servizi erogati dal comune',
	qrcode: '#("PAGOPA|002|" + numeroAvviso + "|" + idDominio + "|10099")',
	barcode: '#notnull'
}
"""

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = response.importo

# aggiornamento della stazione collegata al dominio1

* def stazione = read('classpath:test/api/backoffice/v1/intermediari/put/msg/stazione.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione2
And headers gpBasicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.stazione = idStazione2

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Reset Cache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Pagamento Modello 3

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')


Scenario: Pagamento modello 1 avviso precaricato per un dominio a cui viene modificata la stazione

* def gpBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Reset Cache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And headers basicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status 200
And match response == 
"""
{ 
	stato: 'NON_ESEGUITA',
	importo: 100.99,
	idDominio: '#(idDominio)',
	numeroAvviso: '#(numeroAvviso)',
	dataValidita: '2900-12-31',
	dataScadenza: '2999-12-31',
	descrizione: '#notnull',
	tassonomiaAvviso: 'Servizi erogati dal comune',
	qrcode: '#("PAGOPA|002|" + numeroAvviso + "|" + idDominio + "|10099")',
	barcode: '#notnull'
}
"""

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = response.importo

# aggiornamento della stazione collegata al dominio1

* def stazione = read('classpath:test/api/backoffice/v1/intermediari/put/msg/stazione.json')


Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione2
And headers gpBasicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.stazione = idStazione2

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Reset Cache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Modifico il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '#(idStazione2)',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')

# Pagamento Modello 1

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "RSSMRA30A01H501I",
  "anagrafica": "Mario Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-1234567"
}
"""

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }


# Ripristino il simulatore per utilizzare la prima stazione

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')


Scenario: Pagamento modello 3 avviso precaricato per un dominio a cui viene modificata la stazione, l'aux digit e segregation code 

* def gpBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And headers basicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status 200
And match response == 
"""
{ 
	stato: 'NON_ESEGUITA',
	importo: 100.99,
	idDominio: '#(idDominio)',
	numeroAvviso: '#(numeroAvviso)',
	dataValidita: '2900-12-31',
	dataScadenza: '2999-12-31',
	descrizione: '#notnull',
	tassonomiaAvviso: 'Servizi erogati dal comune',
	qrcode: '#("PAGOPA|002|" + numeroAvviso + "|" + idDominio + "|10099")',
	barcode: '#notnull'
}
"""

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = response.importo

# aggiornamento della stazione collegata al dominio1

* def stazione = read('classpath:test/api/backoffice/v1/intermediari/put/msg/stazione.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione2
And headers gpBasicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.stazione = idStazione2
* set dominio.auxDigit = '3'
* set dominio.segregationCode = '11'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Reset Cache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Pagamento Modello 3

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')


Scenario: Pagamento modello 3 avviso non caricato per un dominio a cui viene modificata la stazione, l'aux digit e segregation code 

* def gpBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# carico avviso sui servizi mock ente

* def idPendenza = getCurrentTimeMillis()
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

# aggiornamento della stazione collegata al dominio1

* def stazione = read('classpath:test/api/backoffice/v1/intermediari/put/msg/stazione.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione2
And headers gpBasicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### aggiorno dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.stazione = idStazione2
* set dominio.auxDigit = '3'
* set dominio.segregationCode = '11'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Reset Cache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* configure cookies = null

Given url pagamentiBaseurl
And path '/avvisi', idDominio, numeroAvviso
And headers basicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status 200
And match response == 
"""
{ 
	stato: 'NON_ESEGUITA',
	importo: 100.99,
	idDominio: '#(idDominio)',
	numeroAvviso: '#(numeroAvviso)',
	dataValidita: '2900-12-31',
	dataScadenza: '2999-12-31',
	descrizione: '#notnull',
	tassonomiaAvviso: 'Servizi erogati dal comune',
	qrcode: '#("PAGOPA|002|" + numeroAvviso + "|" + idDominio + "|10099")',
	barcode: '#notnull'
}
"""

* def ccp = getCurrentTimeMillis()
* def importo = response.importo

# Pagamento Modello 3

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')



