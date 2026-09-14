Feature: Attualizzazione della commissione SEND su applicazione priva di connettore di verifica

# Copre il ramo di VersamentoUtils.aggiornaVersamento introdotto per l'attualizzazione SEND:
# quando la pendenza non e' piu' valida e l'applicazione non ha un connettore di integrazione,
# al posto della riacquisizione dal gestionale viene interrogato SEND. Gli scenari negativi
# delimitano la condizione: pendenza ancora valida, pendenza non sendAbilitato, pendenza
# scaduta. Il caso con connettore di verifica configurato e' coperto da
# send-pagamento-multibeneficiario-dataValidita-decorsa.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_dominio5.feature')

* configure followRedirects = false

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def addEuro = function(a,b){ return Number((a+b).toFixed(2)) }

# NB: le fixture che confrontano gli importi contengono placeholder su sendFeeEurocent e
# pendenzaPut, e vanno lette al punto d'uso: una read() nel Background congelerebbe il valore
# risolto al primo match, portandolo nello scenario successivo.
* def pathEsitoVerifyPayment = 'classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json'
* def pathEsitoGetPayment = 'classpath:test/workflow/send/v1/msg/getPayment-response-ok-multibeneficiario.json'

* def faultBeanScaduto =
"""
{
	faultCode: "PAA_PAGAMENTO_SCADUTO",
	faultString: '#notnull',
	id: "#(idDominio)",
	description: '#notnull',
	serial: '##null'
}
"""

# Configurazione del connettore SEND sul dominio principale della pendenza

# NB: SendClient accoda '/delivery/v2.3/price/{paTaxId}/{noticeCode}' alla url del connettore,
# quindi qui va configurata solo la base. abilitaGDE=true perche' i test rileggono la risposta
# SEND dagli eventi.
* set dominio.servizioSend = { "url": '#(ndpsym_url + "/pagopa/rs")', "auth": { "headerName": "x-api-key", "headerValue": "test-api-key" }, "abilitaGDE": true }

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Configurazione dell'applicazione SENZA connettore di integrazione: e' la condizione sotto
# esame. Con la data di validita' decorsa GovPay vorrebbe riacquisire la pendenza dal
# gestionale dell'ente, ma non ha un connettore con cui farlo.

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* remove applicazione.servizioIntegrazione

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')


Scenario: Pendenza non valida e sendAbilitato: SEND interrogato, importo attualizzato su verifica e attivazione

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* def pendenzaPutBase = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

# Azzero la cache dei prezzi del simulatore per partire da uno stato noto: al caricamento
# della pendenza SEND non e' stato interrogato.

Given url ndpsym_url + '/pagopa/rs/delivery/v2.3'
And path 'reset'
When method get
Then assert responseStatus == 200

# La pendenza non e' stata caricata nel servizio ENTE (pa-prepara-avviso): senza connettore di
# integrazione GovPay non lo interroga, e se lo facesse il test fallirebbe qui.

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerifica = response
* def ccp = response.ccp

# SEND deve essere stato interrogato una volta, nonostante la verifica non sia stata eseguita

* call read('classpath:utils/send-recupera-fee.feature')
* match numEventiSend == 1
* def sendFeeEurocentVerifica = sendFeeEurocent

# L'importo restituito al PSP deve comprendere le spese di notifica, imputate alla sola voce
# dell'ente proprietario della pendenza: idDominio_5 non deve essere impattato

* set pendenzaPut.importo = addEuro(pendenzaPutBase.importo, sendFeeEuro)
* set pendenzaPut.voci[0].importo = addEuro(pendenzaPutBase.voci[0].importo, sendFeeEuro)
* def importo = pendenzaPut.importo

* match rispostaVerifica == read(pathEsitoVerifyPayment)

# Attivazione: l'importo della RPT deve coincidere con quello della verifica. E' la proprieta'
# che tiene in piedi il pagamento, perche' pagoPA confronta i due valori.

