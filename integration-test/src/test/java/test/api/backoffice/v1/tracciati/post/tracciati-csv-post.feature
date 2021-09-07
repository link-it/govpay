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

* def importo = 122.5
* def importo_voce = 61.25

@test1
Scenario: Caricamento di un tracciato in formato CSV valido

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 4
Then match response.numeroAvvisiStampati == 4
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
Scenario: Caricamento di un tracciato in formato CSV con duplicati

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-duplicati.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
And retry until response.stato == 'ESEGUITO_CON_ERRORI'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 2
Then match response.numeroOperazioniEseguite == 1
Then match response.numeroOperazioniFallite == 1

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
Scenario: Caricamento di un tracciato in formato CSV con linee vuote

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-lineavuota.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
And retry until response.stato == 'ESEGUITO_CON_ERRORI'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 5
Then match response.numeroOperazioniEseguite == 2
Then match response.numeroOperazioniFallite == 3

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
Scenario: Caricamento di un tracciato in formato CSV vuoto

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

* def tracciato = ''

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
And retry until response.stato == 'SCARTATO'
When method get
Then match response.descrizioneStato contains 'vuoto'
Then match response.numeroOperazioniTotali == 0
Then match response.numeroOperazioniEseguite == 0
Then match response.numeroOperazioniFallite == 0

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 404

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 404

@test5
Scenario: Caricamento di un tracciato in formato CSV contenente solo l'intestazione

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

* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-solo-intestazione.csv')

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 0
Then match response.numeroOperazioniEseguite == 0
Then match response.numeroOperazioniFallite == 0

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

@test6
Scenario: Caricamento di un tracciato in formato CSV contenente una pendenza con errore di sintassi

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", 'codEntrataSegreteriacodEntrataSegreteriacodEntrataSegreteria');
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
And retry until response.stato == 'ESEGUITO_CON_ERRORI'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniFallite == 4
Then match response.numeroOperazioniEseguite == 0

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

@test7
Scenario: Caricamento di un tracciato in formato CSV contenente una pendenza con errore di validazione

* def importo_voce = 10.01

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codLibero);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
And retry until response.stato == 'ESEGUITO_CON_ERRORI'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniFallite == 1
Then match response.numeroOperazioniEseguite == 3

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

@test8
Scenario: Caricamento di un tracciato in formato CSV valido contenente 2 pendenze

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
* call sleep(200)
* def idPendenza2 = getCurrentTimeMillis()

* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-multipendenza.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{tipoPendenza}", codLibero);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo);

* def tracciato = replace(tracciato,"{idA2A_p2}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza_p2}", idPendenza2);
* def tracciato = replace(tracciato,"{idDominio_p2}", idDominio);
* def tracciato = replace(tracciato,"{ibanAccredito_p2}", ibanAccredito);
* def tracciato = replace(tracciato,"{tipoPendenza_p2}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo_p2}", importo);
* def tracciato = replace(tracciato,"{importo_voce_p2}", importo);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 2
Then match response.numeroOperazioniEseguite == 2
Then match response.numeroOperazioniFallite == 0


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

@test9
Scenario: Caricamento di un tracciato in formato CSV valido contenente 2 pendenze di cui una con errori di sintassi

* def importo_voce2 = 10.01

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
* call sleep(200)
* def idPendenza2 = getCurrentTimeMillis()

* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-multipendenza.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{tipoPendenza}", codLibero);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo);

* def tracciato = replace(tracciato,"{idA2A_p2}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza_p2}", idPendenza2);
* def tracciato = replace(tracciato,"{idDominio_p2}", idDominio);
* def tracciato = replace(tracciato,"{ibanAccredito_p2}", ibanAccredito);
* def tracciato = replace(tracciato,"{tipoPendenza_p2}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo_p2}", importo);
* def tracciato = replace(tracciato,"{importo_voce_p2}", importo_voce2);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
And retry until response.stato == 'ESEGUITO_CON_ERRORI'
When method get
Then match response contains { descrizioneStato: '##null' }
Then match response.numeroOperazioniTotali == 2
Then match response.numeroOperazioniEseguite == 1
Then match response.numeroOperazioniFallite == 1

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

@test10
Scenario: Caricamento di un tracciato in formato CSV valido ma con template di trasformazione della richiesta corrotto

* set patchValue.richiesta =  encodeBase64InputStream(read('msg/freemarker-request-errato.ftl'))
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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
And retry until response.stato == 'ESEGUITO_CON_ERRORI'
When method get
Then match response contains { descrizioneStato: '##null' }
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 0
Then match response.numeroOperazioniFallite == 4

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

@test11
Scenario: Caricamento di un tracciato in formato CSV valido ma con template di trasformazione della risposta corrotto

* set patchValue.richiesta =  encodeBase64InputStream(read('msg/freemarker-request.ftl'))
* set patchValue.risposta = encodeBase64InputStream(read('msg/freemarker-response-errato.ftl'))

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
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)

