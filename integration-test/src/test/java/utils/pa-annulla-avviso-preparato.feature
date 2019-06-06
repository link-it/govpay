Feature: Predisposizione sul verticale di un pagamento dovuto

Background:

* def pendenza = pendenzaPut
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso

Scenario: 

Given url ente_api_url
And path '/v1/avvisi', idDominio, iuv
When method delete
Then status 200

Scenario: 

Given url ente_api_url
And path '/v2/avvisi', idDominio, numeroAvviso
When method delete
Then status 200

Scenario: 

Given url ente_api_url
And path '/v1/pendenze', idDominio, idPendenza
When method delete
Then status 200