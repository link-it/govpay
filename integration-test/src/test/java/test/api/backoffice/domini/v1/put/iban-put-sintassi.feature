Feature: Validazione sintattica iban

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def iban = { postale:false, mybank:false, abilitato:true, bic:'#(bicAccredito)' }

Scenario Outline: <field> non valida

* set iban.<field> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers basicAutenticationHeader
And request iban
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>

Examples:
| field | fieldValue | fieldName | 
| postale | null | 'postale' |
| postale | '' | 'postale' |
| abilitato | null | 'abilitato' |
| abilitato | '' | 'abilitato' |
| mybank | null | 'mybank' |
| mybank | '' | 'mybank' |
| bic | 'AAAAAA3OZZZ' | 'bic' |


Scenario: Iban non valido

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', '0088ABCDEFGHIJKLMNOPQRSTUVWXYZ01'
And headers basicAutenticationHeader
And request iban
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'ibanAccredito'

