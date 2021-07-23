Feature: Lettura di pendenze multibeneficiario

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* configure followRedirects = false

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')
* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/api/backoffice/v1/pendenze/get/msg/getPayment-response-ok-multibeneficiario.json')

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

Scenario: Caricamento pendenza multibeneficiario 

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-put_multibeneficiario.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get_multibeneficiario.json')

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Non eseguito'

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 3

* call read('classpath:utils/nodo-config-dominio-put.feature')

* call read('classpath:utils/nodo-config-stazione-put.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/api/backoffice/v1/pendenze/get/msg/notifica-attivazione-multibeneficiario.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/api/backoffice/v1/pendenze/get/msg/notifica-terminazione-eseguito-multibeneficiario.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'


# ripristino dominio e stazione

* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* call read('classpath:utils/nodo-config-dominio-put.feature')

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')

* call read('classpath:utils/nodo-config-stazione-put.feature')


Scenario: Caricamento pendenza multibeneficiario di due enti che condividono l'IBAN

# configuro secondo dominio per utilizzare lo stesso iban del dominio principale

* def ibanAccreditoEnteNonIntermediato = ibanAccredito
* def ibanAccreditoEnteNonIntermediatoDescrizione = ibanAccreditoDescrizione
* def ibanAccreditoEnteNonIntermediatoPostale = ibanAccreditoPostale
* def ibanAccreditoEnteNonIntermediatoPostaleDescrizione = ibanAccreditoPostaleDescrizione

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

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')


* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-put_multibeneficiario.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-get_multibeneficiario.json')

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Non eseguito'

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def importo = pendenzaPut.importo

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 3

* call read('classpath:utils/nodo-config-dominio-put.feature')

* call read('classpath:utils/nodo-config-stazione-put.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/api/backoffice/v1/pendenze/get/msg/notifica-attivazione-multibeneficiario.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/api/backoffice/v1/pendenze/get/msg/notifica-terminazione-eseguito-multibeneficiario.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'


# ripristino dominio e stazione

* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* call read('classpath:utils/nodo-config-dominio-put.feature')

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')

* call read('classpath:utils/nodo-config-stazione-put.feature')



