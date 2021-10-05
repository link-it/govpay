Feature: Pagamento eseguito ad iniziativa PSP

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
# * def esitoAttivaRPT = read('classpath:test/workflow/modello3/v1/msg/attiva-response-ok.json')
# * def esitoVerificaRPT = read('classpath:test/workflow/modello3/v1/msg/verifica-response-ok.json')
* configure followRedirects = false


* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')
* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')


Scenario: Pagamento eseguito dovuto precaricato con verifica

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

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 2

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
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')

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

Scenario: Pagamento eseguito dovuto precaricato con verifica, dopo un pagamento non eseguito

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
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

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 2

* call read('classpath:utils/nodo-config-dominio-put.feature')

* call read('classpath:utils/nodo-config-stazione-put.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivo il pagamento 

# NON_ESEGUITO_SANP_24("R22")

* def tipoRicevuta = "R22"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'NON_ESEGUITA'
* match response.voci[0].stato == 'Non eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RPT_ACCETTATA_NODO'

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
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* def ccp = 'n_a'
* call read('classpath:utils/pa-notifica-terminazione.feature')

* def ccp =  ccp_numero_avviso
* match response == read('classpath:test/workflow/modello3/v2/msg/notifica-terminazione-eseguito.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[2]'
* match response.rpp[0].stato == 'RPT_ANNULLATA'
* match response.rpp[0].rt == '#notpresent'
* match response.rpp[1].stato == 'RT_ACCETTATA_PA'
* match response.rpp[1].rt == '#notnull'


# ripristino dominio e stazione

* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* call read('classpath:utils/nodo-config-dominio-put.feature')

* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')

* call read('classpath:utils/nodo-config-stazione-put.feature')






