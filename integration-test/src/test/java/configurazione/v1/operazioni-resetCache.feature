Feature: Reset cache

Background:

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario:

Given url backofficeBaseurl
And path 'operazioni', 'resetCacheAnagrafica'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200

# Logout delle sessioni SPID

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

* def spidHeadersVerdi = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'}
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

# API Backoffice

* call sleep(200)

Given url backofficeSpidBaseurl
And path '/logout'
And headers spidHeadersVerdi
When method get

* call sleep(200)

Given url backofficeSpidBaseurl
And path '/logout'
And headers spidHeadersRossi
When method get
