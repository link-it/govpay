Feature: Censimento entrate (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/entrate/put/entrate-put.feature e
# entrate-put-sintassi.feature, per la parte di creazione.
#
# Come per i ruoli, la v1 censiva con PUT in upsert e la v2 separa POST e PUT.
# Le API v2 non hanno una DELETE sulle entrate, quindi lo stato iniziale si
# ripristina dal database.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idEntrata = 'ENTRATA_TEST_V2'
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def entrata =
"""
{
  idEntrata: '#(idEntrata)',
  descrizione: 'Entrata di prova v2',
  tipoContabilita: 'ALTRO',
  codiceContabilita: 'CONTAB_V2'
}
"""

Scenario: Aggiunta di una entrata

* eval db.update("DELETE FROM tipi_tributo WHERE cod_tributo = '" + idEntrata + "'")

Given url consoleBaseurl
And path 'entrate'
And headers basicAutenticationHeader
And request entrata
When method post
Then status 201
And match responseHeaders['Location'][0] == '#notnull'
And match responseHeaders['ETag'][0] == '#notnull'
And match response.idEntrata == idEntrata
And match response.descrizione == 'Entrata di prova v2'
And match response.tipoContabilita == 'ALTRO'
And match response.codiceContabilita == 'CONTAB_V2'

Scenario: Aggiunta di una entrata gia' censita

* def creaEntrata = call read('classpath:utils/api/v2/backoffice/crea-entrata.feature') { idEntrata: '#(idEntrata)' }

Given url consoleBaseurl
And path 'entrate'
And headers basicAutenticationHeader
And request entrata
When method post
Then status 409
And match response.status == 409

Scenario Outline: Aggiunta di una entrata senza il campo obbligatorio <campo>

Given url consoleBaseurl
And path 'entrate'
And headers basicAutenticationHeader
And request <body>
When method post
Then status 400

Examples:
| campo | body |
| idEntrata | { descrizione: 'x', tipoContabilita: 'ALTRO', codiceContabilita: 'C' } |
| descrizione | { idEntrata: 'ENTRATA_INCOMPLETA_V2', tipoContabilita: 'ALTRO', codiceContabilita: 'C' } |
| tipoContabilita | { idEntrata: 'ENTRATA_INCOMPLETA_V2', descrizione: 'x', codiceContabilita: 'C' } |
| codiceContabilita | { idEntrata: 'ENTRATA_INCOMPLETA_V2', descrizione: 'x', tipoContabilita: 'ALTRO' } |

Scenario: Aggiunta di una entrata senza autenticazione

Given url consoleBaseurl
And path 'entrate'
And request entrata
When method post
Then status 401
