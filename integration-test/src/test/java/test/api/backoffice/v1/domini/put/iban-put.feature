Feature: Censimento iban

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def iban = { postale:false, mybank:false, abilitato:true, bic:'#(bicAccredito)' }

Scenario: Aggiunta di un iban

* def suffix = getCurrentTimeMillis()

* set iban.descrizione = 'IBAN Accredito ' + 'XX02L076011234' + suffix
Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', 'XX02L076011234' + suffix
And headers basicAutenticationHeader
And request iban
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di un iban (<field>)

* set iban.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers basicAutenticationHeader
And request iban
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| postale | true |
| postale | false |
| descrizione | null |
| descrizione | 'Iban Accredito' |
| abilitato | true |
| abilitato | false |
| bic | 'AAABBBDABAI' |
| intestatario | null |
| intestatario | 'Comune di Monopoli' |

Scenario: Iban associato ad un dominio non esistente

* def idDominioNonCensito = '11221122331'
* def idIbanNonCensito = 'IT02L1235412345123456789012'

Given url backofficeBaseurl
And path 'domini', idDominioNonCensito, 'contiAccredito' , idIbanNonCensito
And headers basicAutenticationHeader
And request iban
When method put
Then status 422

* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Il dominio " + idDominioNonCensito + " indicato non esiste.")' 


Scenario: Autorizzazioni alla creazione degli iban

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
And path 'domini', idDominioNonCensito, 'contiAccredito', ibanAccredito
And headers operatoreSpidAutenticationHeader
And request iban
When method put
Then status 403
* match response == { categoria: 'AUTORIZZAZIONE', codice: '403000', descrizione: 'Operazione non autorizzata', dettaglio: '#notnull' }




