Feature: Validazione sintattica richieste pagamento

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo.json')
* set pagamentoPost.soggettoVersante.tipo = 'F'
* set pagamentoPost.soggettoVersante.identificativo = 'VRDGPP65B03A112N'
* set pagamentoPost.soggettoVersante.anagrafica = 'Giuseppe Verdi'
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| urlRitorno | pagamentoPost.urlRitorno | 'htttttp://sbagliata.it' |  'urlRitorno' |
| dataPagamento | pagamentoPost.dataEsecuzionePagamento | '2030-19-40' | 'dataEsecuzionePagamento' |
| soggettoVersante.tipo | pagamentoPost.soggettoVersante.tipo | null | 'tipo' |
| soggettoVersante.tipo | pagamentoPost.soggettoVersante.tipo | 'X' | 'tipo' |
| soggettoVersante.identificativo | pagamentoPost.soggettoVersante.identificativo | null | 'identificativo' |
| soggettoVersante.identificativo | pagamentoPost.soggettoVersante.identificativo | '' | 'identificativo' |
| soggettoVersante.identificativo | pagamentoPost.soggettoVersante.identificativo | loremIpsum | 'identificativo' |
| soggettoVersante.anagrafica | pagamentoPost.soggettoVersante.anagrafica | '' | 'anagrafica' |
| soggettoVersante.anagrafica | pagamentoPost.soggettoVersante.anagrafica | loremIpsum | 'anagrafica' |
| soggettoVersante.indirizzo | pagamentoPost.soggettoVersante.indirizzo | '' | 'indirizzo' |
| soggettoVersante.indirizzo | pagamentoPost.soggettoVersante.indirizzo | loremIpsum | 'indirizzo' |
| soggettoVersante.civico | pagamentoPost.soggettoVersante.civico | '' | 'civico' |
| soggettoVersante.civico | pagamentoPost.soggettoVersante.civico | loremIpsum | 'civico' |
| soggettoVersante.cap | pagamentoPost.soggettoVersante.cap | '' | 'cap' |
| soggettoVersante.cap | pagamentoPost.soggettoVersante.cap | loremIpsum | 'cap' |
| soggettoVersante.localita | pagamentoPost.soggettoVersante.localita | '' | 'localita' |
| soggettoVersante.localita | pagamentoPost.soggettoVersante.localita | loremIpsum | 'localita' |
| soggettoVersante.provincia | pagamentoPost.soggettoVersante.provincia | '' | 'provincia' |
| soggettoVersante.provincia | pagamentoPost.soggettoVersante.provincia | loremIpsum | 'provincia' |
| soggettoVersante.nazione | pagamentoPost.soggettoVersante.nazione | 'aaa' | 'nazione' |
| soggettoVersante.email | pagamentoPost.soggettoVersante.email | 'verdi@giuseppe@email' | 'email' |
| soggettoVersante.cellulare | pagamentoPost.soggettoVersante.cellulare | '+390000000000' | 'cellulare' |
| autenticazioneSoggetto | pagamentoPost.autenticazioneSoggetto | 'XXX' | 'autenticazioneSoggetto' |
| lingua | pagamentoPost.lingua | 'XX' | 'lingua' |
| pendenze | pagamentoPost.pendenze | null | 'pendenze' |
| pendenze.nome | pagamentoPost.pendenze[0].nome | loremIpsum | 'nome' |
| pendenze.causale | pagamentoPost.pendenze[0].causale | null | 'causale' |
| pendenze.causale | pagamentoPost.pendenze[0].causale | loremIpsum | 'causale' |
| pendenze.causale | pagamentoPost.pendenze[0].causale | '' | 'causale' |
| pendenze.numeroAvviso | pagamentoPost.pendenze[0].numeroAvviso | loremIpsum | 'numeroAvviso' |
| pendenze.numeroAvviso | pagamentoPost.pendenze[0].numeroAvviso | 'ABC000000000000000' | 'numeroAvviso' |
| pendenze.numeroAvviso | pagamentoPost.pendenze[0].numeroAvviso | '000000000000000' | 'numeroAvviso' |
| pendenze.dataValidita | pagamentoPost.pendenze[0].dataValidita | '2030-19-40' | 'dataValidita' |
| pendenze.dataValidita | pagamentoPost.pendenze[0].dataScadenza | '2030-19-40' | 'dataScadenza' |
| pendenze.annoRiferimento | pagamentoPost.pendenze[0].annoRiferimento | 'aaaa' | 'annoRiferimento' |
| pendenze.tassonomiaAvviso | pagamentoPost.pendenze[0].tassonomiaAvviso | 'xxxx' | 'tassonomiaAvviso' |
| pendenze.soggettoPagatore.tipo | pagamentoPost.pendenze[0].soggettoPagatore | null | 'soggettoPagatore' |
| pendenze.soggettoPagatore.tipo | pagamentoPost.pendenze[0].soggettoPagatore.tipo | null | 'tipo' |
| pendenze.soggettoPagatore.tipo | pagamentoPost.pendenze[0].soggettoPagatore.tipo | 'X' | 'tipo' |
| pendenze.soggettoPagatore.identificativo | pagamentoPost.pendenze[0].soggettoPagatore.identificativo | null | 'identificativo' |
| pendenze.soggettoPagatore.identificativo | pagamentoPost.pendenze[0].soggettoPagatore.identificativo | '' | 'identificativo' |
| pendenze.soggettoPagatore.identificativo | pagamentoPost.pendenze[0].soggettoPagatore.identificativo | loremIpsum | 'identificativo' |
| pendenze.soggettoPagatore.anagrafica | pagamentoPost.pendenze[0].soggettoPagatore.anagrafica | '' | 'anagrafica' |
| pendenze.soggettoPagatore.anagrafica | pagamentoPost.pendenze[0].soggettoPagatore.anagrafica | loremIpsum | 'anagrafica' |
| pendenze.soggettoPagatore.indirizzo | pagamentoPost.pendenze[0].soggettoPagatore.indirizzo | '' | 'indirizzo' |
| pendenze.soggettoPagatore.indirizzo | pagamentoPost.pendenze[0].soggettoPagatore.indirizzo | loremIpsum | 'indirizzo' |
| pendenze.soggettoPagatore.civico | pagamentoPost.pendenze[0].soggettoPagatore.civico | '' | 'civico' |
| pendenze.soggettoPagatore.civico | pagamentoPost.pendenze[0].soggettoPagatore.civico | loremIpsum | 'civico' |
| pendenze.soggettoPagatore.cap | pagamentoPost.pendenze[0].soggettoPagatore.cap | '' | 'cap' |
| pendenze.soggettoPagatore.cap | pagamentoPost.pendenze[0].soggettoPagatore.cap | loremIpsum | 'cap' |
| pendenze.soggettoPagatore.localita | pagamentoPost.pendenze[0].soggettoPagatore.localita | '' | 'localita' |
| pendenze.soggettoPagatore.localita | pagamentoPost.pendenze[0].soggettoPagatore.localita | loremIpsum | 'localita' |
| pendenze.soggettoPagatore.provincia | pagamentoPost.pendenze[0].soggettoPagatore.provincia | '' | 'provincia' |
| pendenze.soggettoPagatore.provincia | pagamentoPost.pendenze[0].soggettoPagatore.provincia | loremIpsum | 'provincia' |
| pendenze.soggettoPagatore.nazione | pagamentoPost.pendenze[0].soggettoPagatore.nazione | 'aaa' | 'nazione' |
| pendenze.soggettoPagatore.email | pagamentoPost.pendenze[0].soggettoPagatore.email | 'verdi@giuseppe@email' | 'email' |
| pendenze.soggettoPagatore.cellulare | pagamentoPost.pendenze[0].soggettoPagatore.cellulare | '+390000000000' | 'cellulare' |
| pendenze.soggettoPagatore.cellulare | pagamentoPost.pendenze[0].soggettoPagatore.cellulare | '+390000000000' | 'cellulare' |
| pendenze.importo | pagamentoPost.pendenze[0].importo | null | 'importo' |
| pendenze.importo | pagamentoPost.pendenze[0].importo | '10.001' | 'importo' |
| pendenze.importo | pagamentoPost.pendenze[0].importo | '10,000' | 'importo' |
| pendenze.importo | pagamentoPost.pendenze[0].importo | '10,00.0' | 'importo' |
| pendenze.importo | pagamentoPost.pendenze[0].importo | 'aaaa' | 'importo' |
| pendenze.voci | pagamentoPost.pendenze[0].voci | null | 'voci' |
| pendenze.voci.idVocePendenza | pagamentoPost.pendenze[0].voci[0].idVocePendenza | null | 'idVocePendenza' |
| pendenze.voci.idVocePendenza | pagamentoPost.pendenze[0].voci[0].idVocePendenza | loremIpsum | 'idVocePendenza' |
| pendenze.voci.importo | pagamentoPost.pendenze[0].voci[0].importo | null | 'importo' |
| pendenze.voci.importo | pagamentoPost.pendenze[0].voci[0].importo | '10.001' | 'importo' |
| pendenze.voci.importo | pagamentoPost.pendenze[0].voci[0].importo | '10,000' | 'importo' |
| pendenze.voci.importo | pagamentoPost.pendenze[0].voci[0].importo | '10,00.0' | 'importo' |
| pendenze.voci.importo | pagamentoPost.pendenze[0].voci[0].importo | 'aaaa' | 'importo' |
| pendenze.voci.descrizione | pagamentoPost.pendenze[0].voci[0].descrizione | null | 'descrizione' |
| pendenze.voci.descrizione | pagamentoPost.pendenze[0].voci[0].descrizione | loremIpsum | 'descrizione' |
| pendenze.voci.codEntrata | pagamentoPost.pendenze[0].voci[0].codEntrata | null | 'codEntrata' |
| pendenze.voci.ibanAccredito | pagamentoPost.pendenze[0].voci[1].ibanAccredito | null | 'ibanAccredito' |
| pendenze.voci.tipoContabilita | pagamentoPost.pendenze[0].voci[1].tipoContabilita | null | 'tipoContabilita' |
| pendenze.voci.tipoContabilita | pagamentoPost.pendenze[0].voci[1].tipoContabilita | 'xxx' | 'tipoContabilita' |
| pendenze.voci.codiceContabilita | pagamentoPost.pendenze[0].voci[1].codiceContabilita | null | 'codiceContabilita' |
| pendenze.voci.codiceContabilita | pagamentoPost.pendenze[0].voci[1].codiceContabilita | '' | 'codiceContabilita' |
| pendenze.voci.codiceContabilita | pagamentoPost.pendenze[0].voci[1].codiceContabilita | 'XX' | 'codiceContabilita' |
| pendenze.voci.codiceContabilita | pagamentoPost.pendenze[0].voci[1].codiceContabilita | 'XX X' | 'codiceContabilita' |
| pendenze.voci.tipoBollo | pagamentoPost.pendenze[0].voci[2].tipoBollo | null | 'tipoBollo' |
| pendenze.voci.tipoBollo | pagamentoPost.pendenze[0].voci[2].tipoBollo | 'xxx' | 'tipoBollo' |
| pendenze.voci.hashDocumento | pagamentoPost.pendenze[0].voci[2].hashDocumento | null | 'hashDocumento' |
| pendenze.voci.hashDocumento | pagamentoPost.pendenze[0].voci[2].hashDocumento | loremIpsum | 'hashDocumento' |
| pendenze.voci.provinciaRedidenza | pagamentoPost.pendenze[0].voci[2].provinciaRedidenza | null | 'provinciaRedidenza' |
| pendenze.voci.provinciaRedidenza | pagamentoPost.pendenze[0].voci[2].provinciaRedidenza | 'xxx' | 'provinciaRedidenza' |

