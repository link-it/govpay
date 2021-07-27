Feature: censimento di domini non intermediati

Background:

* call read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def string71 = '1GLqJdabGYFpRi4RbM8gWlnpCzVvMyeKC2qoCYkqfvTyGZ1eovAxsFqpGfVqzzXXjCfMsKi'
* def string17 = 'LS2wIWYPN0QPsgTbX'
* def string36 = 'VTnniDMiQ2ngyoDMBnfzeGUPKTbhx2U7fMO1'

Scenario: Aggiunta di un dominio non intermediato

* set dominio.intermediato = false
* set dominio.gln = null
* set dominio.cbill = null
* set dominio.iuvPrefix = null
* set dominio.stazione = null
* set dominio.auxDigit = null
* set dominio.segregationCode = null
* set dominio.autStampaPosteItaliane = null

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.intermediato == false
And match response.gln == '#notpresent'
And match response.cbill == '#notpresent'
And match response.iuvPrefix == '#notpresent'
And match response.stazione == '#notpresent'
And match response.auxDigit == '#notpresent'
And match response.segregationCode == '#notpresent'
And match response.autStampaPosteItaliane == '#notpresent'


Scenario Outline: <field> non valida

* set dominio.intermediato = false
* set dominio.gln = null
* set dominio.cbill = null
* set dominio.iuvPrefix = null
* set dominio.stazione = null
* set dominio.auxDigit = null
* set dominio.segregationCode = null
* set dominio.autStampaPosteItaliane = null
* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| gln | dominio.gln | '0000001000000' | 'gln' |
| cbill | dominio.cbill | '00000' | 'cbill' |
| iuvPrefix | dominio.iuvPrefix | '%(a)' | 'iuvPrefix' |
| stazione | dominio.stazione | '#(idStazione)' | 'stazione' |
| auxDigit | dominio.auxDigit | '0' | 'auxDigit' |
| segregationCode | dominio.segregationCode | '01' | 'segregationCode' |
| autStampaPosteItaliane | dominio.autStampaPosteItaliane | 'test' | 'autStampaPosteItaliane' |


Scenario: accesso alle risorse correlate di un dominio non intermediato

* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio.json')

* set dominio.intermediato = false
* set dominio.gln = null
* set dominio.cbill = null
* set dominio.iuvPrefix = null
* set dominio.stazione = null
* set dominio.auxDigit = null
* set dominio.segregationCode = null
* set dominio.autStampaPosteItaliane = null

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# UO nessuna azione consentita

* def unita = read('classpath:test/api/backoffice/v1/domini/put/msg/unita.json')

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then status 422
* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '#("La risorsa richiesta non e\' disponibile per il dominio " + idDominio + " non intermediato.")' 


Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 0,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 404


# Entrate nessuna azione consentita
* def codEntrataSiope = 'SIOPE_IMU'

* def entrata = 
"""
{
  "ibanAccredito": "#(ibanAccredito)",
  "ibanAppoggio": "#(ibanAccreditoPostale)",
  "tipoContabilita": "SIOPE",
  "codiceContabilita": 3321,
  "abilitato": true
}
"""

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request entrata
When method put
Then status 422
* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '#("La risorsa richiesta non e\' disponibile per il dominio " + idDominio + " non intermediato.")' 

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 0,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 404


# Tipi pendenza nessuna azione consentita

* def tipoPendenza = 
"""
{
	"codificaIUV": "999",  
	"pagaTerzi": false,
	"abilitato": true,
  portaleBackoffice: null,
  portalePagamento: null,
  avvisaturaMail: null,
  avvisaturaAppIO: null,
  "visualizzazione": null,
  "tracciatoCsv": null
}
"""

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then status 422
* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '#("La risorsa richiesta non e\' disponibile per il dominio " + idDominio + " non intermediato.")' 


Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201
And match response == 
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 0,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[0]'
}
"""


Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', 'SCDS'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 404


# Iban Accredito tutte le azioni sono consentite

* def iban = { postale:false, mybank:false, abilitato:true, bic:'#(bicAccredito)' }

* def suffix = getCurrentTimeMillis()

* set iban.descrizione = 'IBAN Accredito ' + 'XX02L076011234' + suffix
Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', 'XX02L076011234' + suffix
And headers basicAutenticationHeader
And request iban
When method put
Then assert responseStatus == 200 || responseStatus == 201


Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', 'XX02L076011234' + suffix
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201
And match response == 
"""
{
	numRisultati: '#ignore',
	numPagine: 1,
	risultatiPerPagina: '#ignore',
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""






