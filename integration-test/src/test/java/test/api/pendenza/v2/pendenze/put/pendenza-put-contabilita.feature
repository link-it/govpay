Feature: Caricamento pendenza con campi contabilita

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def pendenzaResponse = read('msg/pendenza-get_monovoce_contabilita.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def esitoPaGetPaymentV2 = read('classpath:test/workflow/modello3/v2/msg/getPaymentV2-response-ok.json')

* configure retry = { count: 25, interval: 10000 }

Scenario: Caricamento pendenza con contabilita definita

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
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

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
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

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
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

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
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

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
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


Scenario: Pagamento pendenza con 2 voci di contabilita attraverso le API V2

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso
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

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def versionePagamento = 3
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoPaGetPaymentV2
* match response.dati.transferList.transfer[0].metadata.mapEntry[0].key == 'CAPITOLOBILANCIO,ARTICOLOBILANCIO,CODICEACCERTAMENTO,ANNORIFERIMENTO,TITOLOBILANCIO,CATEGORIABILANCIO,TIPOLOGIABILANCIO,IMPORTOEUROCENT'
* match response.dati.transferList.transfer[0].metadata.mapEntry[0].value == 'capitolo1,,,2020,,,,99'
* match response.dati.transferList.transfer[0].metadata.mapEntry[1].key == 'CAPITOLOBILANCIO,ARTICOLOBILANCIO,CODICEACCERTAMENTO,ANNORIFERIMENTO,TITOLOBILANCIO,CATEGORIABILANCIO,TIPOLOGIABILANCIO,IMPORTOEUROCENT'
* match response.dati.transferList.transfer[0].metadata.mapEntry[1].value == 'capitolo2,,,2020,,,,10000' 

# Verifico lo stato della pendenza

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITA'
When method get
Then status 200
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[0].key == 'CAPITOLOBILANCIO,ARTICOLOBILANCIO,CODICEACCERTAMENTO,ANNORIFERIMENTO,TITOLOBILANCIO,CATEGORIABILANCIO,TIPOLOGIABILANCIO,IMPORTOEUROCENT'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[0].value == 'capitolo1,,,2020,,,,99'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[1].key == 'CAPITOLOBILANCIO,ARTICOLOBILANCIO,CODICEACCERTAMENTO,ANNORIFERIMENTO,TITOLOBILANCIO,CATEGORIABILANCIO,TIPOLOGIABILANCIO,IMPORTOEUROCENT'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[1].value == 'capitolo2,,,2020,,,,10000' 

Scenario: Pagamento pendenza con singola voce di contabilita attraverso le API V2

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento_contabilita.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso
* def proprietaCustom = 
"""
{ "test1" : 1, "test2" : "test", "test3" : false, "test4" : { "test1" : 1 } }
"""
* set pendenzaPut.voci[0].contabilita.quote[0].proprietaCustom = proprietaCustom
* set pendenzaPut.voci[0].contabilita.quote[0].importo = 100.99
* set pendenzaPut.voci[0].contabilita.quote[0].titolo = 'Titolo'
* set pendenzaPut.voci[0].contabilita.quote[0].tipologia = 'Tipologia'
* remove pendenzaPut.voci[0].contabilita.quote[1]

* set pendenzaResponse.voci[0].contabilita.quote[0].proprietaCustom = proprietaCustom
* set pendenzaResponse.voci[0].contabilita.quote[0].importo = 100.99
* set pendenzaResponse.voci[0].contabilita.quote[0].titolo = 'Titolo'
* set pendenzaResponse.voci[0].contabilita.quote[0].tipologia = 'Tipologia'
* remove pendenzaResponse.voci[0].contabilita.quote[1]

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

* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')

# Attivo il pagamento 

* def versionePagamento = 3
* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoPaGetPaymentV2
* match response.dati.transferList.transfer[0].metadata.mapEntry[0].key == 'CAPITOLOBILANCIO'
* match response.dati.transferList.transfer[0].metadata.mapEntry[0].value == pendenzaPut.voci[0].contabilita.quote[0].capitolo
* match response.dati.transferList.transfer[0].metadata.mapEntry[1].key == 'ANNORIFERIMENTO'
* match response.dati.transferList.transfer[0].metadata.mapEntry[1].value == '2020'
* match response.dati.transferList.transfer[0].metadata.mapEntry[2].key == 'TITOLOBILANCIO'
* match response.dati.transferList.transfer[0].metadata.mapEntry[2].value == pendenzaPut.voci[0].contabilita.quote[0].titolo
* match response.dati.transferList.transfer[0].metadata.mapEntry[3].key == 'TIPOLOGIABILANCIO'
* match response.dati.transferList.transfer[0].metadata.mapEntry[3].value == pendenzaPut.voci[0].contabilita.quote[0].tipologia
* match response.dati.transferList.transfer[0].metadata.mapEntry[4].key == 'test1'
* match response.dati.transferList.transfer[0].metadata.mapEntry[4].value == '1'
* match response.dati.transferList.transfer[0].metadata.mapEntry[5].key == 'test2'
* match response.dati.transferList.transfer[0].metadata.mapEntry[5].value == proprietaCustom.test2
* match response.dati.transferList.transfer[0].metadata.mapEntry[6].key == 'test3'
* match response.dati.transferList.transfer[0].metadata.mapEntry[6].value == 'false'
* match response.dati.transferList.transfer[0].metadata.mapEntry[7].key == 'test4'
* match response.dati.transferList.transfer[0].metadata.mapEntry[7].value == '{test1=1}' 

# Verifico lo stato della pendenza

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITA'
When method get
Then status 200
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[0].key == 'CAPITOLOBILANCIO'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[0].value == pendenzaPut.voci[0].contabilita.quote[0].capitolo
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[1].key == 'ANNORIFERIMENTO'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[1].value == '2020'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[2].key == 'TITOLOBILANCIO'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[2].value == pendenzaPut.voci[0].contabilita.quote[0].titolo
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[3].key == 'TIPOLOGIABILANCIO'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[3].value == pendenzaPut.voci[0].contabilita.quote[0].tipologia
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[4].key == 'test1'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[4].value == '1'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[5].key == 'test2'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[5].value == proprietaCustom.test2
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[6].key == 'test3'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[6].value == 'false'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[7].key == 'test4'
* match response.rpp[0].rt.transferList.transfer[0].metadata.mapEntry[7].value == '{test1=1}' 

    
