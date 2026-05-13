Feature: Creazione e spedizione tracciati notifica pagamenti con tutti i connettori

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Scenario: Pagamento modello unico, configurazione di tutti i connettori e generazione tracciati

# === Step 1: Configurazione mailBatch con simulatore SMTP (MailHog) ===

* def patchRequest =
"""
[
  {
    "op": "REPLACE",
    "path": "/mailBatch",
    "value": {
      "abilitato": true,
      "mailserver": {
        "host": "host.docker.internal",
        "port": 1025,
        "username": "govpay",
        "password": "123456",
        "from": "noreply.govpay@link.it",
        "readTimeout": 120000,
        "connectionTimeout": 10000,
        "sslConfig": {
          "abilitato": false
        },
        "startTls": false
      }
    }
  }
]
"""

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

# === Step 2: Configurazione dominio con tutti i connettori notifica pagamenti ===
# Connettori abilitati: GovPay (REST), MyPivot (EMAIL), Secim (EMAIL), HyperSicAPKappa (EMAIL)

* def downloadBaseUrl = govpay_url + '/servizioDownloadTracciati'

* def dominio = read('classpath:configurazione/v1/msg/dominio.json')

* set dominio.servizioGovPay.abilitato = true
* set dominio.servizioGovPay.tipoConnettore = 'REST'
* set dominio.servizioGovPay.url = 'http://localhost:8888/enteRendicontazioni'
* set dominio.servizioGovPay.versioneApi = 'REST v1'
* set dominio.servizioGovPay.contenuti = ['RPP', 'SINTESI_PAGAMENTI', 'SINTESI_FLUSSI_RENDICONTAZIONE', 'FLUSSI_RENDICONTAZIONE']
* set dominio.servizioGovPay.tipiPendenza = ['*']
* set dominio.servizioGovPay.downloadBaseUrl = downloadBaseUrl
* set dominio.servizioGovPay.versioneZip = '1.0'
* set dominio.servizioGovPay.intervalloCreazioneTracciato = 1

* set dominio.servizioMyPivot.abilitato = true
* set dominio.servizioMyPivot.tipoConnettore = 'EMAIL'
* set dominio.servizioMyPivot.emailIndirizzi = ['pec@creditore.it']
* set dominio.servizioMyPivot.emailAllegato = true
* set dominio.servizioMyPivot.downloadBaseUrl = downloadBaseUrl
* set dominio.servizioMyPivot.codiceIPA = 'IPA'
* set dominio.servizioMyPivot.versioneCsv = '1.0'
* set dominio.servizioMyPivot.tipiPendenza = ['*']
* set dominio.servizioMyPivot.intervalloCreazioneTracciato = 1

* set dominio.servizioSecim.abilitato = true
* set dominio.servizioSecim.tipoConnettore = 'EMAIL'
* set dominio.servizioSecim.emailIndirizzi = ['pec@creditore.it']
* set dominio.servizioSecim.emailAllegato = true
* set dominio.servizioSecim.downloadBaseUrl = downloadBaseUrl
* set dominio.servizioSecim.versioneCsv = '1.0'
* set dominio.servizioSecim.codiceCliente = '1234567'
* set dominio.servizioSecim.tipiPendenza = ['*']
* set dominio.servizioSecim.intervalloCreazioneTracciato = 1

* set dominio.servizioHyperSicAPKappa.abilitato = true
* set dominio.servizioHyperSicAPKappa.tipoConnettore = 'EMAIL'
* set dominio.servizioHyperSicAPKappa.emailIndirizzi = ['pec@creditore.it']
* set dominio.servizioHyperSicAPKappa.emailAllegato = true
* set dominio.servizioHyperSicAPKappa.downloadBaseUrl = downloadBaseUrl
* set dominio.servizioHyperSicAPKappa.versioneCsv = '2.0'
* set dominio.servizioHyperSicAPKappa.tipiPendenza = ['*']
* set dominio.servizioHyperSicAPKappa.intervalloCreazioneTracciato = 1

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# Configurazione dell'applicazione per integrazione v2

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

# === Step 3: Caricamento pendenza e pagamento con modello unico ===

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

# Caricamento della pendenza

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = pendenzaPut.importo

# Verifica del pagamento (paVerifyPaymentNotice)

* def esitoVerifyPayment = read('classpath:test/workflow/modellounico/v1/msg/verifyPayment-response-ok.json')
* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')
* match response == esitoVerifyPayment
* def ccp = response.ccp
* def ccp_numero_avviso = response.ccp

# Attivazione del pagamento (paGetPayment) con esito positivo

* def tipoRicevuta = "R01"
* def esitoGetPayment = read('classpath:test/workflow/modellounico/v1/msg/getPayment-response-ok.json')
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')
* match response.dati == esitoGetPayment

# Verifica notifica di attivazione

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-attivazione.json')

# Verifica notifica di terminazione

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-terminazione.feature')
* def ccp = ccp_numero_avviso
* match response == read('classpath:test/workflow/modellounico/v1/msg/notifica-terminazione-eseguito.json')

# Verifica stato della pendenza: deve risultare ESEGUITA

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

# === Step 4: Elaborazione tracciati notifica pagamenti per tutti i connettori ===

* call read('classpath:utils/govpay-op-elaborazione-tracciati-notifica-pagamenti.feature')

# === Step 5: Spedizione tracciati notifica pagamenti ===

* call read('classpath:utils/govpay-op-spedizione-tracciati-notifica-pagamenti.feature')

# Attesa per il completamento dell'elaborazione asincrona
* call sleep(60000)
