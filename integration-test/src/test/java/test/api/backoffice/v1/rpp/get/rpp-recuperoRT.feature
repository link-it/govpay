Feature: Recupero RT

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* configure followRedirects = false
* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')

# Configurazione Dominio 4 Il simulatore non invia la ricevuta per i pagamenti di questo EC ma devono essere recuperate

* callonce read('classpath:configurazione/v1/anagrafica_dominio4.feature')

# Configurazione intermediario al recuperoRT
* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-recuperoRT.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers gpAdminBasicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

@ModelloUnicoV2
Scenario: Ricevuta per una transazione SANP 2.4.0 V2

# Configurazione dell'applicazione

* print '[RecuperoRT-V2] Inizio configurazione applicazione'

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* print '[RecuperoRT-V2] Fine configurazione applicazione'

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idDominio = idDominio_4
* def versionePagamento = 3

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* print '[RecuperoRT-V2] Inizio caricamento avviso, idPendenza:', idPendenza
* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = pendenzaPut.importo
* print '[RecuperoRT-V2] Fine caricamento avviso, numeroAvviso:', numeroAvviso, 'iuv:', iuv

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* print '[RecuperoRT-V2] Inizio verifica pagamento (paVerifyPaymentNotice)'
* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
# * match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp
* print '[RecuperoRT-V2] Fine verifica pagamento, ccp:', ccp

# Attivo il pagamento

* print '[RecuperoRT-V2] Inizio attivazione pagamento (paGetPayment)'
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* print '[RecuperoRT-V2] Fine attivazione pagamento'
# * match response.dati == esitoGetPayment

# Verifico la notifica di attivazione

* print '[RecuperoRT-V2] Inizio verifica notifica di attivazione'
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* print '[RecuperoRT-V2] Fine verifica notifica di attivazione'
# * match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Il simulatore non manda la ricevuta. Genero FR

* print '[RecuperoRT-V2] Attesa prima di generare le rendicontazioni...'
* call sleep(60000)

* print '[RecuperoRT-V2] Inizio generazione rendicontazioni sul simulatore NdP'
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* print '[RecuperoRT-V2] Fine generazione rendicontazioni sul simulatore NdP'

* print '[RecuperoRT-V2] Attesa prima di acquisire le rendicontazioni...'
* call sleep(60000)

* print '[RecuperoRT-V2] Inizio acquisizione rendicontazioni'
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* print '[RecuperoRT-V2] Fine acquisizione rendicontazioni'

# Sleep per attendere acquisizione FR

* print '[RecuperoRT-V2] Attesa completamento acquisizione FR...'
* call sleep(60000)

# Esecuzione del batch di recupero RT

* print '[RecuperoRT-V2] Inizio batch recupero RT'
* call read('classpath:utils/govpay-op-recupero-rt.feature')
* print '[RecuperoRT-V2] Fine batch recupero RT'

# Sleep per attendere esecuzione batch

* print '[RecuperoRT-V2] Attesa completamento batch recupero RT...'
* call sleep(60000)

* def dataRptEnd2 = getDateTime()

* print '[RecuperoRT-V2] Inizio verifica RPP con retry'
* configure retry = { count: 25, interval: 20000 }

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO'
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
And retry until response.numRisultati == 1
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
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza)'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rpt.versioneOggetto == '#notpresent'
And match response.risultati[0].rt.versioneOggetto == '#notpresent'

* print '[RecuperoRT-V2] Fine verifica RPP'

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

* print '[RecuperoRT-V2] Inizio verifica dettaglio RPP'

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

* print '[RecuperoRT-V2] Fine verifica dettaglio RPP'

# gde

* print '[RecuperoRT-V2] Inizio verifica GDE'

