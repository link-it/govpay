Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Caricamento pendenza con stampa avviso 

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
And param stampaAvviso = true
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', pdf: '#notnull', UUID: '#notnull'}

Scenario: Caricamento pendenza con soglia con stampa avviso 

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = codLibero
* set pendenzaPut.documento =
"""
{
	identificativo: "#(idPendenza + '_DOC')",
	descrizione: "#('Documento ('+ idPendenza +')')",
	soglia: { tipo: "ENTRO", giorni: 5 }
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
And param stampaAvviso = true
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', pdf: '#notnull', UUID: '#notnull'}

Scenario: Caricamento pendenza senza date con stampa avviso

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = codLibero
* set pendenzaPut.dataScadenza = null
* set pendenzaPut.dataValidita = null

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
And param stampaAvviso = true
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', pdf: '#notnull', UUID: '#notnull'}





