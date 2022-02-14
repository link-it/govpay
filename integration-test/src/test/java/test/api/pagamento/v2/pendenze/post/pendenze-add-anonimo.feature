Feature: Caricamento Pendenze Applicazione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'public'})

* def idTipoPendenzaCOSAP = 'COSAP'
* def pendenzaCreataMSG = read('msg/pendenza-creata-anonimo.json')

# Configurazione tipo pendenza
Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenzaCOSAP
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Canone Occupazione Spazi ed Aree Pubbliche' , codificaIUV: null, pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio =
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  abilitato: true,
  portaleBackoffice: {
        abilitato: true,
        form: {
                tipo: "angular2-json-schema-form",
                definizione: null
          },
          validazione: null,
          trasformazione: {
                tipo: "freemarker",
                definizione: null
          },
          inoltro: null
  },
  portalePagamento: {
        abilitato: true,
        form: {
                tipo: "angular2-json-schema-form",
                definizione: null,
                impaginazione: null
          },
          validazione: null,
          trasformazione: {
                tipo: "freemarker",
                definizione: null
          },
          inoltro: null
  },
  visualizzazione: null,
  tracciatoCsv: {
        tipo: "freemarker",
        intestazione: "id,numeroAvviso,pdfAvviso,anagrafica,indirizzo,civico,localita,cap,provincia",
        richiesta: null,
          risposta: null
  }
}
"""
* set tipoPendenzaDominio.portaleBackoffice = null
* set tipoPendenzaDominio.portalePagamento.form.definizione = encodeBase64InputStream(karate.readAsString('msg/cosap/portale-form.json'))
* set tipoPendenzaDominio.portalePagamento.form.impaginazione = encodeBase64InputStream(karate.readAsString('msg/cosap/portale-impaginazione.json'))
* set tipoPendenzaDominio.portalePagamento.trasformazione.definizione = encodeBase64InputStream(karate.readAsString('msg/cosap/portale-trasformazione.ftl'))
* set tipoPendenzaDominio.portalePagamento.validazione = encodeBase64InputStream(karate.readAsString('msg/cosap/portale-validazione.json'))
* set tipoPendenzaDominio.avvisaturaMail = null
* set tipoPendenzaDominio.tracciatoCsv = null

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenzaCOSAP
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario: Inserimento di una nuova pendenza di tipo spontaneo con utenza anonima

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

Scenario: Aggiornamento di una pendenza di tipo spontaneo con utenza anonima

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01
* set requestPendenza.tipoSanzione = 'Pulizia scale.'

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

* copy pendenzaCreata = response

* set requestPendenza.importo = 200.02

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param idA2A = pendenzaCreata.idA2A	
And param idPendenza = pendenzaCreata.idPendenza
And request requestPendenza
When method post
Then status 200
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

Scenario: Aggiornamento di una pendenza di tipo spontaneo con utenza anonima, parametri update non validi

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01
* set requestPendenza.tipoSanzione = 'Pulizia scale.'

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 201
And match response == pendenzaCreataMSG
And match response.idPendenza contains '' + idPendenza

* copy pendenzaCreata = response

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param idA2A = pendenzaCreata.idA2A	
And request requestPendenza
When method post
Then status 422
* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Per effettuare l\'aggiornamento della pendenza sono obbligatori entrambi i paramentri \'idA2A\' e \'idPendenza\''

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param idPendenza = pendenzaCreata.idPendenza
And headers idA2ABasicAutenticationHeader
And request requestPendenza
When method post
Then status 422
* match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'Per effettuare l\'aggiornamento della pendenza sono obbligatori entrambi i paramentri \'idA2A\' e \'idPendenza\''

Scenario: Errore semantico in trasformazione

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null
}
"""
* set requestPendenza.soggettoPagatore =
"""
{
		"identificativo": "XXXXXX00X00X000X",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set requestPendenza.idPendenza = '' + idPendenza
* set requestPendenza.importo = 100.01
* set requestPendenza.tipoSanzione = 'Pulizia scale.'

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And request requestPendenza
When method post
Then status 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: 'Test stop trasformazione' }

