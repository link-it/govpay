Feature: Pagamenti spontanei con autenticazione spid

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

Scenario: Pagamento spontaneo autenticato spid con entrata riferita

* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 201

* match response contains { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Scenario: Pagamento spontaneo spid con entrata riferita non consentita su spontaneo

Given url backofficeBaseurl
And path 'tipiPendenza', codEntrataSegreteria
And headers basicAutenticationHeader
And request { descrizione: 'Diritti e segreteria' ,  codificaIUV: null,  tipo: 'dovuto',  pagaTerzi: true, abilitato: true }
When method put
Then status 200

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codEntrataSegreteria
And headers basicAutenticationHeader
And request { codificaIUV: '89',  abilitato: true,  pagaTerzi: true }
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 422
And match response ==
"""
{
	"categoria":"RICHIESTA",
	"codice":"CIT_002",
	"descrizione":"Richiesta non valida",
	"dettaglio":"#notnull"
}
"""
And match response.dettaglio contains "non e\' abilitato ai pagamenti spontanei"