Feature: Validazione sintattica avvisi ente

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def esitoAttivaRPT = {"faultCode":"PAA_SYSTEM_ERROR","faultString":"Errore generico.","id":"12345678901","description":"#notnull","serial": "#ignore"}
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'


# Configurazione Applicazione per utilizzare le API V2


* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.versioneApi = 'REST v2'
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201


Scenario Outline: <field> non valida

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_definito.json')

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
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenza
When method post
Then status 200

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

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
| voci.descrizioneCausaleRPT | pendenzaPut.voci[0].descrizioneCausaleRPT | '' | 'descrizioneCausaleRPT' |
| voci.descrizioneCausaleRPT | pendenzaPut.voci[0].descrizioneCausaleRPT | loremIpsum | 'descrizioneCausaleRPT' |

Scenario Outline: <field> non valida

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_riferimento_contabilita.json')

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
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenza
When method post
Then status 200

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | null | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '10.001' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '10,000' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '10,00.0' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | 'aaaa' | 'importo' |
| importo | pendenzaPut.voci[0].contabilita.quote[0].importo | '12345678901234567,89' | 'importo' |
| capitolo | pendenzaPut.voci[0].contabilita.quote[0].capitolo | loremIpsum | 'capitolo' |
| capitolo | pendenzaPut.voci[0].contabilita.quote[0].capitolo | null | 'capitolo' |
| accertamento | pendenzaPut.voci[0].contabilita.quote[0].accertamento | loremIpsum | 'accertamento' |
| annoEsercizio | pendenzaPut.voci[0].contabilita.quote[0].annoEsercizio | 12345 | 'annoEsercizio' |
| annoEsercizio | pendenzaPut.voci[0].contabilita.quote[0].annoEsercizio | loremIpsum | 'annoEsercizio' |
| annoEsercizio | pendenzaPut.voci[0].contabilita.quote[0].annoEsercizio | null | 'annoEsercizio' |
| titolo | pendenzaPut.voci[0].contabilita.quote[0].titolo | loremIpsum | 'titolo' |
| tipologia | pendenzaPut.voci[0].contabilita.quote[0].tipologia | loremIpsum | 'tipologia' |
| categoria | pendenzaPut.voci[0].contabilita.quote[0].categoria | loremIpsum | 'categoria' |
| articolo | pendenzaPut.voci[0].contabilita.quote[0].articolo | loremIpsum | 'articolo' |

Scenario Outline: <field> non valida 

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* set <fieldRequest> = <fieldValue>

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| voci.ibanAccredito | pendenzaPut.voci[0].ibanAccredito | null | 'ibanAccredito' |
| voci.tipoContabilita | pendenzaPut.voci[0].tipoContabilita | null | 'tipoContabilita' |
| voci.tipoContabilita | pendenzaPut.voci[0].tipoContabilita | 'xxx' | 'tipoContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[0].codiceContabilita | null | 'codiceContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[0].codiceContabilita | '' | 'codiceContabilita' |

Scenario Outline: <field> non valida

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

* set <fieldRequest> = <fieldValue>

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| voci.tipoBollo | pendenzaPut.voci[0].tipoBollo | null | 'tipoBollo' |
| voci.tipoBollo | pendenzaPut.voci[0].tipoBollo | 'xxx' | 'tipoBollo' |
| voci.hashDocumento | pendenzaPut.voci[0].hashDocumento | null | 'hashDocumento' |
| voci.hashDocumento | pendenzaPut.voci[0].hashDocumento | loremIpsum | 'hashDocumento' |
| voci.provinciaResidenza | pendenzaPut.voci[0].provinciaResidenza | null | 'provinciaResidenza' |
| voci.provinciaResidenza | pendenzaPut.voci[0].provinciaResidenza | 'xxx' | 'provinciaResidenza' |

Scenario: Numero voci eccessivo

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_bollo.json')

* set pendenzaPut.voci[1] = pendenzaPut.voci[0]
* set pendenzaPut.voci[1].idVocePendenza = 2
* set pendenzaPut.voci[2] = pendenzaPut.voci[0]
* set pendenzaPut.voci[2].idVocePendenza = 3
* set pendenzaPut.voci[3] = pendenzaPut.voci[0]
* set pendenzaPut.voci[3].idVocePendenza = 4
* set pendenzaPut.voci[4] = pendenzaPut.voci[0]
* set pendenzaPut.voci[4].idVocePendenza = 5
* set pendenzaPut.voci[5] = pendenzaPut.voci[0]
* set pendenzaPut.voci[5].idVocePendenza = 6

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains 'voci'


Scenario Outline: <field> non valida

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_definito.json')
* set pendenzaPut.allegati[0].nome = 'tipoPendenza-promemoria-oggetto-freemarker.ftl'
* set pendenzaPut.allegati[0].tipo = 'application/json'
* set pendenzaPut.allegati[0].contenuto = encodeBase64InputStream(read('classpath:test/api/backoffice/v1/pendenze/put/msg/tipoPendenza-promemoria-oggetto-freemarker.ftl'))

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
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenza
When method post
Then status 200

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| allegati.nome | pendenzaPut.allegati[0].nome | null | 'nome' |
| allegati.nome | pendenzaPut.allegati[0].nome | loremIpsum | 'nome' |
| allegati.tipo | pendenzaPut.allegati[0].tipo | loremIpsum | 'tipo' |
| allegati.contenuto | pendenzaPut.allegati[0].contenuto | null | 'contenuto' |


