Feature: Eventi nodoInviaRpt

Background:

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* def esitoAttivaRPT = read('classpath:test/workflow/modello3/v1/msg/attiva-response-ok.json')
* def esitoVerificaRPT = read('classpath:test/workflow/modello3/v1/msg/verifica-response-ok.json')
* configure followRedirects = false

Scenario: Verifica tutti gli eventi di un Pagamento eseguito dovuto non precaricato con verifica

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

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('classpath:test/workflow/modello3/v1/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('classpath:test/workflow/modello3/v1/msg/notifica-terminazione-eseguito.json')

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
And param componente = 'API_PAGOPA'
# And param tipoEvento = 'nodoInviaRPT'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 4,
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#[4]'
}
"""

# Ricevuta RT

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
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaInviaRT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciRTservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[0].dettaglioEsito == "Acquisita ricevuta di pagamento [IUV: "+ iuv +" CCP:"+ ccp +"] emessa da Banco di Ponzi S.p.A."
And match response.risultati[0].datiPagoPA == 
"""
{
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# Nodo Invia RPT

And match response.risultati[1] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "#notnull",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "nodoInviaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"url": "http://localhost:8080/govpay-ndpsym/pagopa/PagamentiTelematiciRPTservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[1].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# Attiva RPT

And match response.risultati[2] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "#notnull",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[2].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# Verifica Avviso

And match response.risultati[3] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "##null",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaVerificaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
	  "principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[3].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "##null",
	"idCanale": "##null",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

@debug
Scenario: Verifica tutti gli eventi di un Pagamento eseguito dovuto non precaricato con verifica (applicazione con notifiche disabilitate)

# spengo il connettore notifica dell'applicazione

* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')
* set applicazione.servizioIntegrazione = null
* def backofficeBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers backofficeBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# * def numeroAvviso = buildNumeroAvviso(dominio, applicazione)


# Carico la pendenza

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers backofficeBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#regex[0-9]{18}', UUID: '#notnull' }

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	

# * call read('classpath:utils/pa-prepara-avviso.feature')

* def importo = pendenzaPut.importo

# Verifico il pagamento

# * call read('classpath:utils/psp-verifica-rpt.feature')
# * match response.esitoVerificaRPT == esitoVerificaRPT
# * def ccp = response.ccp
* def ccp = getCurrentTimeMillis()


# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

* call sleep(10000)

# Verifico la notifica di attivazione
 
# * call read('classpath:utils/pa-notifica-attivazione.feature')
# * match response == read('classpath:test/workflow/modello3/v1/msg/notifica-attivazione.json')

# Verifico la notifica di terminazione

# * call read('classpath:utils/pa-notifica-terminazione.feature')
# * match response == read('classpath:test/workflow/modello3/v1/msg/notifica-terminazione-eseguito.json')

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
And param componente = 'API_PAGOPA'
# And param tipoEvento = 'nodoInviaRPT'
And param messaggi = true
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	numRisultati: 3,
	numPagine: '#number',
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '#ignore',
	risultati: '#[3]'
}
"""

# Ricevuta RT

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
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaInviaRT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "#notnull",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciRTservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[0].dettaglioEsito == "Acquisita ricevuta di pagamento [IUV: "+ iuv +" CCP:"+ ccp +"] emessa da Banco di Ponzi S.p.A."
And match response.risultati[0].datiPagoPA == 
"""
{
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# Nodo Invia RPT

And match response.risultati[1] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "#notnull",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "CLIENT",
	"tipoEvento": "nodoInviaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"url": "http://localhost:8080/govpay-ndpsym/pagopa/PagamentiTelematiciRPTservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[1].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""

# Attiva RPT

And match response.risultati[2] ==
"""
{  
	"id": "#notnull",
	"idDominio":"#(idDominio)",
	"iuv":"#(iuv)",
	"ccp":"#(''+ccp)",
	"idA2A": "#(idA2A)",
	"idPendenza": "#(''+idPendenza)",
	"idPagamento": "#notnull",
	"componente": "API_PAGOPA",
	"categoriaEvento": "INTERFACCIA",
	"ruolo": "SERVER",
	"tipoEvento": "paaAttivaRPT",
	"sottotipoEvento": "##null",
	"esito": "OK",
	"sottotipoEsito": "200",
	"dettaglioEsito": "##null",
	"dataEvento": "#notnull",
	"durataEvento": "#notnull",
	"datiPagoPA" : "#notnull",
	"parametriRichiesta": {
		"principal": "#(ndpsym_user)",
		"dataOraRichiesta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"url": "http://localhost:8080/govpay/frontend/api/pagopa/PagamentiTelematiciCCPservice",
		"method": "POST",
		"headers": "#array",
		"payload": "#ignore"
	},
	"parametriRisposta": {
		"dataOraRisposta":"#regex \\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\+\\d\\d\\d",
		"status": 200,
		"headers": "#array",
		"payload": "#ignore"
	}
}
"""
And match response.risultati[2].datiPagoPA == 
"""
{
	"idPsp": "GovPAYPsp1",
	"idIntermediarioPsp": "GovPAYPsp1",
	"idCanale": "GovPAYPsp1_PO",
	"tipoVersamento":"PO",
	"modelloPagamento": "4",
	"idDominio" : "#(''+idDominio)",
	"idIntermediario" : "#(''+idIntermediario)",
	"idStazione" : "#(''+idStazione)"
}
"""


