Feature: Generazione delle rpt da nodo

Background: 

* def ndpsym_rendicontazioni_url = ndpsym_url + '/pagopa/rs/dars/rendicontazioni/'

Scenario: 

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200