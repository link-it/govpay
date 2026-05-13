Feature: Ricerca stazioni di un intermediario

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pathServizio = '/intermediari'


Scenario: Lettura delle stazioni di un intermediario censito

Given url backofficeBaseurl
And path pathServizio, idIntermediario, 'stazioni'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	maxRisultati : '#ignore',
	numRisultati: '#? _ > 0',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '##null',
	risultati: '#[_ > 0]'
}
"""
And match response.risultati[0].idStazione == '#notnull'


Scenario: Lettura delle stazioni di un intermediario non esistente restituisce lista vuota

* def idIntermediarioInesistente = '99999999999'

Given url backofficeBaseurl
And path pathServizio, idIntermediarioInesistente, 'stazioni'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.numRisultati == 0
And match response.risultati == '#[0]'


Scenario Outline: Validazione sintattica idIntermediario non valido: <value>

* def idIntermediarioInvalido = <value>

Given url backofficeBaseurl
And path pathServizio, idIntermediarioInvalido, 'stazioni'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI' }

Examples:
| value |
| ' *&' |
| '123456789012345678901234567890ABCDEF' |
