Feature: Notifica ricevuta all'ente

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

# Configurazione Applicazione per utilizzare le API V2

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.versioneApi = 'REST v2'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201


* def esitoAttivaRPT = read('classpath:test/workflow/modello3/v1/msg/attiva-response-ok.json')
* def esitoVerificaRPT = read('classpath:test/workflow/modello3/v1/msg/verifica-response-ok.json')
* configure followRedirects = false

Scenario: Verifica tutti gli eventi di un Pagamento eseguito dovuto non precaricato con verifica

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')

* def importo = pendenzaPut.importo

# Verifico il pagamento

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.esitoVerificaRPT == esitoVerificaRPT
* def ccp = response.ccp

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('classpath:test/api/ente/v2/ricevute/put/msg/transazione-get-singolo_eseguito_ente.json')

# Verifico lo stato della pendenza

* call read('classpath:utils/api/v1/backoffice/pendenza-get-dettaglio.feature')
* match response.stato == 'ESEGUITA'
* match response.dataPagamento == '#regex \\d\\d\\d\\d-\\d\\d-\\d\\d'
* match response.voci[0].stato == 'Eseguito'
* match response.rpp == '#[1]'
* match response.rpp[0].stato == 'RT_ACCETTATA_PA'
* match response.rpp[0].rt == '#notnull'

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

* call sleep(200)

Given url backofficeBaseurl
And path '/eventi'
And param idDominio = idDominio
And param iuv = iuv
And param componente = 'API_ENTE'
# And param tipoEvento = 'nodoInviaRPT'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 2,
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#[2]'
}
"""

# Notifica Ricevuta

And match response.risultati[0] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "#notnull",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "notificaRicevuta",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##string",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "PUT",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[0].parametriRichiesta.url == ente_api_url + "/v1/ricevute/"+ idDominio + "/" + iuv +"/"+ ccp
# And match response.risultati[0].dettaglioEsito == "Acquisita ricevuta di pagamento [IUV: "+ iuv +" CCP:"+ ccp +"] emessa da Banco di Ponzi S.p.A."

# Get Avviso

And match response.risultati[1] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"##null",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_ENTE",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "getAvviso",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##string",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"url": "#notnull",
		"method": "GET",
		"headers": "#array",
		"payload": "##null"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[1].parametriRichiesta.url == ente_api_url + "/v1/avvisi/"+ idDominio + "/" + iuv