Scenario: Riferimento pendenza errato

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request { pendenze: [ { idA2A: idA2A, idPendenza: null } ] }
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'idPendenza'

Scenario: Riferimento avviso errato

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request { pendenze: [ { idDominio: idDominio, numeroAvviso: null } ] }
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'numeroAvviso'

Scenario: Numero voci eccessivo

* set pagamentoPost.pendenze[0].voci[3] = pagamentoPost.pendenze[0].voci[0]
* set pagamentoPost.pendenze[0].voci[3].idVocePendenza = 4
* set pagamentoPost.pendenze[0].voci[4] = pagamentoPost.pendenze[0].voci[0]
* set pagamentoPost.pendenze[0].voci[4].idVocePendenza = 5
* set pagamentoPost.pendenze[0].voci[5] = pagamentoPost.pendenze[0].voci[0]
* set pagamentoPost.pendenze[0].voci[5].idVocePendenza = 6

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'voci'

Scenario: Array pendenze vuoto

* set pagamentoPost.pendenze = []

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'pendenze'


Scenario: Numero pendenze eccessivo

* set pagamentoPost.pendenze[1] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[2] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[3] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[4] = pagamentoPost.pendenze[0]
* set pagamentoPost.pendenze[5] = pagamentoPost.pendenze[0]

Given url pagamentiBaseurl
And path '/pagamenti'
And headers basicAutenticationHeader
And request pagamentoPost
When method post
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains 'pendenze'
