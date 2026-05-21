Feature: Verifica fix bug raw_esito azzerato al secondo passaggio di elaborazione tracciati

# Riproduce lo scenario in cui un tracciato gia' processato (stepElaborazione=CARICAMENTO_OK) viene
# ripreso da una nuova esecuzione del task ElaborazioneTracciatiPendenze in stato IN_STAMPA: prima
# del fix la chiamata a updateFineElaborazione azzerava la colonna raw_esito perche' il filtro di
# findAll non caricava il campo e l'oggetto in memoria aveva rawEsito=null.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)

# L'endpoint /operazioni/elaborazioneTracciatiPendenze e' sincrono: blocca finche'
# il batch non ha finito di processare tutti i tracciati. Con stampe avvisi multiple
# (134 PDF) il default readTimeout di 20s non basta.
* configure readTimeout = 600000

* def patchValue =
"""
{
	tipo : "freemarker",
	intestazione: "idA2A,idPendenza,idDominio,tipoPendenza,numeroAvviso,pdfAvviso,tipoSoggettoPagatore,identificativoPagatore,anagraficaPagatore,indirizzoPagatore,civicoPagatore,capPagatore,localitaPagatore,provinciaPagatore,nazionePagatore,emailPagatore,cellularePagatore,errore",
	richiesta: null,
	risposta: null
}
"""

* configure retry = { count: 25, interval: 10000 }


@regression-rawesito
Scenario: Bug raw_esito - il file di esito non viene azzerato durante un secondo passaggio in IN_STAMPA

# === Step 1: configurazione tracciato CSV ===
* set patchValue.richiesta = encodeBase64InputStream(read('msg/freemarker-request.ftl'))
* set patchValue.risposta = encodeBase64InputStream(read('msg/freemarker-response.ftl'))

Given url backofficeBaseurl
And path '/configurazioni'
And headers basicAutenticationHeader
And request
"""
[
	{
		op: "REPLACE",
		path: "/tracciatoCsv",
		value: #(patchValue)
	}
]
"""
When method patch
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# === Step 2: caricamento tracciato e attesa completamento ===
* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-avvisi-300-rata-unica.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
And headers { 'X-GOVPAY-FILENAME' : 'regression_rawesito' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

# Forza elaborazione e attendi stato ESEGUITO
Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneTracciatiPendenze'
And headers basicAutenticationHeader
When method get

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response.stampaAvvisi == true

# === Step 3: verifica esito popolato dopo il primo passaggio ===
* def primoPassaggioSize = db.readValue("SELECT length(raw_esito) FROM tracciati WHERE id=" + idTracciato)
* assert primoPassaggioSize != null && primoPassaggioSize > 0

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200
And assert responseBytes != null && responseBytes.length > 0

# === Step 4: forza il "secondo passaggio" via SQL ===
# Riporta il tracciato in IN_STAMPA con stepElaborazione=CARICAMENTO_OK, lasciando raw_esito intatto.
# E' la condizione che si verifica quando il task viene rieseguito (errore in stampa, multi-nodo, ecc).
* def resetSql = "UPDATE tracciati SET stato='IN_STAMPA', data_completamento=NULL, bean_dati=REGEXP_REPLACE(bean_dati, '\"stepElaborazione\":\"[^\"]*\"', '\"stepElaborazione\":\"CARICAMENTO_OK\"') WHERE id=" + idTracciato
* eval db.update(resetSql)

# Verifica che la modifica sia avvenuta
* def statoForzato = db.readValue("SELECT stato FROM tracciati WHERE id=" + idTracciato)
* match statoForzato == 'IN_STAMPA'

# === Step 5: trigger del task per simulare il secondo passaggio ===
Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneTracciatiPendenze'
And param forzaEsecuzione = true
And headers basicAutenticationHeader
When method get

# Attendi che il tracciato torni in ESEGUITO
Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get

# === Step 6: il check del bug — raw_esito deve essere ancora popolato ===
* def secondoPassaggioSize = db.readValue("SELECT length(raw_esito) FROM tracciati WHERE id=" + idTracciato)
* assert secondoPassaggioSize != null && secondoPassaggioSize > 0

# Doppio check via API
Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200
And assert responseBytes != null && responseBytes.length > 0
