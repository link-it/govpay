Feature: Dettaglio di una ricevuta (console-api v2)

# Corrispettivo v2 delle letture che in v1 passavano da /rpp/{idDominio}/{iuv}/{ccp}
# e /rpp/.../rt: in v2 la ricevuta e' identificata da dominio, iuv e
# identificativo della ricevuta, e rpt e rt sono sue sotto-risorse.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

# Si parte da una ricevuta esistente nell'ambiente, presa dall'elenco.
Given url consoleBaseurl
And path 'ricevute'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def presente = response.results[0]

Scenario: Lettura di una ricevuta

Given url consoleBaseurl
And path 'ricevute', presente.idDominio, presente.iuv, presente.idRicevuta
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idDominio == presente.idDominio
And match response.iuv == presente.iuv
And match response.idRicevuta == presente.idRicevuta
And match response.stato == '#notnull'
And match response.dataRicevuta == '#notnull'

Scenario: Lettura della rpt della ricevuta

Given url consoleBaseurl
And path 'ricevute', presente.idDominio, presente.iuv, presente.idRicevuta, 'rpt'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Lettura della rt della ricevuta

Given url consoleBaseurl
And path 'ricevute', presente.idDominio, presente.iuv, presente.idRicevuta, 'rt'
And headers basicAutenticationHeader
When method get
Then status 200

Scenario: Lettura di una ricevuta inesistente

Given url consoleBaseurl
And path 'ricevute', presente.idDominio, presente.iuv, 'RICEVUTA_CHE_NON_ESISTE'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'ricevute', presente.idDominio, presente.iuv, presente.idRicevuta
When method get
Then status 401
