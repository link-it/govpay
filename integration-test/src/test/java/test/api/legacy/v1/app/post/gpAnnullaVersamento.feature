Feature: GpAPP AnnullaVersamento

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def legacyBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Lettura stato pendenza non eseguita

* def idPendenza = getCurrentTimeMillis()
* def generaIuv = false
* def gpCaricaVersamentoRequest = read('msg/gpCaricaVersamentoRequest-monovoce_riferimento.xml')
* def gpCaricaVersamentoResponse = read('msg/gpCaricaVersamentoResponse.xml')

Given url legacyBaseurl
And header SoapAction = 'gpCaricaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaVersamentoRequest
When method post
Then status 200
And match response == gpCaricaVersamentoResponse

* xml res = response

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* def iuvToCheck = getIuvFromNumeroAvviso(response.numeroAvviso)
* match iuvToCheck == $res /Envelope/Body/gpCaricaVersamentoResponse/iuvGenerato/iuv

* def gpChiediStatoVersamentoRequest = read('msg/gpChiediStatoVersamentoRequest.xml')
* def gpChiediStatoVersamentoResponse = read('msg/gpChiediStatoVersamentoResponse-nonEseguito.xml')

Given url legacyBaseurl
And header SoapAction = 'gpChiediStatoVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediStatoVersamentoRequest
When method post
Then status 200
And match response == gpChiediStatoVersamentoResponse

* def gpAnnullaVersamentoRequest = read('msg/gpAnnullaVersamentoRequest.xml')
* def gpAnnullaVersamentoResponse = read('msg/gpAnnullaVersamentoResponse-ok.xml')

Given url legacyBaseurl
And header SoapAction = 'gpAnnullaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpAnnullaVersamentoRequest
When method post
Then status 200
And match response == gpAnnullaVersamentoResponse

Scenario: Lettura stato pendenza eseguita

# Carico Pendenza

* def idPendenza = getCurrentTimeMillis()
* def gpCaricaVersamentoRequest = read('msg/gpCaricaVersamentoRequest-monovoce_riferimento.xml')
* def gpCaricaVersamentoResponse = read('msg/gpCaricaVersamentoResponse.xml')

Given url legacyBaseurl
And header SoapAction = 'gpCaricaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpCaricaVersamentoRequest
When method post
Then status 200
And match response == gpCaricaVersamentoResponse

* xml res = response

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

* match response.stato == 'NON_ESEGUITA'
* match response.voci == '#[1]'
* match response.voci[0].indice == 1
* match response.voci[0].stato == 'Non eseguito'

* def iuvToCheck = getIuvFromNumeroAvviso(response.numeroAvviso)
* match iuvToCheck == $res /Envelope/Body/gpCaricaVersamentoResponse/iuvGenerato/iuv

# Avvio Pagamento

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})
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
And headers idA2ABasicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

* def responseAvviaPagamento = response

* def gpChiediStatoVersamentoRequest = read('msg/gpChiediStatoVersamentoRequest.xml')
* def gpChiediStatoVersamentoResponse = read('msg/gpChiediStatoVersamentoResponse-inCorso.xml')

Given url legacyBaseurl
And header SoapAction = 'gpChiediStatoVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediStatoVersamentoRequest
When method post
Then status 200
And match response == gpChiediStatoVersamentoResponse

# Completo Pagamento
* configure followRedirects = false
* def idSession = responseAvviaPagamento.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamentoCumulativo = '0'
And headers basicAutenticationHeader
When method get
Then status 302

# Verifico la notifica di terminazione

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')


* def gpChiediStatoVersamentoRequest = read('msg/gpChiediStatoVersamentoRequest.xml')
* def gpChiediStatoVersamentoResponse = read('msg/gpChiediStatoVersamentoResponse-eseguito.xml')

Given url legacyBaseurl
And header SoapAction = 'gpChiediStatoVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpChiediStatoVersamentoRequest
When method post
Then status 200
And match response == gpChiediStatoVersamentoResponse

* def gpAnnullaVersamentoRequest = read('msg/gpAnnullaVersamentoRequest.xml')
* def gpAnnullaVersamentoResponse = read('msg/gpAnnullaVersamentoResponse-ko.xml')

Given url legacyBaseurl
And header SoapAction = 'gpAnnullaVersamento'
And header Content-Type = 'text/xml'
And headers idA2ABasicAutenticationHeader
And request gpAnnullaVersamentoRequest
When method post
Then status 200
And match response == gpAnnullaVersamentoResponse

* xml res = response
* def descrizioneEsitoOperazioneCheck = $res /Envelope/Body/gpAnnullaVersamentoResponse/descrizioneEsitoOperazione
* match descrizioneEsitoOperazioneCheck == '#("La pendenza (IdA2A:" + idA2A + ", Id:" + idPendenza + ") e\' in uno stato che non consente l\'annullamento (ESEGUITO)")' 




