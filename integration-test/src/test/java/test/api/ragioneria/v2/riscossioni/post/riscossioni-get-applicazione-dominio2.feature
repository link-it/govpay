Feature: Errori di validazione sintattica della richiesta di riconciliazione 

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-v2.feature')

* def applicazioneRequest = read('msg/applicazione_dominio2.json')
* callonce read('classpath:utils/api/v1/backoffice/applicazione-put.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della riscossione autorizzata

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
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2.riscossioni[0] | 

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della riscossione non autorizzata

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
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.riscossioni[0] |
| applicazione_dominio2.json | notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA.riscossioni[0] |


Scenario Outline: Ricerca riscossioni da applicazione <applicazione>.

Given url ragioneriaBaseurl
And path 'riscossioni'
And headers idA2ABasicAutenticationHeader
And param dataDa = dataInizio
And param dataA = dataFine
When method get
Then status 200
And match response ==
"""
{
	numRisultati: <numRisultati>,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[<numRisultati>]'
}
""" 
Examples:
| applicazione | numRisultati | 
| applicazione_dominio2.json | 6 |

