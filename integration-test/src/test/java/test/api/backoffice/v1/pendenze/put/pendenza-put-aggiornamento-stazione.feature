Feature: Aggiornamento avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def versionePagamento = 2

Scenario: Pagamento di una pendenza Modello 3 per un Ente Creditore che ha cambiato Stazione, AuxDigit = 0 

* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.auxDigit = '0'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo


* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

# abbino al dominio la stazione S2

* def stazione = read('classpath:configurazione/v1/msg/stazione.json')
* def idStazione_2 = '11111111113_02'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione_2 
And headers gpAdminBasicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set dominio.stazione = idStazione_2

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')


# Configuro il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_02',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')


# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')


# Ripristino il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')




Scenario: Pagamento di una pendenza Modello 1 per un Ente Creditore che ha cambiato Stazione, AuxDigit = 0 

* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.auxDigit = '0'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

* set dominio.stazione = idStazione_2

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configuro il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_02',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')


* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
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

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers basicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{
	"identificativoUnivocoVersante": {
		"tipoIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.tipo)",
		"codiceIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.identificativo)"
	},
	"anagraficaVersante":"#(pagamentoPost.soggettoVersante.anagrafica)",
	"indirizzoVersante":"#(pagamentoPost.soggettoVersante.indirizzo)",
	"civicoVersante":"#(pagamentoPost.soggettoVersante.civico)",
	"capVersante":"#(pagamentoPost.soggettoVersante.cap + '')",
	"localitaVersante":"#(pagamentoPost.soggettoVersante.localita)",
	"provinciaVersante":"#(pagamentoPost.soggettoVersante.provincia)",
	"nazioneVersante":"#(pagamentoPost.soggettoVersante.nazione)",
	"e-mailVersante":"#(pagamentoPost.soggettoVersante.email)"
}
"""

# Ripristino il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')
	
Scenario: Pagamento di una pendenza Modello 3 per un Ente Creditore che ha cambiato Stazione, AuxDigit = 3

* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.auxDigit = '3'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo


* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

# abbino al dominio la stazione S2

* def stazione = read('classpath:configurazione/v1/msg/stazione.json')
* def idStazione_2 = '11111111113_02'

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione_2 
And headers gpAdminBasicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set dominio.stazione = idStazione_2

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')


# Configuro il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '3',
 versione : '1',
 segregationCode : '00',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_02',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')


# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')


# Ripristino il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')




Scenario: Pagamento di una pendenza Modello 1 per un Ente Creditore che ha cambiato Stazione, AuxDigit = 3 

* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.auxDigit = '3'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

* set dominio.stazione = idStazione_2

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Configuro il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '3',
 versione : '1',
 segregationCode : '00',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_02',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')


* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
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

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers basicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{
	"identificativoUnivocoVersante": {
		"tipoIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.tipo)",
		"codiceIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.identificativo)"
	},
	"anagraficaVersante":"#(pagamentoPost.soggettoVersante.anagrafica)",
	"indirizzoVersante":"#(pagamentoPost.soggettoVersante.indirizzo)",
	"civicoVersante":"#(pagamentoPost.soggettoVersante.civico)",
	"capVersante":"#(pagamentoPost.soggettoVersante.cap + '')",
	"localitaVersante":"#(pagamentoPost.soggettoVersante.localita)",
	"provinciaVersante":"#(pagamentoPost.soggettoVersante.provincia)",
	"nazioneVersante":"#(pagamentoPost.soggettoVersante.nazione)",
	"e-mailVersante":"#(pagamentoPost.soggettoVersante.email)"
}
"""

# Ripristino il simulatore per utilizzare la seconda stazione

* def dominioNdpSymPut =
"""
{
 urlEC : 'http://localhost:8080/govpay/frontend/web/connector/ecsp/psp',
 auxDigit : '0',
 versione : '1',
 segregationCode : null,
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')



