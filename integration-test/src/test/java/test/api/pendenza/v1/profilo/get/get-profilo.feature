Feature: Acquisizione del profilo utente

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Acquisizione del profilo autenticato basic

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
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
   "entrate":"#[]"
}
"""
And match response.domini[*].idDominio contains ['12345678901','12345678902']
And match each response.domini ==
"""
{
         "idDominio":"#string",
         "ragioneSociale":"#string",
         "indirizzo":"##string",
         "civico":"##string",
         "cap":"##string",
         "localita":"##string",
         "provincia":"##string",
         "nazione":"##string",
         "email":"##string",
         "pec":"##string",
         "tel":"##string",
         "fax":"##string",
         "web":"##string",
         "gln":"##string",
         "logo":"##string",
         "iuvPrefix":"##string", 
         "stazione":"##string", 
         "auxDigit":"##string", 
         "segregationCode":"##string", 
         "abilitato":"#boolean", 
         "unitaOperative":"##string", 
         "contiAccredito":"##string", 
         "entrate":"##string"
}
"""