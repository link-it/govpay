Feature: Censimento applicazioni (console-api v2)

# Corrispettivo v2 della parte di creazione di
# test/api/backoffice/v1/applicazioni/put/applicazioni-put.feature,
# applicazioni-put-sintassi.feature e applicazioni-put-semantica.feature.
#
# Come per gli operatori, la password non e' piu' un campo del corpo: si
# imposta con PUT /applicazioni/{idA2A}/password, coperta da
# put/applicazioni-password.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idA2A = 'APP-TEST-V2'
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def ripulisci =
"""
function(db, idA2A) {
  // L'ordine conta: applicazioni referenzia utenze, quindi l'utenza si elimina
  // dopo. Il principal dell'applicazione di prova coincide con il suo idA2A,
  // che e' come sono censite le altre applicazioni della testsuite.
  db.update("DELETE FROM acl WHERE id_utenza IN (SELECT id_utenza FROM applicazioni WHERE cod_applicazione = '" + idA2A + "')");
  db.update("DELETE FROM utenze_domini WHERE id_utenza IN (SELECT id_utenza FROM applicazioni WHERE cod_applicazione = '" + idA2A + "')");
  db.update("DELETE FROM utenze_tipo_vers WHERE id_utenza IN (SELECT id_utenza FROM applicazioni WHERE cod_applicazione = '" + idA2A + "')");
  db.update("DELETE FROM applicazioni WHERE cod_applicazione = '" + idA2A + "'");
  db.update("DELETE FROM utenze WHERE principal = '" + idA2A + "'");
}
"""

Scenario: Aggiunta di una applicazione

* eval ripulisci(db, idA2A)

Given url consoleBaseurl
And path 'applicazioni'
And headers basicAutenticationHeader
And request { idA2A: '#(idA2A)', principal: '#(idA2A)', abilitato: true, acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] } ] }
When method post
Then status 201
And match responseHeaders['Location'][0] == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'
And match response.idA2A == idA2A
And match response.principal == idA2A
And match response.abilitato == true
# La v2 espone il connettore di integrazione come sotto-risorsa, con un link
And match response._links.connettoreIntegrazione.href == '#notnull'

Scenario: Aggiunta di una applicazione gia' censita

* def creaApplicazione = call read('classpath:utils/api/v2/backoffice/crea-applicazione.feature') { idA2A: '#(idA2A)' }

Given url consoleBaseurl
And path 'applicazioni'
And headers basicAutenticationHeader
And request { idA2A: '#(idA2A)', principal: '#(idA2A)', abilitato: true }
When method post
Then status 409

Scenario Outline: Aggiunta di una applicazione senza il campo obbligatorio <campo>

Given url consoleBaseurl
And path 'applicazioni'
And headers basicAutenticationHeader
And request <body>
When method post
Then status 400

Examples:
| campo | body |
| idA2A | { principal: 'x', abilitato: true } |
| principal | { idA2A: 'APP-INCOMPLETA-V2', abilitato: true } |

Scenario Outline: Sintassi errata nel campo idA2A (<caso>)

# Lo schema vuole lettere, cifre, trattino e trattino basso, da 1 a 35.

Given url consoleBaseurl
And path 'applicazioni'
And headers basicAutenticationHeader
And request { idA2A: '<valore>', principal: 'x', abilitato: true }
When method post
Then status 400

Examples:
| caso | valore |
| con spazi | app test |
| con caratteri non ammessi | app!test |
| vuoto |  |

Scenario: Aggiunta di una applicazione senza autenticazione

Given url consoleBaseurl
And path 'applicazioni'
And request { idA2A: '#(idA2A)', principal: 'x', abilitato: true }
When method post
Then status 401
