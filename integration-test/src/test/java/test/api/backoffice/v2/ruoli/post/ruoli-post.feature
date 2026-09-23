Feature: Censimento ruoli (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/ruoli/put/ruoli-put.feature.
#
# La v1 censiva con PUT, che era un upsert: la stessa chiamata creava o
# aggiornava, e la feature era rieseguibile sullo stesso database. La v2 separa
# creazione e modifica, e POST su un ruolo che esiste gia' risponde 409.
#
# Le API v2 non hanno una DELETE sui ruoli, quindi la feature non puo'
# riportarsi allo stato iniziale passando dalle API: lo fa dal database, come
# gia' altre feature della testsuite. Senza, la prima esecuzione darebbe 201 e
# le successive 409, e non si potrebbe verificare ne' l'una ne' l'altra cosa.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idRuolo = 'RuoloTestV2'
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def ruolo =
"""
{
  idRuolo: '#(idRuolo)',
  acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ]
}
"""

Scenario: Aggiunta di un ruolo

* eval db.update("DELETE FROM acl WHERE ruolo = '" + idRuolo + "'")

Given url consoleBaseurl
And path 'ruoli'
And headers basicAutenticationHeader
And request ruolo
When method post
Then status 201
And match responseHeaders['Location'][0] == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'
And match response.idRuolo == idRuolo
And match response.acl[0].servizio == 'Pagamenti'

Scenario: Aggiunta di un ruolo gia' censito

* def creaRuolo = call read('classpath:utils/api/v2/backoffice/crea-ruolo.feature') { idRuolo: '#(idRuolo)' }

Given url consoleBaseurl
And path 'ruoli'
And headers basicAutenticationHeader
And request ruolo
When method post
Then status 409
And match response.status == 409
And match response.title == 'Conflict'

Scenario: Aggiunta di un ruolo senza acl

Given url consoleBaseurl
And path 'ruoli'
And headers basicAutenticationHeader
And request { idRuolo: 'RuoloSenzaAcl' }
When method post
Then status 400

Scenario: Aggiunta di un ruolo senza autenticazione

Given url consoleBaseurl
And path 'ruoli'
And request ruolo
When method post
Then status 401
