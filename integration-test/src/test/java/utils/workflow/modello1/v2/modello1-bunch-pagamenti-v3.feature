Feature: Setup pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'spid'})

# idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA 
# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
# idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
# idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2

Scenario: Pagamento ad iniziativa Ente

# idPagamentoAnonimo0: pagamento come anonimo SEGRETERIA, idDominio
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-avviso-anonimo.feature')
* def idPagamentoAnonimo0 = idPagamentoAnonimo

* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)

* def spidHeadersVerdi = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 
* def soggettoVersanteVerdi = { tipo: 'F', identificativo: 'VRDGPP65B03A112N', anagrafica: 'Giuseppe Verdi' }

* def spidHeadersRossi = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}
* def soggettoVersanteRossi = { tipo: 'F', identificativo: 'RSSMRA30A01H501I', anagrafica: 'Mario Rossi' }

* def spidHeaders = spidHeadersVerdi
* def soggettoVersante = soggettoVersanteVerdi

# idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA: pagamento come anonimo SEGRETERIA, idDominio
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-avviso-anonimo.feature')
* def idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA = idPagamentoAnonimo
* def idSession = idSessionAnonimo
* call read('classpath:utils/pa-notifica-attivazione-byIdSession.feature')
* def rpt_Anonimo_INCORSO_DOM1_SEGRETERIA = rptNotificaAttivazione
* def idMessaggioRichiesta_Anonimo_INCORSO_DOM1_SEGRETERIA = rptNotificaAttivazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA = idPagamento 
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione.identificativoMessaggioRichiesta


# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A = idPagamento 
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaTerminazione.identificativoMessaggioRichiesta


# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic-idA2A2.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = idPagamento 
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA = idPagamento 
* def rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione
* def notificaTerminazione_Verdi_NONESEGUITO_DOM1_SEGRETERIA = notificaTerminazione
* def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = idPagamento 
* def rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaTerminazione
* def notificaTerminazione_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = notificaTerminazione
* def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic-idA2A2.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = idPagamento 
* def rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE = idPagamento 
* def rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = idPagamento 
* def rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic-idA2A2.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = idPagamento
* def rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE = idPagamento 
* def rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione
* def notificaTerminazione_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE = notificaTerminazione
* def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
* def idPendenza = getCurrentTimeMillis()
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
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

Given url pagamentiBaseurl
And path '/pagamenti', response.id
And headers spidHeaders
When method get
Then status 200

* def rpt_Verdi_RIFIUTATO_DOM1_LIBERO = response.rpp[0].rpt
* def idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO = response.rpp[0].rpt.identificativoMessaggioRichiesta

# idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idDominio = idDominioPagamento
* set pagamentoPost.pendenze[0].idTipoPendenza = codTipoPendenzaPagamento
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codEntrataPagamento

Given url pagamentoBaseurl
And path '/pagamenti'
And headers spidHeaders
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE = response.id

* def idSession = response.idSession
* call read('classpath:utils/pa-notifica-attivazione-byIdSession.feature')

* def rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE = rptNotificaAttivazione
* def idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE = rptNotificaAttivazione.identificativoMessaggioRichiesta



Given url pagamentiBaseurl
And path '/logout'
And headers spidHeaders
When method get
Then status 200

* def spidHeaders = spidHeadersRossi
* def soggettoVersante = soggettoVersanteRossi

# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA = idPagamento 
* def rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione.identificativoMessaggioRichiesta


# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA = idPagamento 
* def rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione
* def notificaTerminazione_Rossi_NONESEGUITO_DOM1_SEGRETERIA = notificaTerminazione
* def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE = idPagamento 
* def rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE = idPagamento 
* def rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione
* def notificaTerminazione_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE = notificaTerminazione
* def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = idPagamento 
* def rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic-idA2A2.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = idPagamento 
* def rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = idPagamento 
* def rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione
* def notificaTerminazione_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = notificaTerminazione
* def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione.identificativoMessaggioRichiesta

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2
* def tipoRicevuta = "R02"
* def cumulativo = "1"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic-idA2A2.feature')
* def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = idPagamento 
* def rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione.identificativoMessaggioRichiesta

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

# idPagamentoAnonimo2: pagamento come anonimo SEGRETERIA, idDominio
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-avviso-anonimo.feature')
* def idPagamentoAnonimo2 = idPagamentoAnonimo
