Feature: Lettura di un intermediario per identificativo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pathServizio = '/intermediari'


Scenario: Lettura di un intermediario censito

Given url backofficeBaseurl
And path pathServizio, idIntermediario
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.idIntermediario == idIntermediario


Scenario: Lettura di un intermediario non esistente

* def idIntermediarioInesistente = '99999999999'

Given url backofficeBaseurl
And path pathServizio, idIntermediarioInesistente
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
And match response ==
"""
{
	categoria: 'OPERAZIONE',
	codice: '404000',
	descrizione: 'Risorsa non trovata',
	dettaglio: '#notnull'
}
"""
And match response.dettaglio contains idIntermediarioInesistente
