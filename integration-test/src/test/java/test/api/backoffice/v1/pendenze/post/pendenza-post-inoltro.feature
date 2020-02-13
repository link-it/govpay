Feature: Trasformazione pendenza

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Given url backofficeBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  form: null,
  trasformazione: null,
  validazione: null
}
"""          
* set tipoPendenzaDominio.inoltro = idA2A

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201


Scenario: Pendenza da form inoltrata all'applicazione 

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

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 201

Scenario: Pendenza da form inoltrata all'applicazione con indicazione dell'UO
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

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And param idUnitaOperativa = idUnitaOperativa
And request pendenza
When method put
Then status 200

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And param idUnitaOperativa = idUnitaOperativa
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 201
And match response.idUnitaOperativa == idUnitaOperativa


Scenario Outline: Pendenza da form inoltrata all'applicazione con <field> non valida

* def pendenzaPut = read('classpath:test/api/pendenza/v1/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

* set pendenza.idA2A = idA2A
* set pendenza.idPendenza = idPendenza
* set pendenza.numeroAvviso = numeroAvviso
* set pendenza.stato = 'NON_ESEGUITA'

* set <fieldRequest> = <fieldValue>

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
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


Scenario: Pendenza da form inoltrata all'applicazione con risposta contenente un tipo pendenza errato

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

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/vTP"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + tipoPendenzaRinnovo +"] all\'applicazione competente [Applicazione:" + idA2A +"] e\' fallito con errore: Il campo IdTipoPendenza della pendenza ricevuta dal servizio di verifica non corrisponde ai parametri di input.")'
}
"""


Scenario: Pendenza da form inoltrata all'applicazione con risposta errata

* set applicazione.servizioIntegrazione.url = ente_api_url + "/vERROR"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + tipoPendenzaRinnovo +"] all\'applicazione competente [Applicazione:" + idA2A +"] e\' fallito con errore: Ricevuto [HTTP 500]")'
}
"""

Scenario: Pendenza da form inoltrata all'applicazione risposta pendenza sconosciuta

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + tipoPendenzaRinnovo +"] all\'applicazione competente [Applicazione:" + idA2A +"] ha dato esito PAA_PAGAMENTO_SCONOSCIUTO")'
}
"""

Scenario: Pendenza da form inoltrata all'applicazione risposta pendenza scaduta

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

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + tipoPendenzaRinnovo +"] all\'applicazione competente [Applicazione:" + idA2A +"] ha dato esito PAA_PAGAMENTO_SCADUTO")'
}
"""

Scenario: Pendenza da form inoltrata all'applicazione risposta pendenza annullata

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

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + tipoPendenzaRinnovo +"] all\'applicazione competente [Applicazione:" + idA2A +"] ha dato esito PAA_PAGAMENTO_ANNULLATO")'
}
"""


Scenario: Numero avviso su multivoce

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
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 502
And match response == 
"""
{ 
	categoria: 'EC',
	codice: '502000',
	descrizione: 'Errore ente creditore',
	dettaglio: '#("L\'inoltro del versamento [Dominio: " + idDominio +" TipoVersamento:" + tipoPendenzaRinnovo +"] all\'applicazione competente [Applicazione:" + idA2A +"] e\' fallito con errore: Non e\' possibile indicare il numero avviso per una pendenza di tipo multivoce.")'
}
"""

Scenario Outline: <field> non valida

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
* set <fieldRequest> = <fieldValue>

Given url ente_api_url
And path '/v1/pendenze', idA2A, idPendenza
And request pendenza
When method put
Then status 200

* set applicazione.servizioIntegrazione.url = ente_api_url + "/v1"

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers basicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idA2A: "#(''+idA2A)",
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
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


