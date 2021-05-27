Feature: dettaglio riconciliazione con filtro sul tipo delle riscossioni 

Background:

* callonce read('classpath:utils/api/v1/ragioneria/bunch-riconciliazioni-v3.feature')

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

Given url backofficeBaseurl
And path '/incassi', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'ENTRATA'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')
And match response.riscossioni[0].tipo == 'ENTRATA'

Given url backofficeBaseurl
And path '/incassi', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'MBT'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response.riscossioni == '#[0]'

Given url backofficeBaseurl
And path '/incassi', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'ENTRATA','MBT'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')
And match response.riscossioni[0].tipo == 'ENTRATA'

Given url backofficeBaseurl
And path '/incassi', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'ENTRATA','MBT', 'ALTRO_INTERMEDIARIO'
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == read('msg/<risposta>')
And match response.riscossioni[0].tipo == 'ENTRATA'

Given url backofficeBaseurl
And path '/incassi', <idDominio>, <idRiconciliazione>
And param riscossioni.tipo = 'TIPO_NON_VALIDO' 
And headers idA2ABasicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'TIPO_NON_VALIDO'


Examples:
| applicazione | idDominio | idRiconciliazione | httpStatus | risposta |
| applicazione_dominio1.json | idDominio | idRiconciliazioneSin_DOM1_A2A | 200 | riconciliazione-singola-response.json |
