Feature: Riconciliazione pagamento tari-tefa

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

# configurazione del secondo ente come non intermediato e censimento iban

* def dominioNonIntermediato = read('classpath:configurazione/v1/msg/dominio.json')

* set dominioNonIntermediato.ragioneSociale = ragioneSocialeDominio_2 + ' N.I.'
* set dominioNonIntermediato.intermediato = false
* set dominioNonIntermediato.gln = null
* set dominioNonIntermediato.cbill = null
* set dominioNonIntermediato.iuvPrefix = null
* set dominioNonIntermediato.stazione = null
* set dominioNonIntermediato.auxDigit = null
* set dominioNonIntermediato.segregationCode = null
* set dominioNonIntermediato.autStampaPosteItaliane = null

Given url backofficeBaseurl
And path 'domini', idDominio_2 
And headers basicAutenticationHeader
And request dominioNonIntermediato
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def ibanAccreditoEnteNonIntermediato = 'IT08L1234512345123456789012'
* def ibanAccreditoEnteNonIntermediatoDescrizione = 'IBAN Accredito N.I.'
* def ibanAccreditoEnteNonIntermediatoPostale = 'IT08L0760112345123456789012'
* def ibanAccreditoEnteNonIntermediatoPostaleDescrizione = 'IBAN Accredito N.I. Postale'

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'contiAccredito', ibanAccreditoEnteNonIntermediato
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoEnteNonIntermediatoDescrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'contiAccredito', ibanAccreditoEnteNonIntermediatoPostale
And headers basicAutenticationHeader
And request {postale:true,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoEnteNonIntermediatoPostaleDescrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

# censimento entrata TEFA per il dominio non intermediato

* def codEntrataTefa = 'TEFA'

* def entrataTefa = 
"""
{
  "descrizione": "Tributo per l'esercizio delle funzioni ambientali",
  "tipoContabilita": "ALTRO",
  "codiceContabilita": "CodiceContabilita"
}
"""

* def entrataTefaDominio = 
"""
{
  "ibanAccredito": "#(ibanAccreditoEnteNonIntermediato)",
  "ibanAppoggio": "#(ibanAccreditoEnteNonIntermediatoPostale)",
  "tipoContabilita": "ALTRO",
  "codiceContabilita": "CodiceContabilita",
  "abilitato": true
}
"""

Given url backofficeBaseurl
And path 'entrate', codEntrataTefa
And headers basicAutenticationHeader
And request entrataTefa
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'entrate', codEntrataTefa
And headers basicAutenticationHeader
And request entrataTefaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* callonce sleep(10000)

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* configure followRedirects = false
* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')

Scenario: Riconciliazione pendenza tari tefa

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

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def versionePagamento = 3

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-put_multibeneficiario.json')

# * call read('classpath:utils/pa-carica-avviso.feature')
Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def responsePut = response
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo
* def importo1 = pendenzaPut.voci[0].importo
* def importo2 = pendenzaPut.voci[1].importo

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get_multibeneficiario.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Non eseguito'

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
# * match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
# * match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
#	* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
# * match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')

* def dataRptEnd2 = getDateTime()

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

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get_multivoce.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'ESEGUITA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Eseguito'

* def iuv1 = iuvDet
* def iuv2 = iuvDet

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(10000)

Given url backofficeBaseurl
And path '/incassi', idDominio
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-singola-response.json')

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get_multivoce.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'INCASSATA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Eseguito'


