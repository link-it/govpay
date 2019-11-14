Feature: Eventi di verifica della pendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')

Scenario: Autorizzazione alla lettura del dettaglio di un evento

Given url backofficeBaseurl
And path 'ruoli', 'Operatore'
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', ] } ],
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['*'],
  tipiPendenza: ['*'],
  acl: null,
  ruoli: ['Operatore'],
  abilitato: true
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* call sleep(200)

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeBaseurl
And path '/eventi'
And param idA2A = idA2A
And param idPendenza = idPendenza
And headers operatoreSpidAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path '/eventi', response.risultati[0].id
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
