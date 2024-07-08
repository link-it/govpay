Feature: Ricerca richieste di pagamento pendenza filtrate per retrocompatibilitaMessaggiPagoPAV1

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

Scenario: Filtro su retrocompatibilitaMessaggiPagoPAV1 per una RPT SANP 2.3.0, lettura del dettaglio di rpp, rpt e rt

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

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

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Given url ndpsym_psp_url 
And path 'attiva' 
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = riversamentoCumulativo
When method get
Then assert responseStatus == 200

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v1/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('classpath:test/workflow/modello3/v1/msg/notifica-terminazione-eseguito.json')

* def dataRptEnd2 = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri per la retrocompatibilita' valori: null, false e true
# retrocompatibilitaMessaggiPagoPAV1 = null

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
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
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.risultati[0].rpt.versioneOggetto == '6.2.0'
And match response.risultati[0].rt.versioneOggetto == '6.2.0'

* def idDominioDet = response.risultati[0].rpt.dominio.identificativoDominio
* def iuvDet = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpDet = response.risultati[0].rt.datiPagamento.CodiceContestoPagamento

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.rpt.versioneOggetto == '6.2.0'
And match response.rt.versioneOggetto == '6.2.0'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RPT/versioneOggetto == '6.2.0'

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RT/versioneOggetto == '6.2.0'

# retrocompatibilitaMessaggiPagoPAV1 = false

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
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
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.risultati[0].rpt.versioneOggetto == '6.2.0'
And match response.risultati[0].rt.versioneOggetto == '6.2.0'

* def idDominioDet = response.risultati[0].rpt.dominio.identificativoDominio
* def iuvDet = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpDet = response.risultati[0].rt.datiPagamento.CodiceContestoPagamento

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.rpt.versioneOggetto == '6.2.0'
And match response.rt.versioneOggetto == '6.2.0'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RPT/versioneOggetto == '6.2.0'

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RT/versioneOggetto == '6.2.0'

# retrocompatibilitaMessaggiPagoPAV1 = true

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
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
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.risultati[0].rpt.versioneOggetto == '6.2.0'
And match response.risultati[0].rt.versioneOggetto == '6.2.0'

* def idDominioDet = response.risultati[0].rpt.dominio.identificativoDominio
* def iuvDet = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpDet = response.risultati[0].rt.datiPagamento.CodiceContestoPagamento

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.rpt.versioneOggetto == '6.2.0'
And match response.rt.versioneOggetto == '6.2.0'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RPT/versioneOggetto == '6.2.0'

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RT/versioneOggetto == '6.2.0'

Scenario: Filtro su retrocompatibilitaMessaggiPagoPAV1 per una RPT SANP 2.4.0

* def versionePagamento = 2

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

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

# Configurazione dell'applicazione

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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')

* def dataRptEnd2 = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri per la retrocompatibilita' valori: null, false e true
# retrocompatibilitaMessaggiPagoPAV1 = null

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
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

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paGetPaymentRes/data/creditorReferenceId == iuvDet

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paSendRTReq/receipt/receiptId == ccpDet

# retrocompatibilitaMessaggiPagoPAV1 = false

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
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

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paGetPaymentRes/data/creditorReferenceId == iuvDet

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paSendRTReq/receipt/receiptId == ccpDet


# retrocompatibilitaMessaggiPagoPAV1 = true

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
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
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.risultati[0].rpt.versioneOggetto == '6.2.0'
And match response.risultati[0].rt.versioneOggetto == '6.2.0'

* def idDominioDet = response.risultati[0].rpt.dominio.identificativoDominio
* def iuvDet = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpDet = response.risultati[0].rt.datiPagamento.CodiceContestoPagamento

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.rpt.versioneOggetto == '6.2.0'
And match response.rt.versioneOggetto == '6.2.0'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RPT/versioneOggetto == '6.2.0'

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RT/versioneOggetto == '6.2.0'


Scenario: Filtro su retrocompatibilitaMessaggiPagoPAV1 per una RPT SANP 2.4.0 entrata definita

