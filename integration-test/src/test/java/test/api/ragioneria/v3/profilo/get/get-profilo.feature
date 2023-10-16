Feature: Acquisizione del profilo utente

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Acquisizione del profilo autenticato basic

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url pagamentiBaseurl
And path '/profilo'
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
   "nome":"IDA2A01",
   "domini": "#[]",
   "acl":"#[]",
   "tipiPendenza":"#[]"
}
"""
And match response.domini[*].idDominio contains ['12345678901','12345678902']
And match each response.domini ==
"""
{
         "idDominio":"#string",
         "ragioneSociale":"#string"
}
"""

Scenario: Acquisizione del profilo autenticato apikey

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'apikey'})

Given url pagamentiBaseurl
And path '/profilo'
And header X-API-ID = idA2A
And header X-API-KEY = pwdA2A
When method get
Then status 200
And match response ==
"""
{
   "nome":"IDA2A01",
   "domini": "#[]",
   "acl":"#[]",
   "tipiPendenza":"#[]"
}
"""
And match response.domini[*].idDominio contains ['12345678901','12345678902']
And match each response.domini ==
"""
{
         "idDominio":"#string",
         "ragioneSociale":"#string"
}
"""

Scenario: Acquisizione del profilo autenticato apikey non autorizzato

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'apikey'})

Given url pagamentiBaseurl
And path '/profilo'
And header X-API-ID = idA2A
And header X-API-KEY = pwdA2A2
When method get
Then status 401

