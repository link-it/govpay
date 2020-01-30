Feature: Ricerca richieste di pagamento pendenza filtrate per esito

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v1', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

Scenario: Filtro su data

* def dataRptStart = getDateTime()

* def idPendenza = getCurrentTimeMillis()
* def idPendenza1 = idPendenza

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt1 = response 
* def dataRptEnd1 = getDateTime()

* def idPendenza = getCurrentTimeMillis()
* def idPendenza2 = idPendenza

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt2 = response 
* def dataRptEnd2 = getDateTime()

# Ho avviato due pagamenti. Verifico i filtri.

Given url pendenzeBaseurl
And path '/rpp'
And param esito = 'IN_CORSO' 
And param idPendenza = idPendenza1
And headers basicAutenticationHeader
When method get
Then status 200
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
And match response.risultati[0].pendenza contains '#(""+idPendenza1)'
And match response.risultati[0].rt == '#notpresent'

Given url pendenzeBaseurl
And path '/rpp'
And param esito = 'IN_CORSO' 
And param idPendenza = idPendenza2
And headers basicAutenticationHeader
When method get
Then status 200
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
And match response.risultati[0].pendenza contains '#(""+idPendenza2)' 

# Fine verifiche. Completo i pagamenti

 
* def idSession = responseRpt1.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def dataRtEnd1 = getDateTime()

* def idSession = responseRpt2.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R02'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')

* def dataRtEnd2 = getDateTime()

# Fine verifiche. Verifico i filtri

Given url pendenzeBaseurl
And path '/rpp'
And param esito = 'ESEGUITO' 
And param idPendenza = idPendenza1
And headers basicAutenticationHeader
When method get
Then status 200
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
And match response.risultati[0].pendenza contains '#(""+idPendenza1)'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '0'

Given url pendenzeBaseurl
And path '/rpp'
And param esito = 'NON_ESEGUITO' 
And param idPendenza = idPendenza2
And headers basicAutenticationHeader
When method get
Then status 200
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
And match response.risultati[0].pendenza contains '#(""+idPendenza2)'
And match response.risultati[0].rt == '#notnull'
And match response.risultati[0].rt.datiPagamento.codiceEsitoPagamento == '1'


Scenario: Controllo di sintassi sul valore del filtro per esito
Given url pendenzeBaseurl
And path '/rpp'
And param esito = 'ESITO_NON_VALIDO' 
And headers basicAutenticationHeader
When method get
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'ESITO_NON_VALIDO'

