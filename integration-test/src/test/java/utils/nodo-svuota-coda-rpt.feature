Feature: Eliminazione delle RPT in coda sul nodo

Background: 

* def ndpsym_manutenzione_url = ndpsym_url + '/pagopa/rs/dars/manutenzione/'

Scenario: 

Given url ndpsym_manutenzione_url 
And path 'trash'
When method get
Then assert responseStatus == 200