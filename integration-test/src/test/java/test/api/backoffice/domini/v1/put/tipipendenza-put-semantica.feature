Feature: Validazione sintattica tipoPendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: tipoPendenza non valido

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', 'XXXX'
And headers basicAutenticationHeader
And request { }
When method put
Then status 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }

