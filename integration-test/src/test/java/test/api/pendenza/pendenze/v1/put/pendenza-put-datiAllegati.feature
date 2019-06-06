Feature: Caricamento pendenza multivoce

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})

Scenario Outline: Caricamento pendenza con dati allegati

* set pendenzaPut.datiAllegati = <datiAllegati>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.datiAllegati == <datiAllegati>

Examples:
| datiAllegati |
| "datoAllegato" |
| 10 |
| [ "datoAllegato1" , "datoAllegato2" ] |
| [ 10 , 20 ] |
| [ datoAllegato1: 10 , datoAllegato2: 20 ] |

Scenario Outline: Caricamento pendenza con dati allegati nella voce

* set pendenzaPut.voci[0].datiAllegati = <datiAllegati>
* set pendenzaPut.voci[1].datiAllegati = <datiAllegati>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.voci[0].datiAllegati == <datiAllegati>
And match pendenzaPut.voci[1].datiAllegati == <datiAllegati>

Examples:
| datiAllegati |
| "datoAllegato" |
| 10 |
| [ "datoAllegato1" , "datoAllegato2" ] |
| [ 10 , 20 ] |
| [ datoAllegato1: 10 , datoAllegato2: 20 ] |