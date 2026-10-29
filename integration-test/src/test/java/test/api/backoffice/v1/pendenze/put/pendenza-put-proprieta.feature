Feature: Caricamento pendenze con definizione delle proprieta

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Caricamento pendenza con linguaSecondaria = false

* set pendenzaPut.proprieta = 
"""
{
	linguaSecondaria : 'false'
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
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
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.proprieta == pendenzaPut.proprieta

Scenario: Caricamento pendenza con linguaSecondaria = EN

* set pendenzaPut.proprieta = 
"""
{
	linguaSecondaria : 'en'
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
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
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.proprieta == pendenzaPut.proprieta

Scenario: Caricamento pendenza con linguaSecondaria = DE

* set pendenzaPut.proprieta = 
"""
{
	linguaSecondaria : 'de'
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
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
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.proprieta == pendenzaPut.proprieta

Scenario: Caricamento pendenza con linguaSecondaria errata

* set pendenzaPut.proprieta = 
"""
{
	linguaSecondaria : 'XXX'
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400
* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains 'linguaSecondaria'

Scenario: Caricamento pendenza con linguaSecondariacCausale

* set pendenzaPut.proprieta = 
"""
{
	linguaSecondaria : 'de',
	linguaSecondariaCausale : 'TEST Causale'
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
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
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.proprieta == pendenzaPut.proprieta

@test-dataScadenza
Scenario: Caricamento pendenza con dataScandenzaAvviso personalizzata

* set pendenzaPut.proprieta = 
"""
{
  dataScandenzaAvviso : '2023-12-31'
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
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
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.proprieta == pendenzaPut.proprieta

Scenario: Caricamento pendenza con nascondiDataScadenzaAvviso

# Issue #898: la pendenza puo' chiedere che l'avviso non riporti alcuna data
# entro cui pagare, pur avendo dataValidita o dataScadenza valorizzate. Qui si
# verifica che la proprieta' attraversi la PUT e la rilettura; la resa
# sull'avviso non e' verificabile da qui, perche' il PDF non e' ispezionabile.

* set pendenzaPut.proprieta = 
"""
{
  nascondiDataScadenzaAvviso : true
}
"""
* set pendenzaPut.dataValidita = '2026-12-31'
* set pendenzaPut.dataScadenza = '2026-12-31'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
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

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.proprieta.nascondiDataScadenzaAvviso == true
# le due date restano quelle inviate: la proprieta' agisce sulla stampa, non sulla pendenza
* match response.dataValidita == '2026-12-31'
* match response.dataScadenza == '2026-12-31'

Scenario: Caricamento pendenza senza nascondiDataScadenzaAvviso

# Il comportamento di chi non usa la proprieta' non cambia: assente o false
# sono equivalenti.

* set pendenzaPut.proprieta = 
"""
{
  nascondiDataScadenzaAvviso : false
}
"""

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
* match response.proprieta.nascondiDataScadenzaAvviso == false


@test-dataScadenza-null
Scenario: Caricamento pendenza senza date con dataScandenzaAvviso null e stampa avviso

* set pendenzaPut.dataValidita = null
* set pendenzaPut.dataScadenza = null
* set pendenzaPut.proprieta = 
"""
{
  dataScandenzaAvviso : null
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
And param stampaAvviso = true
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', pdf: '#notnull', UUID: '#notnull' }

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.dataValidita == '#notpresent'
* match response.dataScadenza == '#notpresent'
* match response.proprieta.dataScandenzaAvviso == '#notpresent'


@test-dataValidita-dataScadenzaAvviso-null
Scenario: Caricamento pendenza con dataValidita e dataScandenzaAvviso null e stampa avviso

* set pendenzaPut.proprieta = 
"""
{
  dataScandenzaAvviso : null
}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
And param stampaAvviso = true
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', pdf: '#notnull', UUID: '#notnull' }

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.dataValidita == pendenzaPut.dataValidita
* match response.dataScadenza == pendenzaPut.dataScadenza
* match response.proprieta.dataScandenzaAvviso == '#notpresent'
