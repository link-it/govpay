Feature: Pagamento eseguito ad iniziativa PSP

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def esitoAttivaRPT = read('classpath:test/workflow/modello3/v1/msg/attiva-response-ok.json')
* def esitoVerificaRPT = read('classpath:test/workflow/modello3/v1/msg/verifica-response-ok.json')
* configure followRedirects = false


* def stazioneNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/stazione.json')
* def dominioNdpSymPut = read('classpath:test/workflow/modello3/v2/msg/dominio.json')

* def esitoVerifyPayment = read('classpath:test/workflow/modello3/v2/msg/verifyPayment-response-ok.json')
* def esitoGetPayment = read('classpath:test/workflow/modello3/v2/msg/getPayment-response-ok.json')


* configure afterFeature = function(){ karate.call('classpath:utils/nodo-config-dominio-put.feature'); }

Scenario: Pagamento eseguito dovuto precaricato con verifica

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 3

* call read('classpath:utils/nodo-config-dominio-put.feature')

* set stazioneNdpSymPut.urlCCP = 'http://localhost:8080/govpay/frontend/api/pagopa/PaForNodeservice'
* set stazioneNdpSymPut.urlRT = 'http://localhost:8080/govpay/frontend/api/pagopa/PaForNodeservice'

* call read('classpath:utils/nodo-config-stazione-put.feature')

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response == esitoVerifyPayment

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoGetPayment

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v1/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('classpath:test/workflow/modello3/v1/msg/notifica-terminazione-eseguito.json')

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






