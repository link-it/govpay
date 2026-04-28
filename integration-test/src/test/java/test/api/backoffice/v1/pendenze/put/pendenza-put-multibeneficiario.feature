Feature: Caricamento pendenza multibeneficiario

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

# configurazione del secondo ente come non intermediato e censimento iban

* def dominioNonIntermediato = read('classpath:configurazione/v1/msg/dominio.json')

* set dominioNonIntermediato.ragioneSociale = ragioneSocialeDominio_2 + ' N.I.'
* set dominioNonIntermediato.intermediato = false
* set dominioNonIntermediato.gln = null
* set dominioNonIntermediato.cbill = null
* set dominioNonIntermediato.iuvPrefix = null
* set dominioNonIntermediato.stazione = null
* set dominioNonIntermediato.auxDigit = null
* set dominioNonIntermediato.segregationCode = null
* set dominioNonIntermediato.autStampaPosteItaliane = null

Given url backofficeBaseurl
And path 'domini', idDominio_2 
And headers basicAutenticationHeader
And request dominioNonIntermediato
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def ibanAccreditoEnteNonIntermediato = 'IT08L1234512345123456789012'
* def ibanAccreditoEnteNonIntermediatoDescrizione = 'IBAN Accredito N.I.'
* def ibanAccreditoEnteNonIntermediatoPostale = 'IT08L0760112345123456789012'
* def ibanAccreditoEnteNonIntermediatoPostaleDescrizione = 'IBAN Accredito N.I. Postale'

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'contiAccredito', ibanAccreditoEnteNonIntermediato
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoEnteNonIntermediatoDescrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'contiAccredito', ibanAccreditoEnteNonIntermediatoPostale
And headers basicAutenticationHeader
And request {postale:true,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoEnteNonIntermediatoPostaleDescrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

# censimento entrata TEFA per il dominio non intermediato

* def codEntrataTefa = 'TEFA'

* def entrataTefa = 
"""
{
  "descrizione": "Tributo per l'esercizio delle funzioni ambientali",
  "tipoContabilita": "ALTRO",
  "codiceContabilita": "CodiceContabilita"
}
"""

* def entrataTefaDominio = 
"""
{
  "ibanAccredito": "#(ibanAccreditoEnteNonIntermediato)",
  "ibanAppoggio": "#(ibanAccreditoEnteNonIntermediatoPostale)",
  "tipoContabilita": "ALTRO",
  "codiceContabilita": "CodiceContabilita",
  "abilitato": true
}
"""

Given url backofficeBaseurl
And path 'entrate', codEntrataTefa
And headers basicAutenticationHeader
And request entrataTefa
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'entrate', codEntrataTefa
And headers basicAutenticationHeader
And request entrataTefaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario: Caricamento pendenza multibeneficiario 

* def pendenzaPut = read('msg/pendenza-put_multibeneficiario.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get_multibeneficiario.json')

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Non eseguito'

Scenario: Caricamento pendenza multibeneficiario e pagamento a iniziativa psp con api SANP-SPC 2.4.0.

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multibeneficiario.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')


Scenario: Caricamento pendenza multibeneficiario e pagamento a iniziativa psp con api SANP-SPC 2.4.0 e numero avviso definito

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multibeneficiario.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
Then match response.numeroAvviso == numeroAvviso

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')




Scenario: Caricamento pendenza multibeneficiario definita

* def pendenzaPut = read('msg/pendenza-put_multibeneficiario_riferimento.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get_multibeneficiario.json')

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Non eseguito'

Scenario: Caricamento pendenza multibeneficiario definita e pagamento a iniziativa psp con api SANP-SPC 2.4.0.

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multibeneficiario_riferimento.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')


Scenario: Caricamento pendenza multibeneficiario definita e pagamento a iniziativa psp con api SANP-SPC 2.4.0 e numero avviso definito

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multibeneficiario_riferimento.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* set pendenzaPut.numeroAvviso = numeroAvviso
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
Then match response.numeroAvviso == numeroAvviso

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo
* def tipoRicevuta = "R01"
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')





