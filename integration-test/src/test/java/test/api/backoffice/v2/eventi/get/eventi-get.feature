Feature: Dettaglio di un evento del giornale (console-api v2)

# La v1 non ha un corrispettivo: esponeva il solo elenco, con i payload di
# richiesta e risposta gia' dentro ogni elemento. La v2 li sposta su due
# sotto-risorse dedicate, perche' sono il contenuto sensibile: gli header di
# credenziali sono mascherati, ogni lettura e' tracciata su gp_audit e la
# lettura in chiaro lo e' con un'azione sua.
#
# Gli scenari partono sempre da un evento preso dall'elenco: non esistono
# identificativi stabili da mettere a costante, e il giornale e' gia' popolato
# dalle chiamate della testsuite.

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def consoleBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v2', autenticazione: 'basic'})
* def DbUtils = Java.type('utils.java.DbUtils')
* def db = new DbUtils(govpayDbConfig)
* def contaAudit = function(azione, id){ return db.readValue("SELECT count(*) FROM gp_audit WHERE tipo_oggetto = '" + azione + "' AND id_oggetto = " + id) }

Scenario: Dettaglio di un evento

Given url consoleBaseurl
And path 'eventi'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
And match response.results == '#[1]'
* def evento = response.results[0]

Given url consoleBaseurl
And path 'eventi', evento.id
And headers basicAutenticationHeader
When method get
Then status 200
And match response.id == evento.id
And match response.dataEvento == evento.dataEvento
And match response.componente == evento.componente
And match response.tipoEvento == evento.tipoEvento
And match response._links.self.href == '/eventi/' + evento.id
And match response._links.richiesta.href == '/eventi/' + evento.id + '/richiesta'
And match response._links.risposta.href == '/eventi/' + evento.id + '/risposta'

Scenario: Dettaglio di un evento inesistente

Given url consoleBaseurl
And path 'eventi', 999999999
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Richiesta e risposta registrate per l'evento

Given url consoleBaseurl
And path 'eventi'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def idEvento = response.results[0].id

Given url consoleBaseurl
And path 'eventi', idEvento, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.headers == '#[_ > 0]'
And match each response.headers == { nome: '#string', valore: '##string', redatto: '#boolean' }

Given url consoleBaseurl
And path 'eventi', idEvento, 'risposta'
And headers basicAutenticationHeader
When method get
Then status 200
And match response.headers == '#[_ > 0]'

Scenario: Gli header di credenziali sono mascherati, e in chiaro solo su richiesta esplicita

# Gli eventi dell'API di backoffice portano l'Authorization della chiamata che
# li ha generati: sono quelli su cui il mascheramento si vede.

Given url consoleBaseurl
And path 'eventi'
And param componente = 'API_BACKOFFICE'
And param ruolo = 'SERVER'
And param limit = 10
And headers basicAutenticationHeader
When method get
Then status 200
* def idEventi = $response.results[*].id

* def trovaConAuthorization =
"""
function(ids) {
  for (var i = 0; i < ids.length; i++) {
    var res = karate.call('classpath:utils/api/v2/backoffice/leggi-richiesta-evento.feature',
        { idEvento: ids[i], unmask: false });
    if (res.authorization != null) { return { id: ids[i], header: res.authorization }; }
  }
  return null;
}
"""
* def conAuthorization = trovaConAuthorization(idEventi)
And match conAuthorization != null
And match conAuthorization.header.redatto == true
And match conAuthorization.header.valore == '***REDACTED***'

* def inChiaro = karate.call('classpath:utils/api/v2/backoffice/leggi-richiesta-evento.feature', { idEvento: conAuthorization.id, unmask: true })
And match inChiaro.authorization.redatto == false
And match inChiaro.authorization.valore != '***REDACTED***'

Scenario: Ogni lettura dei payload e' tracciata, quella in chiaro con un'azione sua

Given url consoleBaseurl
And path 'eventi'
And param componente = 'API_BACKOFFICE'
And param limit = 1
And headers basicAutenticationHeader
When method get
Then status 200
* def idEvento = response.results[0].id
* def letturePrima = contaAudit('EVENTO_RICHIESTA_VISUALIZZA', idEvento)
* def credenzialiPrima = contaAudit('EVENTO_CREDENZIALI_VISUALIZZA', idEvento)

Given url consoleBaseurl
And path 'eventi', idEvento, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 200

# Lettura mascherata: traccia solo la visualizzazione.
* def lettureDopo = contaAudit('EVENTO_RICHIESTA_VISUALIZZA', idEvento)
* def credenzialiDopo = contaAudit('EVENTO_CREDENZIALI_VISUALIZZA', idEvento)
And match lettureDopo == letturePrima + 1
And match credenzialiDopo == credenzialiPrima

Given url consoleBaseurl
And path 'eventi', idEvento, 'richiesta'
And param unmask = true
And headers basicAutenticationHeader
When method get
Then status 200

# Lettura in chiaro: traccia anche l'accesso alle credenziali.
* def lettureInChiaro = contaAudit('EVENTO_RICHIESTA_VISUALIZZA', idEvento)
* def credenzialiInChiaro = contaAudit('EVENTO_CREDENZIALI_VISUALIZZA', idEvento)
And match lettureInChiaro == letturePrima + 2
And match credenzialiInChiaro == credenzialiPrima + 1

Scenario: Payload di un evento inesistente

Given url consoleBaseurl
And path 'eventi', 999999999, 'richiesta'
And headers basicAutenticationHeader
When method get
Then status 404

Given url consoleBaseurl
And path 'eventi', 999999999, 'risposta'
And headers basicAutenticationHeader
When method get
Then status 404

Scenario: Dettaglio senza autenticazione

Given url consoleBaseurl
And path 'eventi', 1
When method get
Then status 401
