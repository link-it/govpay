Feature: Password di una applicazione (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/applicazioni/put/applicazioni-put-password.feature
# e applicazioni/patch/applicazioni-patch-password.feature, che in v2 sono la
# stessa cosa: la password non e' piu' un campo dell'applicazione, e' una
# sotto-risorsa con una sola operazione, PUT /applicazioni/{idA2A}/password.
#
# Come in v1, la verifica non si ferma al codice di stato: si prova ad
# autenticarsi con le credenziali nuove.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idA2A = 'APP-TEST-V2'
* def creaApplicazione = call read('classpath:utils/api/v2/backoffice/crea-applicazione.feature') { idA2A: '#(idA2A)' }

Scenario: Impostazione della password e accesso con le nuove credenziali

Given url consoleBaseurl
And path 'applicazioni', idA2A, 'password'
And headers basicAutenticationHeader
And request { nuovaPassword: 'PasswordV2test1' }
When method put
Then status 204

* def credenzialiNuove = getBasicAuthenticationHeader( { username: idA2A, password: 'PasswordV2test1' } )

Given url consoleBaseurl
And path 'profilo'
And headers credenzialiNuove
When method get
Then status 200
And match response.principal == idA2A

Scenario: Cambio della password e accesso con le credenziali precedenti

Given url consoleBaseurl
And path 'applicazioni', idA2A, 'password'
And headers basicAutenticationHeader
And request { nuovaPassword: 'PasswordV2test1' }
When method put
Then status 204

Given url consoleBaseurl
And path 'applicazioni', idA2A, 'password'
And headers basicAutenticationHeader
And request { nuovaPassword: 'PasswordV2test2' }
When method put
Then status 204

* def credenzialiVecchie = getBasicAuthenticationHeader( { username: idA2A, password: 'PasswordV2test1' } )

Given url consoleBaseurl
And path 'profilo'
And headers credenzialiVecchie
When method get
Then status 401

* def credenzialiNuove = getBasicAuthenticationHeader( { username: idA2A, password: 'PasswordV2test2' } )

Given url consoleBaseurl
And path 'profilo'
And headers credenzialiNuove
When method get
Then status 200

Scenario Outline: Password che non rispetta la policy (<caso>)

# La policy e' verificata dal server: almeno otto caratteri, una minuscola, una
# maiuscola, una cifra, niente spaziatura.

Given url consoleBaseurl
And path 'applicazioni', idA2A, 'password'
And headers basicAutenticationHeader
And request { nuovaPassword: '<valore>' }
When method put
Then status 400

Examples:
| caso | valore |
| troppo corta | Pw1a |
| senza cifre | PasswordSenzaCifre |
| senza maiuscole | password1test |
| con spazi | Password 1 test |

Scenario: Password mancante nel corpo

Given url consoleBaseurl
And path 'applicazioni', idA2A, 'password'
And headers basicAutenticationHeader
And request { }
When method put
Then status 400

Scenario: Impostazione della password di una applicazione inesistente

Given url consoleBaseurl
And path 'applicazioni', 'APP-CHE-NON-ESISTE', 'password'
And headers basicAutenticationHeader
And request { nuovaPassword: 'PasswordV2test1' }
When method put
Then status 404

Scenario: Impostazione della password senza autenticazione

Given url consoleBaseurl
And path 'applicazioni', idA2A, 'password'
And request { nuovaPassword: 'PasswordV2test1' }
When method put
Then status 401
