Feature: Pagamento spontaneo modello 4 con trasformazione dell'input senza inoltro

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def idTipoPendenza = codSpontaneo
* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: false,
  form: null,
  trasformazione: {
  	tipo: "freemarker",
  	definizione: null
  },
  validazione: null,
  abilitato: true
}
"""  
* set tipoPendenzaDominio.inoltro = idA2A
* set tipoPendenzaDominio.validazione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-validazione-form.json.payload'))
* set tipoPendenzaDominio.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-spontanea-inoltro-freemarker.ftl'))

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v2', autenticazione: 'basic'})

* def pagamentoPost = read('classpath:test/api/pagamento/v2/pagamenti/post/msg/pagamento-post_spontaneo_modello4.json')

Scenario: Pagamento spontaneo modello 4 autenticato basic

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
#* def importo = 100.99

* set pendenza.idTipoPendenza = idTipoPendenza
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'
* set pendenza.causale = "#(Pagamento n. "+ pagamentoPost.pendenze[0].dati.numero +" buoni pasto)"
* set pendenza.importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.voci[0].importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.soggettoPagatore = pagamentoPost.pendenze[0].dati.soggettoPagatore

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 201
And match response ==  { id: '#notnull', location: '#notnull', redirect: '#notnull', idSession: '#notnull' }

Given url pagamentiBaseurl
And path '/pagamenti/byIdSession/', response.idSession
And headers basicAutenticationHeader
When method get
Then status 200
And match response.rpp[0].rpt.soggettoVersante == 
"""
{
	"identificativoUnivocoVersante": {
		"tipoIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.tipo)",
		"codiceIdentificativoUnivoco":"#(pagamentoPost.soggettoVersante.identificativo)"
	},
	"anagraficaVersante":"#(pagamentoPost.soggettoVersante.anagrafica)",
	"indirizzoVersante":"#(pagamentoPost.soggettoVersante.indirizzo)",
	"civicoVersante":"#(pagamentoPost.soggettoVersante.civico)",
	"capVersante":"#(pagamentoPost.soggettoVersante.cap + '')",
	"localitaVersante":"#(pagamentoPost.soggettoVersante.localita)",
	"provinciaVersante":"#(pagamentoPost.soggettoVersante.provincia)",
	"nazioneVersante":"#(pagamentoPost.soggettoVersante.nazione)",
	"e-mailVersante":"#(pagamentoPost.soggettoVersante.email)"
}
"""

Scenario Outline: Pagamento spontaneo modello 4 inoltrato all'applicazione con <field> non valida

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()

* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'
* set pendenza.causale = "#(Pagamento n. "+ pagamentoPost.pendenze[0].dati.numero +" buoni pasto)"
* set pendenza.importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.voci[0].importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.soggettoPagatore = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* set <fieldRequest> = <fieldValue>

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#notnull'
}
"""
And match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| nome | pendenzaPut.nome | loremIpsum | 'nome' |
| causale | pendenzaPut.causale | null | 'causale' |
| causale | pendenzaPut.causale | loremIpsum | 'causale' |
| causale | pendenzaPut.causale | '' | 'causale' |
| numeroAvviso | pendenzaPut.numeroAvviso | loremIpsum | 'numeroAvviso' |
| numeroAvviso | pendenzaPut.numeroAvviso | 'ABC000000000000000' | 'numeroAvviso' |
| dataValidita | pendenzaPut.dataValidita | '2030-19-40' | 'dataValidita' |
| dataValidita | pendenzaPut.dataScadenza | '2030-19-40' | 'dataScadenza' |
| annoRiferimento | pendenzaPut.annoRiferimento | 'aaaa' | 'annoRiferimento' |
| tassonomiaAvviso | pendenzaPut.tassonomiaAvviso | 'xxxx' | 'tassonomiaAvviso' |
| soggettoPagatore | pendenzaPut.soggettoPagatore | null | 'soggettoPagatore' |
| soggettoPagatore.tipo | pendenzaPut.soggettoPagatore.tipo | null | 'tipo' |
| soggettoPagatore.tipo | pendenzaPut.soggettoPagatore.tipo | 'X' | 'tipo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | null | 'identificativo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | '' | 'identificativo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | loremIpsum | 'identificativo' |
| soggettoPagatore.anagrafica | pendenzaPut.soggettoPagatore.anagrafica | '' | 'anagrafica' |
| soggettoPagatore.anagrafica | pendenzaPut.soggettoPagatore.anagrafica | loremIpsum | 'anagrafica' |
| soggettoPagatore.indirizzo | pendenzaPut.soggettoPagatore.indirizzo | '' | 'indirizzo' |
| soggettoPagatore.indirizzo | pendenzaPut.soggettoPagatore.indirizzo | loremIpsum | 'indirizzo' |
| soggettoPagatore.civico | pendenzaPut.soggettoPagatore.civico | '' | 'civico' |
| soggettoPagatore.civico | pendenzaPut.soggettoPagatore.civico | loremIpsum | 'civico' |
| soggettoPagatore.cap | pendenzaPut.soggettoPagatore.cap | '' | 'cap' |
| soggettoPagatore.cap | pendenzaPut.soggettoPagatore.cap | loremIpsum | 'cap' |
| soggettoPagatore.localita | pendenzaPut.soggettoPagatore.localita | '' | 'localita' |
| soggettoPagatore.localita | pendenzaPut.soggettoPagatore.localita | loremIpsum | 'localita' |
| soggettoPagatore.provincia | pendenzaPut.soggettoPagatore.provincia | '' | 'provincia' |
| soggettoPagatore.provincia | pendenzaPut.soggettoPagatore.provincia | loremIpsum | 'provincia' |
| soggettoPagatore.nazione | pendenzaPut.soggettoPagatore.nazione | 'aaa' | 'nazione' |
| soggettoPagatore.email | pendenzaPut.soggettoPagatore.email | 'verdi@giuseppe@email' | 'email' |
| soggettoPagatore.cellulare | pendenzaPut.soggettoPagatore.cellulare | '+390000000000' | 'cellulare' |
| soggettoPagatore.cellulare | pendenzaPut.soggettoPagatore.cellulare | '+390000000000' | 'cellulare' |
| importo | pendenzaPut.importo | null | 'importo' |
| importo | pendenzaPut.importo | '10.001' | 'importo' |
| importo | pendenzaPut.importo | '10,000' | 'importo' |
| importo | pendenzaPut.importo | '10,00.0' | 'importo' |
| importo | pendenzaPut.importo | 'aaaa' | 'importo' |
| voci | pendenzaPut.voci | null | 'voci' |
| voci.idVocePendenza | pendenzaPut.voci[0].idVocePendenza | null | 'idVocePendenza' |
| voci.idVocePendenza | pendenzaPut.voci[0].idVocePendenza | loremIpsum | 'idVocePendenza' |
| voci.importo | pendenzaPut.voci[0].importo | null | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | '10.001' | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | '10,000' | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | '10,00.0' | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | 'aaaa' | 'importo' |
| voci.descrizione | pendenzaPut.voci[0].descrizione | null | 'descrizione' |
| voci.descrizione | pendenzaPut.voci[0].descrizione | loremIpsum | 'descrizione' |
| voci.codEntrata | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", codEntrata: "xxxxx" } | 'xxxxx' |
| voci.codEntrata | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", codEntrata: null } | 'codEntrata' |
| voci.ibanAccredito | pendenzaPut.voci[0].ibanAccredito | null | 'ibanAccredito' |
| voci.tipoContabilita | pendenzaPut.voci[0].tipoContabilita | null | 'tipoContabilita' |
| voci.tipoContabilita | pendenzaPut.voci[0].tipoContabilita | 'xxx' | 'tipoContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[0].codiceContabilita | null | 'codiceContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[0].codiceContabilita | '' | 'codiceContabilita' |
| voci.tipoBollo | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", tipoBollo: null, hashDocumento: "a/CWqtFtCEyA/ymBySahGSaqKMiak5mlX3BoX0jupy8=", provinciaResidenza: "RO" } | 'tipoBollo' |
| voci.tipoBollo | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", tipoBollo: "09", hashDocumento: "a/CWqtFtCEyA/ymBySahGSaqKMiak5mlX3BoX0jupy8=", provinciaResidenza: "RO" } | 'tipoBollo' |
| voci.hashDocumento | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", tipoBollo: "01", hashDocumento: null, provinciaResidenza: "RO" } | 'hashDocumento' |
| voci.hashDocumento | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", tipoBollo: "01", hashDocumento: "#(loremIpsum)", provinciaResidenza: "RO" } | 'hashDocumento' |
| voci.provinciaResidenza | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", tipoBollo: "01", hashDocumento: "a/CWqtFtCEyA/ymBySahGSaqKMiak5mlX3BoX0jupy8=", provinciaResidenza: null } | 'provinciaResidenza' |
| voci.provinciaResidenza | pendenzaPut.voci[0] | { idVocePendenza: 1, importo: 10.00, descrizione: "descrizione", tipoBollo: "01", hashDocumento: "a/CWqtFtCEyA/ymBySahGSaqKMiak5mlX3BoX0jupy8=", provinciaResidenza: "xxx" } | 'provinciaResidenza' |


