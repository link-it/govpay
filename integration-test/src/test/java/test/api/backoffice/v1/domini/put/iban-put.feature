Feature: Censimento iban

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def iban = { postale:false, mybank:false, abilitato:true, bic:'#(bicAccredito)' }

Scenario: Aggiunta di un iban

* def suffix = getCurrentTimeMillis()

* set iban.descrizione = 'IBAN Accredito ' + 'XX02L076011234' + suffix
Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', 'XX02L076011234' + suffix
And headers basicAutenticationHeader
And request iban
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di un iban (<field>)

* set iban.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers basicAutenticationHeader
And request iban
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| postale | true |
| postale | false |
| descrizione | null |
| descrizione | 'Iban Accredito' |
| abilitato | true |
| abilitato | false |
| bic | 'AAABBBDABAI' |




