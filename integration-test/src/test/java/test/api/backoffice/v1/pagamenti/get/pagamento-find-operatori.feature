Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')

Scenario Outline: Lettura dettaglio operatore <operatore> filtrati per data

* def operatore = read('msg/<operatore>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/logout'
And headers spidHeadersRossi
When method get
Then status 200

Given url backofficeBaseurl
And path '/pagamenti'
And param dataDa = dataInizio
And param dataA = dataFine
And headers spidHeadersRossi
When method get
And match response.risultati[15].id == idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
And match response.risultati[14].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
And match response.risultati[13].id == idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[12].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[11].id == idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
And match response.risultati[10].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[9].id == idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[8].id == idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[7].id == idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
And match response.risultati[6].id == idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
And match response.risultati[5].id == idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
And match response.risultati[4].id == idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
And match response.risultati[3].id == idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[2].id == idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
And match response.risultati[1].id == idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
And match response.risultati[0].id == idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
Then status 200
And match response == 
"""
{
	numRisultati: 16,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[16]'
}
"""

Examples:
| operatore |
| operatore_star_star.json |
| operatore_domini1e2_segreteria.json |
| operatore_domini1_segreteria.json |
| operatore_domini1_star.json |
| operatore_none_none.json |

Scenario: Ricerca pagamenti operatore non autorizzato al servizio

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def operatore = read('msg/operatore_nonAuth.json')

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/logout'
And headers spidHeadersRossi
When method get
Then status 200

Given url backofficeBaseurl
And path '/pagamenti'
And headers spidHeadersRossi
When method get
Then status 403

Scenario: Ricerca pagamenti operatore non abilitato

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def operatore = read('msg/operatore_disabilitato.json')

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/logout'
And headers spidHeadersRossi
When method get
Then status 200

Given url backofficeBaseurl
And path '/pagamenti'
And headers spidHeadersRossi
When method get
Then status 403


Scenario: Ricerca pagamenti operatore non censito

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'XXXYYY30A01H501I','X-SPID-NAME': 'John','X-SPID-FAMILYNAME': 'Doe','X-SPID-EMAIL': 'jdoe@mailserver.host.it'}

Given url backofficeBaseurl
And path '/logout'
And headers spidHeadersRossi
When method get
Then status 200

Given url backofficeBaseurl
And path '/pagamenti'
And headers spidHeadersRossi
When method get
Then status 401

