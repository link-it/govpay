Feature: Lista tipipendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Verifica associati star su domini

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['*'],
  tipiPendenza: ['*'],
  ruoli: ['Amministratore'],
  abilitato: true
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'unitaOperative'
And headers operatoreSpidAutenticationHeader
And param associati = true
When method get
Then status 200
And match response.numRisultati == 4


Scenario: Verifica associati star su uo

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: [{ idDominio: '#(idDominio)', unitaOperative: [ '*' ] } ],
  tipiPendenza: ['*'],
  ruoli: ['Amministratore'],
  abilitato: true
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'unitaOperative'
And headers operatoreSpidAutenticationHeader
And param associati = true
When method get
Then status 200
And match response.numRisultati == 4

Scenario: Verifica associati null su uo

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: [{ idDominio: '#(idDominio)', unitaOperative: null } ],
  tipiPendenza: ['*'],
  ruoli: ['Amministratore'],
  abilitato: true
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'unitaOperative'
And headers operatoreSpidAutenticationHeader
And param associati = true
When method get
Then status 200
And match response.numRisultati == 4


Scenario: Verifica associati espliciti su uo

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: [{ idDominio: '#(idDominio)', unitaOperative: ['EC'] } ],
  tipiPendenza: ['*'],
  ruoli: ['Amministratore'],
  abilitato: true
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})

Given url backofficeSpidBaseurl
And path 'domini', idDominio, 'unitaOperative'
And headers operatoreSpidAutenticationHeader
And param associati = true
When method get
Then status 200
And match response.numRisultati == 1
And match response.risultati[0].idUnita == 'EC'


