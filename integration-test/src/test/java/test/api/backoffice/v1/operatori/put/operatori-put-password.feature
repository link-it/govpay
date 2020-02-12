Feature: Censimento Operatori con definizione della password per l'autenticazione HTTP-Basic

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  password: '#(pwdOperatore)',
  domini: ['#(idDominio)'],
  tipiPendenza: ['#(codLibero)'],
  acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true
}
"""

* def usernameOperatore = 'MarioRossi'

Scenario: Aggiunta di un operatore, test login, modifica dell'operatore (non del campo password ) e login con le stesse credenziali

Given url backofficeBaseurl
And path 'operatori', usernameOperatore
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200

* def operatoreAutenticationHeader = getBasicAuthenticationHeader( { username: usernameOperatore , password: pwdOperatore } )

Given url backofficeBaseurl
And path 'profilo'
And headers operatoreAutenticationHeader
When method get
Then assert responseStatus == 200

# modifica operatore

* set operatore.ragioneSociale = 'Mario Rossi II'

Given url backofficeBaseurl
And path 'operatori', usernameOperatore
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response.ragioneSociale == 'Mario Rossi II'

* def operatoreAutenticationHeader = getBasicAuthenticationHeader( { username: usernameOperatore , password: pwdOperatore } )

Given url backofficeBaseurl
And path 'profilo'
And headers operatoreAutenticationHeader
When method get
Then assert responseStatus == 200


Scenario: Aggiunta di un operatore, test login, modifica dell'operatore (anche campo password ) e login con le nuove credenziali

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'operatori', usernameOperatore
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200

* def operatoreAutenticationHeader = getBasicAuthenticationHeader( { username: usernameOperatore , password: pwdOperatore } )

Given url backofficeBaseurl
And path 'profilo'
And headers operatoreAutenticationHeader
When method get
Then assert responseStatus == 200

# modifica operatore

* def nuovaPasswordOperatore = 'Password2'
* set operatore.password = nuovaPasswordOperatore

Given url backofficeBaseurl
And path 'operatori', usernameOperatore
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'profilo'
And headers operatoreAutenticationHeader
When method get
Then assert responseStatus == 401

* def nuovoOperatoreBasicAutenticationHeader = getBasicAuthenticationHeader( { username: usernameOperatore, password: nuovaPasswordOperatore } )

Given url backofficeBaseurl
And path 'profilo'
And headers nuovoOperatoreBasicAutenticationHeader
When method get
Then assert responseStatus == 200



