Feature: Pagamento avviso precaricato

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Acquisizione del profilo in forma anonima

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'public'})

Given url pagamentiBaseurl
And path '/profilo'
When method get
Then status 403

Scenario: Acquisizione del profilo autenticato spid

* configure cookies = null

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 

Given url pagamentiBaseurl
And path '/profilo'
And headers spidHeaders
When method get
Then status 200
And match response ==
"""
{
	"nome":"RSSMRA30A01H501I",
	"domini":[],
	"tipiPendenza":[],
	"acl":[],
	"anagrafica": {
		"identificativo":"RSSMRA30A01H501I",
		"anagrafica":"Mario Rossi",
		"email":"mrossi@mailserver.host.it"
	}
}
"""

Scenario: Acquisizione del profilo autenticato basic

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})
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
   "domini":"#[]",
   "tipiPendenza":[],
   "acl":[]
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
   "unitaOperative":"##string", 
   "tipiPendenza":"##string"
}
"""

Scenario: Acquisizione del profilo autenticato apikey

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'apikey'})

Given url pagamentiBaseurl
And path '/profilo'
And header X-APP-ID = idA2A
And header X-API-KEY = pwdA2A
When method get
Then status 200
And match response ==
"""
{
   "nome":"IDA2A01",
   "domini":"#[]",
   "tipiPendenza":[],
   "acl":[]
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
   "unitaOperative":"##string", 
   "tipiPendenza":"##string"
}
"""

Scenario: Acquisizione del profilo autenticato apikey non autorizzato

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'apikey'})

Given url pagamentiBaseurl
And path '/profilo'
And header X-APP-ID = idA2A
And header X-API-KEY = pwdA2A2
When method get
Then status 401

