Feature: Ricerca Tipi Pendenza con diversi filtri

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def backofficeSpidBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def tipoPendenza = 
"""
{
  descrizione: "Tipo Pendenza ABCD",
  codificaIUV: "030",
  pagaTerzi: true,
  portaleBackoffice: null,
  portalePagamento: null,
  avvisaturaMail: null,
  avvisaturaAppIO: null,
  visualizzazione: null,
  tracciatoCsv: null
}
"""          
* def idTipoPendenza1 = 'SCDS_XYZ'

* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['*'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true
}
"""

Scenario: Aggiunta di un tipoPendenza e test che il filtro per descrizione filtri sia sul campo idTipoPendenza che sul campo descrizione.

Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza1
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza'
And param descrizione = 'XYZ'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response.risultati[0].idTipoPendenza == idTipoPendenza1

Given url backofficeBaseurl
And path 'tipiPendenza'
And param descrizione = 'ABCD'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response.risultati[0].idTipoPendenza == idTipoPendenza1


Scenario: Operatore con diritti sugli enti creditori e parametro associati  = false

* set operatore.acl = 
"""
[ 
	{ 
		servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] 
	},
	{ 
		servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] 
	}
]
"""

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'domini'
And headers operatoreSpidAutenticationHeader
And param associati = false
When method get
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }

Scenario: Operatore con diritti sugli enti creditori e parametro associati non impostato

* set operatore.acl = 
"""
[ 
	{ 
		servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] 
	},
	{ 
		servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] 
	}
]
"""

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'domini'
And headers operatoreSpidAutenticationHeader
When method get
Then status 200
And match response.numRisultati == 1

