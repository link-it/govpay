Feature: Censimento entrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def entrata = 
"""
{
  "ibanAccredito": "#(ibanAccredito)",
  "ibanAppoggio": "#(ibanAccreditoPostale)",
  "tipoContabilita": "SIOPE",
  "codiceContabilita": 3321,
  "abilitato": true
}
"""

Scenario: Aggiunta di una entrata

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request entrata
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una entrata (<field>)

* set entrata.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request entrata
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| ibanAccredito | ibanAccreditoPostale |
| ibanAppoggio | ibanAccredito |
| ibanAppoggio | null |
| tipoContabilita | 'CAPITOLO' |
| tipoContabilita | 'SPECIALE' |
| tipoContabilita | 'SIOPE' |
| tipoContabilita | 'ALTRO' |
| tipoContabilita | null |
| codiceContabilita | 'AAAAA' |
| codiceContabilita | null |
