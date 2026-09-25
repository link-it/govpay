Feature: Dettaglio di una riconciliazione (console-api v2)

# Corrispettivo v2 di test/api/backoffice/v1/riconciliazioni/get/
# riconciliazione-applicazione-get.feature, riconciliazione-operatore-get.feature
# e riconciliazione-applicazione-getbyTipoRiscossione.feature.
#
# ATTENZIONE, un limite dell'API e non del test: l'identificativo nel percorso
# e' vincolato al pattern ^[0-9A-Za-z]{1,35}$, che non ammette il trattino,
# mentre gli identificativi che l'elenco restituisce per le riconciliazioni
# acquisite dal flusso hanno la forma 2026-08-22GovPAYPsp1-0850160128, con i
# trattini. Il dettaglio di quelle riconciliazioni e' quindi irraggiungibile:
# GET risponde 400 sul parametro di percorso, non 200 ne' 404.
#
# Gli scenari qui sotto verificano cio' che si puo' verificare: il rifiuto
# dell'identificativo non conforme, e il 404 su un identificativo conforme ma
# inesistente. Il dettaglio di una riconciliazione vera e la registrazione con
# PUT restano da coprire quando ci sara' una riconciliazione con un
# identificativo che l'API accetta.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def idDominio = '12345678901'

Scenario: Lettura con un identificativo non conforme al pattern

Given url consoleBaseurl
And path 'riconciliazioni', idDominio, '2026-08-22GovPAYPsp1-0850160128'
And headers basicAutenticationHeader
When method get
Then status 400
And match response.status == 400

Scenario: Lettura di una riconciliazione inesistente con identificativo conforme

Given url consoleBaseurl
And path 'riconciliazioni', idDominio, 'RICONCILIAZIONECHENONESISTE'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Lettura con un dominio inesistente

Given url consoleBaseurl
And path 'riconciliazioni', '99999999999', 'RICONCILIAZIONEQUALSIASI'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 404 || responseStatus == 400

Scenario: Lettura senza autenticazione

Given url consoleBaseurl
And path 'riconciliazioni', idDominio, 'RICONCILIAZIONEQUALSIASI'
When method get
Then status 401
