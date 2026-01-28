Feature: Lista operazioni disponibili

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Lista delle operazioni disponibili

Given url backofficeBaseurl
And path 'operazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.risultati == '#notnull'
And match response.numRisultati == '#number'

Scenario: Esecuzione operazione chiusuraRptScadute

Given url backofficeBaseurl
And path 'operazioni', 'chiusuraRptScadute'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idOperazione == 'chiusuraRptScadute'
And match response.stato == '0'

Scenario: Esecuzione operazione elaborazioneRiconciliazioni

Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneRiconciliazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idOperazione == 'elaborazioneRiconciliazioni'
And match response.stato == '0'

Scenario: Esecuzione operazione inviaPosizioniDebitorieAca

Given url backofficeBaseurl
And path 'operazioni', 'inviaPosizioniDebitorieAca'
And headers basicAutenticationHeader
When method get
# Then status 200
# And match response.idOperazione == 'inviaPosizioniDebitorieAca'
# And match response.stato == '0'
