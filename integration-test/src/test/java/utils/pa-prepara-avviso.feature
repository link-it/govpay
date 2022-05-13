Feature: Predisposizione sul verticale di un pagamento dovuto

Background:

Scenario: 

* def pendenza = pendenzaPut
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'

Given url ente_api_url
And path '/v1/avvisi', idDominio, iuv
And request pendenza
When method post
Then status 200

Scenario: 

* def pendenzaV2 = pendenzaPut
* set pendenzaV2.idA2A = idA2A
* set pendenzaV2.idPendenza = idPendenza
* set pendenzaV2.numeroAvviso = numeroAvviso
* remove pendenzaV2.stato

* def pendenzaVerificataV2 = 
"""
{
	"stato" : null,
	"pendenza" : null
}
"""

* set pendenzaVerificataV2.pendenza = pendenzaV2
* set pendenzaVerificataV2.stato = 'NON_ESEGUITA'

Given url ente_api_url
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenzaVerificataV2
When method post
Then status 200

Scenario: 

* def pendenza = pendenzaPut
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200