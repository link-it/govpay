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

* def numEventiSend = response.risultati.length
* def eventiSendOrdinati = response.risultati.sort((a, b) => a.dataEvento.localeCompare(b.dataEvento))
* def ultimoEventoSend = eventiSendOrdinati[eventiSendOrdinati.length - 1]
* match ultimoEventoSend.esito == 'OK'

* json payloadRispostaSend = decodeBase64(ultimoEventoSend.parametriRisposta.payload)
* def sendFeeEurocent = payloadRispostaSend.totalPrice
* def sendFeeEuro = sendFeeEurocent / 100
