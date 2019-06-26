Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Caricamento pendenza dovuta 

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}' }

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('classpath:test/api/pendenza/pendenze/v1/get/msg/pendenza-get-dettaglio.json')

* match response.numeroAvviso == responsePut.numeroAvviso
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'


Scenario: Caricamento pendenza con entrata definita, riferita e marca da bollo

* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* set pendenzaPut.idTipoPendenza = codLibero

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

* match response.numeroAvviso == '#notpresent'
* match response.stato == 'NON_ESEGUITA'


Scenario: Caricamento pendenza dovuta con Tipo pendenza non esistente

* def idTipoPendenza2 = 'codLibero'
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = idTipoPendenza2

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
	codice: 'TVR_000',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Tipo pendenza (" + idTipoPendenza2 +") inesistente")'
}
"""


Scenario: Caricamento pendenza dovuta con Tipo pendenza disabilitata

Given url backofficeBasicBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : false}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaRinnovo

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
	codice: 'TVR_001',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Tipo pendenza (" + tipoPendenzaRinnovo +") disabilitato")'
}
"""

Scenario: Caricamento pendenza dovuta con Tipo pendenza non definita per il dominio

* def tipoPendenzaRinnovo2 = tipoPendenzaRinnovo + '2' 

Given url backofficeBasicBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo2
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
When method put
Then assert responseStatus == 200 || responseStatus == 201


# * set tipoPendenzaDominio.abilitato = false

#Given url backofficeBasicBaseurl
#And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
#And headers gpAdminBasicAutenticationHeader
#And request tipoPendenzaDominio
#When method put
#Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaRinnovo2

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
	codice: 'TVD_000',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Tipo pendenza (" + tipoPendenzaRinnovo2 +") del dominio ("+ idDominio +") inesistente")'
}
"""

Scenario: Caricamento pendenza dovuta con Tipo pendenza disabilitata per il dominio

Given url backofficeBasicBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true, abilitato : true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  form: null,
  trasformazione: null,
  validazione: null,
  abilitato: false
}
"""

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idTipoPendenza = tipoPendenzaRinnovo

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
	codice: 'TVD_001',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Tipo pendenza (" + tipoPendenzaRinnovo +") del dominio ("+ idDominio +") disabilitato")'
}
"""

