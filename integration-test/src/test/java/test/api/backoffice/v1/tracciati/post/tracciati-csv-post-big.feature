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
Scenario: Caricamento di un tracciato in formato CSV valido versione 5

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
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze-v6.csv')
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
Then match response.numeroOperazioniTotali == 1433
Then match response.numeroOperazioniEseguite == 1433
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

