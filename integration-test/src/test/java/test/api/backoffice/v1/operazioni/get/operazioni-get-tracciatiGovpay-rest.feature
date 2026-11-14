Feature: Creazione Tracciato GovPay

Background:

* call read('classpath:utils/common-utils.feature')
* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-govpay.json')

* set dominio.servizioGovPay.tipoConnettore = 'REST'
* set dominio.servizioGovPay.url = 'http://localhost:8888/enteRendicontazioni'
* set dominio.servizioGovPay.versioneApi = 'REST v1'
* set dominio.servizioGovPay.contenuti = ['RPP', 'SINTESI_PAGAMENTI', 'SINTESI_FLUSSI_RENDICONTAZIONE', 'FLUSSI_RENDICONTAZIONE']
* set dominio.servizioGovPay.tipiPendenza = ['*']
* set dominio.servizioGovPay.intervalloCreazioneTracciato = 1

* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def Timestamp = Java.type('java.sql.Timestamp')
* def System = Java.type('java.lang.System')

Scenario Outline: Spedizione tracciati govpay via rest con versioneZip <versioneZip>: il tracciato viene creato con la versione 1.0

* set dominio.servizioGovPay.versioneZip = <versioneZip>
* eval if (<versioneZip> == null) karate.remove('dominio', '$.servizioGovPay.versioneZip')

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
When method get
Then status 200
And match response.servizioGovPay.tipoConnettore == 'REST'
And match response.servizioGovPay.versioneZip == <versioneZipLetta>

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Senza un tracciato precedente l'elaborazione include tutte le RT del dominio ricevute dal 1970, che nella
# testsuite sono migliaia, e il tracciato diventa visibile solo al termine dell'elaborazione. Si registra quindi
# un tracciato GOVPAY gia' caricato che termina un'ora prima dell'ora corrente: con un intervallo di creazione di
# un'ora il nuovo tracciato comprende solo le RT ricevute nell'ora precedente, tra cui una RT spostata a quell'ora.
* def idDominioDb = db.readValue("SELECT id FROM domini WHERE cod_dominio = '" + idDominio + "'")
* def idRpt = db.readValue("SELECT MAX(id) FROM rpt WHERE cod_dominio = '" + idDominio + "' AND stato = 'RT_ACCETTATA_PA' AND cod_esito_pagamento IN (0, 2)")
* match idRpt == '#notnull'

* def Calendar = Java.type('java.util.Calendar')
* def calcolaInizioOraCorrente =
"""
function() {
  var c = Calendar.getInstance();
  c.set(Calendar.MINUTE, 0);
  c.set(Calendar.SECOND, 0);
  c.set(Calendar.MILLISECOND, 0);
  return c.getTimeInMillis();
}
"""
* def inizioOraCorrente = calcolaInizioOraCorrente()
* def fineTracciatoPrecedente = new Timestamp(inizioOraCorrente - 60 * 60 * 1000 - 1)
* def inizioTracciatoPrecedente = new Timestamp(inizioOraCorrente - 2 * 60 * 60 * 1000)
* def nomeTracciatoPrecedente = 'tracciato-precedente-' + getCurrentTimeMillis()

* eval db.update("DELETE FROM trac_notif_pag WHERE tipo = 'GOVPAY' AND id_dominio = " + idDominioDb)
* eval db.update("INSERT INTO trac_notif_pag (nome_file, tipo, versione, stato, data_creazione, data_rt_da, data_rt_a, data_caricamento, data_completamento, identificativo, id_dominio) VALUES (?, 'GOVPAY', '1.0', 'FILE_CARICATO', ?, ?, ?, ?, ?, ?, ?)", nomeTracciatoPrecedente, fineTracciatoPrecedente, inizioTracciatoPrecedente, fineTracciatoPrecedente, fineTracciatoPrecedente, fineTracciatoPrecedente, nomeTracciatoPrecedente, idDominioDb)
* eval db.update("UPDATE rpt SET data_msg_ricevuta = ? WHERE id = ?", new Timestamp(inizioOraCorrente - 30 * 60 * 1000), idRpt)

* call read('classpath:utils/govpay-op-elaborazione-tracciati-notifica-pagamenti.feature')

# L'elaborazione e' asincrona: si attende la creazione del tracciato.
* def queryTracciati = "SELECT versione, stato FROM trac_notif_pag WHERE tipo = 'GOVPAY' AND id_dominio = " + idDominioDb + " AND nome_file <> '" + nomeTracciatoPrecedente + "'"
* def attendiTracciati =
"""
function() {
  for (var i = 0; i < 60; i++) {
    var righe = db.readRows(queryTracciati);
    if (righe.size() > 0) return righe;
    java.lang.Thread.sleep(3000);
  }
  return db.readRows(queryTracciati);
}
"""
* def tracciati = attendiTracciati()
* match tracciati == '#[1]'
* match tracciati[0].versione == '1.0'

* call read('classpath:utils/govpay-op-spedizione-tracciati-notifica-pagamenti.feature')

* call sleep(60000)

Examples:
| versioneZip | versioneZipLetta |
| null        | '#notpresent'    |
| '1.0'       | '1.0'            |
