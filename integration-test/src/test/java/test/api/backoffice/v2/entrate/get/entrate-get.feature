Feature: Dettaglio di una entrata (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/entrate/get/entrate-get.feature.
# In v1 la lettura di una entrata inesistente dava 404 con la
# TipoTributoNonTrovatoException; qui il 404 ha il corpo Problem di RFC 7807.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idEntrata = 'SEGRETERIA'

Scenario: Lettura di una entrata censita

Given url consoleBaseurl
And path 'entrate', idEntrata
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idEntrata == idEntrata
And match response.descrizione == '#notnull'
And match response.tipoContabilita == '#notnull'
And match response.codiceContabilita == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: Lettura di una entrata non esistente

Given url consoleBaseurl
And path 'entrate', 'ENTRATA_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 404
And match response.status == 404
And match response.title == 'Not Found'

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'entrate', idEntrata
When method get
Then status 401
