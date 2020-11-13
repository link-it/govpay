Feature: Validazione sintattica tipoPendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def idTipoPendenzaNonCensita = 'XXXX'

Scenario: tipoPendenza non valido

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenzaNonCensita
And headers basicAutenticationHeader
And request { }
When method put
Then status 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '#("Il tipo pendenza " + idTipoPendenzaNonCensita + " indicato non esiste.")' 

Scenario: tipoPendenza associato ad un dominio non esistente

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'domini', idDominioNonCensito, 'tipiPendenza' , idTipoPendenzaNonCensita
And headers basicAutenticationHeader
And request { }
When method put
Then status 422

* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Il dominio " + idDominioNonCensito + " indicato non esiste.")' 