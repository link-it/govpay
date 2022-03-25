Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Acquisizione del profilo in forma anonima

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'public'})

Given url backofficeBaseurl
And path '/profilo'
When method get
Then status 403

Scenario: Acquisizione del profilo autenticato spid

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

Given url backofficeBaseurl
And path '/profilo'
And headers spidHeaders
When method get
Then status 200
And match response ==
"""
{
	"nome":"Mario Rossi",
	"domini":"#[]",
	"tipiPendenza":"#[]",
	"acl":"#[]",
	"autenticazione":"SPID"
}
"""

Scenario: Acquisizione del profilo autenticato basic

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url backofficeBaseurl
And path '/profilo'
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
   "nome":"IDA2A01",
   "domini": "#[1]",
   "tipiPendenza":"#[1]",
   "acl":"#[]",
   "autenticazione":"BASIC"
}
"""
# And match response.domini[*].idDominio contains ['12345678901','12345678902']
And match each response.domini ==
"""
{
         "idDominio":"#string",
         "ragioneSociale":"#string"
}
"""

And match each response.tipiPendenza ==
"""
{
         "idTipoPendenza":"#string",
         "descrizione":"#string",
         "pagaTerzi": "#ignore",
         "abilitato": "#ignore"
}
"""


