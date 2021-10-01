Feature: Censimento unita operative

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def unita = read('classpath:test/api/backoffice/v1/domini/put/msg/unita.json')

Scenario: Aggiunta di una unita operativa

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una unita operativa (<field>)

* set unita.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'


Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201
And match response.<field> == checkValue

Examples:
| field | value | 
| ragioneSociale | 'Nuova ragione sociale' |
| indirizzo | 'Via nuova' |
| indirizzo | null |
| civico | '000A' |
| civico | null |
| cap | '12345' |
| cap | null |
| localita | 'Nuova localita' |
| localita | null |
| provincia | 'Nuova provincia' |
| provincia | null |
| nazione | 'EN' |
| nazione | null |
| email | 'mail@aaa.it' |
| email | null |
| pec | 'mail@aaa.it' |
| pec | null |
| tel | '000 000 000' |
| tel | null |
| fax | '000 000 000' |
| fax | null |
| web | 'www.nuova.it' |
| web | null |
| abilitato | false |
| abilitato | true |
| area | 'Nuova area' |
| area | null |

Scenario: Configurazione di due UO con idUo della seconda che e' una sottostringa del primo idUo	 

* def idDominioUO = '55555555555'
* def idUo1 = 'PROVA_' + idDominioUO
* def idUo2 = 'OVA_' + idDominioUO
* set dominio.ragioneSociale = 'Ente Creditore Test 3'


Given url backofficeBaseurl
And path 'domini', idDominioUO
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominioUO, 'unitaOperative', idUo1
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominioUO, 'unitaOperative', idUo2
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario: Uo associata ad un dominio non esistente

* def idDominioNonCensito = '11221122331'
* def idUoNonCensita = '11221122331'

Given url backofficeBaseurl
And path 'domini', idDominioNonCensito, 'unitaOperative' , idUoNonCensita
And headers basicAutenticationHeader
And request unita
When method put
Then status 422

* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Il dominio " + idDominioNonCensito + " indicato non esiste.")' 


Scenario: Autorizzazioni alla creazione delle uo

* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['*'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] }, 	{ servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true	
}
"""

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeSpidBaseurl
And path 'domini', idDominioNonCensito, 'unitaOperative', idUnitaOperativa
And headers operatoreSpidAutenticationHeader
And request unita
When method put
Then status 403
* match response == { categoria: 'AUTORIZZAZIONE', codice: '403000', descrizione: 'Operazione non autorizzata', dettaglio: '#notnull' }
