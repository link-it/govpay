Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-v2.feature')

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
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
And path '/riscossioni', <riscossione>.idDominio, <riscossione>.iuv, <riscossione>.iur, <riscossione>.indice
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	dominio:'#notnull', 
	iuv:'#(<riscossione>.iuv)', 
	iur:'#(<riscossione>.iur)', 
	indice:'#(<riscossione>.indice)', 
	riconciliazione:'##string',
	vocePendenza:'#notnull',
	rt:'#notnull',
	stato:'RISCOSSA', 
	tipo:'ENTRATA', 
	importo:'#(<riscossione>.importo)', 
	data:'#(<riscossione>.data)'
}
"""
And match response.dominio.idDominio == '#(<riscossione>.idDominio)'

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
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url ragioneriaBaseurl
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
| applicazione_nonAuthServizio.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_nonAuthServizio.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 
| applicazione_nonAuthDominio.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_nonAuthDominio.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 