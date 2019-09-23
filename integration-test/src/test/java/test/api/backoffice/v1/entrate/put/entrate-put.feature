Feature: Censimento entrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def entrata = 
"""
{
  "descrizione": "Imposta municipale urbana",
  "tipoContabilita": "SIOPE",
  "codiceContabilita": 3321
}
"""

Scenario: Aggiunta di una entrata

Given url backofficeBaseurl
And path 'entrate', 'SIOPE-3321'
And headers basicAutenticationHeader
And request entrata
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una entrata (<field>)

* set entrata.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'entrate', 'SIOPE-3321'
And headers basicAutenticationHeader
And request entrata
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'entrate', 'SIOPE-3321'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| descrizione | 'Nuova descrizione' |
| tipoContabilita | 'CAPITOLO' |
| tipoContabilita | 'SPECIALE' |
| tipoContabilita | 'SIOPE' |
| tipoContabilita | 'ALTRO' |
| codiceContabilita | 'AAAAA' |
