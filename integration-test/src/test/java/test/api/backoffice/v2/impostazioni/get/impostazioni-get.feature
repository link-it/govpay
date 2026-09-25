Feature: Impostazioni di sistema (console-api v2)

# Corrispettivo v2 delle nove feature di
# test/api/backoffice/v1/configurazione/, che in v1 agivano tutte sulla stessa
# risorsa /configurazioni con una PATCH per area.
#
# In v2 le impostazioni sono divise per area, ciascuna con il proprio percorso,
# e /impostazioni e' l'indice che le elenca con i rispettivi href. Le aree sono
# otto: servizio GDE, giornale eventi, server di posta e relativo template,
# server App IO e relativo template, tracciati CSV e hardening.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Indice delle aree di impostazioni

Given url consoleBaseurl
And path 'impostazioni'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.aree == '#[_ > 0]'
And match each response.aree contains { codice: '#string', nome: '#string', href: '#string' }
* def codici = $response.aree[*].codice
And match codici contains ['servizioGDE', 'giornale-eventi', 'mail-server', 'app-io-server', 'tracciati-csv', 'hardening']

Scenario Outline: Lettura dell'area <area>

Given url consoleBaseurl
And path <percorso>
And headers basicAutenticationHeader
When method get
Then status 200
And match responseHeaders['ETag'][0] == '#notnull'

Examples:
| area | percorso |
| servizio GDE | 'impostazioni', 'servizioGDE' |
| giornale eventi | 'impostazioni', 'giornale-eventi' |
| server di posta | 'impostazioni', 'mail', 'server' |
| template promemoria posta | 'impostazioni', 'mail', 'template-promemoria' |
| server App IO | 'impostazioni', 'app-io', 'server' |
| template promemoria App IO | 'impostazioni', 'app-io', 'template-promemoria' |
| tracciati CSV | 'impostazioni', 'tracciati-csv' |
| hardening | 'impostazioni', 'hardening' |

Scenario: Le aree con credenziali non le espongono in lettura

# La password del server di posta non e' leggibile: la rappresentazione dice
# soltanto se e' impostata.

Given url consoleBaseurl
And path 'impostazioni', 'mail', 'server'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.password == '#notpresent'
And match response.passwordImpostata == '#boolean'

Scenario: Lettura di un'area inesistente

Given url consoleBaseurl
And path 'impostazioni', 'area-che-non-esiste'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'impostazioni'
When method get
Then status 401
