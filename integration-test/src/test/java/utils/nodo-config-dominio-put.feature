Feature: Configurazione di un dominio nel simulatore del nodo

Background: 

* def ndpsym_config_url = ndpsym_url + '/config/rs/domini/'

Scenario: 

Given url ndpsym_config_url 
And path 'idDominio', idDominio
And request dominioNdpSymPut
When method put
Then assert responseStatus == 200 || responseStatus == 201