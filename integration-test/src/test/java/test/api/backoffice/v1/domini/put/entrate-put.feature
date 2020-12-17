Feature: Censimento entrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
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


Scenario: Autorizzazioni alla creazione delle entrate

* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['*'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] }, 	{ servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true	
}
"""

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'domini', idDominioNonCensito, 'entrate', codEntrataSiope
And headers operatoreSpidAutenticationHeader
And request entrata
When method put
Then status 403
* match response == { categoria: 'AUTORIZZAZIONE', codice: '403000', descrizione: 'Operazione non autorizzata', dettaglio: '#notnull' }





