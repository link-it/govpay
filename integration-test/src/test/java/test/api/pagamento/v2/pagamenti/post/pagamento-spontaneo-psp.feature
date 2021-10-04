Feature: Pagamenti spontanei definizione del parametro opzionale del PSP gestore del pagamento

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def identificativoPSP = "GovPAYPsp1"
* def identificativoIntermediarioPSP = "GovPAYPsp1"
* def identificativoCanale = "GovPAYPsp1_CP"


@pagamentopsp @pagamentopsp-1
Scenario: Pagamento spontaneo cittadino con PSP specificato

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'} 
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And param identificativoPSP = identificativoPSP
And param identificativoIntermediarioPSP = identificativoIntermediarioPSP
And param identificativoCanale = identificativoCanale
And headers spidHeaders
And request pagamentoPost
When method post
Then status 403
And match response == { categoria : 'AUTORIZZAZIONE', codice : '403000', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.descrizione == 'Operazione non autorizzata'
And match response.dettaglio == 'Il richiedente non è autorizzato ad indicare un PSP per il pagamento'

@pagamentopsp @pagamentopsp-2
Scenario: Pagamento spontaneo basic e PSP specificato

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* configure retry = { count: 25, interval: 10000 }

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And param identificativoPSP = identificativoPSP
And param identificativoIntermediarioPSP = identificativoIntermediarioPSP
And param identificativoCanale = identificativoCanale
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url backofficeBaseurl
And path 'eventi'
And param tipoEvento = "nodoInviaCarrelloRPT"
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].datiPagoPA.idPsp == identificativoPSP
And match response.risultati[0].datiPagoPA.idCanale == identificativoCanale
And match response.risultati[0].datiPagoPA.idIntermediarioPsp == identificativoIntermediarioPSP

@pagamentopsp @pagamentopsp-3
Scenario: Pagamento spontaneo basic e PSP indicato inesistente

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* configure retry = { count: 25, interval: 10000 }

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And param identificativoPSP = "XXX"
And param identificativoIntermediarioPSP = "XXX"
And param identificativoCanale = "XXX"
And request pagamentoPost
When method post
Then status 502
And match response == { id: '#notnull', location: '#notnull', categoria : 'PAGOPA', codice : 'PPT_PSP_SCONOSCIUTO', descrizione: '##ignore', dettaglio: '##ignore' }

Given url backofficeBaseurl
And path 'eventi'
And param tipoEvento = "nodoInviaCarrelloRPT"
And param idPendenza = idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].datiPagoPA.idPsp == "XXX"
And match response.risultati[0].datiPagoPA.idCanale == "XXX"
And match response.risultati[0].datiPagoPA.idIntermediarioPsp == "XXX"

@pagamentopsp @pagamentopsp-4
Scenario Outline: Validazione sintassi parametro PSP

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* configure retry = { count: 25, interval: 10000 }

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')

Given url pagamentiBaseurl
And path '/pagamenti'
And headers idA2ABasicAutenticationHeader
And param identificativoPSP = <identificativoPSP>
And param identificativoIntermediarioPSP = <identificativoIntermediarioPSP>
And param identificativoCanale = <identificativoCanale>
And request pagamentoPost
When method post
Then status 400
And match response == { categoria : 'RICHIESTA', codice : 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <dettaglio>

Examples:
| identificativoPSP | identificativoIntermediarioPSP | identificativoCanale | dettaglio
#| '123456789012345678901234567890123456' | 'XXX' | 'XXX' | "identificativoPSP"
#| 'XXX' | '123456789012345678901234567890123456' | 'XXX' | "identificativoIntermediarioPSP"
#| 'XXX' | 'XXX' | '123456789012345678901234567890123456' | "identificativoCanale"
| null | 'XXX' | 'XXX' | "identificativoPSP"
| 'XXX' | null | 'XXX' | "identificativoIntermediarioPSP"
| 'XXX' | 'XXX' | null | "identificativoCanale"


@pagamentopsp @pagamentopsp-5
Scenario: Pagamento spontaneo anonimo 

* def idPendenza = getCurrentTimeMillis()
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And param identificativoPSP = identificativoPSP
And param identificativoIntermediarioPSP = identificativoIntermediarioPSP
And param identificativoCanale = identificativoCanale
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_pendenza.json')
* set pagamentoPost.soggettoVersante = 
"""
{
  "tipo": "F",
  "identificativo": "RSSMRA30A01H501I",
  "anagrafica": "Mario Rossi",
  "indirizzo": "Piazza della Vittoria",
  "civico": "10/A",
  "cap": 0,
  "localita": "Roma",
  "provincia": "Roma",
  "nazione": "IT",
  "email": "mario.rossi@host.eu",
  "cellulare": "+39 000-1234567"
}
"""

Given url pagamentiBaseurl
And path '/pagamenti'
And param identificativoPSP = identificativoPSP
And param identificativoIntermediarioPSP = identificativoIntermediarioPSP
And param identificativoCanale = identificativoCanale
And request pagamentoPost
When method post
Then status 403
And match response == { categoria : 'AUTORIZZAZIONE', codice : '403000', descrizione: '#notnull', dettaglio: '#notnull' }
And match response.descrizione == 'Operazione non autorizzata'
And match response.dettaglio == 'Il richiedente non è autorizzato ad indicare un PSP per il pagamento'