Given url backofficeBaseurl
And path '/eventi'
And param idDminio = idDominio
And param iuv = iuv
And param tipoEvento = 'getOrganizationReceiptIuvIur'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#ignore",
	"ccp":"#ignore",
	"idA2A": "##null",
	"idPendenza": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getOrganizationReceiptIuvIur",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "##null",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"clusterId" : "#notnull",
	"transactionId" : "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == ndpsym_docker_base_url + '/pagopa/rs/bizEvents/organizations/' + idDominio_4 + '/receipts/' + ccpDet + '/paymentoptions/' + iuvDet

@ModelloUnico
Scenario: Ricevuta per una transazione SANP 2.4.0

# Configurazione dell'applicazione

* print '[RecuperoRT-MU] Inizio configurazione applicazione'

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* print '[RecuperoRT-MU] Fine configurazione applicazione'

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idDominio = idDominio_4
* def versionePagamento = 2

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* print '[RecuperoRT-MU] Inizio caricamento avviso, idPendenza:', idPendenza
* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = pendenzaPut.importo
* print '[RecuperoRT-MU] Fine caricamento avviso, numeroAvviso:', numeroAvviso, 'iuv:', iuv

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* print '[RecuperoRT-MU] Inizio verifica pagamento (paVerifyPaymentNotice)'
* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
# * match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp
* print '[RecuperoRT-MU] Fine verifica pagamento, ccp:', ccp

# Attivo il pagamento

* print '[RecuperoRT-MU] Inizio attivazione pagamento (paGetPayment)'
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* print '[RecuperoRT-MU] Fine attivazione pagamento'
# * match response.dati == esitoGetPayment

# Verifico la notifica di attivazione

* print '[RecuperoRT-MU] Inizio verifica notifica di attivazione'
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* print '[RecuperoRT-MU] Fine verifica notifica di attivazione'
# * match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Il simulatore non manda la ricevuta. Genero FR

* print '[RecuperoRT-MU] Attesa prima di generare le rendicontazioni...'
* call sleep(60000)

* print '[RecuperoRT-MU] Inizio generazione rendicontazioni sul simulatore NdP'
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* print '[RecuperoRT-MU] Fine generazione rendicontazioni sul simulatore NdP'

* print '[RecuperoRT-MU] Attesa prima di acquisire le rendicontazioni...'
* call sleep(60000)

* print '[RecuperoRT-MU] Inizio acquisizione rendicontazioni'
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* print '[RecuperoRT-MU] Fine acquisizione rendicontazioni'

# Sleep per attendere acquisizione FR

* print '[RecuperoRT-MU] Attesa completamento acquisizione FR...'
* call sleep(60000)

# Esecuzione del batch di recupero RT

* print '[RecuperoRT-MU] Inizio batch recupero RT'
* call read('classpath:utils/govpay-op-recupero-rt.feature')
* print '[RecuperoRT-MU] Fine batch recupero RT'

# Sleep per attendere esecuzione batch

* print '[RecuperoRT-MU] Attesa completamento batch recupero RT...'
* call sleep(60000)

* def dataRptEnd2 = getDateTime()

* print '[RecuperoRT-MU] Inizio verifica RPP con retry'
* configure retry = { count: 25, interval: 20000 }

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO'
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
And retry until response.numRisultati == 1
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
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza)'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rpt.versioneOggetto == '#notpresent'
And match response.risultati[0].rt.versioneOggetto == '#notpresent'

* print '[RecuperoRT-MU] Fine verifica RPP'

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

* print '[RecuperoRT-MU] Inizio verifica dettaglio RPP'

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

* print '[RecuperoRT-MU] Fine verifica dettaglio RPP'

# gde

* print '[RecuperoRT-MU] Inizio verifica GDE'

Given url backofficeBaseurl
And path '/eventi'
And param idDminio = idDominio
And param iuv = iuv
And param tipoEvento = 'getOrganizationReceiptIuvIur'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#ignore",
	"ccp":"#ignore",
	"idA2A": "##null",
	"idPendenza": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getOrganizationReceiptIuvIur",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "##null",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"clusterId" : "#notnull",
	"transactionId" : "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == ndpsym_docker_base_url + '/pagopa/rs/bizEvents/organizations/' + idDominio_4 + '/receipts/' + ccpDet + '/paymentoptions/' + iuvDet

