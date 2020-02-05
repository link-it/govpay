Feature: Validazione sintattica delle richieste di patch

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzaGet = read('classpath:test/api/pendenza/v2/pendenze/get/msg/pendenza-get-dettaglio.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def patchOP = [ { "op": "REPLACE", "path": "/stato", "value": "ANNULLATA" }, { "op": "REPLACE", "path": "/descrizioneStato", "value": "Test annullamento" }]

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request patchOP
When method patch
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio == <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| op | patchOP[0].op | null | 'Il campo op non deve essere vuoto.' |
| op | patchOP[0].op | 'xxx' | 'Il campo op non e\' valido.' |
| path | patchOP[0].path | null | 'Il campo path non deve essere vuoto.' |
| path | patchOP[0].path | 'xxx' | 'Il campo path non e\' valido.' |
| value | patchOP[0].value | null | 'Il campo value non e\' valido per il path indicato.' |
| value | patchOP[0].value | 'xxx' | 'Il campo value indicato per il path \'/stato\' non e\' valido.' |

