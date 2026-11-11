Feature: Creazione Tracciato GovPay

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-govpay.json')

* set dominio.servizioGovPay.tipoConnettore = 'REST'
* set dominio.servizioGovPay.url = 'http://localhost:8888/enteRendicontazioni'
* set dominio.servizioGovPay.versioneApi = 'REST v1'
* set dominio.servizioGovPay.contenuti = ['RPP', 'SINTESI_PAGAMENTI', 'SINTESI_FLUSSI_RENDICONTAZIONE', 'FLUSSI_RENDICONTAZIONE']
* set dominio.servizioGovPay.tipiPendenza = ['*']

* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def Timestamp = Java.type('java.sql.Timestamp')
* def System = Java.type('java.lang.System')

Scenario Outline: Spedizione tracciati govpay via rest con versioneZip <versioneZip>: il tracciato viene creato con la versione 1.0

* set dominio.servizioGovPay.versioneZip = <versioneZip>
* if (<versioneZip> == null) karate.remove('dominio', '$.servizioGovPay.versioneZip')

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

# Il tracciato viene creato solo se dall'ultimo tracciato GOVPAY del dominio e' trascorso l'intervallo configurato
# e se ci sono RT ricevute prima dell'ora corrente: si eliminano i tracciati GOVPAY del dominio e si anticipa di un giorno
# la data di una RT, cosi' l'elaborazione crea sempre un nuovo tracciato.
* def idDominioDb = db.readValue("SELECT id FROM domini WHERE cod_dominio = '" + idDominio + "'")
* def idRpt = db.readValue("SELECT MAX(id) FROM rpt WHERE cod_dominio = '" + idDominio + "' AND stato = 'RT_ACCETTATA_PA'")
* match idRpt == '#notnull'
* eval db.update("DELETE FROM trac_notif_pag WHERE tipo = 'GOVPAY' AND id_dominio = " + idDominioDb)
* eval db.update("UPDATE rpt SET data_msg_ricevuta = ? WHERE id = ?", new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000), idRpt)

* call read('classpath:utils/govpay-op-elaborazione-tracciati-notifica-pagamenti.feature')

* def tracciati = db.readRows("SELECT versione, stato FROM trac_notif_pag WHERE tipo = 'GOVPAY' AND id_dominio = " + idDominioDb)
* match tracciati == '#[1]'
* match tracciati[0].versione == '1.0'

* call read('classpath:utils/govpay-op-spedizione-tracciati-notifica-pagamenti.feature')

* call sleep(60000)

Examples:
| versioneZip | versioneZipLetta |
| null        | '##null'         |
| '1.0'       | '1.0'            |
