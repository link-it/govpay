Feature: Applicazioni con autorizzazione per UO

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')
* callonce read('classpath:configurazione_unita.feature')

Scenario Outline: Modifica delle autorizzazioni una applicazione sui domini per uo (<field>)

* set applicazione.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value | 
| domini | [ ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa1)', '#(idUnitaOperativa2)' ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: null } ] |
| domini | [ { idDominio: '#(idDominio)' } ] |

Scenario Outline: Errore di semantica nella modifica delle autorizzazioni per uo (<field>)

* set applicazione.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then status 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }

Examples:
| field | value | 
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ 'INESISTENTE'] } ] |
| domini | [ { idDominio: '#(idDominio2)', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa1)', 'INESISTENTE' ] } ] |
| domini | [ { idDominio: null, unitaOperative: [ '#(idUnitaOperativa2)' } ] |

Scenario Outline: Errore di sintassi nella modifica delle autorizzazioni per uo (<field>)

* set applicazione.<field> = <value>
* def checkValue = <value> != null ? <value> : '#notpresent'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then status 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }

Examples:
| field | value | 
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ 'INESISTENTE'] } ] |
| domini | [ { idDominio: '#(idDominio2)', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa1)', 'INESISTENTE' ] } ] |
| domini | [ { idDominio: null, unitaOperative: [ '#(idUnitaOperativa2)' } ] |
