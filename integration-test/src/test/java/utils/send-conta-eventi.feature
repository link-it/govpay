Feature: Conteggio delle interrogazioni al servizio SEND per un avviso

# Gemella di send-recupera-fee, ma senza l'assert sulla presenza di risultati e senza la
# lettura del payload: serve per asserire che SEND NON sia stato interrogato. Espone
# numEventiSend, letto da numRisultati e non dalla lunghezza della pagina, che si fermerebbe
# a risultatiPerPagina.

Background:

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario:

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'notificationPriceV23'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200

* def numEventiSend = response.numRisultati
