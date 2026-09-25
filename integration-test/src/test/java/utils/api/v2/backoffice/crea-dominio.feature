@ignore
Feature: Crea un dominio se non esiste gia' (console-api v2)

Background:

* callonce read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario:

Given url consoleBaseurl
And path 'domini'
And headers basicAutenticationHeader
And request { idDominio: '#(idDominio)', ragioneSociale: 'Dominio di prova v2', abilitato: true, scaricaFr: false, idStazione: '11111111113_01', gln: '1234567890123' }
When method post
Then assert responseStatus == 201 || responseStatus == 409