* def idPendenza = getCurrentTimeMillis()
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers basicAutenticationHeader
When method get
Then status 200
Then match response contains '#("inserita con esito \'ESEGUITO_OK\': scrittura dell\'esito sul file csv conclusa con con errore.")' 

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

@test12
Scenario: Caricamento di un tracciato in formato CSV con header Content-Type errato 

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

* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 415

@test13
Scenario: Caricamento di un tracciato in formato CSV valido con separatore non standard

* set patchValue.richiesta = encodeBase64InputStream(read('msg/freemarker-request-separatore.ftl'))
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
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)

* def idPendenza = getCurrentTimeMillis()
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-separatore.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 1
Then match response.numeroOperazioniEseguite == 1
Then match response.numeroOperazioniFallite == 0

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

@test14
Scenario: Caricamento di un tracciato in formato CSV valido versione 2

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v2.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 68
Then match response.numeroOperazioniEseguite == 68
Then match response.numeroOperazioniFallite == 0

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

@test15
Scenario: Caricamento di un tracciato in formato CSV valido versione 2 due volte consecutive 

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v2.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 68
Then match response.numeroOperazioniEseguite == 68
Then match response.numeroOperazioniFallite == 0

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

# ripeto il caricamento

* def tracciato2 = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v2.csv')
* def tracciato2 = replace(tracciato2,"{idA2A}", idA2A);
* def tracciato2 = replace(tracciato2,"{idPendenza}", idPendenza);
* def tracciato2 = replace(tracciato2,"{idDominio}", idDominio);
* def tracciato2 = replace(tracciato2,"{numeroAvviso}", numeroAvviso);
* def tracciato2 = replace(tracciato2,"{ibanAccredito}", ibanAccredito);
* def tracciato2 = replace(tracciato2,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato2 = replace(tracciato2,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato2 = replace(tracciato2,"{importo}", importo);
* def tracciato2 = replace(tracciato2,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
And headers basicAutenticationHeader
And request tracciato2
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
Then match response.numeroOperazioniTotali == 68
Then match response.numeroOperazioniEseguite == 68
Then match response.numeroOperazioniFallite == 0

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

@test16
Scenario: Caricamento di un tracciato in formato CSV valido con pagamenti con soglia temporale

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v4.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 5
Then match response.numeroOperazioniEseguite == 5
Then match response.numeroOperazioniFallite == 0

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

@test17
Scenario: Caricamento di un tracciato in formato CSV valido e cancellazione

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-annullamento.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0

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

* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-annullamento-del.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0

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


@test18
Scenario: Caricamento di un tracciato in formato CSV valido dove non viene richiesta la stampa degli avvisi

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And param stampaAvvisi = false
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 0
Then match response.numeroAvvisiStampati == 0
Then match response.numeroAvvisiFalliti == 0
Then match response.stampaAvvisi == false

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers basicAutenticationHeader
When method get
Then status 404
And match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
And match response.dettaglio contains '#("La stampa degli avvisi di pagamento non e\' stata richiesta per il tracciato")' 

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

@test19
Scenario: Caricamento di un tracciato in formato CSV valido con pagamenti con soglia temporale

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v4.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 5
Then match response.numeroOperazioniEseguite == 5
Then match response.numeroOperazioniFallite == 0

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


@test20
Scenario: Caricamento di un tracciato in formato CSV valido con pagamenti con sole rate

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v5.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 7
Then match response.numeroOperazioniEseguite == 7
Then match response.numeroOperazioniFallite == 0

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


@test21
Scenario: Caricamento di un tracciato in formato CSV valido parametro linguaSecondaria impostato

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v7.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 10
Then match response.numeroOperazioniEseguite == 10
Then match response.numeroOperazioniFallite == 0

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


@test22
Scenario: Caricamento di un tracciato in formato CSV valido

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v8.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 4
Then match response.numeroAvvisiStampati == 4
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


@test23
Scenario: Caricamento di un tracciato in formato CSV valido con versamento con soglia 60 giorni

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v9.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
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
Then match response.numeroOperazioniTotali == 1
Then match response.numeroOperazioniEseguite == 1
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 1
Then match response.numeroAvvisiStampati == 1
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


@test24
Scenario: Caricamento di un tracciato in formato CSV valido tramite applicazione

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
And headers idA2ABasicAutenticationHeader
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
And headers idA2ABasicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get
Then match response contains { descrizioneStato: '##null' } 
Then match response.numeroOperazioniTotali == 4
Then match response.numeroOperazioniEseguite == 4
Then match response.numeroOperazioniFallite == 0
Then match response.numeroAvvisiTotali == 4
Then match response.numeroAvvisiStampati == 4
Then match response.numeroAvvisiFalliti == 0
Then match response.stampaAvvisi == true

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'stampe'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'richiesta'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato, 'esito'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200