Scenario: Pagamento spontaneo modello 4 inoltrato all'applicazione con risposta contenente un tipo pendenza errato

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
#* def importo = 100.99

* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'
* set pendenza.causale = "#(Pagamento n. "+ pagamentoPost.pendenze[0].dati.numero +" buoni pasto)"
* set pendenza.importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.voci[0].importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.soggettoPagatore = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/vTP"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + codSpontaneo +"] all\'applicazione competente [Applicazione:" + idA2A +"] e\' fallito con errore: Il campo IdTipoPendenza della pendenza ricevuta dal servizio di verifica non corrisponde ai parametri di input.")'
}
"""

Scenario: Pagamento spontaneo modello 4 inoltrato all'applicazione con risposta errata

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* set applicazione.servizioIntegrazione.url = ente_api_url + "/vERROR"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* def idPendenza = getCurrentTimeMillis()

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + codSpontaneo +"] all\'applicazione competente [Applicazione:" + idA2A +"] e\' fallito con errore: Ricevuto [HTTP 500]")'
}
"""

Scenario: Pagamento spontaneo modello 4 inoltrato all'applicazione risposta pendenza sconosciuta

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + codSpontaneo +"] all\'applicazione competente [Applicazione:" + idA2A +"] ha dato esito PAA_PAGAMENTO_SCONOSCIUTO")'
}
"""

Scenario: Pagamento spontaneo modello 4 inoltrato all'applicazione risposta pendenza scaduta

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
#* def importo = 100.99

* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenzaPut.stato = 'SCADUTA'
* set pendenzaPut.descrizioneStato = 'Test scadenza'
* set pendenza.causale = "#(Pagamento n. "+ pagamentoPost.pendenze[0].dati.numero +" buoni pasto)"
* set pendenza.importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.voci[0].importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.soggettoPagatore = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + codSpontaneo +"] all\'applicazione competente [Applicazione:" + idA2A +"] ha dato esito PAA_PAGAMENTO_SCADUTO")'
}
"""

