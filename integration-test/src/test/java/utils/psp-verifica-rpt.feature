Feature: Verifica della rpt

Background: 

* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Scenario: 

Given url ndpsym_psp_url 
And path 'verifica' 
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
When method get
Then assert responseStatus == 200
