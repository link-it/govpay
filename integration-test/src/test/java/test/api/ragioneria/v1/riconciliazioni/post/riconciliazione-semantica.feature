Feature: Errori di validazione semantica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "false"
* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* def importo = response.response.rh[0].importo
* def causale = response.response.rh[0].causale
* def incassoPost = { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789'}

Scenario Outline: Errore semantico nella richiesta di riconciliazione: <scenario>

* def incassoPost = { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789'}
* set <fieldRequest> = <fieldValue>
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url ragioneriaBaseurl
And path '/incassi', idDominio
And headers basicAutenticationHeader
And request incassoPost
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: '#notnull', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.codice == <codice>
And match response.descrizione == <descrizione>

Examples:
| scenario | fieldRequest | fieldValue | codice | descrizione |
| 'Importo errato' | incassoPost.importo | '1000' | '021407' | 'Importo errato.' |
| 'Causale non valida' | incassoPost.causale | '/XXX/000000000000000' | '021401' | "Il formato della causale non e' conforme alle specifiche AgID." |
| 'RFB non trovato' | incassoPost.causale | '/RFB/999999999999999' | '021402' | "Pagamento non trovato." |
| 'RFS non trovato' | incassoPost.causale | '/RFS/RF000000000000000' | '021402' | "Pagamento non trovato." |
| 'IDF non trovato' | incassoPost.causale | '/PUR/LGPE/RIVERSAMENTO/XXXXXXXXXXX' | '021401' | "Il formato della causale non e' conforme alle specifiche AgID." |

Scenario: Errore semantico nella richiesta di riconciliazione: Dominio inesistente

* def incassoPost = { causale: '#(causale)', importo: '#(importo)' , sct : 'SCT0123456789'}
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url ragioneriaBaseurl
And path '/incassi', '00000000000'
And headers basicAutenticationHeader
And request incassoPost
When method post
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: '#notnull', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.codice == '021410'
And match response.descrizione == 'Dominio inesistente.'

Scenario: Errore semantico nella richiesta di riconciliazione: Pagamento gia incassato

# TODO


