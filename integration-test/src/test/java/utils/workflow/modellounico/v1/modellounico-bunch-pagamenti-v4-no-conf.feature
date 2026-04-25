Feature: Setup pagamenti

Background:

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

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
# idPagamentoVerdi_A2A2 
# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2

Scenario: Pagamento ad iniziativa Ente

* def idDominio_1 = idDominio

# idPagamentoAnonimo0: pagamento come anonimo SEGRETERIA, idDominio
* def tipoRicevuta = "R02"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
* def idPagamentoAnonimo0 = idCart
* def idSessionAnonimo0 = idCart

* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)

# idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA: pagamento come anonimo SEGRETERIA, idDominio
* def tipoRicevuta = "R02"
* def cumulativo = "true"
* def idDominioPagamento = idDominio_1
* def idDominio = idDominio_1
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
* def idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA = idCart
* def idSession = idCart
* call read('classpath:utils/pa-notifica-attivazione.feature')
* def rpt_Anonimo_INCORSO_DOM1_SEGRETERIA = rptNotificaAttivazione
* def idMessaggioRichiesta_Anonimo_INCORSO_DOM1_SEGRETERIA = rptNotificaAttivazione.creditorReferenceId

# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_1
* set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA = idCart 
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione.creditorReferenceId


# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_1
* set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A = idCart 
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaTerminazione.creditorReferenceId


# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_1
* set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito-idA2A2.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = idCart
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione.creditorReferenceId

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_1
# * set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
# * def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA = idCart 
# * def rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaAttivazione
# * def notificaTerminazione_Verdi_NONESEGUITO_DOM1_SEGRETERIA = notificaAttivazione
# * def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaAttivazione.creditorReferenceId

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_1
# * set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
# * def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = idCart 
# * def rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaAttivazione
# * def notificaTerminazione_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = notificaAttivazione
# * def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = rptNotificaAttivazione.creditorReferenceId

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_1
# * set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito-idA2A2.feature')
# * def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = idCart 
# * def rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaAttivazione
# * def notificaTerminazione_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = notificaAttivazione
# * def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaAttivazione.creditorReferenceId

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* def idDominio = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE = idCart 
* def rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione.creditorReferenceId

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* def idDominio = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = idCart 
* def rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione.creditorReferenceId

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* def idDominio = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito-idA2A2.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = idCart
* def rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione.creditorReferenceId

# idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_2
# * set pendenzaPut.voci[0].codEntrata = codEntrataSiope
# * def idDominio = idDominio_2
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
# * def idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE = idCart 
# * def rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaAttivazione
# * def notificaTerminazione_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE = notificaAttivazione
# * def idMessaggioRichiesta_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaAttivazione.creditorReferenceId

# idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
* def idDominio = idDominio_1
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.idDominio = idDominio_1
* set pendenzaPut.voci[0].ibanAccredito = ibanAccreditoErrato
* call read('classpath:utils/pa-carica-avviso.feature')

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo
* def idCart = getCurrentTimeMillis()

# Attivo il pagamento tramite il simulatore

* def idDominio = idDominioPagamento
* def inviaRicevuta = 'true'
* call read('classpath:utils/psp-paGetPayment.feature')

* def idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO = idCart

Given url pendenzeBaseurl
And path '/rpp', idDominio, iuv , ccp
And headers basicAutenticationHeader
When method get
Then status 404

# * def rpt_Verdi_RIFIUTATO_DOM1_LIBERO = response.rpp[0].rpt
# * def idMessaggioRichiesta_Verdi_RIFIUTATO_DOM1_LIBERO = response.rpp[0].rpt.identificativoMessaggioRichiesta

# idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* call read('classpath:utils/pa-carica-avviso.feature')

* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def importo = pendenzaPut.importo
* def idCart = getCurrentTimeMillis()

# Attivo il pagamento tramite il simulatore

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = 0
* def inviaRicevuta = 'false'
* def idDominio = idDominio_2
* call read('classpath:utils/psp-paGetPayment.feature')

* def idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE = idCart

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-attivazione.feature')

* def rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE = rptNotificaAttivazione
* def idMessaggioRichiesta_Verdi_INCORSO_DOM2_ENTRATASIOPE = rptNotificaAttivazione.creditorReferenceId

# idPagamentoVerdi_A2A2

* def idDominio = idDominio_1
* def autenticationHeader = idA2A2BasicAutenticationHeader
* def idA2APendenza = idA2A2
* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }
* call read('classpath:utils/api/v1/pendenze/caricamento-pendenza-generico.feature')
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = numeroAvviso
* def idCart = getCurrentTimeMillis()
* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"
* def inviaRicevuta = 'false'
* call read('classpath:utils/psp-paGetPayment.feature')

# Verifico la notifica di attivazione
 
* call read('classpath:utils/pa-notifica-attivazione.feature')

* def idPagamentoVerdi_A2A2 = idCart
* def rpt_Verdi_A2A2 = rptNotificaAttivazione
* def idMessaggioRichiesta_Verdi_A2A2 = rptNotificaAttivazione.creditorReferenceId

# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
* def idDominio = idDominio_1
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_1
* set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito.feature')
* def idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA = idCart 
* def rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM1_SEGRETERIA = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM1_SEGRETERIA = rptNotificaTerminazione.creditorReferenceId


# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_1
# * set pendenzaPut.voci[0].codEntrata = codEntrataSegreteria
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
# * def idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA = idCart 
# * def rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaAttivazione
# * def notificaTerminazione_Rossi_NONESEGUITO_DOM1_SEGRETERIA = notificaAttivazione
# * def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM1_SEGRETERIA = rptNotificaAttivazione.creditorReferenceId

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* def idDominio = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE = idCart 
* def rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE = rptNotificaTerminazione.creditorReferenceId

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_2
# * set pendenzaPut.voci[0].codEntrata = codEntrataSiope
# * def idDominio = idDominio_2
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
# * def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE = idCart 
# * def rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaAttivazione
# * def notificaTerminazione_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE = notificaAttivazione
# * def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE = rptNotificaAttivazione.creditorReferenceId

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* def idDominio = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = idCart 
* def rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaTerminazione.creditorReferenceId

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2
* def tipoRicevuta = "R01"
* def cumulativo = "true"
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* def idDominio = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-eseguito-idA2A2.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = idCart 
* def rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaTerminazione.creditorReferenceId

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_2
# * set pendenzaPut.voci[0].codEntrata = codEntrataSiope
# * def idDominio = idDominio_2
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
# * def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = idCart 
# * def rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaAttivazione
# * def notificaTerminazione_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = notificaAttivazione
# * def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = rptNotificaAttivazione.creditorReferenceId

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2
# * def tipoRicevuta = "R02"
# * def cumulativo = "true"
# * def idPendenza = getCurrentTimeMillis()
# * def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
# * set pendenzaPut.idDominio = idDominio_2
# * set pendenzaPut.voci[0].codEntrata = codEntrataSiope
# * def idDominio = idDominio_2
# * call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito-idA2A2.feature')
# * def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = idCart 
# * def rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaAttivazione
# * def notificaTerminazione_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = notificaAttivazione
# * def idMessaggioRichiesta_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 = rptNotificaAttivazione.creditorReferenceId

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

# idPagamentoAnonimo2: pagamento come anonimo SEGRETERIA, idDominio
* def tipoRicevuta = "R02"
* def cumulativo = "true"
* def idDominio = idDominio_1
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* call read('classpath:utils/workflow/modellounico/v1/modellounico-pagamento-noneseguito.feature')
* def idPagamentoAnonimo2 = idCart
