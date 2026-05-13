Feature: Lettura di una entrata per identificativo

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pathServizio = '/entrate'


Scenario: Lettura di una entrata censita

Given url backofficeBaseurl
And path pathServizio, codEntrataSegreteria
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.idEntrata == codEntrataSegreteria


Scenario: Lettura di una entrata non esistente (TipoTributoNonTrovatoException)

* def idEntrataInesistente = 'ENTRATA_INESISTENTE_99'

Given url backofficeBaseurl
And path pathServizio, idEntrataInesistente
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
And match response.dettaglio contains idEntrataInesistente
And match response.dettaglio contains 'non censita'
