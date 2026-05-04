Feature: Caricamento pendenza con tutti i valori possibili di tipoContabilita

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )


Scenario Outline: Caricamento pendenza con tipoContabilita <tipoContabilita>

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.voci[0].tipoContabilita = '<tipoContabilita>'
* set pendenzaPut.voci[0].codiceContabilita = 'XXXXX'

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

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].tipoContabilita == '<tipoContabilita>'
* match response.voci[0].codiceContabilita == 'XXXXX'

Examples:
| tipoContabilita |
| CAPITOLO |
| SPECIALE |
| SIOPE |
| SRTP_ESCLUSA_RAVV_OPEROSO |
| SRTP_ESCLUSA_ALTRO_OPERATORE |
| SRTP_ESCLUSA |
| ALTRO |


Scenario: Caricamento pendenza con tipoContabilita SRTP_ESCLUSA_RAVV_OPEROSO e pagamento

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.voci[0].tipoContabilita = 'SRTP_ESCLUSA_RAVV_OPEROSO'
* set pendenzaPut.voci[0].codiceContabilita = 'XXXXX'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def importo = pendenzaPut.voci[0].importo
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-terminazione.feature')

# Verifica che la pendenza sia in stato ESEGUITA

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ESEGUITA'

# Verifica che la rpp contenga il valore tassonomico scambiato (prefisso 6 per SRTP_ESCLUSA_RAVV_OPEROSO)

* configure retry = { count: 10, interval: 2000 }

Given url pendenzeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO'
And param idPendenza = idPendenza
And headers idA2ABasicAutenticationHeader
And retry until response.numRisultati == 1
When method get
Then status 200
And match response.risultati[0].rpt == '#notnull'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rpt.transferList.transfer[0].transferCategory == '6/XXXXX'
And match response.risultati[0].rt.transferList.transfer[0].transferCategory == '6/XXXXX'

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# Dettaglio rpp

Given url pendenzeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.rpt.transferList.transfer[0].transferCategory == '6/XXXXX'
And match response.rt.transferList.transfer[0].transferCategory == '6/XXXXX'

Scenario: Caricamento pendenza con tipoContabilita SRTP_ESCLUSA_RAVV_OPEROSO definito nell entrata e pagamento

Given url backofficeBaseurl
And path 'entrate', codEntrataSegreteria
And headers gpAdminBasicAutenticationHeader
And request {  descrizione: 'Diritti e segreteria',  tipoContabilita: 'SRTP_ESCLUSA_RAVV_OPEROSO',  codiceContabilita: 'XXXXX' }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSegreteria
And headers gpAdminBasicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito)', ibanAppoggio: '#(ibanAccreditoPostale)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201


* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def importo = pendenzaPut.voci[0].importo
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-terminazione.feature')

# Verifica che la pendenza sia in stato ESEGUITA

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ESEGUITA'

# Verifica che la rpp contenga il valore tassonomico scambiato (prefisso 6 per SRTP_ESCLUSA_RAVV_OPEROSO)

* configure retry = { count: 10, interval: 2000 }

Given url pendenzeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO'
And param idPendenza = idPendenza
And headers idA2ABasicAutenticationHeader
And retry until response.numRisultati == 1
When method get
Then status 200
And match response.risultati[0].rpt == '#notnull'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rpt.transferList.transfer[0].transferCategory == '6/XXXXX'
And match response.risultati[0].rt.transferList.transfer[0].transferCategory == '6/XXXXX'

* def idDominioDet = response.risultati[0].rt.fiscalCode
* def iuvDet = response.risultati[0].rpt.creditorReferenceId
* def ccpDet = response.risultati[0].rt.receiptId

# Dettaglio rpp

Given url pendenzeBaseurl
And path '/rpp', idDominioDet, iuvDet, ccpDet
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.rpt.transferList.transfer[0].transferCategory == '6/XXXXX'
And match response.rt.transferList.transfer[0].transferCategory == '6/XXXXX'


Scenario: Validazione sintattica tipoContabilita con valore non ammesso

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.voci[0].tipoContabilita = 'SRTP-ESCLUSA-RAVV-OPEROSO'
* set pendenzaPut.voci[0].codiceContabilita = 'XXXXX'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains 'tipoContabilita'
