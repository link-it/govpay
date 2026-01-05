Feature: Sonde di monitoraggio GovPay

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Lista sonde disponibili

Given url backofficeBaseurl
And path 'sonde'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.risultati == '#notnull'

Scenario: Sonda check-db

Given url backofficeBaseurl
And path 'sonde', 'check-db'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-db'

Scenario: Sonda check-ntfy

Given url backofficeBaseurl
And path 'sonde', 'check-ntfy'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-ntfy'

Scenario: Sonda check-tracciati

Given url backofficeBaseurl
And path 'sonde', 'check-tracciati'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-tracciati'

Scenario: Sonda spedizione-promemoria

Given url backofficeBaseurl
And path 'sonde', 'spedizione-promemoria'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'spedizione-promemoria'

Scenario: Sonda check-promemoria

Given url backofficeBaseurl
And path 'sonde', 'check-promemoria'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-promemoria'

Scenario: Sonda check-ntfy-appio

Given url backofficeBaseurl
And path 'sonde', 'check-ntfy-appio'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-ntfy-appio'

Scenario: Sonda check-gestione-promemoria

Given url backofficeBaseurl
And path 'sonde', 'check-gestione-promemoria'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-gestione-promemoria'

Scenario: Sonda check-elab-trac-notif-pag

Given url backofficeBaseurl
And path 'sonde', 'check-elab-trac-notif-pag'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-elab-trac-notif-pag'

Scenario: Sonda check-spedizione-trac-notif-pag

Given url backofficeBaseurl
And path 'sonde', 'check-spedizione-trac-notif-pag'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-spedizione-trac-notif-pag'

Scenario: Sonda check-riconciliazioni

Given url backofficeBaseurl
And path 'sonde', 'check-riconciliazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-riconciliazioni'

Scenario: Sonda check-rpt-scadute

Given url backofficeBaseurl
And path 'sonde', 'check-rpt-scadute'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-rpt-scadute'

Scenario: Sonda check-recupero-rt

Given url backofficeBaseurl
And path 'sonde', 'check-recupero-rt'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'check-recupero-rt'

Scenario: Sonda spedizione-trac-notif-pag

Given url backofficeBaseurl
And path 'sonde', 'spedizione-trac-notif-pag'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'spedizione-trac-notif-pag'

Scenario: Sonda elaborazione-trac-notif-pag

Given url backofficeBaseurl
And path 'sonde', 'elaborazione-trac-notif-pag'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'elaborazione-trac-notif-pag'

Scenario: Sonda riconciliazioni

Given url backofficeBaseurl
And path 'sonde', 'riconciliazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'riconciliazioni'

Scenario: Sonda caricamento-tracciati

Given url backofficeBaseurl
And path 'sonde', 'caricamento-tracciati'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'caricamento-tracciati'

Scenario: Sonda update-rnd

Given url backofficeBaseurl
And path 'sonde', 'update-rnd'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'update-rnd'

Scenario: Sonda gestione-promemoria

Given url backofficeBaseurl
And path 'sonde', 'gestione-promemoria'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'gestione-promemoria'

Scenario: Sonda update-ntfy-appio

Given url backofficeBaseurl
And path 'sonde', 'update-ntfy-appio'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'update-ntfy-appio'

Scenario: Sonda update-ntfy

Given url backofficeBaseurl
And path 'sonde', 'update-ntfy'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'update-ntfy'

Scenario: Sonda rpt-scadute

Given url backofficeBaseurl
And path 'sonde', 'rpt-scadute'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'rpt-scadute'

Scenario: Sonda recupero-rt

Given url backofficeBaseurl
And path 'sonde', 'recupero-rt'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == 'recupero-rt'