* def tipoRicevuta = "R01"
* def inviaRicevuta = 'false'
* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == read(pathEsitoGetPayment)

# Anche l'attivazione passa dallo stesso ramo, ma SEND non deve essere reinterrogato: il valore
# e' entro la finestra di retention (it.govpay.client.send.retention, default 900000 ms, molto
# piu' ampia dei pochi secondi che separano verifica e attivazione).
# NB: il controllo sul numero di eventi e' necessario perche' il simulatore memoizza il prezzo
# per avviso (Send.java, cache su paTaxId|noticeCode): una seconda interrogazione restituirebbe
# la stessa fee e non sarebbe rilevabile dal confronto degli importi.

* call read('classpath:utils/send-recupera-fee.feature')
* match numEventiSend == 1
* match sendFeeEurocent == sendFeeEurocentVerifica

# La pendenza in base dati conserva l'importo originale: l'attualizzazione e' mantenuta separata
# in versamenti.send_importo_totale ed esposta a parte.
# NB: la GET e' inline e il match e' 'contains', non la utils/api/v1/backoffice/pendenza-get-dettaglio,
# che confronta con == un elenco esaustivo di chiavi in cui i tre campi SEND non compaiono.

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { importo: '#? _ == pendenzaPutBase.importo', sendAbilitato: true, sendImportoTotale: '#? _ == sendFeeEuro', sendDataAggiornamento: '#notnull' }


Scenario: Pendenza ancora valida e sendAbilitato: SEND non interrogato

# La condizione che governa l'attualizzazione e' la stessa della verifica, cioe' la data di
# validita' decorsa. Con la pendenza ancora valida aggiornaVersamento esce dal ramo 'versamento
# valido' e nessuno dei due punti di attualizzazione viene raggiunto.

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* def pendenzaPutBase = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.dataValidita = '2999-12-30'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerifica = response

* call read('classpath:utils/send-conta-eventi.feature')
* match numEventiSend == 0

# L'importo e' quello originale della pendenza, senza spese di notifica

* match rispostaVerifica == read(pathEsitoVerifyPayment)

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { importo: '#? _ == pendenzaPutBase.importo', sendAbilitato: true }
And match response.sendImportoTotale == '#notpresent'
And match response.sendDataAggiornamento == '#notpresent'


Scenario: Pendenza non valida ma non sendAbilitato: SEND non interrogato e comportamento invariato

# Terzo ramo della catena: senza connettore di verifica e senza sendAbilitato si prosegue con la
# pendenza che GovPay ha in base dati, come prima dell'intervento. Con
# it.govpay.context.aggiornamentoValiditaMandatorio a true questo caso solleverebbe invece
# PAA_PAGAMENTO_SCADUTO: la property e' di installazione e non e' modificabile da qui, quindi lo
# scenario vale per la configurazione di default, a false.

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* def pendenzaPutBase = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* remove pendenzaPut.sendAbilitato

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* def rispostaVerifica = response

* call read('classpath:utils/send-conta-eventi.feature')
* match numEventiSend == 0

* match rispostaVerifica == read(pathEsitoVerifyPayment)

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response contains { importo: '#? _ == pendenzaPutBase.importo', sendAbilitato: false }


Scenario: Pendenza scaduta e sendAbilitato: SEND non interrogato

# La data di scadenza decorsa fa uscire aggiornaVersamento per eccezione prima della catena
# sul connettore, quindi l'attualizzazione non viene tentata su una pendenza non piu' pagabile.

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/workflow/send/v1/msg/pendenza-put_multibeneficiario_send.json')
* set pendenzaPut.dataValidita = '1999-12-30'
* set pendenzaPut.dataScadenza = '1999-12-31'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response.faultBean == faultBeanScaduto

* call read('classpath:utils/send-conta-eventi.feature')
* match numEventiSend == 0
