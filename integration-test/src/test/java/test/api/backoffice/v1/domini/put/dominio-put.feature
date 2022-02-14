Feature: Censimento unita operative

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio.json')

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

Scenario: Aggiunta di un dominio

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di un dominio (<field>)

* set dominio.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| ragioneSociale | 'Nuova ragione sociale' |
| indirizzo | 'Via nuova' |
| indirizzo | null |
| civico | '000A' |
| civico | '000' |
| civico | null |
| cap | '12345' |
| cap | null |
| localita | 'Nuova localita' |
| localita | null |
| provincia | 'Nuova provincia' |
| provincia | null |
| nazione | 'EN' |
| nazione | null |
| email | 'mail@aaa.it' |
| email | null |
| pec | 'mail@aaa.it' |
| pec | null |
| tel | '000 000 000' |
| tel | null |
| fax | '000 000 000' |
| fax | null |
| web | 'www.nuova.it' |
| web | null |
| abilitato | false |
| abilitato | true |
| area | 'Nuova area' |
| area | null |
| gln | '0000001000000' |
| gln | null |
| cbill | '00000' |
| cbill | null |
| iuvPrefix | '%(a)' |
| iuvPrefix | '%(t)' |
| iuvPrefix | '%(p)' |
| iuvPrefix | '%(y)' |
| iuvPrefix | '%(Y)' |
| iuvPrefix | '%(u)' |
| iuvPrefix | '1%(u)0' |
| iuvPrefix | '1%(u)%(Y)0' |
| iuvPrefix | '1%(u)0%(Y)0' |
| iuvPrefix | null |
| auxDigit | '0' |
| segregationCode | '49' |
| segregationCode | '01' |
| logo | '#("/domini/"+ idDominio +"/logo")' |
| logo | null |
| autStampaPosteItaliane | 'test' |
| autStampaPosteItaliane | null |
| intermediato | true |

Scenario: Modifica di un dominio (segregation code)

* set dominio.segregationCode = null
* set dominio.auxDigit = 0

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.segregationCode == '#notpresent'

Scenario: Autorizzazioni alla creazione dei domini

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'domini', idDominioNonCensito
And headers operatoreSpidAutenticationHeader
And request dominio
When method put
Then status 403
* match response == { categoria: 'AUTORIZZAZIONE', codice: '403000', descrizione: 'Operazione non autorizzata', dettaglio: '#notnull' }


