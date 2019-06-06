Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

Scenario: Caricamento avviso senza numeroAvviso con voce riferita

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/pendenza/pendenze/v1/get/msg/pendenza-get-dettaglio.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

Scenario: Caricamento avviso senza numeroAvviso con voce autodeterminata

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

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/pendenza/pendenze/v1/get/msg/pendenza-get-dettaglio.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0] contains pendenzaPut.voci[0]
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

