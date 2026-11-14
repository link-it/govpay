Feature: Data di pubblicazione della pendenza

# Issue #914. Fino alla data di pubblicazione l'avviso non esiste per pagoPA (PAA_PAGAMENTO_SCONOSCIUTO),
# la pendenza risulta NON_PUBBLICATA e il batch ACA la invia alla data (data_ultima_modifica_aca);
# i promemoria non partono prima. Controlli: data antecedente alla scadenza (VER_045), non oltre
# it.govpay.pendenza.dataPubblicazione.giorniMassimi giorni, default 7 (VER_046), nessuna data futura
# su una pendenza gia' pubblicata (VER_047). L'applicazione e' senza connettore di verifica: con la data
# di validita' decorsa GovPay usa la pendenza che ha in base dati.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_dominio5.feature')

* configure followRedirects = false

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

* def giorno = function(n){ return '' + Java.type('java.time.LocalDate').now().plusDays(n).toString() }
* def oggi = giorno(0)
* def domani = giorno(1)
* def dopodomani = giorno(2)

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* remove applicazione.servizioIntegrazione

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def nuovaPendenza =
"""
function(dataPubblicazione) {
	var p = karate.read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json');
	p.sendAbilitato = false;
	if (dataPubblicazione) p.dataPubblicazione = dataPubblicazione;
	return p;
}
"""


Scenario: Data di pubblicazione futura: pendenza non pubblicata, inesistente per pagoPA, inviata all'ACA alla data

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call nuovaPendenza domani

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response contains { dataPubblicazione: '#(domani)', stato: 'NON_PUBBLICATA' }

# La data ACA slitta alla data di pubblicazione: il batch invia la pendenza a quella data.
# NB: anche data_notifica_avviso slitta (VersamentoUtils.applicaDataPubblicazioneAiPromemoria), ma le API
# non la espongono in lettura.

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { dataPubblicazione: '#(domani)', stato: 'NON_PUBBLICATA' }
And match response.dataUltimaModificaAca contains domani

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean contains { faultCode: 'PAA_PAGAMENTO_SCONOSCIUTO' }

# Ricerca per stato: la pendenza e' tra le non pubblicate e non tra quelle da pagare

Given url backofficeBaseurl
And path '/pendenze'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param stato = 'NON_PUBBLICATA'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.risultati[*].idPendenza == ['#(""+idPendenza)']

Given url backofficeBaseurl
And path '/pendenze'
And param idA2A = idA2A
And param idPendenza = idPendenza
And param stato = 'NON_ESEGUITA'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.risultati == []


Scenario: Data di pubblicazione odierna: pendenza pubblicata e pagabile

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call nuovaPendenza oggi

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.esitoVerifica.outcome == 'OK'

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { dataPubblicazione: '#(oggi)', stato: 'NON_ESEGUITA' }


Scenario: Aggiornamento della pendenza non pubblicata: la data si sposta, e senza il campo resta quella salvata

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call nuovaPendenza domani

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* set pendenzaPut.dataPubblicazione = dopodomani

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200

* remove pendenzaPut.dataPubblicazione

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 200

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { dataPubblicazione: '#(dopodomani)', stato: 'NON_PUBBLICATA' }
And match response.dataUltimaModificaAca contains dopodomani


Scenario: Data di pubblicazione futura su una pendenza gia' pubblicata: rifiutata (VER_047)

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call nuovaPendenza null

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* set pendenzaPut.dataPubblicazione = domani

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_047' }


Scenario: Data di pubblicazione non antecedente alla scadenza: rifiutata (VER_045)

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call nuovaPendenza domani
* set pendenzaPut.dataScadenza = domani

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_045' }


Scenario: Data di pubblicazione oltre il numero massimo di giorni: rifiutata (VER_046)

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call nuovaPendenza giorno(8)

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response contains { categoria: 'RICHIESTA', codice: 'VER_046' }

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = call nuovaPendenza giorno(7)

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201


Scenario: Caricamento dal Backoffice con data di pubblicazione

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/backoffice/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.dataPubblicazione = domani

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { dataPubblicazione: '#(domani)', stato: 'NON_PUBBLICATA' }
