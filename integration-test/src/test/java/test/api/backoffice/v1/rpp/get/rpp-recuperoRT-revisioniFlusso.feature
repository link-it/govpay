Feature: Recupero RT di un pagamento rendicontato in un flusso con piu' revisioni

# Issue #911. Il flusso di rendicontazione viene acquisito prima della ricevuta, quindi la rendicontazione
# resta in stato ANOMALA senza pagamento. Una seconda revisione dello stesso flusso aggiunge un'altra
# rendicontazione ANOMALA con gli stessi estremi: all'arrivo della ricevuta il pagamento deve essere
# collegato a entrambe. Prima della correzione la ricevuta veniva rifiutata con PAA_SYSTEM_ERROR.
# La seconda revisione viene prodotta sul database come la produce il batch FdR: la revisione precedente
# e' marcata obsoleta e la sua data_ora_flusso spostata indietro di 1ms.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"
* def inviaRicevuta = 'false'

* configure followRedirects = false

* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def Timestamp = Java.type('java.sql.Timestamp')

# Configurazione Dominio 4 Il simulatore non invia la ricevuta per i pagamenti di questo EC ma devono essere recuperate

* callonce read('classpath:configurazione/v1/anagrafica_dominio4.feature')

# Configurazione intermediario al recuperoRT
* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario-recuperoRT.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers gpAdminBasicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Ricevuta recuperata dopo due revisioni del flusso: il pagamento viene collegato a tutte le rendicontazioni

# Configurazione dell'applicazione

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'
* set applicazione.servizioIntegrazione.versioneApi = 'REST v1'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

* def idDominio = idDominio_4
* def versionePagamento = 3

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

* call read('classpath:utils/pa-carica-avviso.feature')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def importo = pendenzaPut.importo
* def ccp = numeroAvviso

* call read('classpath:utils/psp-paVerifyPaymentNotice.feature')

# Attivo il pagamento, il simulatore non invia la ricevuta

* def idCart = getCurrentTimeMillis()
* call read('classpath:utils/psp-paGetPaymentV2.feature')

* def ccp = numeroAvviso
* call read('classpath:utils/pa-notifica-attivazione.feature')

# Genero e acquisisco il flusso di rendicontazione prima della ricevuta

* call sleep(60000)
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* call sleep(60000)
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* call sleep(60000)

* def queryRendicontazioni = "SELECT r.id, r.id_fr, r.stato, r.id_pagamento FROM rendicontazioni r JOIN fr f ON f.id = r.id_fr WHERE f.cod_dominio = '" + idDominio + "' AND r.iuv = '" + iuv + "' ORDER BY r.id"

* def rendicontazioni = db.readRows(queryRendicontazioni)
* match rendicontazioni == '#[1]'
* match rendicontazioni[0].stato == 'ANOMALA'
* match rendicontazioni[0].id_pagamento == '#null'

# Seconda revisione del flusso con la stessa rendicontazione

* def idFr = db.readValue("SELECT id_fr FROM rendicontazioni WHERE id = " + rendicontazioni[0].id)
* def codFlusso = db.readValue("SELECT cod_flusso FROM fr WHERE id = " + idFr)
* def dataOraFlusso = db.readValue("SELECT data_ora_flusso FROM fr WHERE id = " + idFr)
* def nuovaRevisione = db.readValue("SELECT revisione + 1 FROM fr WHERE id = " + idFr)

* eval db.update("UPDATE fr SET obsoleto = ?, data_ora_flusso = ? WHERE id = ?", true, new Timestamp(dataOraFlusso.getTime() - 1), idFr)
* eval db.update("INSERT INTO fr (cod_psp, cod_dominio, cod_flusso, stato, descrizione_stato, iur, data_ora_flusso, data_regolamento, data_acquisizione, numero_pagamenti, importo_totale_pagamenti, cod_bic_riversamento, ragione_sociale_psp, ragione_sociale_dominio, obsoleto, data_ora_pubblicazione, data_ora_aggiornamento, revisione, id_incasso, id_dominio) SELECT cod_psp, cod_dominio, cod_flusso, stato, descrizione_stato, iur, ?, data_regolamento, data_acquisizione, numero_pagamenti, importo_totale_pagamenti, cod_bic_riversamento, ragione_sociale_psp, ragione_sociale_dominio, ?, data_ora_pubblicazione, data_ora_aggiornamento, ?, id_incasso, id_dominio FROM fr WHERE id = ?", dataOraFlusso, false, nuovaRevisione, idFr)
* def idFrRevisione = db.readValue("SELECT id FROM fr WHERE cod_dominio = '" + idDominio + "' AND cod_flusso = '" + codFlusso + "' AND revisione = " + nuovaRevisione)
* eval db.update("INSERT INTO rendicontazioni (iuv, iur, indice_dati, importo_pagato, esito, data, stato, anomalie, esegui_recupero_rt, notifica_inviata, id_fr, id_pagamento, id_singolo_versamento) SELECT iuv, iur, indice_dati, importo_pagato, esito, data, stato, anomalie, esegui_recupero_rt, notifica_inviata, ?, id_pagamento, id_singolo_versamento FROM rendicontazioni WHERE id = " + rendicontazioni[0].id, idFrRevisione)

* def rendicontazioni = db.readRows(queryRendicontazioni)
* match rendicontazioni == '#[2]'
* match each rendicontazioni contains { stato: 'ANOMALA', id_pagamento: '#null' }

# Recupero della ricevuta

* call read('classpath:utils/govpay-op-recupero-rt.feature')
* call sleep(60000)

* configure retry = { count: 25, interval: 20000 }

Given url backofficeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO'
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
And retry until response.numRisultati == 1
When method get
Then status 200
And match response.risultati[0].rt == '#notnull'

# Il pagamento e' collegato a entrambe le rendicontazioni, che passano in stato OK

* def rendicontazioni = db.readRows(queryRendicontazioni)
* match rendicontazioni == '#[2]'
* match each rendicontazioni contains { stato: 'OK', id_pagamento: '#notnull' }
* match rendicontazioni[0].id_pagamento == rendicontazioni[1].id_pagamento

# Ripristino del flusso: rimuovo la revisione aggiunta e riattivo quella originale

* eval db.update("DELETE FROM rendicontazioni WHERE id_fr = ?", idFrRevisione)
* eval db.update("DELETE FROM fr WHERE id = ?", idFrRevisione)
* eval db.update("UPDATE fr SET obsoleto = ?, data_ora_flusso = ? WHERE id = ?", false, dataOraFlusso, idFr)
