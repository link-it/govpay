Feature: Censimento tipi pendenza (console-api v2)

# Corrispettivo v2 di
# test/api/backoffice/v1/tipipendenza/put/tipipendenza-put.feature e
# tipipendenza-put-sintassi.feature, per la parte di creazione.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idTipoPendenza = 'TIPO_TEST_V2'
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)

Scenario: Aggiunta di un tipo pendenza

* eval db.update("DELETE FROM tipi_vers_domini WHERE id_tipo_versamento IN (SELECT id FROM tipi_versamento WHERE cod_tipo_versamento = '" + idTipoPendenza + "')")
* eval db.update("DELETE FROM tipi_versamento WHERE cod_tipo_versamento = '" + idTipoPendenza + "'")

Given url consoleBaseurl
And path 'tipiPendenza'
And headers basicAutenticationHeader
And request { idTipoPendenza: '#(idTipoPendenza)', descrizione: 'Tipo di prova v2' }
When method post
Then status 201
And match responseHeaders['Location'][0] == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'
And match response.idTipoPendenza == idTipoPendenza
And match response.descrizione == 'Tipo di prova v2'
# I default dichiarati nello schema devono comparire nella rappresentazione
And match response.abilitato == true
And match response.pagaTerzi == false

Scenario: Aggiunta di un tipo pendenza gia' censito

* def creaTipo = call read('classpath:utils/api/v2/backoffice/crea-tipo-pendenza.feature') { idTipoPendenza: '#(idTipoPendenza)' }

Given url consoleBaseurl
And path 'tipiPendenza'
And headers basicAutenticationHeader
And request { idTipoPendenza: '#(idTipoPendenza)', descrizione: 'Tipo di prova v2' }
When method post
Then status 409
And match response.status == 409

Scenario Outline: Aggiunta di un tipo pendenza senza il campo obbligatorio <campo>

Given url consoleBaseurl
And path 'tipiPendenza'
And headers basicAutenticationHeader
And request <body>
When method post
Then status 400

Examples:
| campo | body |
| idTipoPendenza | { descrizione: 'x' } |
| descrizione | { idTipoPendenza: 'TIPO_INCOMPLETO_V2' } |

Scenario Outline: Sintassi errata nel campo codificaIUV (<valore>)

# Lo schema vuole da una a tre cifre.

Given url consoleBaseurl
And path 'tipiPendenza'
And headers basicAutenticationHeader
And request { idTipoPendenza: 'TIPO_SINTASSI_V2', descrizione: 'x', codificaIUV: '<valore>' }
When method post
Then status 400

Examples:
| valore |
| 1234 |
| abc |
| -1 |

Scenario: Aggiunta di un tipo pendenza senza autenticazione

Given url consoleBaseurl
And path 'tipiPendenza'
And request { idTipoPendenza: '#(idTipoPendenza)', descrizione: 'x' }
When method post
Then status 401
