Feature: Censimento Operatori

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
* def operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['#(codLibero)'],
  acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true
}
"""

Scenario: Aggiunta di un operatore

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di un operatore (<field>)

* set operatore.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| ragioneSociale | 'Nuova Ragione' |
| acl | [ { servizio: 'Anagrafica PagoPA', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica Applicazioni', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica Ruoli', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Rendicontazioni e Incassi', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Giornale degli Eventi', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Configurazione e manutenzione', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica PagoPA', autorizzazioni: [ 'R', 'W' ] },  { servizio: 'Anagrafica Creditore', autorizzazioni: [ 'W' ] } ] |
| abilitato | true |
| abilitato | false |

Scenario: Modifica di un operatore (domini)

* set operatore.domini = ['#(idDominio)','#(idDominio_2)']

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.domini == '#[2]'
And match response.domini[0] contains { idDominio: '#(idDominio)' }
And match response.domini[1] contains { idDominio: '#(idDominio_2)' } 

Scenario: Modifica di un operatore (domini)

* set operatore.domini = ['#(idDominio)','*']

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.domini == [ { idDominio: '*', ragioneSociale: 'Tutti' } ]

Scenario Outline: Modifica di un operatore (domini)

* set operatore.<field> = <value>

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.domini == [ ]

Examples:
| field | value | 
| domini | null |
| domini | [ ] |

Scenario: Modifica di un operatore (tipiPendenza)

* set operatore.tipiPendenza = ['#(codEntrataBollo)','#(codLibero)']

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.tipiPendenza == '#[2]'
And match response.tipiPendenza[0] contains { idTipoPendenza: '#(codEntrataBollo)' }
And match response.tipiPendenza[1] contains { idTipoPendenza: '#(codLibero)' } 

Scenario: Modifica di un operatore (tipiPendenza)

* set operatore.tipiPendenza = ['#(codEntrataBollo)','*']

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.tipiPendenza == '#[1]'
And match response.tipiPendenza[0] contains { idTipoPendenza: '*', descrizione: 'Tutti' } 

Scenario Outline:  Modifica di un operatore (tipiPendenza)

* set operatore.<field> = <value>

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.tipiPendenza == [ ]

Examples:
| field | value | 
| tipiPendenza | [ ] |
| tipiPendenza | null |

Scenario: Caratteri codificati nel field (idOperatore)
# ' &*' = '%20%26%2A'

Given url backofficeBaseurl
And path 'operatori', '%20%26%2A' 
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', '%20%26%2A'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.principal == ' &*'

Scenario Outline: Modifica delle autorizzazioni un operatore sui domini per uo (<field>)

* set operatore.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', 'MarioRossi'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == <checkValue>

Examples:
| field | value | checkValue |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '*' ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '*', ragioneSociale: '#string' } ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '*', ragioneSociale: '#string' } ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa2)', ragioneSociale: '#string' } ] } ] | 
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa)', '#(idUnitaOperativa2)' ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa)', ragioneSociale: '#string' }, { idUnita: '#(idUnitaOperativa2)', ragioneSociale: '#string' } ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: null } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '*', ragioneSociale: '#string' } ] } ] |
| domini | [ { idDominio: '#(idDominio)' } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '*', ragioneSociale: '#string' } ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ ] }, { idDominio: '#(idDominio_2)', unitaOperative: [ ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '*', ragioneSociale: '#string' } ] }, { idDominio: '#(idDominio_2)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '*', ragioneSociale: '#string' } ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa2)' ] }, { idDominio: '#(idDominio_2)', unitaOperative: [ '#(idUnitaOperativa)' ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa2)', ragioneSociale: '#string' } ] }, { idDominio: '#(idDominio_2)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa)', ragioneSociale: '#string' } ] } ] |

Scenario: Configurazione di due operatori con il principal del secondo che e' una sottostringa del primo principal	 

* def idComune = getCurrentTimeMillis()
* def idOperatore1 = 'PROVA_' + idComune
* def idOperatore2 = 'OVA_' + idComune


Given url backofficeBaseurl
And path 'operatori', idOperatore1
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'operatori', idOperatore2
And headers basicAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201


Scenario Outline: Configurazione di un'operatore da parte di un'operatore che non ha i diritti su tutti i <field>.

* def utente_operatore = 
"""
{
  ragioneSociale: 'Mario Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['#(codEntrataSegreteria)'],
  acl: [ { servizio: 'Pendenze', autorizzazioni: [ 'R', 'W' ] }, 	{ servizio: 'Anagrafica Ruoli', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true	
}
"""

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers gpAdminBasicAutenticationHeader
And request utente_operatore
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def operatore = 
"""
{
  ragioneSociale: 'Marco Rossi',
  domini: ['#(idDominio)'],
  tipiPendenza: ['#(codLibero)'],
  acl: [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ],
  abilitato: true
}
"""
* set operatore.<field> = <value>

Given url backofficeSpidBaseurl
And path 'operatori', 'MarcoRossi'
And headers operatoreSpidAutenticationHeader
And request operatore
When method put
Then assert responseStatus == 403

Examples:
| field | value |
| domini | [ '*' ] |
| tipiPendenza | [ '*' ] |



