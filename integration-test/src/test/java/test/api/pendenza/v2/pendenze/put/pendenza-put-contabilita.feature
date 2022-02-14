Feature: Caricamento pendenza con campi contabilita

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
* def pendenzaResponse = read('msg/pendenza-get_monovoce_contabilita.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'


Scenario: Caricamento pendenza con contabilita definita

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

* match response.voci[0].contabilita == pendenzaResponse.voci[0].contabilita
* match response.stato == 'NON_ESEGUITA'


Scenario: Caricamento pendenza con contabilita.proprietaCustom definita

* def proprietaCustom = 
"""
{ "test1" : 1, "test2" : "test", "test3" : false, "test4" : { "test1" : 1 } }
"""
* set pendenzaPut.voci[0].contabilita.proprietaCustom = proprietaCustom
* set pendenzaResponse.voci[0].contabilita.proprietaCustom = proprietaCustom

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

* match response.voci[0].contabilita == pendenzaResponse.voci[0].contabilita
* match response.stato == 'NON_ESEGUITA'

Scenario: Caricamento pendenza con contabilita.quote[0].proprietaCustom definita

* def proprietaCustom = 
"""
{ "test1" : 1, "test2" : "test", "test3" : false, "test4" : { "test1" : 1 } }
"""
* set pendenzaPut.voci[0].contabilita.quote[0].proprietaCustom = proprietaCustom
* set pendenzaResponse.voci[0].contabilita.quote[0].proprietaCustom = proprietaCustom

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

* match response.voci[0].contabilita == pendenzaResponse.voci[0].contabilita
* match response.stato == 'NON_ESEGUITA'


Scenario: Caricamento pendenza con contabilita errore validazione importi

* def pendenzaPutImportoOrig = pendenzaPut.voci[0].importo
* set pendenzaPut.voci[0].importo = pendenzaPut.voci[0].importo + 10

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_035',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("La voce ("+pendenzaPut.voci[0].idVocePendenza+") della pendenza (IdA2A:" + idA2A + ", Id:" + idPendenza + ") ha un importo (" + pendenzaPut.voci[0].importo + ") diverso dalla somma dei singoli importi definiti nella lista delle contabilita\' (" + pendenzaPutImportoOrig + ")")'
}
"""
Scenario Outline: <field> non valida

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
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | null | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '10.001' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '10,000' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '10,00.0' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | 'aaaa' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '12345678901234567,89' | 'importo' |
| capitolo | pendenzaPut.voci[0].contabilita.quote[0].capitolo | loremIpsum | 'capitolo' |
| capitolo | pendenzaPut.voci[0].contabilita.quote[0].capitolo | null | 'capitolo' |
| accertamento | pendenzaPut.voci[0].contabilita.quote[0].accertamento | loremIpsum | 'accertamento' |
| annoEsercizio | pendenzaPut.voci[0].contabilita.quote[0].annoEsercizio | 12345 | 'annoEsercizio' |
| annoEsercizio | pendenzaPut.voci[0].contabilita.quote[0].annoEsercizio | loremIpsum | 'annoEsercizio' |
| annoEsercizio | pendenzaPut.voci[0].contabilita.quote[0].annoEsercizio | null | 'annoEsercizio' |
| titolo | pendenzaPut.voci[0].contabilita.quote[0].titolo | loremIpsum | 'titolo' |
| tipologia | pendenzaPut.voci[0].contabilita.quote[0].tipologia | loremIpsum | 'tipologia' |
| categoria | pendenzaPut.voci[0].contabilita.quote[0].categoria | loremIpsum | 'categoria' |
| articolo | pendenzaPut.voci[0].contabilita.quote[0].articolo | loremIpsum | 'articolo' |






    
