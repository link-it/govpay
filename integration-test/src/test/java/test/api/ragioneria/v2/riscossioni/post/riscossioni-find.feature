Feature: Ricerca delle riscossioni

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-v2.feature')

Scenario Outline: Ricerca riscossioni da applicazione <applicazione>.

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path 'riscossioni'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
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
| applicazione_star.json | 10 |
| applicazione_dominio1.json | 4 |
| applicazione_dominio2.json | 6 |
| applicazione_nonAuthDominio.json | 0 |

Scenario Outline: Verifico che la find restituisca tutti e sole le riconciliazioni caricate dal verticale <applicazione>.

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riscossioni'
And headers idA2ABasicAutenticationHeader
When method get
Then status 403

Examples:
| applicazione |
| applicazione_disabilitato.json |
| applicazione_nonAuthServizio.json |
