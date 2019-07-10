Feature: Attivazione o Verifica RPT annullata

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def faultBean =
"""
{
	faultCode: "PAA_PAGAMENTO_ANNULLATO",
	faultString: "Pagamento in attesa risulta annullato all'Ente Creditore.",
	id: "#(idDominio)",
	description: '#ignore',
	serial: null
}
"""

* configure followRedirects = false

Scenario: Attivazione RPT annullata precaricata

* call read('classpath:utils/pa-carica-avviso.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request [ { "op": "REPLACE", "path": "/stato", "value": "ANNULLATA" }, { "op": "REPLACE", "path": "/descrizioneStato", "value": "Test annullamento" }]
When method patch
Then status 200

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean

Scenario: Attivazione RPT annullata non precaricato

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.stato = 'ANNULLATA'
* set pendenzaPut.descrizioneStato = 'Test annullamento'
* call read('classpath:utils/pa-prepara-avviso-annullato.feature')
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == faultBean


Scenario: Verifica RPT annullata precaricata

* call read('classpath:utils/pa-carica-avviso.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request [ { "op": "REPLACE", "path": "/stato", "value": "ANNULLATA" }, { "op": "REPLACE", "path": "/descrizioneStato", "value": "Test annullamento" }]
When method patch
Then status 200

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == faultBean


Scenario: Verifica RPT annullata non precaricato

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* set pendenzaPut.stato = 'ANNULLATA'
* set pendenzaPut.descrizioneStato = 'Test annullamento'
* call read('classpath:utils/pa-prepara-avviso-annullato.feature')

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == faultBean
