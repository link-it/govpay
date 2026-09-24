Feature: Acquisizione del profilo (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/profilo/get/get-profilo.feature,
# limitato ai casi che la v2 puo' avere.
#
# La v1 esponeva /profilo su piu' catene di autenticazione, una per modalita',
# e la feature le percorreva tutte: public, spid, basic, apikey. console-api ha
# un solo endpoint e la modalita' la determina la richiesta, quindi qui restano
# i due casi che si possono esercitare con autenticazione basic senza sessione:
# autenticato e non autenticato. Il profilo SPID e il profilo apikey non hanno
# corrispettivo in questa forma.
#
# L'altra differenza: in v1 la richiesta anonima dava 403, in v2 da' 401, perche'
# console-api distingue l'assenza di credenziali dal difetto di autorizzazione.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Acquisizione del profilo autenticato basic

Given url consoleBaseurl
And path 'profilo'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.principal == govpay_backoffice_user
And match response.autenticazione == 'BASIC'
And match response.nome == '#notnull'
And match response.acl == '#[]'
And match response.domini == '#[]'
And match response.tipiPendenza == '#[]'
And match response.ruoli == '#[]'

Scenario: Il profilo dell'amministratore vede tutti i domini e tutti i tipi pendenza

Given url consoleBaseurl
And path 'profilo'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.domini[*].idDominio contains '*'
And match response.tipiPendenza[*].idTipoPendenza contains '*'

Scenario: Acquisizione del profilo in forma anonima

Given url consoleBaseurl
And path 'profilo'
When method get
Then status 401

Scenario: Acquisizione del profilo con credenziali errate

* def credenzialiErrate = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: 'password-sbagliata' } )

Given url consoleBaseurl
And path 'profilo'
And headers credenzialiErrate
When method get
Then status 401
