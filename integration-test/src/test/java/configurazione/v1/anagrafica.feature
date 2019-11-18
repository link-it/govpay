Feature: Configurazione

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def idStazione = '11111111113_01'
* def idDominio = '12345678901'
* def idUnitaOperativa = '12345678901_01'
* def idA2A = 'IDA2A01'
* def idA2A2 = 'IDA2A02'
* def ibanAccredito = 'IT02L1234512345123456789012'
* def bicAccredito = 'DABAIE2D'
* def ibanAccreditoPostale = 'IT02L0760112345123456789012'
* def bicAccreditoPostale = 'DABAIE2C'
* def ibanAccreditoErrato = 'IT00X9999900000000000000000'
* def codEntrataSegreteria = 'SEGRETERIA'
* def codEntrataSenzaAppoggio = 'SEGRETERIA-no-appoggio'
* def codEntrataBollo = 'BOLLOT' 
* def tipoPendenzaRinnovo = 'RINNOVO' 
* def codLibero = 'LIBERO' 
* def codSpontaneo = 'SPONTANEO' 
* def codDovuto = 'DOVUTO'
* def idOperatoreSpid = 'RSSMRA30A01H501I'  
* def idOperatoreSpid2 = 'RSSMRA30A01H502I'  

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )
* def idA2A2BasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A2, password: 'password' } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def operatoreSpidAutenticationHeader = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}
* def operatoreSpid2AutenticationHeader = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H502I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'mverdi@mailserver.host.it'}

Scenario: configurazione anagrafica base

* def configurazione_generale = read('classpath:configurazione/v1/msg/configurazione_generale.json')
* set configurazione_generale.tracciatoCsv.intestazione = "idA2A,idPendenza,idDominio,tipoPendenza,numeroAvviso,pdfAvviso,tipoSoggettoPagatore,identificativoPagatore,anagraficaPagatore,indirizzoPagatore,civicoPagatore,capPagatore,localitaPagatore,provinciaPagatore,nazionePagatore,emailPagatore,cellularePagatore,errore"
* set configurazione_generale.tracciatoCsv.richiesta = encodeBase64InputStream(read('classpath:configurazione/v1/msg/csv-standard-request.ftl'))
* set configurazione_generale.tracciatoCsv.risposta = encodeBase64InputStream(read('classpath:configurazione/v1/msg/csv-standard-response.ftl'))
* set configurazione_generale.mailPromemoria.oggetto = encodeBase64InputStream(read('classpath:configurazione/v1/msg/promemoria-oggetto-freemarker.ftl'))
* set configurazione_generale.mailPromemoria.messaggio = encodeBase64InputStream(read('classpath:configurazione/v1/msg/promemoria-messaggio-freemarker.ftl'))
* set configurazione_generale.mailRicevuta.oggetto = encodeBase64InputStream(read('classpath:configurazione/v1/msg/notifica-oggetto-freemarker.ftl'))
* set configurazione_generale.mailRicevuta.messaggio = encodeBase64InputStream(read('classpath:configurazione/v1/msg/notifica-messaggio-freemarker.ftl'))

#### configurazione del giornale degli eventi
Given url backofficeBaseurl
And path 'configurazioni'
And headers gpAdminBasicAutenticationHeader
And request configurazione_generale
When method POST
Then assert responseStatus == 200 || responseStatus == 201

#### creazione operatore spid
* def operatoreSpid = read('classpath:configurazione/v1/msg/operatore.json')

Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid
And headers basicAutenticationHeader
And request operatoreSpid
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'ruoli', 'operatore'
And headers basicAutenticationHeader
And request 
"""
{
  "acl": [
    { "servizio": "Pagamenti", "autorizzazioni": [ "R", "W" ] },
    { "servizio": "Pendenze", "autorizzazioni": [ "R", "W" ] }
  ]
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201


Given url backofficeBaseurl
And path 'operatori', idOperatoreSpid2
And headers basicAutenticationHeader
And request 
"""
{
  "ragioneSociale": "Mario Verdi",
  "domini": ["*"],
  "tipiPendenza": ["*"],
  "acl": null,
  "ruoli": ["operatore"],
  "abilitato": true
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione intermediario
* def intermediario = read('classpath:configurazione/v1/msg/intermediario.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione stazione
* def stazione = read('classpath:configurazione/v1/msg/stazione.json')

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione 
And headers basicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione unitaOperative
* def unitaOperativa = read('classpath:configurazione/v1/msg/unitaOperativa.json')

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unitaOperativa
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione contiAccredito

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true,bic:'#(bicAccredito)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccreditoPostale
And headers basicAutenticationHeader
And request {postale:true,mybank:false,abilitato:true,bic:'#(bicAccreditoPostale)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccreditoErrato
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione entrate

Given url backofficeBaseurl
And path 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
And request {  descrizione: 'Diritti e segreteria',  tipoContabilita: 'ALTRO',  codiceContabilita: '#(codEntrataSegreteria)' }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito)', ibanAppoggio: '#(ibanAccreditoPostale)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'entrate', codEntrataSenzaAppoggio
And headers basicAutenticationHeader
And request {  descrizione: 'Diritti e segreteria senza ibanAppoggio',  tipoContabilita: 'ALTRO',  codiceContabilita: '#(codEntrataSenzaAppoggio)' }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataSenzaAppoggio
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito)', ibanAppoggio: null, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataBollo
And headers basicAutenticationHeader
And request { tipoContabilita: 'ALTRO', codiceContabilita: 'MBT', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201


Given url backofficeBaseurl
And path 'entrate', codSpontaneo
And headers basicAutenticationHeader
And request {  descrizione: 'Pagamento spontaneo',  tipoContabilita: 'ALTRO',  codiceContabilita: '#(codSpontaneo)' }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codSpontaneo
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito)', ibanAppoggio: null, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'entrate', codDovuto
And headers basicAutenticationHeader
And request {  descrizione: 'Pagamento dovuto',  tipoContabilita: 'ALTRO',  codiceContabilita: '#(codDovuto)' }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codDovuto
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito)', ibanAppoggio: null, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione tipi pendenza

Given url backofficeBaseurl
And path 'tipiPendenza', codEntrataSegreteria
And headers basicAutenticationHeader
And request {  descrizione: 'Diritti e segreteria' ,  codificaIUV: null,  tipo: 'spontaneo',  pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codEntrataSegreteria
And headers basicAutenticationHeader
And request { codificaIUV: '89',  pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza', codEntrataSenzaAppoggio
And headers basicAutenticationHeader
And request {  descrizione: 'Diritti e segreteria senza ibanAppoggio',  codificaIUV: null,  tipo: 'spontaneo',  pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codEntrataSenzaAppoggio
And headers basicAutenticationHeader
And request { codificaIUV: '89', pagaTerzi: true, abilitato: true  }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza', codEntrataBollo
And headers basicAutenticationHeader
And request { descrizione: 'Marca da bollo telematica', codificaIUV: null, tipo: 'spontaneo', pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codEntrataBollo
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza', codLibero
And headers basicAutenticationHeader
And request { descrizione: 'Pendenza libera' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: false, abilitato: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codLibero
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: false, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza', codSpontaneo
And headers basicAutenticationHeader
And request { descrizione: 'Pendenza spontaneo' , codificaIUV: null, tipo: 'spontaneo', pagaTerzi: false}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: false }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza', codDovuto
And headers basicAutenticationHeader
And request { descrizione: 'Pendenza dovuta' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: false}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codDovuto
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: false }
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione applicazione
* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione applicazione
* def applicazione_2 = read('classpath:configurazione/v1/msg/applicazione2.json')

Given url backofficeBaseurl
And path 'applicazioni', idA2A2
And headers basicAutenticationHeader
And request applicazione_2
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

#### reset simulatore

Given url ndpsym_url + '/pagopa/rs/dars/manutenzione/trash' 
When method get
Then assert responseStatus == 200