@modello1
Scenario: Recupero RT per una transanzazione SANP 2.3.0

# Configurazione dell'applicazione

* print '[RecuperoRT-M1] Inizio configurazione applicazione'

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* print '[RecuperoRT-M1] Fine configurazione applicazione'

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idDominio = idDominio_4
* def versionePagamento = 2

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* print '[RecuperoRT-M1] Inizio caricamento avviso, idPendenza:', idPendenza
* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = pendenzaPut.importo
* print '[RecuperoRT-M1] Fine caricamento avviso, numeroAvviso:', numeroAvviso, 'iuv:', iuv

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

# pagamento modello 1

* print '[RecuperoRT-M1] Inizio pagamento modello 1'

* def pagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')

Given url pagamentoBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* configure followRedirects = false
* def idSession = response.idSession
* def tipoRicevuta = "R01"
* def cumulativo = "1"

* print '[RecuperoRT-M1] Esecuzione pagamento sul simulatore NdP, idSession:', idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = tipoRicevuta
And param riversamento = cumulativo
And headers basicAutenticationHeader
When method get
Then status 302

* print '[RecuperoRT-M1] Fine pagamento modello 1'

# Il simulatore non manda la ricevuta. Genero FR

* print '[RecuperoRT-M1] Attesa prima di generare le rendicontazioni...'
* call sleep(60000)

* print '[RecuperoRT-M1] Inizio generazione rendicontazioni sul simulatore NdP'
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* print '[RecuperoRT-M1] Fine generazione rendicontazioni sul simulatore NdP'

* print '[RecuperoRT-M1] Attesa prima di acquisire le rendicontazioni...'
* call sleep(60000)

* print '[RecuperoRT-M1] Inizio acquisizione rendicontazioni'
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* print '[RecuperoRT-M1] Fine acquisizione rendicontazioni'

# Sleep per attendere acquisizione FR

* print '[RecuperoRT-M1] Attesa completamento acquisizione FR...'
* call sleep(60000)

# Esecuzione del batch di recupero RT

* print '[RecuperoRT-M1] Inizio batch recupero RT'
* call read('classpath:utils/govpay-op-recupero-rt.feature')
* print '[RecuperoRT-M1] Fine batch recupero RT'

# Sleep per attendere esecuzione batch

* print '[RecuperoRT-M1] Attesa completamento batch recupero RT...'
* call sleep(60000)

* def dataRptEnd2 = getDateTime()
* print '[RecuperoRT-M1] Inizio verifica RPP con retry'

* configure retry = { count: 25, interval: 20000 }

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
And retry until response.numRisultati == 1
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
And match response.risultati[0].pendenza.idPendenza == '#(""+idPendenza)'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rpt.versioneOggetto == '6.2.0'
And match response.risultati[0].rt.versioneOggetto == '#notpresent'

* print '[RecuperoRT-M1] Fine verifica RPP'

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

* print '[RecuperoRT-M1] Inizio verifica dettaglio RPP'

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '6.2.0'
And match response.rt.versioneOggetto == '#notpresent'

* print '[RecuperoRT-M1] Fine verifica dettaglio RPP'

# gde

* print '[RecuperoRT-M1] Inizio verifica GDE'

Given url backofficeBaseurl
And path '/eventi'
And param idDminio = idDominio
And param iuv = iuv
And param tipoEvento = 'getOrganizationReceiptIuvIur'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#ignore",
	"ccp":"#ignore",
	"idA2A": "##null",
	"idPendenza": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getOrganizationReceiptIuvIur",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "##null",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "##null",
	"clusterId" : "#notnull",
	"transactionId" : "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == ndpsym_docker_base_url + '/pagopa/rs/bizEvents/organizations/' + idDominio_4 + '/receipts/' + ccpDet + '/paymentoptions/' + iuvDet

