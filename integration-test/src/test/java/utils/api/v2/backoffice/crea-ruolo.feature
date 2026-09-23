@ignore
Feature: Crea un ruolo se non esiste gia' (console-api v2)

# Utility: la v2 non ha una DELETE sui ruoli, quindi una feature che ne ha
# bisogno non puo' ricrearlo pulito a ogni esecuzione. Qui la creazione e'
# idempotente nell'esito: 201 la prima volta, 409 dalla seconda in poi.

Background:

* callonce read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario:

Given url consoleBaseurl
And path 'ruoli'
And headers basicAutenticationHeader
And request { idRuolo: '#(idRuolo)', acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ] }
When method post
Then assert responseStatus == 201 || responseStatus == 409
