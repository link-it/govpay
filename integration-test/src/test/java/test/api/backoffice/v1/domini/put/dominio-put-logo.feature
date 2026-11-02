Feature: Logo del dominio e stampa degli avvisi (Issue #902)

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')
* def dominioOriginale = read('classpath:configurazione/v1/msg/dominio.json')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def pendenzeAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def toBytes = function(s) { return new java.lang.String(s).getBytes('UTF-8') }

Scenario Outline: Logo stampabile (<logo>): il dominio viene salvato e l'avviso stampato con il logo

* set dominio.logo = read('msg/loghi/<logo>.txt')

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers pendenzeAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/avvisi', response.idDominio, response.numeroAvviso
And headers pendenzeAutenticationHeader
And header Accept = 'application/pdf'
When method get
Then status 200
And match responseHeaders contains { 'Content-Type': ['application/pdf'] }

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioOriginale
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Examples:
| logo |
| svg-semplice |
| svg-png-incorporato |

Scenario Outline: Logo non stampabile (<logo>): il dominio viene rifiutato

* set dominio.logo = read('msg/loghi/<logo>.txt')

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then status 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains "Il logo non e' utilizzabile nella stampa degli avvisi"

Examples:
| logo |
| svg-immagine-esterna |
| png-troncato |

Scenario: Logo non stampabile gia' salvato: l'avviso viene stampato con il logo di default e il dominio resta modificabile

* def logoNonStampabile = read('msg/loghi/svg-immagine-esterna.txt')
* eval db.update("UPDATE domini SET logo = ? WHERE cod_dominio = ?", toBytes(logoNonStampabile), idDominio)
* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idPendenza = getCurrentTimeMillis()

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers pendenzeAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/avvisi', response.idDominio, response.numeroAvviso
And headers pendenzeAutenticationHeader
And header Accept = 'application/pdf'
When method get
Then status 200
And match responseHeaders contains { 'Content-Type': ['application/pdf'] }

# il logo gia' salvato, riproposto invariato o come link restituito dalla GET, non blocca le altre modifiche

* set dominio.logo = logoNonStampabile
* set dominio.ragioneSociale = 'Ragione sociale modificata'

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then status 200

* set dominio.logo = '/domini/' + idDominio + '/logo'

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then status 200

* def logoSalvato = db.readValue("SELECT logo FROM domini WHERE cod_dominio = '" + idDominio + "'")
* def logoSalvatoTesto = new java.lang.String(logoSalvato, 'UTF-8')
* match logoSalvatoTesto == logoNonStampabile

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominioOriginale
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')
