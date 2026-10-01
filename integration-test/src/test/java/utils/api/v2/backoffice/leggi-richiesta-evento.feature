@ignore
Feature: Legge la richiesta registrata per un evento e ne estrae l'header Authorization

# Utility chiamata da eventi-get.feature con idEvento e unmask. Restituisce
# l'header Authorization dell'evento, o null se quell'evento non ne ha uno:
# serve a cercare, fra gli eventi disponibili, uno su cui il mascheramento sia
# osservabile. Sta qui e non accanto alla feature che la usa perche' sotto
# test/ il runner eseguirebbe anche lei, senza i parametri che si aspetta.

Background:

* callonce read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})

Scenario: Legge la richiesta

Given url consoleBaseurl
And path 'eventi', idEvento, 'richiesta'
And param unmask = unmask
And headers basicAutenticationHeader
When method get
Then status 200
* def authorization = karate.filter(response.headers, function(h){ return h.nome.toLowerCase() == 'authorization' })[0]
