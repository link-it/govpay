Feature: Caricamento tracciato CSV

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

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

@test1
Scenario: Caricamento di un tracciato in formato CSV valido con rata unica, rate e bollettino

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
And headers { 'X-GOVPAY-FILENAME' : 'test1_avvisi_con_rata_unica' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneTracciatiPendenze'
And headers basicAutenticationHeader
When method get

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 134
Then match response.numeroOperazioniEseguite == 134
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 134
Then match response.numeroAvvisiStampati == 134
Then match response.numeroAvvisiFalliti == 0
Then match response.stampaAvvisi == true

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200


@test2
Scenario: Caricamento di un tracciato in formato CSV valido con solo rate e bollettino

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

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-avvisi-300-no-rata-unica.csv')
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
And headers { 'X-GOVPAY-FILENAME' : 'test2_avvisi_solo_rate' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneTracciatiPendenze'
And headers basicAutenticationHeader
When method get

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 119
Then match response.numeroOperazioniEseguite == 119
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 119
Then match response.numeroAvvisiStampati == 119
Then match response.numeroAvvisiFalliti == 0
Then match response.stampaAvvisi == true

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200


@test3
Scenario: Caricamento di un tracciato in formato CSV valido con rata unica, rate e senza bollettino postale

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

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-avvisi-300-rata-unica.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", '');
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
And headers { 'X-GOVPAY-FILENAME' : 'test3_avvisi_con_rata_unica_no_bollettino' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneTracciatiPendenze'
And headers basicAutenticationHeader
When method get

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 134
Then match response.numeroOperazioniEseguite == 134
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 134
Then match response.numeroAvvisiStampati == 134
Then match response.numeroAvvisiFalliti == 0
Then match response.stampaAvvisi == true

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200


@test4
Scenario: Caricamento di un tracciato in formato CSV valido con solo rate e senza bollettino postale

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

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-avvisi-300-no-rata-unica.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", '');
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
And headers { 'X-GOVPAY-FILENAME' : 'test4_avvisi_solo_rate_no_bollettino' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneTracciatiPendenze'
And headers basicAutenticationHeader
When method get

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 119
Then match response.numeroOperazioniEseguite == 119
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 119
Then match response.numeroAvvisiStampati == 119
Then match response.numeroAvvisiFalliti == 0
Then match response.stampaAvvisi == true

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200
