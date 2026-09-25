Feature: Dettaglio di un flusso di rendicontazione (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/flussiRendicontazione/get/flussiRendicontazione-get.feature
# e -getByIdEData.feature. In v2 il flusso e' identificato dalla quaterna
# dominio, flusso, psp e revisione: la revisione fa parte del percorso, e non e'
# piu' un parametro di ricerca.
#
# Non ha corrispettivo la lettura in formato XML della v1
# (flussiRendicontazione-get-xml.feature): console-api non espone il tracciato
# originale del flusso.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Given url consoleBaseurl
And path 'flussi-rendicontazione'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def flusso = response.results[0]

Scenario: Lettura di un flusso

Given url consoleBaseurl
And path 'flussi-rendicontazione', flusso.idDominio, flusso.idFlusso, flusso.idPsp, flusso.revisione
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == flusso.idFlusso
And match response.idDominio == flusso.idDominio
And match response.idPsp == flusso.idPsp
And match response.stato == '#notnull'
And match response.numeroPagamenti == '#number'
And match response.importoTotale == '#number'

Scenario: Lettura di un flusso inesistente

Given url consoleBaseurl
And path 'flussi-rendicontazione', flusso.idDominio, 'FLUSSO-CHE-NON-ESISTE', flusso.idPsp, 1
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura di una revisione inesistente

Given url consoleBaseurl
And path 'flussi-rendicontazione', flusso.idDominio, flusso.idFlusso, flusso.idPsp, 99
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'flussi-rendicontazione', flusso.idDominio, flusso.idFlusso, flusso.idPsp, flusso.revisione
When method get
Then status 401
