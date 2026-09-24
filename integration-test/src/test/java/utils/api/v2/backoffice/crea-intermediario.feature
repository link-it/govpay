@ignore
Feature: Crea un intermediario se non esiste gia' (console-api v2)

Background:

* callonce read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario:

Given url consoleBaseurl
And path 'intermediari'
And headers basicAutenticationHeader
And request { idIntermediario: '#(idIntermediario)', denominazione: 'Intermediario di prova v2', principalPagoPa: 'ndpsym', abilitato: true }
When method post
Then assert responseStatus == 201 || responseStatus == 409
