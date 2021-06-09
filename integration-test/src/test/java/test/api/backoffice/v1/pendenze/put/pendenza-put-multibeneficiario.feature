Feature: Caricamento pagamento dovuto con avviso

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


Scenario: Caricamento pendenza dovuta 

* def pendenzaPut = read('msg/pendenza-put_multibeneficiario.json')
* set pendenzaPut.idTipoPendenza = codLibero

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', UUID: '#notnull' }
And match response.numeroAvviso == '#notpresent'

* def responsePut = response

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == read('msg/pendenza-get_multivoce.json')

* match response.numeroAvviso == '#notpresent'
* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[2]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'
* match response.voci[1].indice == 2
* match response.voci[1].stato == 'Non eseguito'




