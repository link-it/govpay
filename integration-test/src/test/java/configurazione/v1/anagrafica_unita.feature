Feature: Censimento unita operative

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def unita = read('classpath:test/api/backoffice/v1/domini/put/msg/unita.json')
* def unita2 = read('classpath:test/api/backoffice/v1/domini/put/msg/unita.json')
* def idUnitaOperativa = '12345678901_01'
* def idUnitaOperativa2 = '12345678901_02'

Scenario: Aggiunta di due unita operative

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set unita2.ragioneSociale = 'Ufficio tre'

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa2
And headers basicAutenticationHeader
And request unita2
When method put
Then assert responseStatus == 200 || responseStatus == 201


Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response == 
"""
{
	numRisultati: 2,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[2]'
}
"""
And match response.risultati[0].idUnita == idUnitaOperativa
And match response.risultati[1].idUnita == idUnitaOperativa2