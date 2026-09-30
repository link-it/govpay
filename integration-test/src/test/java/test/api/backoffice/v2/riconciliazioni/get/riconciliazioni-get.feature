Feature: Dettaglio di una riconciliazione (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/riconciliazioni/get/
# riconciliazione-applicazione-get.feature, riconciliazione-operatore-get.feature
# e riconciliazione-applicazione-getbyTipoRiscossione.feature.
#
# Le riconciliazioni acquisite da flusso hanno per identificativo l'idFlusso
# del PSP, nella forma 2026-08-22GovPAYPsp1-0850160128: contiene i trattini
# della data. Fino alla govpay-console-api#92 il percorso di dettaglio era
# vincolato al pattern ^[0-9A-Za-z]{1,35}$ e quelle riconciliazioni erano
# irraggiungibili, benche' l'elenco le restituisse. Ora il vincolo
# alfanumerico vale solo in scrittura, dove l'identificativo lo sceglie il
# client.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '12345678901'

Scenario: Lettura di una riconciliazione acquisita da flusso

# L'identificativo e' quello che l'elenco restituisce, trattini compresi.

Given url consoleBaseurl
And path 'riconciliazioni'
And param idDominio = idDominio
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 0]'
* def idRiconciliazione = response.results[0].id

Given url consoleBaseurl
And path 'riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == idRiconciliazione
And match response.dominio.idDominio == idDominio
And match response.riscossioni == '#present'
And match response._links.self.href == '/riconciliazioni/' + idDominio + '/' + idRiconciliazione

Scenario: Lettura di una riconciliazione inesistente con identificativo conforme

Given url consoleBaseurl
And path 'riconciliazioni', idDominio, 'RICONCILIAZIONECHENONESISTE'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura con un dominio inesistente

Given url consoleBaseurl
And path 'riconciliazioni', '99999999999', 'RICONCILIAZIONEQUALSIASI'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 404 || responseStatus == 400

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'riconciliazioni', idDominio, 'RICONCILIAZIONEQUALSIASI'
When method get
Then status 401
