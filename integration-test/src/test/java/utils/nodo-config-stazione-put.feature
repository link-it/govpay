Feature: Configurazione di un dominio nel simulatore del nodo

Background: 

* def ndpsym_config_url = ndpsym_url + '/config/rs/'

Scenario: 

Given url ndpsym_config_url 
And path 'stazioni', idIntermediario, idStazione
And request stazioneNdpSymPut
When method put
Then assert responseStatus == 200 || responseStatus == 201