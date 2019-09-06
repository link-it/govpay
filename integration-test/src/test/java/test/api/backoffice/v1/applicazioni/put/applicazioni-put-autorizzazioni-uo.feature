Feature: Censimento applicazioni

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')

Scenario: Aggiunta di una applicazione con autorizzazione sulle UO

* call read('classpath:test/api/backoffice/v1/domini/put/unita-put-multiple.feature')

* set applicazione.domini = 
"""
[ 
{
	'idDominio' : '#(idDominio)',
	'unitaOperative' : []
	
}
]
"""

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response.domini[0].idDominio == applicazione.domini[0].idDominio
And match response.domini[0].unitaOperative == applicazione.domini[0].unitaOperative

Scenario Outline: Modifica di una applicazione (<field>)

* set applicazione.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| domini | [ ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: null } ] |
| domini | [ { idDominio: '#(idDominio)' } ] |
