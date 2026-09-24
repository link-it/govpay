Feature: Censimento operatori (console-api v2)

# Corrispettivo v2 della parte di creazione di
# test/api/backoffice/v1/operatori/put/operatori-put.feature,
# operatori-put-sintassi.feature e operatori-put-semantica.feature.
#
# Differenza di rilievo: in v1 la password dell'operatore era un campo del corpo
# della PUT, in v2 la creazione non la contiene affatto e si imposta con
# PUT /operatori/{principal}/password. E' il motivo per cui le feature
# operatori-put-password e operatori-patch-password della v1 qui diventano una
# feature sola, put/operatori-password.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def principal = 'OPERATORE_TEST_V2'
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def ripulisci =
"""
function(db, principal) {
  db.update("DELETE FROM utenze_domini WHERE id_utenza IN (SELECT id FROM utenze WHERE principal = '" + principal + "')");
  db.update("DELETE FROM utenze_tipo_vers WHERE id_utenza IN (SELECT id FROM utenze WHERE principal = '" + principal + "')");
  db.update("DELETE FROM acl WHERE id_utenza IN (SELECT id FROM utenze WHERE principal = '" + principal + "')");
  db.update("DELETE FROM operatori WHERE id_utenza IN (SELECT id FROM utenze WHERE principal = '" + principal + "')");
  db.update("DELETE FROM utenze WHERE principal = '" + principal + "'");
}
"""

Scenario: Aggiunta di un operatore

* eval ripulisci(db, principal)

Given url consoleBaseurl
And path 'operatori'
And headers basicAutenticationHeader
And request { principal: '#(principal)', nome: 'Operatore di prova v2', abilitato: true, ruoli: [ { id: 'Amministratore' } ] }
When method post
Then status 201
And match responseHeaders['Location'][0] == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'
And match response.principal == principal
And match response.nome == 'Operatore di prova v2'
And match response.abilitato == true
And match response.ruoli[*].id contains 'Amministratore'

Scenario: Aggiunta di un operatore gia' censito

* def creaOperatore = call read('classpath:utils/api/v2/backoffice/crea-operatore.feature') { principal: '#(principal)' }

Given url consoleBaseurl
And path 'operatori'
And headers basicAutenticationHeader
And request { principal: '#(principal)', nome: 'Operatore di prova v2', abilitato: true }
When method post
Then status 409

Scenario Outline: Aggiunta di un operatore senza il campo obbligatorio <campo>

Given url consoleBaseurl
And path 'operatori'
And headers basicAutenticationHeader
And request <body>
When method post
Then status 400

Examples:
| campo | body |
| principal | { nome: 'x', abilitato: true } |
| nome | { principal: 'OPERATORE_INCOMPLETO_V2', abilitato: true } |

Scenario: Aggiunta di un operatore con nome piu' lungo del consentito

# Lo schema limita nome a 35 caratteri.

Given url consoleBaseurl
And path 'operatori'
And headers basicAutenticationHeader
And request { principal: 'OPERATORE_NOME_LUNGO_V2', nome: 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', abilitato: true }
When method post
Then status 400

Scenario: Aggiunta di un operatore senza autenticazione

Given url consoleBaseurl
And path 'operatori'
And request { principal: '#(principal)', nome: 'x', abilitato: true }
When method post
Then status 401
