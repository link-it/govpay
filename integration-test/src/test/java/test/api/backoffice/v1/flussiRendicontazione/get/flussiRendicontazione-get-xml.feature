Feature: Lettura in formato XML del dettaglio di un flusso di rendicontazione

# Copre la GET del dettaglio del flusso con Accept: application/xml (Issue #881).
# I flussi acquisiti tramite il batch FdR esterno non hanno il tracciato originale su fr.xml, che quindi
# va ricostruito a partire dai dati del flusso e dalle rendicontazioni: il primo scenario copre questo
# caso, il secondo il caso in cui la colonna sia valorizzata. Lo stato di fr.xml viene forzato dal test,
# per non dipendere dalla modalita' con cui il flusso e' stato acquisito, e ripristinato alla fine.

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v2.feature')

* callonce sleep(10000)

* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def bytesToString = function(bytes){ return new java.lang.String(bytes, 'UTF-8') }

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Ricostruzione dell'XML del flusso quando il tracciato originale non e' presente

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_dominio1e2.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# dettaglio in formato json, i cui dati vengono usati come riferimento per il tracciato ricostruito

Given url backofficeBaseurl
And path 'flussiRendicontazione', idflusso_dom1_1
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idflusso_dom1_1

* def dataFlusso = response.dataFlusso
* def idDominioFlusso = response.idDominio
* def rendicontazioni = response.rendicontazioni

# azzeramento del tracciato originale, come per i flussi acquisiti dal batch FdR esterno

* def idFr = db.readValue("SELECT id FROM fr WHERE cod_flusso = '" + idflusso_dom1_1 + "' AND cod_dominio = '" + idDominioFlusso + "'")
* def xmlIniziale = db.readValue("SELECT xml FROM fr WHERE id = " + idFr)

* eval db.update("UPDATE fr SET xml = NULL WHERE id = " + idFr)
* assert db.readValue("SELECT xml FROM fr WHERE id = " + idFr) == null

# il tracciato viene ricostruito a partire dai dati del flusso e dalle rendicontazioni

Given url backofficeBaseurl
And path 'flussiRendicontazione', idDominioFlusso, idflusso_dom1_1, dataFlusso
And header Accept = 'application/xml'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response /FlussoRiversamento/versioneOggetto == '1.0'
And match response /FlussoRiversamento/identificativoFlusso == idflusso_dom1_1
And match response /FlussoRiversamento/istitutoRicevente/identificativoUnivocoRicevente/codiceIdentificativoUnivoco == idDominioFlusso

# un elemento datiSingoliPagamenti per ogni rendicontazione del flusso, con i dati della riscossione

* def numeroDatiSingoliPagamenti = karate.xmlPath(response, 'count(/FlussoRiversamento/datiSingoliPagamenti)')
* assert numeroDatiSingoliPagamenti == rendicontazioni.length

* def xmlRicostruito = bytesToString(responseBytes)
* def rendicontazioneNonPresente =
"""
function(xml, list) {
	for (var i = 0; i < list.length; i++) {
		var r = list[i];
		if (xml.indexOf('<identificativoUnivocoVersamento>' + r.iuv + '</identificativoUnivocoVersamento>') < 0) return r.iuv;
		if (xml.indexOf('<identificativoUnivocoRiscossione>' + r.iur + '</identificativoUnivocoRiscossione>') < 0) return r.iur;
	}
	return null;
}
"""
* assert rendicontazioneNonPresente(xmlRicostruito, rendicontazioni) == null

# gli importi devono essere espressi con due cifre decimali, come previsto dal tracciato

* def importoTotale = karate.xmlPath(response, '/FlussoRiversamento/importoTotalePagamenti')
* match importoTotale == '#regex \\d+\\.\\d\\d'

* def singoloImportoPagato = karate.xmlPath(response, '/FlussoRiversamento/datiSingoliPagamenti[1]/singoloImportoPagato')
* match singoloImportoPagato == '#regex \\d+\\.\\d\\d'

# anche le altre forme della risorsa ricostruiscono il tracciato

Given url backofficeBaseurl
And path 'flussiRendicontazione', idflusso_dom1_1
And header Accept = 'application/xml'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response /FlussoRiversamento/identificativoFlusso == idflusso_dom1_1

Given url backofficeBaseurl
And path 'flussiRendicontazione', idflusso_dom1_1, dataFlusso
And header Accept = 'application/xml'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response /FlussoRiversamento/identificativoFlusso == idflusso_dom1_1

# la lettura in formato json non deve essere influenzata dall'assenza del tracciato

Given url backofficeBaseurl
And path 'flussiRendicontazione', idflusso_dom1_1
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.idFlusso == idflusso_dom1_1
And assert response.rendicontazioni.length == rendicontazioni.length

# ripristino dello stato iniziale del tracciato

* eval if (xmlIniziale != null) db.update("UPDATE fr SET xml = ? WHERE id = ?", xmlIniziale, idFr)

Scenario: Lettura in formato XML del flusso con tracciato originale presente

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request read('msg/applicazione_dominio1e2.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'flussiRendicontazione', idflusso_dom1_1
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def dataFlusso = response.dataFlusso
* def idDominioFlusso = response.idDominio

* def idFr = db.readValue("SELECT id FROM fr WHERE cod_flusso = '" + idflusso_dom1_1 + "' AND cod_dominio = '" + idDominioFlusso + "'")
* def xmlIniziale = db.readValue("SELECT xml FROM fr WHERE id = " + idFr)

# il tracciato restituito viene persistito su fr.xml, per poter verificare anche il caso in cui il flusso
# sia stato acquisito con il tracciato originale

* eval db.update("UPDATE fr SET xml = NULL WHERE id = " + idFr)

Given url backofficeBaseurl
And path 'flussiRendicontazione', idDominioFlusso, idflusso_dom1_1, dataFlusso
And header Accept = 'application/xml'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* def tracciato = responseBytes
* eval db.update("UPDATE fr SET xml = ? WHERE id = ?", tracciato, idFr)
* assert db.readValue("SELECT xml FROM fr WHERE id = " + idFr) != null

# il tracciato memorizzato viene restituito cosi' com'e', nelle tre forme della risorsa

Given url backofficeBaseurl
And path 'flussiRendicontazione', idDominioFlusso, idflusso_dom1_1, dataFlusso
And header Accept = 'application/xml'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And assert bytesToString(responseBytes).equals(bytesToString(tracciato))

Given url backofficeBaseurl
And path 'flussiRendicontazione', idflusso_dom1_1
And header Accept = 'application/xml'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And assert bytesToString(responseBytes).equals(bytesToString(tracciato))

Given url backofficeBaseurl
And path 'flussiRendicontazione', idflusso_dom1_1, dataFlusso
And header Accept = 'application/xml'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And assert bytesToString(responseBytes).equals(bytesToString(tracciato))

# ripristino dello stato iniziale del tracciato

* eval db.update("UPDATE fr SET xml = NULL WHERE id = " + idFr)
* eval if (xmlIniziale != null) db.update("UPDATE fr SET xml = ? WHERE id = ?", xmlIniziale, idFr)
