Feature: Aggiornamento di una pendenza scaduta tramite API di verifica

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def esitoAttivaRPT = read('classpath:test/workflow/modello3/v1/msg/attiva-response-ok.json')

@test1
Scenario: Aggiornamento pendenza non pagata scaduta tramite API di verifica

* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

* set pendenzaPut.dataValidita = '2022-01-01'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def pendenzaPutResponse = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet

* set pendenzaPut.dataValidita = '2022-01-01'
* set pendenzaPut.importo = 200.99
* set pendenzaPut.voci[0].importo = 200.99

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* call read('classpath:utils/pa-prepara-avviso.feature')


# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet
And match response.importo == 200.99
And match response.voci[0].importo == 200.99
And match response.rpp[0].rpt.datiVersamento.importoTotaleDaVersare == '200.99'
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].importoSingoloVersamento == '200.99'

@test2
Scenario: Aggiornamento pendenza non pagata scaduta non presente nelle API di verifica

* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

* set pendenzaPut.dataValidita = '2022-01-01'

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
And match response == pendenzaGet

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == pendenzaGet
And match response.importo == 100.99
And match response.voci[0].importo == 100.99
And match response.rpp[0].rpt.datiVersamento.importoTotaleDaVersare == '100.99'
And match response.rpp[0].rpt.datiVersamento.datiSingoloVersamento[0].importoSingoloVersamento == '100.99'


