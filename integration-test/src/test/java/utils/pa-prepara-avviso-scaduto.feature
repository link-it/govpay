Feature: Predisposizione sul verticale di un pagamento dovuto

Background:

* def descrizioneStato = pendenzaPut.descrizioneStato

Scenario: 

* def pendenza = pendenzaPut
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'SCADUTA'
* set pendenza.descrizioneStato = descrizioneStato

Given url ente_api_url
And path '/v1/avvisi', idDominio, iuv
And request pendenza
When method post
Then status 200

Scenario: 

* def pendenza = pendenzaPut
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'SCADUTA'
* set pendenza.descrizioneStato = descrizioneStato

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

Scenario: 

* def pendenzaVerificataV2 = 
"""
{
	"stato" : null,
	"pendenza" : null
}
"""

* def pendenzaV2 = pendenzaPut
* set pendenzaV2.idA2A = idA2A
* set pendenzaV2.idPendenza = idPendenza
* set pendenzaV2.numeroAvviso = numeroAvviso

* set pendenzaVerificataV2.descrizioneStato = descrizioneStato
* set pendenzaVerificataV2.pendenza = pendenzaV2
* set pendenzaVerificataV2.stato = 'SCADUTA'

* remove pendenzaVerificataV2.pendenza.stato
* remove pendenzaVerificataV2.pendenza.descrizioneStato

Given url ente_api_url
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenzaVerificataV2
When method post
Then status 200



