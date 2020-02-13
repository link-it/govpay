Feature: Ricerca e lettura di flussi di rendicontazione con autorizzazione per UO 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-rendicontazioni.feature')
* def rendicontazioneSchema = read('msg/rendicontazione.json')

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
And param dataDa = dataInizio
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
| applicazione_star.json | 5 | 
| applicazione_dominio1e2.json | 5 |
| applicazione_dominio1.json | 3 |
| applicazione_dominio2.json | 2 |
| applicazione_none.json | 0 |
| applicazione_dominio1e2_ec.json | 2 |
| applicazione_dominio1_ec.json | 1 |
| applicazione_dominio1_uo1.json | 1 |
| applicazione_dominio1_uo1e2.json | 2 |


Scenario Outline: Ricerca rendicontazioni da operatore <operatore>.

* def operatore = read('msg/<operatore>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path 'flussiRendicontazione'
And headers spidHeadersRossi
And param dataDa = dataInizio
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
| operatore_star.json | 5 | 
| operatore_domini1e2.json | 5 |
| operatore_domini1.json | 3 |
| operatore_domini2.json | 2 |
| operatore_none.json | 0 |
| operatore_domini1e2_ec.json | 2 |
| operatore_domini1_ec.json | 1 |
| operatore_domini1_uo1.json | 1 |
| operatore_domini1_uo1e2.json | 2 |

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] del flusso di rendicontazione [<idFlusso>]

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
And path '/flussiRendicontazione', <idFlusso>
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| applicazione | idFlusso | httpStatus | risposta |
| applicazione_star.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idflusso_dom2_uo | 200 | flussoRendicontazioni.json |
| applicazione_star.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2.json | idflusso_dom2_uo | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1.json | idflusso_dom2_uo | 403 | errore_auth.json |
| applicazione_dominio1.json | idflusso_dom2 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom1 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom1_uo | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| applicazione_dominio2.json | idflusso_dom2_uo | 200 | flussoRendicontazioni.json |
| applicazione_dominio2.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| applicazione_none.json | idflusso_dom1 | 403 | errore_auth.json |
| applicazione_none.json | idflusso_dom1_uo | 403 | errore_auth.json |
| applicazione_none.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| applicazione_none.json | idflusso_dom2_uo | 403 | errore_auth.json |
| applicazione_none.json | idflusso_dom2 | 403 | errore_auth.json |
| applicazione_dominio1e2_ec.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1e2_ec.json | idflusso_dom1_uo | 403 | errore_auth.json |
| applicazione_dominio1e2_ec.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| applicazione_dominio1e2_ec.json | idflusso_dom2_uo | 403 | errore_auth.json |
| applicazione_dominio1e2_ec.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1_ec.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1_ec.json | idflusso_dom1_uo | 403 | errore_auth.json |
| applicazione_dominio1_ec.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| applicazione_dominio1_ec.json | idflusso_dom2_uo | 403 | errore_auth.json |
| applicazione_dominio1_ec.json | idflusso_dom2 | 403 | errore_auth.json |
| applicazione_dominio1_uo1.json | idflusso_dom1 | 403 | errore_auth.json |
| applicazione_dominio1_uo1.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| applicazione_dominio1_uo1.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| applicazione_dominio1_uo1.json | idflusso_dom2_uo | 403 | errore_auth.json |
| applicazione_dominio1_uo1.json | idflusso_dom2 | 403 | errore_auth.json |
| applicazione_dominio1_uo1e2.json | idflusso_dom1 | 403 | errore_auth.json |
| applicazione_dominio1_uo1e2.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| applicazione_dominio1_uo1e2.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| applicazione_dominio1_uo1e2.json | idflusso_dom2_uo | 403 | errore_auth.json |
| applicazione_dominio1_uo1e2.json | idflusso_dom2 | 403 | errore_auth.json |

Scenario Outline: Lettura dettaglio operatore [<operatore>] del flusso di rendicontazione [<idFlusso>]

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
And path '/flussiRendicontazione', <idFlusso>
And headers spidHeadersRossi
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')

Examples:
| operatore | idFlusso | httpStatus | risposta |
| operatore_star.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| operatore_star.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| operatore_star.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| operatore_star.json | idflusso_dom2_uo | 200 | flussoRendicontazioni.json |
| operatore_star.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| operatore_domini1e2.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| operatore_domini1e2.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| operatore_domini1e2.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| operatore_domini1e2.json | idflusso_dom2_uo | 200 | flussoRendicontazioni.json |
| operatore_domini1e2.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| operatore_domini1.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| operatore_domini1.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| operatore_domini1.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| operatore_domini1.json | idflusso_dom2_uo | 403 | errore_auth.json |
| operatore_domini1.json | idflusso_dom2 | 403 | errore_auth.json |
| operatore_domini2.json | idflusso_dom1 | 403 | errore_auth.json |
| operatore_domini2.json | idflusso_dom1_uo | 403 | errore_auth.json |
| operatore_domini2.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| operatore_domini2.json | idflusso_dom2_uo | 200 | flussoRendicontazioni.json |
| operatore_domini2.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| operatore_none.json | idflusso_dom1 | 403 | errore_auth.json |
| operatore_none.json | idflusso_dom1_uo | 403 | errore_auth.json |
| operatore_none.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| operatore_none.json | idflusso_dom2_uo | 403 | errore_auth.json |
| operatore_none.json | idflusso_dom2 | 403 | errore_auth.json |
| operatore_domini1e2_ec.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| operatore_domini1e2_ec.json | idflusso_dom1_uo | 403 | errore_auth.json |
| operatore_domini1e2_ec.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| operatore_domini1e2_ec.json | idflusso_dom2_uo | 403 | errore_auth.json |
| operatore_domini1e2_ec.json | idflusso_dom2 | 200 | flussoRendicontazioni.json |
| operatore_domini1_ec.json | idflusso_dom1 | 200 | flussoRendicontazioni.json |
| operatore_domini1_ec.json | idflusso_dom1_uo | 403 | errore_auth.json |
| operatore_domini1_ec.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| operatore_domini1_ec.json | idflusso_dom2_uo | 403 | errore_auth.json |
| operatore_domini1_ec.json | idflusso_dom2 | 403 | errore_auth.json |
| operatore_domini1_uo1.json | idflusso_dom1 | 403 | errore_auth.json |
| operatore_domini1_uo1.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| operatore_domini1_uo1.json | idflusso_dom1_uo2 | 403 | errore_auth.json |
| operatore_domini1_uo1.json | idflusso_dom2_uo | 403 | errore_auth.json |
| operatore_domini1_uo1.json | idflusso_dom2 | 403 | errore_auth.json |
| operatore_domini1_uo1e2.json | idflusso_dom1 | 403 | errore_auth.json |
| operatore_domini1_uo1e2.json | idflusso_dom1_uo | 200 | flussoRendicontazioni.json |
| operatore_domini1_uo1e2.json | idflusso_dom1_uo2 | 200 | flussoRendicontazioni.json |
| operatore_domini1_uo1e2.json | idflusso_dom2_uo | 403 | errore_auth.json |
| operatore_domini1_uo1e2.json | idflusso_dom2 | 403 | errore_auth.json |


