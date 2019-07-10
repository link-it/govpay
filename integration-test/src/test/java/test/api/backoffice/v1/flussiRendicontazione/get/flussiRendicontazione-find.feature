Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/ragioneria/bunch-riconciliazioni-v2.feature')

Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione>.

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizioFR
And param dataA = dataFineFR
When method get
Then status 200
And match response ==
"""
{
	numRisultati: <numRisultati>,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[<numRisultati>]'
}
""" 
Examples:
| applicazione | numRisultati | 
| applicazione_star.json | 2 |
| applicazione_dominio1e2.json | 2 |
| applicazione_dominio1.json | 1 |
| applicazione_dominio2.json | 1 |
| applicazione_none.json | 0 |

Scenario Outline: Ricerca rendicontazioni da applicazione <applicazione>.

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403

Examples:
| applicazione |
| applicazione_disabilitato.json |
| applicazione_nonAuth.json |

Scenario Outline: Ricerca rendicontazioni da operatore <operatore>.

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request read('msg/<operatore>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers spidHeadersRossi
And param dataDa = dataInizioFR
And param dataA = dataFineFR
When method get
Then status 200
And match response ==
"""
{
	numRisultati: <numRisultati>,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[<numRisultati>]'
}
""" 

Examples:
| operatore | numRisultati | 
| operatore_star.json | 2 |
| operatore_domini1e2.json | 2 |
| operatore_domini1.json | 1 |
| operatore_domini2.json | 1 |
| operatore_none.json | 0 |

Scenario Outline: Ricerca rendicontazioni da operatore <operatore>.

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request read('msg/<operatore>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers spidHeadersRossi
When method get
Then status 403

Examples:
| operatore |
| operatore_disabilitato.json |
| operatore_nonAuth.json |

