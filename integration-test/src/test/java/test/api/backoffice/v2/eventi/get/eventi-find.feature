Feature: Ricerca degli eventi del giornale (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/eventi/get/eventi-find-sintassi.feature,
# per la parte di validazione dei filtri.
#
# In v2 gli eventi non stanno piu' nel database di GovPay: la lista e' una
# proiezione di quanto risponde il servizio GDE, interrogato da console-api.
# Gli eventi ci sono sempre, perche' le chiamate di questa stessa testsuite ne
# producono; gli scenari percio' non creano dati e si limitano a verificare la
# forma della risposta e la selettivita' dei filtri.
#
# La lista espone i soli metadati (EventoSummary): i payload di richiesta e
# risposta stanno sulle sotto-risorse del dettaglio, con audit dedicato, e sono
# verificati in eventi-get.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Elenco paginato degli eventi

Given url consoleBaseurl
And path 'eventi'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	results: '#[]',
	pagination: { page: 1, limit: 5, hasNextPage: '#boolean' }
}
"""
And match each response.results ==
"""
{
	id: '#number',
	dataEvento: '#string',
	componente: '#string',
	categoriaEvento: '#string',
	ruolo: '#string',
	tipoEvento: '#string',
	esito: '#string',
	durataEventoMs: '##number',
	sottotipoEvento: '##string',
	sottotipoEsito: '##string',
	dettaglioEsito: '##string',
	severita: '##number',
	idDominio: '##string',
	iuv: '##string',
	ccp: '##string',
	idA2A: '##string',
	idPendenza: '##string',
	transactionId: '##string',
	clusterId: '##string'
}
"""

Scenario: La lista non espone i payload di richiesta e risposta

# Niente header ne' body inline: e' la ragione per cui l'elenco non genera
# audit GDPR, a differenza delle sotto-risorse del dettaglio.

Given url consoleBaseurl
And path 'eventi'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results[*].parametriRichiesta == '#[0]'
And match response.results[*].parametriRisposta == '#[0]'

Scenario: Ordinamento fisso per data decrescente

# A differenza delle altre liste paginate, qui ?sort non e' esposto:
# l'ordinamento e' sempre (dataEvento DESC, id DESC).

Given url consoleBaseurl
And path 'eventi'
And param limit = 10
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[_ > 1]'
* def date = $response.results[*].dataEvento
* def ordinate = karate.filter(date, function(x, i){ return i == 0 || date[i-1] >= x })
And match ordinate == date

Given url consoleBaseurl
And path 'eventi'
And param sort = '-dataEvento'
And headers basicAutenticationHeader
When method get
Then status 400
And match response.detail contains 'sort'

Scenario: Filtro per componente

Given url consoleBaseurl
And path 'eventi'
And param componente = 'API_BACKOFFICE'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].componente == 'API_BACKOFFICE'

Scenario: Filtro per categoria, ruolo ed esito

Given url consoleBaseurl
And path 'eventi'
And param categoria = 'INTERFACCIA'
And param ruolo = 'SERVER'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].categoriaEvento == 'INTERFACCIA'
And match each response.results[*].ruolo == 'SERVER'

Given url consoleBaseurl
And path 'eventi'
And param esito = 'OK'
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].esito == 'OK'

Scenario: Filtro per tipo evento

Given url consoleBaseurl
And path 'eventi'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[1]'
* def tipoEvento = response.results[0].tipoEvento

Given url consoleBaseurl
And path 'eventi'
And param tipoEvento = tipoEvento
And param limit = 5
And headers basicAutenticationHeader
When method get
Then status 200
And match each response.results[*].tipoEvento == tipoEvento

Scenario: Filtro per intervallo temporale

# L'intervallo seleziona per dataEvento, estremi inclusi.

Given url consoleBaseurl
And path 'eventi'
And param dataDa = '2020-01-01T00:00:00Z'
And param dataA = '2020-01-02T00:00:00Z'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'

Scenario: Filtro che non seleziona nulla

Given url consoleBaseurl
And path 'eventi'
And param idDominio = '99999999999'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[0]'

Scenario: Valore non ammesso su un filtro a vocabolario chiuso

Given url consoleBaseurl
And path 'eventi'
And param componente = 'COMPONENTE_INESISTENTE'
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Data malformata

Given url consoleBaseurl
And path 'eventi'
And param dataDa = 'non-una-data'
And headers basicAutenticationHeader
When method get
Then status 400

Scenario: Filtro non supportato

Given url consoleBaseurl
And path 'eventi'
And param filtroInventato = 'x'
And headers basicAutenticationHeader
When method get
Then status 400
And match response.detail contains 'filtroInventato'

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'eventi'
When method get
Then status 401
