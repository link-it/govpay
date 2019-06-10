Feature: Eventi nodoInviaCarrelloRpt

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'spid'})

Scenario: Pagamento avviato con successo utente spid

* def spidHeaders = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 
* def soggettoVersante = { tipo: 'F', identificativo: 'VRDGPP65B03A112N', anagrafica: 'Giuseppe Verdi' }

* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento-spontaneo-spid.feature')

Scenario: Eventi nodoInviaRpt accettato

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idPagamento = idPagamento
And param tipoEvento = 'nodoInviaCarrelloRPT'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: '#number',
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#array'
}
"""
And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio": "#notnull",
	"iuv": "#notnull",
	"ccp": "#notnull",
	"idA2A": "#notnull",
	"idPendenza": "#notnull",
	"idPagamento" : "#(idPagamento)",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "nodoInviaCarrelloRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"url": "http://localhost:8080/govpay-ndpsym/pagopa/PagamentiTelematiciRPTservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#notnull"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#notnull"
	}
}
"""
And match response.risultati[0].datiPagoPA == 
"""
{
	"idPsp": "AGID_01",
	"idCanale": "97735020584_02",
	"idIntermediarioPsp": "97735020584",
	"tipoVersamento":"BBT",
	"modelloPagamento": "1",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""
