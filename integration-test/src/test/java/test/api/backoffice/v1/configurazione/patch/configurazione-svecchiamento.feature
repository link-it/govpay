Feature: Configurazione svecchiamento

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/svecchiamento",
      "value": {
				"stampeAvvisi" : null,
				"stampeRicevute" : null,
				"tracciatiPendenzeScartati" : null,
				"tracciatiPendenzeCompletati" : null,
				"pendenzeScadute" : null,
				"pendenzePagate" : null,
				"pendenzeAnnullate" : null,
				"pendenzeDaPagareSenzaScadenza" : null,
				"pagamentiEseguiti" : null,
				"pagamentiNonEseguiti" : null,
				"pagamentiFalliti" : null,
				"rendicontazioni" : null,
				"eventi" : null,
				"notificheConsegnate" : null,
				"notificheNonConsegnate" : null
			}
   }
]
"""

Scenario Outline: Modifica della configurazione svecchiamento (<field>)

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
And match response.svecchiamento.<field> == checkValue

Examples:
| field | value | 
| stampeAvvisi | 1 |
| stampeAvvisi | null |
| stampeRicevute | 1 |
| stampeRicevute | null |
| tracciatiPendenzeScartati | 1 |
| tracciatiPendenzeScartati | null |
| tracciatiPendenzeCompletati | 1 |
| tracciatiPendenzeCompletati | null |
| pendenzeScadute | 30 |
| pendenzeScadute | null |
| pendenzePagate | 365 |
| pendenzePagate | null |
| pendenzeAnnullate | 30 |
| pendenzeAnnullate | null |
| pendenzeDaPagareSenzaScadenza | 1825 |
| pendenzeDaPagareSenzaScadenza | null |
| pagamentiEseguiti | 365 |
| pagamentiEseguiti | null |
| pagamentiNonEseguiti | 30 |
| pagamentiNonEseguiti | null |
| pagamentiFalliti | 7 |
| pagamentiFalliti | null |
| rendicontazioni | 730 |
| rendicontazioni | null |
| eventi | 365 |
| eventi | null |
| notificheConsegnate | 7 |
| notificheConsegnate | null |
| notificheNonConsegnate | 180 |
| notificheNonConsegnate | null |

Scenario Outline: Errore sintassi della configurazione svecchiamento (<field>)

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
| stampeAvvisi | stampeAvvisi | "aaa" |
| stampeAvvisi | stampeAvvisi | -1 |
| stampeRicevute | stampeRicevute | "aaa" |
| stampeRicevute | stampeRicevute | -1 |
| tracciatiPendenzeScartati | tracciatiPendenzeScartati | "aaa" |
| tracciatiPendenzeScartati | tracciatiPendenzeScartati | -1 |
| tracciatiPendenzeCompletati | tracciatiPendenzeCompletati | "aaa" |
| tracciatiPendenzeCompletati | tracciatiPendenzeCompletati | -1 |
| pendenzeScadute | pendenzeScadute | "aaa" |
| pendenzeScadute | pendenzeScadute | -1 |
| pendenzePagate | pendenzePagate | "aaa" |
| pendenzePagate | pendenzePagate | -1 |
| pendenzeAnnullate | pendenzeAnnullate | "aaa" |
| pendenzeAnnullate | pendenzeAnnullate | -1 |
| pendenzeDaPagareSenzaScadenza | pendenzeDaPagareSenzaScadenza | "aaa" |
| pendenzeDaPagareSenzaScadenza | pendenzeDaPagareSenzaScadenza | -1 |
| pagamentiEseguiti | pagamentiEseguiti | "aaa" |
| pagamentiEseguiti | pagamentiEseguiti | -1 |
| pagamentiNonEseguiti | pagamentiNonEseguiti | "aaa" |
| pagamentiNonEseguiti | pagamentiNonEseguiti | -1 |
| pagamentiFalliti | pagamentiFalliti | "aaa" |
| pagamentiFalliti | pagamentiFalliti | -1 |
| rendicontazioni | rendicontazioni | "aaa" |
| rendicontazioni | rendicontazioni | -1 |
| eventi | eventi | "aaa" |
| eventi | eventi | -1 |
| notificheConsegnate | notificheConsegnate | "aaa" |
| notificheConsegnate | notificheConsegnate | -1 |
| notificheNonConsegnate | notificheNonConsegnate | "aaa" |
| notificheNonConsegnate | notificheNonConsegnate | -1 |



