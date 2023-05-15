Feature: Simulazione login alla console gui. 

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Acquisizione del profilo autenticato basic

* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'form'})

* configure cookies = null

Given url backofficeBaseurl
And path '/profilo'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200

# Karate mette sempre il cookie quindi usiamo un loop di curl
# for i in {1..10}; do curl -s -k 'GET' -H 'Authorization: Basic Z3BhZG1pbjpQYXNzd29yZDEh' 'http://localhost:8080/govpay/backend/api/backoffice/rs/form/v1/profilo'; done