Scenario: Pagamento spontaneo modello 4 inoltrato all'applicazione risposta pendenza annullata

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
#* def importo = 100.99

* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenzaPut.stato = 'ANNULLATA'
* set pendenzaPut.descrizioneStato = 'Test annullamento'
* set pendenza.causale = "#(Pagamento n. "+ pagamentoPost.pendenze[0].dati.numero +" buoni pasto)"
* set pendenza.importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.voci[0].importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.soggettoPagatore = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + codSpontaneo +"] all\'applicazione competente [Applicazione:" + idA2A +"] ha dato esito PAA_PAGAMENTO_ANNULLATO")'
}
"""


Scenario: Pagamento spontaneo modello 4 inoltrato all'applicazione Numero avviso su multivoce

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_multivoce_bollo.json')
* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + codSpontaneo +"] all\'applicazione competente [Applicazione:" + idA2A +"] e\' fallito con errore: Non e\' possibile indicare il numero avviso per una pendenza di tipo multivoce.")'
}
"""

Scenario Outline: Pagamento spontaneo modello 4 inoltrato all'applicazione <field> non valida

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
* set pagamentoPost.pendenze[0].dati.soggettoPagatore =
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""

* def idPendenza = getCurrentTimeMillis()
* def pendenza = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = 10.00
* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'
* set pendenza.causale = "#(Pagamento n. "+ pagamentoPost.pendenze[0].dati.numero +" buoni pasto)"
* set pendenza.importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.voci[0].importo = pagamentoPost.pendenze[0].dati.numero * 2.00
* set pendenza.soggettoPagatore = 
"""
{
		"tipo": "F",
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
}
"""
* set <fieldRequest> = <fieldValue>

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBasicBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

* set pagamentoPost.pendenze[0].dati.idA2A = idA2A
* set pagamentoPost.pendenze[0].dati.idPendenza = idPendenza

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#notnull'
}
"""
And match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| idDominio | pendenza.idDominio | idDominio_2 | 'IdDominio' |
| importo | pendenza.importo | 0.01 | 'importo' |


