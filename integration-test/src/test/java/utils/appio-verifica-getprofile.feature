Feature: Controllo invocazione AppIO

Background: 

* configure retry = { count: 30, interval: 5000 }

Scenario: 

Given url appio_api_url
And path 'checkGetProfile', codiceFiscaleDebitore
And retry until responseStatus == 200 
When method get
