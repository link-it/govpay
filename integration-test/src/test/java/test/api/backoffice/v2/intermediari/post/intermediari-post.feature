Feature: Censimento intermediari (console-api v2)

# Corrispettivo v2 della parte di creazione di
# test/api/backoffice/v1/intermediari/put/intermediari-put.feature e
# intermediari-put-sintassi.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idIntermediario = '99999999998'
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def ripulisci =
"""
function(db, idIntermediario) {
  // Le stazioni referenziano l'intermediario, quindi vanno prima.
  db.update("DELETE FROM stazioni WHERE id_intermediario IN (SELECT id FROM intermediari WHERE cod_intermediario = '" + idIntermediario + "')");
  db.update("DELETE FROM intermediari WHERE cod_intermediario = '" + idIntermediario + "'");
}
"""

Scenario: Aggiunta di un intermediario

* eval ripulisci(db, idIntermediario)

Given url consoleBaseurl
And path 'intermediari'
And headers basicAutenticationHeader
And request { idIntermediario: '#(idIntermediario)', denominazione: 'Intermediario di prova v2', principalPagoPa: 'ndpsym', abilitato: true }
When method post
Then status 201
And match responseHeaders['Location'][0] == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'
And match response.idIntermediario == idIntermediario
And match response.denominazione == 'Intermediario di prova v2'
And match response.principalPagoPa == 'ndpsym'
And match response.abilitato == true

Scenario: Aggiunta di un intermediario gia' censito

* def creaIntermediario = call read('classpath:utils/api/v2/backoffice/crea-intermediario.feature') { idIntermediario: '#(idIntermediario)' }

Given url consoleBaseurl
And path 'intermediari'
And headers basicAutenticationHeader
And request { idIntermediario: '#(idIntermediario)', denominazione: 'x', principalPagoPa: 'ndpsym', abilitato: true }
When method post
Then status 409

Scenario Outline: Aggiunta di un intermediario senza il campo obbligatorio <campo>

Given url consoleBaseurl
And path 'intermediari'
And headers basicAutenticationHeader
And request <body>
When method post
Then status 400

Examples:
| campo | body |
| idIntermediario | { denominazione: 'x', principalPagoPa: 'p' } |
| denominazione | { idIntermediario: '99999999997', principalPagoPa: 'p' } |

Scenario Outline: Sintassi errata nel campo idIntermediario (<caso>)

# Lo schema ammette lettere, cifre, trattino e trattino basso, da 1 a 35
# caratteri. Non e' il codice fiscale: cifre in numero qualsiasi vanno bene.

Given url consoleBaseurl
And path 'intermediari'
And headers basicAutenticationHeader
And request { idIntermediario: '<valore>', denominazione: 'x', principalPagoPa: 'p', abilitato: true }
When method post
Then status 400

Examples:
| caso | valore |
| con spazi | inter mediario |
| con caratteri non ammessi | intermediario! |
| vuoto |  |

Scenario: Aggiunta senza autenticazione

Given url consoleBaseurl
And path 'intermediari'
And request { idIntermediario: '#(idIntermediario)', denominazione: 'x', principalPagoPa: 'p', abilitato: true }
When method post
Then status 401
