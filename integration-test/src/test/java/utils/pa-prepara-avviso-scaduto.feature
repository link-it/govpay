Feature: Predisposizione sul verticale di un pagamento dovuto

Background:

* def pendenza = pendenzaPut
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'SCADUTA'
Scenario: 

Given url ente_api_url
And path '/v1/avvisi', idDominio, iuv
And request pendenza
When method post
Then status 200

Scenario: 

Given url ente_api_url
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenza
When method post
Then status 200

Scenario: 

Given url ente_api_url
And path '/v1/pendenze', idDominio, idPendenza
And request pendenza
When method post
Then status 200