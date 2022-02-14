Feature: dettaglio riconciliazione con filtro sul tipo delle riscossioni 

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni.feature')

Scenario Outline: Lettura dettaglio applicazione [<applicazione>] della riconciliazione [<idRiconciliazione>]

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
And path '/riconciliazioni', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'ENTRATA'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == <risposta>
And match response.riscossioni[0].tipo == 'ENTRATA'

Given url ragioneriaBaseurl
And path '/riconciliazioni', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'MBT'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response.riscossioni == '#[0]'

Given url ragioneriaBaseurl
And path '/riconciliazioni', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'ENTRATA','MBT'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == <risposta>
And match response.riscossioni[0].tipo == 'ENTRATA'

Given url ragioneriaBaseurl
And path '/riconciliazioni', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'ENTRATA','MBT', 'ALTRO_INTERMEDIARIO'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == <risposta>
And match response.riscossioni[0].tipo == 'ENTRATA'

Given url ragioneriaBaseurl
And path '/riconciliazioni', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'TIPO_NON_VALIDO' 
And headers idA2ABasicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'TIPO_NON_VALIDO'


Examples:
| applicazione | idDominio | idRiconciliazione | httpStatus | risposta |
| applicazione_auth.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 200 | riconciliazioneSin_DOM1_A2A |
