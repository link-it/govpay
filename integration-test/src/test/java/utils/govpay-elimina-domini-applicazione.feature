Feature: Aggiorna applicazione eliminandone i domini associati

Scenario: 

* def codApp = idA2A
Given url backofficeBaseurl
And path 'applicazioni', codApp
And headers gpAdminBasicAutenticationHeader
When method get
Then assert responseStatus == 200

* def newApp = response
* set newApp.domini = []
* set newApp.tipiPendenza = []
* remove newApp.idA2A
* remove newApp.password

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request newApp
When method put
Then assert responseStatus == 200 || responseStatus == 201