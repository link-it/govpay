Feature: Caricamento pendenze con allegati

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

Scenario: Caricamento di una pendenza con allegato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.allegati[0].nome = 'tipoPendenza-promemoria-oggetto-freemarker.ftl'
* set pendenzaPut.allegati[0].tipo = 'application/json'
* set pendenzaPut.allegati[0].descrizione = 'test allegato'
* set pendenzaPut.allegati[0].contenuto = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/pendenze/put/msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.allegati[0].nome == pendenzaPut.allegati[0].nome
* match response.allegati[0].tipo == pendenzaPut.allegati[0].tipo
* match response.allegati[0].descrizione == pendenzaPut.allegati[0].descrizione


Scenario: Aggiornamento di una pendenza con allegato

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.allegati[0].nome = 'tipoPendenza-promemoria-oggetto-freemarker.ftl'
* set pendenzaPut.allegati[0].tipo = 'application/json'
* set pendenzaPut.allegati[0].descrizione = 'test allegato'
* set pendenzaPut.allegati[0].contenuto = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/pendenze/put/msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.allegati[0].nome == pendenzaPut.allegati[0].nome
* match response.allegati[0].tipo == pendenzaPut.allegati[0].tipo
* match response.allegati[0].descrizione == pendenzaPut.allegati[0].descrizione

* set pendenzaPut.allegati[0].nome = 'updated-tipoPendenza-promemoria-oggetto-freemarker.ftl'

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200
And match response == responsePut

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

* match response.allegati[0].nome == pendenzaPut.allegati[0].nome

* set pendenzaPut.allegati = null

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200
And match response == responsePut

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

* match response.allegati == '#notpresent'

* set pendenzaPut.allegati[0].nome = 'tipoPendenza-promemoria-oggetto-freemarker.ftl'
* set pendenzaPut.allegati[0].tipo = 'application/json'
* set pendenzaPut.allegati[0].descrizione = 'test allegato'
* set pendenzaPut.allegati[0].contenuto = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/pendenze/put/msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200
And match response == responsePut

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

* match response.allegati[0].nome == pendenzaPut.allegati[0].nome
* match response.allegati[0].tipo == pendenzaPut.allegati[0].tipo
* match response.allegati[0].descrizione == pendenzaPut.allegati[0].descrizione

* def allegatoPath = response.allegati[0].contenuto

Given url pendenzeBaseurl
And path allegatoPath
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
* match responseHeaders['Content-Type'][0] == pendenzaPut.allegati[0].tipo
* match responseHeaders['content-disposition'][0] contains pendenzaPut.allegati[0].nome


Scenario Outline: <field> non valida

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.allegati[0].nome = 'tipoPendenza-promemoria-oggetto-freemarker.ftl'
* set pendenzaPut.allegati[0].tipo = 'application/json'
* set pendenzaPut.allegati[0].descrizione = 'test allegato'
* set pendenzaPut.allegati[0].contenuto = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/pendenze/put/msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))

* set <fieldRequest> = <fieldValue>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| allegati.nome | pendenzaPut.allegati[0].nome | null | 'nome' |
| allegati.nome | pendenzaPut.allegati[0].nome | loremIpsum | 'nome' |
| allegati.tipo | pendenzaPut.allegati[0].tipo | loremIpsum | 'tipo' |
| allegati.descrizione | pendenzaPut.allegati[0].descrizione | loremIpsum | 'descrizione' |
| allegati.contenuto | pendenzaPut.allegati[0].contenuto | null | 'contenuto' |

