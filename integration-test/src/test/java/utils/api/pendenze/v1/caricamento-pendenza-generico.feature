Feature: caricamento dovuto segreteria

Scenario: Pagamento ad iniziativa PSP

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBasicBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idDominio = idDominioPendenza
* set pendenzaPut.soggettoPagatore = soggettoPagatore
* set pendenzaPut.voci[0] = vociPendenza

Given url pendenzeBasicBaseurl
And path 'pendenze', idA2APendenza, idPendenza
And headers autenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo


