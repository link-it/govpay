Feature: Dettaglio di un tipo pendenza (console-api v2)

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idTipoPendenza = 'LIBERO'

Scenario: Lettura di un tipo pendenza censito

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idTipoPendenza == idTipoPendenza
And match response.descrizione == '#notnull'
And match response.abilitato == '#boolean'
And match response.avvisaturaMail == '#notnull'
And match response.avvisaturaAppIO == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: Lettura di un tipo pendenza non esistente

Given url consoleBaseurl
And path 'tipiPendenza', 'TIPO_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 404
And match response.status == 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'tipiPendenza', idTipoPendenza
When method get
Then status 401
