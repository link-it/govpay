Feature: Censimento applicazioni

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')

Scenario: Aggiunta di una applicazione

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario Outline: Modifica di una applicazione (<field>)

* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')
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
| principal | 'principal123456' |
| codificaAvvisi.codificaIuv | null |
| codificaAvvisi.codificaIuv | '01' |
| codificaAvvisi.regExpIuv | '10(.*)' |
| codificaAvvisi.generazioneIuvInterna | true |
| codificaAvvisi.generazioneIuvInterna | false |
| codificaAvvisi | null |
| domini | [ ] |
| tipiPendenza | [ ] |
| acl | [ { servizio: 'Anagrafica PagoPA', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica Creditore', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica Applicazioni', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica Ruoli', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Pagamenti', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Rendicontazioni e Incassi', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Giornale degli Eventi', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Configurazione e manutenzione', autorizzazioni: [ 'R', 'W' ] } ] |
| acl | [ { servizio: 'Anagrafica PagoPA', autorizzazioni: [ 'R', 'W' ] },  { servizio: 'Anagrafica Creditore', autorizzazioni: [ 'W' ] } ] |
| acl | [ ] |
| servizioIntegrazione | null |
| servizioIntegrazione.url | 'http://prova.it' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { username: 'usr', password: 'pwd' } } |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} } | 
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Server', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	} } | 
| abilitato | false | 
| abilitato | true |
| apiPagamenti | false | 
| apiPagamenti | true |
| apiPendenze | false | 
| apiPendenze | true |
| apiRagioneria | false | 
| apiRagioneria | true |

Scenario Outline: Modifica di una applicazione (<field>)

* set applicazione.<field> = <value>

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
And match response.<checkfield> == <checkValue>

Examples:
| field | value | checkfield | checkValue |
| domini | [ '#(idDominio_2)' ] | domini[0].idDominio | '#(idDominio_2)' |
| domini | null | domini | [] |
| domini | [ '*' ] | domini |  [ { idDominio: '*', ragioneSociale: 'Tutti' } ] |
| tipiPendenza | [ '#(codEntrataSegreteria)' ] | tipiPendenza[0].idTipoPendenza | '#(codEntrataSegreteria)' |
| tipiPendenza | null | tipiPendenza | [] |
| acl | null | acl | [] |


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
And match response.<field> == <checkValue>

Examples:
| field | value | checkValue |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa2)', ragioneSociale: '#string' } ] } ] | 
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa)', '#(idUnitaOperativa2)' ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa)', ragioneSociale: '#string' }, { idUnita: '#(idUnitaOperativa2)', ragioneSociale: '#string' } ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: null } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ ] } ] |
| domini | [ { idDominio: '#(idDominio)' } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ ] }, { idDominio: '#(idDominio_2)', unitaOperative: [ ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ ] }, { idDominio: '#(idDominio_2)', ragioneSociale: '#string', unitaOperative: [ ] } ] |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: [ '#(idUnitaOperativa2)' ] }, { idDominio: '#(idDominio_2)', unitaOperative: [ '#(idUnitaOperativa)' ] } ] | [ { idDominio: '#(idDominio)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa2)', ragioneSociale: '#string' } ] }, { idDominio: '#(idDominio_2)', ragioneSociale: '#string', unitaOperative: [ { idUnita: '#(idUnitaOperativa)', ragioneSociale: '#string' } ] } ] |






