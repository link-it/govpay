Feature: Validazione semantica stazioni

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioBasicAuth.json')
* def stazione = read('classpath:test/api/backoffice/v1/intermediari/put/msg/stazione.json')

Scenario: Stazione a intermediario errato

Given url backofficeBaseurl
And path 'intermediari', '00000000000'
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', '00000000000_01'
And headers basicAutenticationHeader
And request stazione
When method put
Then status 422

* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }


Scenario: Stazione a intermediario errato

* def idIntermediarioNonCensito = '01234543210'
* def idStazioneNonCensito = '01234543210_01'

Given url backofficeBaseurl
And path 'intermediari', idIntermediarioNonCensito, 'stazioni', idStazioneNonCensito
And headers basicAutenticationHeader
And request stazione
When method put
Then status 422

* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '#("L\'intermediario " + idIntermediarioNonCensito + " indicato non esiste.")' 