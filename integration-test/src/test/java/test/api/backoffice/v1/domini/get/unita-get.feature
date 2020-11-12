Feature: Lista tipipendenza

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
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
And match response.numRisultati == 3


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
And match response.numRisultati == 3

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
And match response.numRisultati == 3


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

Scenario: Lettura delle uo associate ad un dominio non esistente

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'domini', idDominioNonCensito , 'unitaOperative'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 


Scenario: Lettura di una uo associata ad un dominio non esistente

* def idDominioNonCensito = '11221122331'
* def idUoNonCensita = '11221122331'

Given url backofficeBaseurl
And path 'domini', idDominioNonCensito , 'unitaOperative' , idUoNonCensita
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 

Scenario: Lettura di una uo inesistente associata ad un dominio

* def idUoNonCensita = '11221122331'

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative' , idUoNonCensita
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Unita Operativa "+idUoNonCensita+" non censita in Anagrafica per il Dominio "+idDominio+"")' 


