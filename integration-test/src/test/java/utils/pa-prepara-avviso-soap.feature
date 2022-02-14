Feature: Predisposizione sul verticale con API-SOAP di un pagamento dovuto

Background:

* def pendenza = pendenzaPut
# * set pendenza.idA2A = idA2A
# * set pendenza.idPendenza = idPendenza
# * set pendenza.numeroAvviso = numeroAvviso
# * set pendenza.stato = 'NON_ESEGUITA'

Scenario: 

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

Scenario: 

Given url ente_api_url
And path '/v1/avvisi', idDominio, iuv
And request pendenza
When method post
Then status 200