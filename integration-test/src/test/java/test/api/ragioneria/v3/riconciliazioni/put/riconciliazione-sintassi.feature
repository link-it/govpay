Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Scenario Outline: Errore di validazione della richiesta: <scenario>

* def incassoPost = { causale: 'string', importo: '10' , sct : 'string'}
* set <fieldRequest> = <fieldValue>
* def idRiconciliazione = getCurrentTimeMillis()

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request incassoPost
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>
Examples:
| scenario | fieldRequest | fieldValue | fieldName | 
| 'importo null' | incassoPost.importo | null | 'importo' |
| 'importo void' | incassoPost.importo | '' | 'importo' |
| 'importo non numerico' | incassoPost.importo | 'aaaa' | 'importo' |
| 'importo separatore errato' | incassoPost.importo | '10,01' | 'importo' |
| 'importo 10.0001' | incassoPost.importo | '10.0001' | 'importo' |
| 'causale null' | incassoPost.causale | null | 'causale' |

Scenario Outline: Controllo sintassi idRiconciliazione

* def incassoPost = { causale: 'string', importo: '10' , sct : 'string'}
* def idRiconciliazione = <value>

Given url ragioneriaBaseurl
And path '/riconciliazioni', idDominio, idRiconciliazione
And headers basicAutenticationHeader
And request incassoPost
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>

Examples:
| value |  fieldName | 
| 'importo null' | 'id' |
| '123456789012345678901234567890123456' | 'id' |