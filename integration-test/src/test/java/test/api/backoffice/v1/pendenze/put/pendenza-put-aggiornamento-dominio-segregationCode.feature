Feature: Aggiornamento avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Scenario: Pagamento di una pendenza modello 3 per un dominio che cambia il codice di segregazione con auxdigit = 0

* set dominio.segregationCode = '03'
* set dominio.auxDigit = '0'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '0',
 versione : '1',
 segregationCode : '03',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

* set dominio.segregationCode = '25'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '0',
 versione : '1',
 segregationCode : '01',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

# carico e pago una pendenza con segregation code 25

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

# Ripristino il simulatore per utilizzare la prima stazione

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '3',
 versione : '1',
 segregationCode : '00',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')

Scenario: Pagamento di una pendenza modello 3 per un dominio che cambia il codice di segregazione con auxdigit = 3

* set dominio.segregationCode = '03'
* set dominio.auxDigit = '3'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

* set dominio.segregationCode = '25'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '3',
 versione : '1',
 segregationCode : '03',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

# carico e pago una pendenza con segregation code 25

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '3',
 versione : '1',
 segregationCode : '25',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')


# Ripristino il simulatore per utilizzare la prima stazione

* def dominioNdpSymPut =
"""
{
 urlEC : '#(govpay_web_connector_url + "/ecsp/psp")',
 auxDigit : '3',
 versione : '1',
 segregationCode : '00',
 ragioneSociale : 'Ente Creditore Test',
 idStazione : '11111111113_01',
 idIntermediario : '11111111113' 
 }
"""

* call read('classpath:utils/nodo-config-dominio-put.feature')

