Feature: Setup pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

# idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
# idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE

Scenario: Pagamento ad iniziativa Ente

# idPagamentoAnonimo0: pagamento come anonimo SEGRETERIA, idDominio
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-avviso-anonimo.feature')
* def idPagamentoAnonimo0 = idPagamentoAnonimo

* def dataInizio = getDateTime()
* call sleep(1000)

# idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA: pagamento come anonimo SEGRETERIA, idDominio
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-avviso-anonimo.feature')
* def idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA = idPagamentoAnonimo

* def spidHeadersVerdi = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 
* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

* def spidHeaders = spidHeadersVerdi

# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA = idPagamento 

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA = idPagamento 

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE = idPagamento 

# idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE = idPagamento 

# idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
* def idPendenza = getCurrentTimeMillis()
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def pendenza = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_definito.json')
* set pendenza.voci[0].ibanAccredito = ibanAccreditoErrato

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenza
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_riferimento_avviso.json')
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

Given url pagamentiBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 502
And match response ==  
"""
{
   "categoria":"PAGOPA",
   "codice":"PPT_SEMANTICA",
   "descrizione":"Errore semantico",
   "dettaglio":"#notnull",
   "id":"#notnull",
   "location":"#notnull"
}
"""
* def idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO = response.id

# idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idDominio = idDominioPagamento
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codEntrataPagamento

Given url pagamentoBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE = response.id





Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def spidHeaders = spidHeadersRossi

# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA = idPagamento 

# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA = idPagamento 

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE = idPagamento 

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE = idPagamento 

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

# idPagamentoAnonimo2: pagamento come anonimo SEGRETERIA, idDominio
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-avviso-anonimo.feature')
* def idPagamentoAnonimo2 = idPagamentoAnonimo
