Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario Outline: Lettura dettaglio pendenza con dati allegati

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_multivoce.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

* set pendenzaPut.datiAllegati = <datiAllegati>

* def applicazione = read('msg/applicazione_auth.json')

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def dataPutStart = getDateTime()

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def dataPutEnd = getDateTime()

Given url pagamentoBaseurl
And path '/pendenze'
And param idA2A = idA2A 
And param dataDa = dataPutStart 
And param dataA = dataPutEnd
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].datiAllegati == <datiAllegati>

Given url pagamentoBaseurl
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


