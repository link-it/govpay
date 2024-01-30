Feature: Aggiornamento avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Aggiornamento pendenza non pagata senza modifiche

* def pendenzaGet = read('msg/pendenza-get.json')

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

* def pendenzaGet = read('msg/pendenza-get.json')

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

* def pendenzaGet = read('msg/pendenza-get.json')

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

@test-update-dominio
Scenario: Aggiornamento pendenza non pagata ma e' stato modificato il dominio

* def pendenzaGet = read('msg/pendenza-get.json')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso

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

# Modifica l'auxdigits del dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* set dominio.auxDigit = '3'

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Aggiornamento pendenza
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
And match response.numeroAvviso == pendenzaGetResponse.numeroAvviso


