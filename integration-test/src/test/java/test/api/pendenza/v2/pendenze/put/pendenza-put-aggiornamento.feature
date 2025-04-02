Feature: Aggiornamento avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Scenario: Aggiornamento pendenza non pagata senza modifiche

* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

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
And match response == pendenzaGet

* def pendenzaGetResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200
And match response == pendenzaPutResponse

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGetResponse


Scenario: Aggiornamento pendenza non pagata con modifica permessa

* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

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
And match response == pendenzaGet

* def pendenzaGetResponse = response

* set pendenzaPut.importo = pendenzaPut.importo + 10
* set pendenzaPut.voci[0].importo = pendenzaPut.voci[0].importo + 10
* set pendenzaPut.causale = pendenzaPut.causale + ' con importo maggiorato' 
* set pendenzaPut.dataValidita = '2900-01-31'
* set pendenzaPut.dataScadenza = '2999-01-31'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200
And match response == pendenzaPutResponse

* set pendenzaGetResponse.importo = pendenzaPut.importo
* set pendenzaGetResponse.voci[0].importo = pendenzaPut.voci[0].importo
* set pendenzaGetResponse.causale = pendenzaPut.causale 
* set pendenzaGetResponse.dataValidita = pendenzaPut.dataValidita
* set pendenzaGetResponse.dataScadenza = pendenzaPut.dataScadenza

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGetResponse


Scenario: Aggiornamento pendenza non pagata eliminazione data scadenza

* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

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
And match response == pendenzaGet

* def pendenzaGetResponse = response

* set pendenzaPut.importo = pendenzaPut.importo + 10
* set pendenzaPut.voci[0].importo = pendenzaPut.voci[0].importo + 10
* set pendenzaPut.causale = pendenzaPut.causale + ' con importo maggiorato' 
* set pendenzaPut.dataValidita = null
* set pendenzaPut.dataScadenza = null

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200
And match response.dataValidita == '#notpresent'
And match response.dataScadenza == '#notpresent'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.dataValidita == '#notpresent'
And match response.dataScadenza == '#notpresent'


Scenario: Aggiornamento pendenza non pagata con modifica dell'iban di accredito

* set pendenzaPut.voci = 
"""
[
	{
		idVocePendenza: '1',
		importo: 100.99,
		descrizione: 'Diritti e segreteria',
		ibanAccredito: '#(ibanAccredito)',
		tipoContabilita: 'ALTRO',
		codiceContabilita: 'XXXXX'
	}
]
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* set pendenzaPut.voci[0].ibanAccredito = ibanAccreditoPostale 

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_023',
	descrizione: 'Richiesta non valida',
        dettaglio: '#("La pendenza (IdA2A:" + idA2A + ", Id:" + idPendenza + ") ha la voce (1) con un iban di accredito diverso dall\'originale.")'
}
"""

Scenario: Aggiornamento pendenza annullata 

* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

* def pendenzaGetResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request [ { "op": "REPLACE", "path": "/stato", "value": "ANNULLATA" }, { "op": "REPLACE", "path": "/descrizioneStato", "value": "Test annullamento" }]
When method patch
Then status 200

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200
And match response == pendenzaPutResponse

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGetResponse

Scenario: Aggiornamento pendenza non pagata con aggiunta di una voce

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce.json')

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

* def pendenzaPutUpdate = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_update.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutUpdate
When method put
Then status 200

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def pendenzaGetResponse = response

* set pendenzaGetResponse.importo = pendenzaPutUpdate.importo
* set pendenzaGetResponse.voci[0].importo = pendenzaPutUpdate.voci[0].importo
* set pendenzaGetResponse.voci[1].importo = pendenzaPutUpdate.voci[1].importo
* set pendenzaGetResponse.voci[2].importo = pendenzaPutUpdate.voci[2].importo
* set pendenzaGetResponse.causale = pendenzaPutUpdate.causale 
* set pendenzaGetResponse.dataValidita = pendenzaPutUpdate.dataValidita
* set pendenzaGetResponse.dataScadenza = pendenzaPutUpdate.dataScadenza

Scenario: Aggiornamento pendenza non pagata con rimozione di una voce

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_update.json')

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

* def pendenzaPutUpdate = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutUpdate
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_005',
	descrizione: 'Richiesta non valida',
        dettaglio: '#("La pendenza (IdA2A:" + idA2A + ", Id:" + idPendenza + ") inviata ha un numero di voci (2) diverso da quello originale (3)")'
}
"""

Scenario: Aggiornamento pendenza non pagata con aggiunta di una voce e ordine diverso

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce.json')

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

* def pendenzaPutUpdate = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_update.json')
* set pendenzaPutUpdate.voci[0].idVocePendenza = '3'
* set pendenzaPutUpdate.voci[1].idVocePendenza = '2'
* set pendenzaPutUpdate.voci[2].idVocePendenza = '1'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutUpdate
When method put
Then status 200

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def pendenzaGetResponse = response

* set pendenzaGetResponse.importo = pendenzaPutUpdate.importo
* set pendenzaGetResponse.voci[0].importo = pendenzaPutUpdate.voci[0].importo
* set pendenzaGetResponse.voci[1].importo = pendenzaPutUpdate.voci[1].importo
* set pendenzaGetResponse.voci[2].importo = pendenzaPutUpdate.voci[2].importo
* set pendenzaGetResponse.causale = pendenzaPutUpdate.causale 
* set pendenzaGetResponse.dataValidita = pendenzaPutUpdate.dataValidita
* set pendenzaGetResponse.dataScadenza = pendenzaPutUpdate.dataScadenza


Scenario: Aggiornamento pendenza non pagata con una voce non presente

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce.json')
* def vocePendenzaZeroOrig =  pendenzaPut.voci[0].idVocePendenza

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

* def pendenzaPutUpdate = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce_update.json')
* set pendenzaPutUpdate.voci[0].idVocePendenza = '3'
* set pendenzaPutUpdate.voci[1].idVocePendenza = '2'
* set pendenzaPutUpdate.voci[2].idVocePendenza = '4'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutUpdate
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_006',
	descrizione: 'Richiesta non valida',
        dettaglio: '#("La pendenza (IdA2A:" + idA2A + ", Id:" + idPendenza + ") inviata non contiene una voce con codSingoloVersamentoEnte (" + vocePendenzaZeroOrig + ") presente nell\'originale")'
}
"""
