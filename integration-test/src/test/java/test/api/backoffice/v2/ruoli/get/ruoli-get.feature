Feature: Dettaglio di un ruolo (console-api v2)

# Corrispettivo v2 della parte di lettura di
# test/api/backoffice/v1/ruoli/put/ruoli-put.feature, dove la GET serviva a
# verificare l'esito della modifica. Qui la GET e' una risorsa a se', e in piu'
# restituisce l'ETag che le operazioni di modifica devono rispedire.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idRuolo = 'RuoloTestV2'

# Il ruolo deve esistere: la creazione e' idempotente nell'esito, 201 la prima
# volta e 409 dalla seconda.
* def creaRuolo = call read('classpath:utils/api/v2/backoffice/crea-ruolo.feature') { idRuolo: '#(idRuolo)' }

Scenario: Lettura di un ruolo censito

Given url consoleBaseurl
And path 'ruoli', idRuolo
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idRuolo == idRuolo
And match response.acl == '#[_ > 0]'
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: Lettura di un ruolo inesistente

Given url consoleBaseurl
And path 'ruoli', 'RuoloCheNonEsiste'
And headers basicAutenticationHeader
When method get
Then status 404
And match response.status == 404
And match response.title == 'Not Found'

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'ruoli', idRuolo
When method get
Then status 401
