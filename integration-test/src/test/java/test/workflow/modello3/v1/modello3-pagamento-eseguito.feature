Feature: Pagamento eseguito ad iniziativa PSP

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* def esitoAttivaRPT = read('msg/attiva-response-ok.json')
* def esitoVerificaRPT = read('msg/verifica-response-ok.json')
* configure followRedirects = false

Scenario: Pagamento eseguito dovuto precaricato senza verifica

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('msg/notifica-terminazione-eseguito.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

Scenario: Pagamento eseguito dovuto precaricato con verifica

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.esitoVerificaRPT == esitoVerificaRPT

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('msg/notifica-terminazione-eseguito.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

Scenario: Pagamento eseguito dovuto non precaricato senza verifica

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('msg/notifica-terminazione-eseguito.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

Scenario: Pagamento eseguito dovuto non precaricato con verifica

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.esitoVerificaRPT == esitoVerificaRPT

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('msg/notifica-terminazione-eseguito.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'
