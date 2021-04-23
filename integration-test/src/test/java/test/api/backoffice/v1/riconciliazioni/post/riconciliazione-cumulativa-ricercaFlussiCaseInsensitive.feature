Feature: Riconciliazione pagamento cumulativo con ricercaFlussiCaseInsensitive

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Scenario: Riconciliazione cumulativa da applicazione

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = toUpperCase(response.response.rh[0].causale)

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url backofficeBaseurl
And path '/incassi', idDominio
And param idFlussoCaseInsensitive = true
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 201
And match response == read('msg/riconciliazione-cumulativa-list-response.json')

Scenario: Riconciliazione cumulativa da applicazione

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv1 = iuv
* def importo1 = importo

* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* def iuv2 = iuv
* def importo2 = importo

* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = toUpperCase(response.response.rh[0].causale)

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* def idFlusso = estraiIdFlussoDallaCausale(causale)

Given url backofficeBaseurl
And path '/incassi', idDominio
And param idFlussoCaseInsensitive = false
And headers idA2ABasicAutenticationHeader
And request { causale: '#(causale)', importo: '#(importo)', sct : 'SCT0123456789' }
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: '#notnull', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.codice == '021405'
And match response.descrizione == 'Flusso rendicontazione non trovato.'
And match response.dettaglio == 'L\'identificativo '+ idFlusso +' estratto dalla causale di incasso non identifica alcun flusso di rendicontazione'
