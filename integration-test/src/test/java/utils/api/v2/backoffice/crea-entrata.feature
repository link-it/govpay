@ignore
Feature: Crea una entrata se non esiste gia' (console-api v2)

# La v2 non ha una DELETE sulle entrate: la creazione e' idempotente nell'esito,
# 201 la prima volta e 409 dalla seconda in poi.

Background:

* callonce read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario:

Given url consoleBaseurl
And path 'entrate'
And headers basicAutenticationHeader
And request { idEntrata: '#(idEntrata)', descrizione: 'Entrata di prova v2', tipoContabilita: 'ALTRO', codiceContabilita: 'CONTAB_V2' }
When method post
Then assert responseStatus == 201 || responseStatus == 409
