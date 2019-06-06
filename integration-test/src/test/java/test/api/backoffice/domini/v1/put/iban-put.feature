Feature: Censimento iban

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def iban = { postale:false, mybank:false, abilitato:true, bic:'#(bicAccredito)' }

Scenario: Aggiunta di un iban

# Leggo l'entrata per la successiva verifica
Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200

* def getEntrataSegreteria = response


* def suffix = getCurrentTimeMillis()

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', 'XX02L076011234' + suffix
And headers basicAutenticationHeader
And request iban
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Controllo che l'entrata non sia alterata
Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response == getEntrataSegreteria

Scenario Outline: Modifica di un iban (<field>)

# Leggo l'entrata per la successiva verifica
Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200

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

# Controllo che l'entrata non sia alterata
Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response == getEntrataSegreteria

Examples:
| field | value | 
| postale | true |
| postale | false |
| mybank | true |
| mybank | false |
| abilitato | true |
| abilitato | false |
| bic | 'AAABBBDABAI' |




