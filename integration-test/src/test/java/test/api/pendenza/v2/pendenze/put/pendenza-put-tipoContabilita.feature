Feature: Caricamento pendenza con tutti i valori possibili di tipoContabilita

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
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
