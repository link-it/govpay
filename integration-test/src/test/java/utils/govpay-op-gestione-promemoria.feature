Feature: Generazione dei promemoria da spedire via mail e AppIO

Background:

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: 

Given url backofficeBaseurl
And path 'operazioni', 'gestionePromemoria' 
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200