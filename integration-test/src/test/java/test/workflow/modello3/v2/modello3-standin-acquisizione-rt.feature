Feature: Acquisizione di una RT non attivata o verificata

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* configure followRedirects = false

* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')

* def ndpsym_standin_url = ndpsym_url + '/pagopa/rs/standin'

Scenario: Acquisizione di una ricevuta di pagamento tramite stand in

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

#			@QueryParam(value = "codPsp") String idPsp, opt
#			@QueryParam(value = "numeroAvviso") String numeroAvviso, obb
# 		@QueryParam(value = "ccp") String ccp, obb
#			@QueryParam(value = "idDominio") String idDominio, obb
#			@QueryParam(value = "importo") String importo, obb 
#			@QueryParam(value = "tipoRicevuta") String tipoRicevuta, obb
#			@QueryParam(value = "ibanAccredito") String ibanAccredito, obb
#			@QueryParam(value = "riversamentoCumulativo")@DefaultValue(value = "true") boolean riversamentoCumulativo, opt 
#			@QueryParam(value = "versione") @DefaultValue(value = "2")  Integer versione obb

# * def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def ccp = getCurrentTimeMillis()
# * def importo = 100.99
* def tipoRicevuta = "R23"
* def ibanAccreditoRT = ibanAccredito
* def versionePagamento = 2

# * def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

Given url ndpsym_standin_url 
And path 'generaRicevuta' 
And param idDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccreditoRT
And param versione = versionePagamento
When method get
Then assert responseStatus == 200
* def responseStandIn = response

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

#* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito-standin.json')

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

# Il simulatore non manda la ricevuta. Genero FR

* call sleep(1000)
* def dataInizioFR = getDateTime()
* call sleep(1000)

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def idflusso_dom1_1 = response.response.rendicontazioni[0].identificativoFlusso

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataFineFR = getDateTime()
* call sleep(1000)

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers gpAdminBasicAutenticationHeader
And param dataDa = dataInizioFR
And param dataA = dataFineFR
And param idFlusso = idflusso_dom1_1
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

* def idFlusso = response.risultati[0].idFlusso
* def dataFlusso = response.risultati[0].dataFlusso

Given url backofficeBaseurl
And path 'flussiRendicontazione', idFlusso, dataFlusso 
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso
And match response.rendicontazioni[0].esito == 4
And match response.rendicontazioni[0].iuv == iuvDet


Scenario: Pagamento eseguito dovuto precaricato con verifica, RT con importo diverso

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def versionePagamento = 2

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

# Configurazione dell'applicazione

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 


* def tipoRicevuta = "R01"
* def ibanAccreditoRT = ibanAccredito
* def importoRicevuta = 200.99

Given url ndpsym_standin_url 
And path 'generaRicevutaImportoDifferente' 
And param idDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccreditoRT
And param versione = versionePagamento
And param importoRicevuta = importoRicevuta
When method get
Then assert responseStatus == 200
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito-rt-importo-modificato.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ANOMALA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
# * match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

* def idDominioDet = response.rpp[0].rt.fiscalCode
* def iuvDet = response.rpp[0].rpt.creditorReferenceId
* def ccpDet = response.rpp[0].rt.receiptId

# Il simulatore non manda la ricevuta. Genero FR

* call sleep(1000)
* def dataInizioFR = getDateTime()
* call sleep(1000)

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')

* def idflusso_dom1_1 = response.response.rendicontazioni[0].identificativoFlusso

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataFineFR = getDateTime()
* call sleep(1000)

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers gpAdminBasicAutenticationHeader
And param dataDa = dataInizioFR
And param dataA = dataFineFR
And param idFlusso = idflusso_dom1_1
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

* def idFlusso = response.risultati[0].idFlusso
* def dataFlusso = response.risultati[0].dataFlusso

Given url backofficeBaseurl
And path 'flussiRendicontazione', idFlusso, dataFlusso 
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idFlusso 
And match response.dataFlusso == dataFlusso
And match response.rendicontazioni[0].esito == 0
And match response.rendicontazioni[0].iuv == iuvDet
