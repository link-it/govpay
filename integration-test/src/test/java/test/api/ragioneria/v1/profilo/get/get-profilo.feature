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
   "domini":[
      {
         "idDominio":"12345678901",
         "ragioneSociale":"Ente Creditore Test",
         "indirizzo":"Piazzale Paolino Paperino",
         "civico":"1",
         "cap":"00000",
         "localita":"Roma",
         "provincia":"RO",
         "nazione":"IT",
         "email":"info@entecreditore.it",
         "pec":"protocollo.generale@pec.entecreditore.it",
         "tel":"00 1234 5678",
         "fax":"00 1234 5678",
         "web":"http://www.entecreditore.it",
         "gln":"8088888000000",
         "logo":"/domini/12345678901/logo",
         "iuvPrefix":"%(a)", 
         "stazione":"11111111113_01", 
         "auxDigit":"0", 
         "segregationCode":"00", 
         "abilitato":true
      },
      {
         "idDominio":"12345678902",
         "ragioneSociale":"Ente Creditore Test",
         "indirizzo":"Piazzale Paolino Paperino",
         "civico":"1",
         "cap":"00000",
         "localita":"Roma",
         "provincia":"RO",
         "nazione":"IT",
         "email":"info@entecreditore.it",
         "pec":"protocollo.generale@pec.entecreditore.it",
         "tel":"00 1234 5678",
         "fax":"00 1234 5678",
         "web":"http://www.entecreditore.it",
         "gln":"8088888000000",
         "logo":"/domini/12345678902/logo",
         "iuvPrefix":"%(a)", 
         "stazione":"11111111113_01", 
         "auxDigit":"0", 
         "segregationCode":"00", 
         "abilitato":true
      }
   ],
   "acl":"#[]",
   "entrate":"#[]"
}
"""
