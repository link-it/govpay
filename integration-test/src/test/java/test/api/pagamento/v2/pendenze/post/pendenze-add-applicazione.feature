Feature: Caricamento Pendenze Applicazione

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* def idTipoPendenzaCOSAP = 'COSAP'

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

Scenario: Inserimento di una nuova pendenza di tipo spontaneo basic

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null,
	"tipoSanzione" : null
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
And headers idA2ABasicAutenticationHeader
And request requestPendenza
When method post
Then status 201
And match response == 
"""
{
	idDominio: '#(idDominio)',
	numeroAvviso: '#regex[0-9]{18}',
	idA2A: 'IDA2A01',
	idPendenza: '#string'
}
"""
And match response.idPendenza contains '' + idPendenza

* copy pendenzaCreata = response

* def dataEnd = getDateTime()

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataStart	
And param dataA = dataEnd
And param mostraSpontaneiNonPagati = true		
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == pendenzaCreata.idPendenza
And match response.risultati[0].numeroAvviso == pendenzaCreata.numeroAvviso
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

Scenario: Aggiornamento di una pendenza di tipo spontaneo basic

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null,
	"tipoSanzione" : null
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
And headers idA2ABasicAutenticationHeader
And request requestPendenza
When method post
Then status 201
And match response == 
"""
{
	idDominio: '#(idDominio)',
	numeroAvviso: '#regex[0-9]{18}',
	idA2A: 'IDA2A01',
	idPendenza: '#string'
}
"""
And match response.idPendenza contains '' + idPendenza

* copy pendenzaCreata = response

* def dataEnd = getDateTime()

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataStart	
And param dataA = dataEnd
And param mostraSpontaneiNonPagati = true		
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == pendenzaCreata.idPendenza
And match response.risultati[0].numeroAvviso == pendenzaCreata.numeroAvviso
And match response.risultati[0].importo == requestPendenza.importo
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

* set requestPendenza.importo = 200.02

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param idA2A = pendenzaCreata.idA2A	
And param idPendenza = pendenzaCreata.idPendenza
And headers idA2ABasicAutenticationHeader
And request requestPendenza
When method post
Then status 200
And match response == 
"""
{
	idDominio: '#(idDominio)',
	numeroAvviso: '#regex[0-9]{18}',
	idA2A: 'IDA2A01',
	idPendenza: '#string'
}
"""
And match response.idPendenza contains '' + idPendenza

* copy pendenzaAggiornata = response

And match pendenzaCreata.idPendenza == pendenzaAggiornata.idPendenza
And match pendenzaCreata.numeroAvviso == pendenzaAggiornata.numeroAvviso

Given url backofficeBaseurl
And path '/pendenze'
And param dataDa = dataStart	
And param dataA = dataEnd
And param mostraSpontaneiNonPagati = true		
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.risultati[0].idPendenza == pendenzaAggiornata.idPendenza
And match response.risultati[0].numeroAvviso == pendenzaAggiornata.numeroAvviso
And match response.risultati[0].importo == requestPendenza.importo
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

Scenario: Aggiornamento di una pendenza di tipo spontaneo basic, parametri update non validi

* def dataStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
* def requestPendenza =
"""
{
	"idPendenza": null,
	"importo": null,
	"tipoSanzione" : null
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
And headers idA2ABasicAutenticationHeader
And request requestPendenza
When method post
Then status 201
And match response == 
"""
{
	idDominio: '#(idDominio)',
	numeroAvviso: '#regex[0-9]{18}',
	idA2A: 'IDA2A01',
	idPendenza: '#string'
}
"""
And match response.idPendenza contains '' + idPendenza

Given url pagamentiBaseurl
And path '/pendenze', idDominio, idTipoPendenzaCOSAP
And param idA2A = pendenzaCreata.idA2A	
And headers idA2ABasicAutenticationHeader
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

