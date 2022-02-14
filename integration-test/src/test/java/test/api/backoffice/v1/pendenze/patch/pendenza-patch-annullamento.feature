Feature: Annullamento di una pendenza da console

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Scenario: Annullamento con operatore autorizzato per ruolo

* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeBasicBaseurl
And path '/pendenze', idA2A, idPendenza
And headers operatoreSpid2AutenticationHeader
And request
"""
[
  { "op":"REPLACE","path":"/stato","value":"ANNULLATA"},
  {"op":"REPLACE","path":"/descrizioneStato","value":"Test annullamento"}
]
"""
When method patch
Then status 200

Scenario: Annullamento con operatore autorizzato per acl

* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeBasicBaseurl
And path '/pendenze', idA2A, idPendenza
And headers operatoreSpidAutenticationHeader
And request
"""
[
  { "op":"REPLACE","path":"/stato","value":"ANNULLATA"},
  {"op":"REPLACE","path":"/descrizioneStato","value":"Test annullamento"}
]
"""
When method patch
Then status 200

Scenario: Annullamento di una pendenza annullata

* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeBasicBaseurl
And path '/pendenze', idA2A, idPendenza
And headers operatoreSpid2AutenticationHeader
And request
"""
[
  { "op":"REPLACE","path":"/stato","value":"ANNULLATA"},
  {"op":"REPLACE","path":"/descrizioneStato","value":"Test annullamento"}
]
"""
When method patch
Then status 200

Given url backofficeBasicBaseurl
And path '/pendenze', idA2A, idPendenza
And headers operatoreSpid2AutenticationHeader
When method get
Then status 200
And match response.stato == 'ANNULLATA'
And match response.descrizioneStato == 'Test annullamento'


Given url backofficeBasicBaseurl
And path '/pendenze', idA2A, idPendenza
And headers operatoreSpid2AutenticationHeader
And request
"""
[
  { "op":"REPLACE","path":"/stato","value":"ANNULLATA"},
  {"op":"REPLACE","path":"/descrizioneStato","value":"Test annullamento 2"}
]
"""
When method patch
Then status 200

Given url backofficeBasicBaseurl
And path '/pendenze', idA2A, idPendenza
And headers operatoreSpid2AutenticationHeader
When method get
Then status 200
And match response.stato == 'ANNULLATA'
And match response.descrizioneStato == 'Test annullamento 2'



