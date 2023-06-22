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

* def importo = 0.02
* def importo_voce = 0.01

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

* def idA2A_VEN = 'UNIVE-ESSE3'
* def idDominio_VEN = '80007720271'
* def numeroAvviso_VEN = ''  
* def ibanAccredito_VEN = 'IT07K0503402071000000780260'
* def ibanAccreditoPostale_VEN = ''
* def codEntrataSegreteria_VEN = 'GENERICO'
* def applicazione_VEN = 
"""
{
	"codificaAvvisi":{"generazioneIuvInterna":true}
}
"""
* def dominio_VEN = 
"""
{
	"stazione":"00317740371_01",
	"auxDigit":"0",
	"segregationCode" : null
}
"""

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def tracciato = karate.readAsString('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.csv')
* def tracciato = replace(tracciato,"{idA2A}", idA2A_VEN);
* def tracciato = replace(tracciato,"{idPendenza}", idPendenza);
* def tracciato = replace(tracciato,"{idDominio}", idDominio_VEN);
* def tracciato = replace(tracciato,"{numeroAvviso}", numeroAvviso_VEN);
* def tracciato = replace(tracciato,"{ibanAccredito}", ibanAccredito_VEN);
* def tracciato = replace(tracciato,"{ibanAppoggio}", ibanAccreditoPostale_VEN);
* def tracciato = replace(tracciato,"{tipoPendenza}", codEntrataSegreteria_VEN);
* def tracciato = replace(tracciato,"{importo}", importo);
* def tracciato = replace(tracciato,"{importo_voce}", importo_voce);

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idDominio, codEntrataSegreteria
And headers { 'Content-Type' : 'text/csv' }
And headers { 'X-GOVPAY-FILENAME' : 'test1_tracciato_valido' }
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'operazioni', 'elaborazioneTracciatiPendenze'
And headers basicAutenticationHeader
When method get




