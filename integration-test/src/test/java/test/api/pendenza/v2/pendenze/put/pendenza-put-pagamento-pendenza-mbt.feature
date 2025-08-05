Feature: Pagamento di una pendenza con MBT con le varie versioni delle API SANP

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def esitoPaGetPaymentV2 = read('classpath:test/workflow/modello3/v2/msg/getPaymentV2-response-ok.json')

* def faultBean = 
"""
	{
		"faultCode":"PAA_SEMANTICA",
		"faultString":"Errore semantico.",
		"id":"#(idDominio)",
		"description":'#("Il versamento contiene una marca da bollo telematica, non ammessa per pagamenti ad iniziativa psp.")',
		"serial":'##null'
	}
"""

* def faultBean2 = 
"""
	{
		"faultCode":"PAA_SEMANTICA",
		"faultString":"Errore semantico.",
		"id":"#(idDominio)",
		"description":'#("Il versamento contiene una marca da bollo telematica, non ammessa per pagamenti ad iniziativa psp.")',
		"serial":'##null',
		"originalFaultCode":'##null',
		"originalFaultString":'##null',
		"originalDescription":'##null'
	}
"""

@test1
Scenario: Attivazione di una RPT per una pendenza con MBT Sanp 2.3.0

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean

@test2
Scenario: Attivazione di una RPT per una pendenza con MBT Sanp 2.4.0

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def versionePagamento = 2
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.faultBean == faultBean2


@test3
Scenario: Attivazione di una RPT per una pendenza con MBT Sanp 3.2.1

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

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def versionePagamento = 3
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoPaGetPaymentV2

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
#* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

#* def ccp =  ccp_numero_avviso
#* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

# check hash

* def pendenzaHash = pendenzaPut.voci[0].hashDocumento
* def attachmentBase64 = response.rpp[0].rt.transferList.transfer[0].MBDAttachment

# Decodifica base64
* def decodedXml = decodeBase64(attachmentBase64)

# Log XML decodificato (per debug)
* print 'XML Decodificato:', decodedXml

* match decodedXml contains '<ns2:DigestValue>' + pendenzaHash + '</ns2:DigestValue>'

