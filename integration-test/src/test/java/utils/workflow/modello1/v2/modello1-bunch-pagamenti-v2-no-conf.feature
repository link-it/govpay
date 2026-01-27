Feature: Setup pagamenti

Background:

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

# idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA
# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
# idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
# idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE
# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A

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

# idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA: pagamento come anonimo SEGRETERIA, idDominio (solo caricamento, senza attivazione)
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def idPagamentoAnonimo_INCORSO_DOM1_SEGRETERIA = getIuvFromNumeroAvviso(response.numeroAvviso)

# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA = iuv

# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A = iuv

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA = iuv

# idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM1_SEGRETERIA_A2A = iuv

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE = iuv

# idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoVerdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = iuv

# idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoVerdi_NONESEGUITO_DOM2_ENTRATASIOPE = iuv

# idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.voci[0].ibanAccredito = ibanAccreditoErrato

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def numeroAvviso = response.numeroAvviso
* def idPagamentoVerdi_RIFIUTATO_DOM1_LIBERO = getIuvFromNumeroAvviso(numeroAvviso)

# idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE (solo caricamento, senza attivazione)
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v2/pendenze/put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.idTipoPendenza = codTipoPendenzaPagamento
* set pendenzaPut.voci[0].codEntrata = codEntrataPagamento

Given url pendenzeBaseurl
And path 'pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def idPagamentoVerdi_INCORSO_DOM2_ENTRATASIOPE = getIuvFromNumeroAvviso(response.numeroAvviso)

* def spidHeaders = spidHeadersRossi
* def soggettoVersante = soggettoVersanteRossi

# idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_ESEGUITO_DOM1_SEGRETERIA = iuv

# idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_NONESEGUITO_DOM1_SEGRETERIA = iuv

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE = iuv

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-spid.feature')
* def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE = iuv

# idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R01"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoRossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A = iuv

# idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A
* def tipoRicevuta = "R02"
* def cumulativo = "0"
* def idDominioPagamento = idDominio_2
* def codEntrataPagamento = codEntrataSiope
* def codTipoPendenzaPagamento = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-spontaneo-basic.feature')
* def idPagamentoRossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A = iuv

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

# idPagamentoAnonimo2: pagamento come anonimo SEGRETERIA, idDominio
* call read('classpath:utils/workflow/modello1/v2/modello1-pagamento-avviso-anonimo.feature')
* def idPagamentoAnonimo2 = idPagamentoAnonimo

