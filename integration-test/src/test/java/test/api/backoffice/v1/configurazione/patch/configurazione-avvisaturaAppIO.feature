Feature: Configurazione Avvisatura via AppIO

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def patchRequest = 
"""
[
  {
    "op": "REPLACE",
    "path": "/avvisaturaAppIO",
    "value": {
			"promemoriaAvviso": {
				"tipo": "freemarker",
				"oggetto": "..base64 freemarker..",
				"messaggio": "..base64 freemarker.."
			},
			"promemoriaRicevuta": {
				"tipo": "freemarker",
				"oggetto": "..base64 freemarker..",
				"messaggio": "..base64 freemarker..",
				"soloEseguiti": true
			},
			"promemoriaScadenza": {
				"tipo": "freemarker",
				"oggetto": "..base64 freemarker..",
				"messaggio": "..base64 freemarker..",
				"preavviso": 0
			}
		}
	}
]
"""

Scenario Outline: Modifica della configurazione avvisaturaAppIO (<field>)


* set patchRequest[0].value.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.avvisaturaAppIO.<field> == checkValue

Examples:
| field | value | 
| promemoriaAvviso.tipo | "freemarker" |
| promemoriaAvviso.oggetto | "aaa" |
| promemoriaAvviso.messaggio | "aaa" |
| promemoriaRicevuta.tipo | "freemarker" |
| promemoriaRicevuta.oggetto | "aaa" |
| promemoriaRicevuta.messaggio | "aaa" |
| promemoriaRicevuta.soloEseguiti | false |
| promemoriaRicevuta.soloEseguiti | true |
| promemoriaScadenza.tipo | "freemarker" |
| promemoriaScadenza.oggetto | "aaa" |
| promemoriaScadenza.messaggio | "aaa" |
| promemoriaScadenza.preavviso | 10 |

Scenario Outline: Errore sintassi della configurazione avvisaturaAppIO (<field>)

* set patchRequest[0].value.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '<fieldName>'

Examples:
| field | fieldName | value | 
| promemoriaAvviso | promemoriaAvviso | null |
| promemoriaAvviso.tipo | tipo | null |
| promemoriaAvviso.tipo | tipo | "aaa" |
| promemoriaAvviso.oggetto | oggetto | null |
| promemoriaAvviso.messaggio | messaggio | null |
| promemoriaRicevuta | promemoriaRicevuta | null |
| promemoriaRicevuta.tipo | tipo | null |
| promemoriaRicevuta.tipo | tipo | "aaa" |
| promemoriaRicevuta.oggetto | oggetto | null |
| promemoriaRicevuta.messaggio | messaggio | null |
| promemoriaRicevuta.soloEseguiti | soloEseguiti | null |
| promemoriaRicevuta.soloEseguiti | soloEseguiti | "aaa" |
| promemoriaScadenza | promemoriaScadenza | null |
| promemoriaScadenza.tipo | tipo | null |
| promemoriaScadenza.tipo | tipo | "aaa" |
| promemoriaScadenza.oggetto | oggetto | null |
| promemoriaScadenza.messaggio | messaggio | null |
| promemoriaScadenza.preavviso | preavviso | null |
| promemoriaScadenza.preavviso | preavviso | "aaa" |
| promemoriaScadenza.preavviso | preavviso | -1 |

