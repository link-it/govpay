Feature: Ricerca richieste di pagamento pendenza filtrate per esito

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

Scenario: Filtro su esito

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})
* def rispostaListaVuota = read('msg/lista_vuota.json')
* def rispostaNotFound = read('msg/notFound.json')

Given url pagamentiBaseurl
And path '/ricevute', rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.dominio.identificativoDominio, rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And headers idA2ABasicAutenticationHeader
And param esito = 'IN_CORSO' 
When method get
Then status 404
And match response == rispostaNotFound

Given url pagamentiBaseurl
And path '/ricevute', rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.dominio.identificativoDominio, rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And param esitoPagamento = 'ESEGUITO' 
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 1,
	pagina: '##null',
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].dominio.idDominio == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.dominio.identificativoDominio
And match response.risultati[0].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[0].idRicevuta == '#notnull'
And match response.risultati[0].data == '#notnull'
And match response.risultati[0].esito == 'ESEGUITO'

Given url pagamentiBaseurl
And path '/ricevute', rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A.dominio.identificativoDominio, rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And param esitoPagamento = 'NON_ESEGUITO' 
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 1,
	pagina: '##null',
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""
And match response.risultati[0].dominio.idDominio == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A.dominio.identificativoDominio
And match response.risultati[0].iuv == rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And match response.risultati[0].idRicevuta == '#notnull'
And match response.risultati[0].data == '#notnull'
And match response.risultati[0].esito == 'NON_ESEGUITO'


Scenario: Controllo di sintassi sul valore del filtro per esito
Given url pagamentiBaseurl
And path '/ricevute', rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.dominio.identificativoDominio, rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.datiVersamento.identificativoUnivocoVersamento
And headers idA2ABasicAutenticationHeader
And param esito = 'XXX' 
And headers basicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'XXX'

