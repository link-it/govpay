Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/ragioneria/bunch-riconciliazioni-v2.feature')

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della riscossione autorizzata

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/riscossioni', <riscossione>.idDominio, <riscossione>.iuv, <riscossione>.iur, <riscossione>.indice
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	idDominio:'#(<riscossione>.idDominio)', 
	iuv:'#(<riscossione>.iuv)', 
	iur:'#(<riscossione>.iur)', 
	indice:'#(<riscossione>.indice)', 
	pendenza:'#notnull',
	idVocePendenza:'#notnull',
	rpp:'#notnull',
	stato:'RISCOSSA', 
	tipo:'ENTRATA', 
	importo:'#(<riscossione>.importo)', 
	data:'#(<riscossione>.data)',
	allegato: '#ignore'
}
"""

Examples:
| applicazione | riscossione | 
| applicazione_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| applicazione_dominio1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| applicazione_dominio1.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della riscossione non autorizzata

* def applicazione = read('msg/<applicazione>')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path '/riscossioni', <riscossione>.idDominio, <riscossione>.iuv, <riscossione>.iur, <riscossione>.indice
And headers idA2ABasicAutenticationHeader
When method get
Then status 403
And match response == 
"""
{
	categoria: 'AUTORIZZAZIONE', 
	codice: '#notnull', 
	descrizione: 'Operazione non autorizzata', 
	dettaglio: '#notnull' 
}
"""

Examples:
| applicazione | riscossione | 
| applicazione_dominio1.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio1.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| applicazione_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| applicazione_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |


Scenario Outline: Lettura dettaglio operatore [<operatore>] della riscossione autorizzata

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request read('msg/<operatore>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/riscossioni', <riscossione>.idDominio, <riscossione>.iuv, <riscossione>.iur, <riscossione>.indice
And headers spidHeadersRossi
When method get
Then status 200
And match response == 
"""
{
	idDominio:'#(<riscossione>.idDominio)', 
	iuv:'#(<riscossione>.iuv)', 
	iur:'#(<riscossione>.iur)', 
	indice:'#(<riscossione>.indice)', 
	pendenza:'#notnull',
	idVocePendenza:'#notnull',
	rpp:'#notnull',
	stato:'RISCOSSA', 
	tipo:'ENTRATA', 
	importo:'#(<riscossione>.importo)', 
	data:'#(<riscossione>.data)',
	allegato: '#ignore'
}
"""

Examples:
| operatore | riscossione | 
| operatore_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_star.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| operatore_domini1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_domini1e2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| operatore_domini1.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 

Scenario Outline: Lettura dettaglio operatore [<operatore>] della riscossione non autorizzata

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'operatori', 'RSSMRA30A01H501I'
And headers gpAdminBasicAutenticationHeader
And request read('msg/<operatore>')
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'spid'})
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

Given url backofficeBaseurl
And path '/riscossioni', <riscossione>.idDominio, <riscossione>.iuv, <riscossione>.iur, <riscossione>.indice
And headers spidHeadersRossi
When method get
Then status 403
And match response == 
"""
{
	categoria: 'AUTORIZZAZIONE', 
	codice: '#notnull', 
	descrizione: 'Operazione non autorizzata', 
	dettaglio: '#notnull' 
}
"""

Examples:
| operatore | riscossione | 
| operatore_domini1.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_domini1.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| operatore_domini2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| operatore_domini2.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_disabilitato.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| operatore_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_nonAuth.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| operatore_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| operatore_none.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |  