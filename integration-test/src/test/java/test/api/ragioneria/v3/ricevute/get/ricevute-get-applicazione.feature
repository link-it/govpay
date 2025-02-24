Feature: Controllo autorizzazione applicazione lettura dettaglio ricevute

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def idA2ABasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def gpAdminBasicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )

* def spidHeadersVerdi = {'X-SPID-FISCALNUMBER': 'VRDGPP65B03A112N','X-SPID-NAME': 'Giuseppe','X-SPID-FAMILYNAME': 'Verdi','X-SPID-EMAIL': 'gverdi@mailserver.host.it'} 
* def soggettoVersanteVerdi = { tipo: 'F', identificativo: 'VRDGPP65B03A112N', anagrafica: 'Giuseppe Verdi' }

* def spidHeaders = spidHeadersVerdi
* def soggettoVersante = soggettoVersanteVerdi

Scenario: Ricerca transazioni BASIC filtrati per data e dominio1

* def applicazione = read('classpath:test/api/backoffice/v1/pendenze/get/msg/applicazione_domini1_star.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)

# idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2
* def tipoRicevuta = "R01"
* def cumulativo = "1"
* def idDominioPagamento = idDominio
* def codEntrataPagamento = codEntrataSegreteria
* def codTipoPendenzaPagamento = codEntrataSegreteria

* def idPendenza = getCurrentTimeMillis()
* def pagamentoBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idDominio = idDominioPagamento
* set pagamentoPost.pendenze[0].idA2A = idA2A2
* set pagamentoPost.pendenze[0].idTipoPendenza = codTipoPendenzaPagamento
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codEntrataPagamento
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A2, password: pwdA2A2  } )
* set pagamentoPost.soggettoVersante = soggettoVersante 

Given url pagamentoBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response == { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* configure followRedirects = false
* def idSession = response.idSession
* def idPagamento = response.id

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominioPagamento
And param codice = tipoRicevuta
And param riversamento = cumulativo
And headers spidHeaders
When method get
Then status 302
And match responseHeaders.Location == '#notnull'

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')


* def idPagamentoVerdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = idPagamento 
* def rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione
* def notificaTerminazione_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = notificaTerminazione
* def idMessaggioRichiesta_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 = rptNotificaTerminazione.identificativoMessaggioRichiesta

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)

* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v3', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )

Given url ragioneriaBaseurl
And path '/ricevute'
And param dataDa = dataInizio
And param dataA = dataFine
And headers basicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].iuv == rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2.datiVersamento.identificativoUnivocoVersamento
And match response == 
"""
{
	numRisultati: 1,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[1]'
}
"""

* def rpt = response.risultati[0]


Given url ragioneriaBaseurl
And path '/ricevute', rpt.dominio.idDominio, rpt.iuv, rpt.idRicevuta
And headers idA2ABasicAutenticationHeader
And header Accept = 'application/json'
When method get
Then status 200
