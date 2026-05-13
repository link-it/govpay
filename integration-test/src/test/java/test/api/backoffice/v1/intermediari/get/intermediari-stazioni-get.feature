Feature: Lettura di una stazione di un intermediario

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pathServizio = '/intermediari'


Scenario: Lettura di una stazione censita

Given url backofficeBaseurl
And path pathServizio, idIntermediario, 'stazioni', idStazione
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.idStazione == idStazione


Scenario: Lettura di una stazione non esistente su intermediario censito (StazioneNonTrovataException)

* def idStazioneInesistente = idIntermediario + '_99'

Given url backofficeBaseurl
And path pathServizio, idIntermediario, 'stazioni', idStazioneInesistente
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
And match response.dettaglio contains idStazioneInesistente
And match response.dettaglio contains 'non censita'


Scenario: Lettura di una stazione con intermediario non esistente (IntermediarioNonTrovatoException)

* def idIntermediarioInesistente = '99999999999'

Given url backofficeBaseurl
And path pathServizio, idIntermediarioInesistente, 'stazioni', idStazione
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