* def versionePagamento = 2

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.importo = 100.99
* set pendenzaPut.voci[0].importo = 100.99

* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

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

# Configurazione dell'applicazione

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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione_entrata_definita.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito_entrata_definita.json')

* def dataRptEnd2 = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri per la retrocompatibilita' valori: null, false e true
# retrocompatibilitaMessaggiPagoPAV1 = null

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
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

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paGetPaymentRes/data/creditorReferenceId == iuvDet

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paSendRTReq/receipt/receiptId == ccpDet

# retrocompatibilitaMessaggiPagoPAV1 = false

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
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

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paGetPaymentRes/data/creditorReferenceId == iuvDet

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paSendRTReq/receipt/receiptId == ccpDet


# retrocompatibilitaMessaggiPagoPAV1 = true

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
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
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.risultati[0].rpt.versioneOggetto == '6.2.0'
And match response.risultati[0].rt.versioneOggetto == '6.2.0'

* def idDominioDet = response.risultati[0].rpt.dominio.identificativoDominio
* def iuvDet = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpDet = response.risultati[0].rt.datiPagamento.CodiceContestoPagamento

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.rpt.versioneOggetto == '6.2.0'
And match response.rt.versioneOggetto == '6.2.0'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RPT/versioneOggetto == '6.2.0'

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RT/versioneOggetto == '6.2.0'



Scenario: Filtro su retrocompatibilitaMessaggiPagoPAV1 per una RPT SANP 2.4.0 con MBT

* def versionePagamento = 3

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso
# * set pendenzaPut.importo = 125.99
# * set pendenzaPut.voci[0].importo = 100.99

* call read('classpath:utils/pa-carica-avviso.feature')
* def responsePut = response
# * def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

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

# Configurazione dell'applicazione

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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* def esitoGetPaymentV2 = read('classpath:test/workflow/modello3/v2/msg/getPaymentV2-bollo-response-ok.json')
* match response.dati == esitoGetPaymentV2

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione_bollo.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito_bollo.json')

* def dataRptEnd2 = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri per la retrocompatibilita' valori: null, false e true
# retrocompatibilitaMessaggiPagoPAV1 = null

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
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

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paGetPaymentV2Response/data/creditorReferenceId == iuvDet

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paSendRTV2Request/receipt/receiptId == ccpDet

# retrocompatibilitaMessaggiPagoPAV1 = false

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
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

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rpt.versioneOggetto == '#notpresent'
And match response.rt.versioneOggetto == '#notpresent'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '#notpresent'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paGetPaymentV2Response/data/creditorReferenceId == iuvDet

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = false
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /paSendRTV2Request/receipt/receiptId == ccpDet


# retrocompatibilitaMessaggiPagoPAV1 = true

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
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
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.risultati[0].rpt.versioneOggetto == '6.2.0'
And match response.risultati[0].rt.versioneOggetto == '6.2.0'

* def idDominioDet = response.risultati[0].rpt.dominio.identificativoDominio
* def iuvDet = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpDet = response.risultati[0].rt.datiPagamento.CodiceContestoPagamento

# dettaglio rpp

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.pendenza.idPendenza == '#(""+idPendenza)'
And match response.rt == '#notnull'
And match response.rt.datiPagamento.codiceEsitoPagamento == '0'
And match response.rpt.versioneOggetto == '6.2.0'
And match response.rt.versioneOggetto == '6.2.0'
And match response.rt.datiPagamento.datiSingoloPagamento[0].allegatoRicevuta == '#notnull'

# rpt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'

# rt formato json

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.versioneOggetto == '6.2.0'
And match response.datiPagamento.datiSingoloPagamento[0].allegatoRicevuta == '#notnull'

# rpt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rpt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RPT/versioneOggetto == '6.2.0'

# rt formato xml

Given url backofficeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet, 'rt'
And param retrocompatibilitaMessaggiPagoPAV1 = true
And header Accept = 'application/xml'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response /RT/versioneOggetto == '6.2.0'

