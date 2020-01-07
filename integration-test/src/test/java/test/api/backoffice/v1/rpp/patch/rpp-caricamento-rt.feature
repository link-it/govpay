Feature: Ricerca richieste di pagamento pendenza filtrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

Scenario: Scarico rt del pagamento

# Effettuo il pagamento

* def dataRptStart = getDateTime()

* def idPendenza = getCurrentTimeMillis()

Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt1 = response 
* def dataRptEnd1 = getDateTime()

# Completo il pagamento

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

# Fine pagamento. Leggo la lista RPT

Given url backofficeBaseurl
And path '/rpp'
And param dataRtDa = dataRptStart 
And param dataRtA = dataRtEnd1 
And headers gpAdminBasicAutenticationHeader
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

# Prelevo idDominio/iuv/ccp per leggere l'RT in formato xml

* def idDominioRT = response.risultati[0].rpt.dominio.identificativoDominio
* def iuvRT = response.risultati[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpRT = response.risultati[0].rpt.datiVersamento.codiceContestoPagamento

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
And headers {'Accept' : 'application/xml'}
When method get
Then status 200


# Eseguo la patch

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/rt",
      "value": null
   }
]
"""

* set patchRequest[0].value = encodeBase64Bytes(responseBytes)

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200



