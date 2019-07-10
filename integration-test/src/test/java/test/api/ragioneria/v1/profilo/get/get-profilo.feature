Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Acquisizione del profilo autenticato basic

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

Given url pagamentiBaseurl
And path '/profilo'
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
   "nome":"IDA2A01",
   "domini":"#[]",
   "acl":"#[]",
   "entrate":"#[]"
}
"""
