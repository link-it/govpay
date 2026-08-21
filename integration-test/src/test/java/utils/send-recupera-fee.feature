Feature: Recupero dell'ultima commissione SEND interrogata per un avviso

Background:

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario:

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param tipoEvento = 'notificationPriceV23'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And assert response.risultati.length > 0

# il servizio /eventi ordina per id DESC (EventiDAO.getDefaultFilterSortWrapperDesc), quindi il
# primo risultato e' l'interrogazione SEND piu' recente
* def numEventiSend = response.risultati.length
* def ultimoEventoSend = response.risultati[0]
* match ultimoEventoSend.esito == 'OK'

* json payloadRispostaSend = decodeBase64(ultimoEventoSend.parametriRisposta.payload)
* def sendFeeEurocent = payloadRispostaSend.totalPrice
* def sendFeeEuro = sendFeeEurocent / 100