Feature: Sotto-risorse di un dominio (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/domini/get/: uo-find, unita-get,
# iban-find, iban-get, entrate-find, entrate-get, tipipendenza-find,
# tipipendenza-get. In v1 erano otto feature, una per sotto-risorsa e per
# operazione; qui sono uno scenario parametrico sull'elenco e uno sul dettaglio,
# perche' le quattro sotto-risorse hanno la stessa forma.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '12345678901'

Scenario Outline: Elenco paginato della sotto-risorsa <sottorisorsa>

Given url consoleBaseurl
And path 'domini', idDominio, '<sottorisorsa>'
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	results: '#[]',
	pagination: { page: '#number', limit: '#number', hasNextPage: '#boolean' }
}
"""

Examples:
| sottorisorsa |
| unitaOperative |
| contiAccredito |
| entrate |
| tipiPendenza |

Scenario Outline: Elenco della sotto-risorsa <sottorisorsa> di un dominio inesistente

Given url consoleBaseurl
And path 'domini', '99999999999', '<sottorisorsa>'
And headers basicAutenticationHeader
When method get
Then status 404

Examples:
| sottorisorsa |
| unitaOperative |
| contiAccredito |
| entrate |
| tipiPendenza |

Scenario Outline: Dettaglio di <sottorisorsa> inesistente

Given url consoleBaseurl
And path 'domini', idDominio, '<sottorisorsa>', '<identificativo>'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 404 || responseStatus == 400

Examples:
| sottorisorsa | identificativo |
| unitaOperative | UO_CHE_NON_ESISTE |
| entrate | ENTRATA_CHE_NON_ESISTE |
| tipiPendenza | TIPO_CHE_NON_ESISTE |

Scenario: Dettaglio di una unita' operativa censita

Given url consoleBaseurl
And path 'domini', idDominio, 'unitaOperative'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def uo = response.results[0]

Given url consoleBaseurl
And path 'domini', idDominio, 'unitaOperative', uo.idUnitaOperativa
And headers basicAutenticationHeader
When method get
Then status 200
And match response.idUnitaOperativa == uo.idUnitaOperativa
And match responseHeaders['ETag'][0] == '#notnull'

Scenario: Dettaglio di un conto di accredito censito

Given url consoleBaseurl
And path 'domini', idDominio, 'contiAccredito'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def conto = response.results[0]

Given url consoleBaseurl
And path 'domini', idDominio, 'contiAccredito', conto.ibanAccredito
And headers basicAutenticationHeader
When method get
Then status 200
And match response.ibanAccredito == conto.ibanAccredito

Scenario: Elenco senza autenticazione

Given url consoleBaseurl
And path 'domini', idDominio, 'unitaOperative'
When method get
Then status 401
