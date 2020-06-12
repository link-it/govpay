Feature: Attivazione RPT con importo errato

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def esitoAttivaRPT = read('msg/attiva-response-ok.json')
* def esitoVerificaRPT = read('msg/verifica-response-ok.json')
* configure followRedirects = false

Scenario: Attivazione RPT con importo errato dovuto precaricato

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo + 10

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

Scenario: Attivazione RPT con importo errato dovuto non precaricato

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo + 10

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT