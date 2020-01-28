Feature: Censimento unita operative

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def unita = read('classpath:test/api/backoffice/v1/domini/put/msg/unita.json')
* def idUnitaOperativa = '12345678900'

Scenario: Aggiunta di una unita operativa

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una unita operativa (<field>)

* set unita.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'


Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201
And match response.<field> == checkValue

Examples:
| field | value | 
| ragioneSociale | 'Nuova ragione sociale' |
| indirizzo | 'Via nuova' |
| indirizzo | null |
| civico | '000A' |
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

Scenario: Configurazione di due UO con idUo della seconda che e' una sottostringa del primo idUo	 

* def idComune = getCurrentTimeMillis()
* def idUo1 = 'PROVA_' + idComune
* def idUo2 = 'OVA_' + idComune


Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUo1
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUo2
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201


