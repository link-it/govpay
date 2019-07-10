Feature: Aggiornamento avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

Scenario: Aggiornamento pendenza non pagata senza modifiche

* def pendenzaGet = read('classpath:test/api/pendenza/v1/pendenze/get/msg/pendenza-get-dettaglio.json')

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

* def pendenzaGet = read('classpath:test/api/pendenza/v1/pendenze/get/msg/pendenza-get-dettaglio.json')

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
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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
	dettaglio: '#("Il versamento (" + idPendenza + ") dell\'applicazione (" + idA2A + ") ha il singolo versamento con codSingoloVersamentoEnte (1) inviato ha un iban di accredito diverso dall\'originale.")'
}
"""

Scenario: Aggiornamento pendenza annullata 

* def pendenzaGet = read('classpath:test/api/pendenza/v1/pendenze/get/msg/pendenza-get-dettaglio.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

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
