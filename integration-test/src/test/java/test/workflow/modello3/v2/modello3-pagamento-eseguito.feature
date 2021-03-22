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

Scenario: Pagamento eseguito dovuto precaricato con verifica

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Configurazione del simulatore

* set dominioNdpSymPut.versione = 1

* call read('classpath:utils/nodo-config-dominio-put.feature')

# Verifico il pagamento

# * call read('classpath:utils/psp-verifica-rpt.feature')
# * match response.esitoVerifica == esitoVerifyPayment




