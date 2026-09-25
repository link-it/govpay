Feature: Censimento domini (console-api v2)

# Corrispettivo v2 della parte di creazione di
# test/api/backoffice/v1/domini/put/dominio-put.feature,
# domini-put-sintassi.feature, domini-put-semantica.feature e
# domini-put-intermediato.feature.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '99999999997'
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def ripulisci =
"""
function(db, idDominio) {
  // Le sotto-risorse referenziano il dominio: vanno prima.
  db.update("DELETE FROM uo WHERE id_dominio IN (SELECT id FROM domini WHERE cod_dominio = '" + idDominio + "')");
  db.update("DELETE FROM iban_accredito WHERE id_dominio IN (SELECT id FROM domini WHERE cod_dominio = '" + idDominio + "')");
  db.update("DELETE FROM tipi_vers_domini WHERE id_dominio IN (SELECT id FROM domini WHERE cod_dominio = '" + idDominio + "')");
  db.update("DELETE FROM tributi WHERE id_dominio IN (SELECT id FROM domini WHERE cod_dominio = '" + idDominio + "')");
  db.update("DELETE FROM domini WHERE cod_dominio = '" + idDominio + "'");
}
"""

Scenario: Aggiunta di un dominio

* eval ripulisci(db, idDominio)

Given url consoleBaseurl
And path 'domini'
And headers basicAutenticationHeader
And request { idDominio: '#(idDominio)', ragioneSociale: 'Dominio di prova v2', abilitato: true, scaricaFr: false, idStazione: '11111111113_01', gln: '1234567890123' }
When method post
Then status 201
And match responseHeaders['Location'][0] == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'
And match response.idDominio == idDominio
And match response.ragioneSociale == 'Dominio di prova v2'
# Il dominio con una stazione e' intermediato, e il server lo dichiara
And match response.intermediato == true
And match response.riferimentoIntermediario == '#notnull'

Scenario: Aggiunta di un dominio gia' censito

* def creaDominio = call read('classpath:utils/api/v2/backoffice/crea-dominio.feature') { idDominio: '#(idDominio)' }

Given url consoleBaseurl
And path 'domini'
And headers basicAutenticationHeader
And request { idDominio: '#(idDominio)', ragioneSociale: 'x', abilitato: true, scaricaFr: false }
When method post
Then status 409

Scenario Outline: Aggiunta di un dominio senza il campo obbligatorio <campo>

Given url consoleBaseurl
And path 'domini'
And headers basicAutenticationHeader
And request <body>
When method post
Then status 400

Examples:
| campo | body |
| idDominio | { ragioneSociale: 'x', abilitato: true, scaricaFr: false } |
| ragioneSociale | { idDominio: '99999999996', abilitato: true, scaricaFr: false } |
| scaricaFr | { idDominio: '99999999996', ragioneSociale: 'x', abilitato: true } |

Scenario: Aggiunta di un dominio intermediato senza gln

# Regola di dominio e non di schema: il server la segnala con 422 e non con 400.

Given url consoleBaseurl
And path 'domini'
And headers basicAutenticationHeader
And request { idDominio: '99999999996', ragioneSociale: 'x', abilitato: true, scaricaFr: false, idStazione: '11111111113_01' }
When method post
Then status 422
And match response.detail contains 'gln'

Scenario: Aggiunta senza autenticazione

Given url consoleBaseurl
And path 'domini'
And request { idDominio: '#(idDominio)', ragioneSociale: 'x', abilitato: true, scaricaFr: false }
When method post
Then status 401
