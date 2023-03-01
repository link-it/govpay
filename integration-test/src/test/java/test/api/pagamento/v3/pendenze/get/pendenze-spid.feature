Feature: Risorsa pendenze con utenza SPID

Background:

* callonce read('classpath:utils/api/v2/pendenze/bunch-pendenze.feature')
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'spid'})
* def spidHeaders = {'X-SPID-FISCALNUMBER': 'RSSMRA30A01H501I','X-SPID-NAME': 'Mario','X-SPID-FAMILYNAME': 'Rossi','X-SPID-EMAIL': 'mrossi@mailserver.host.it'}

* configure cookies = null

Scenario Outline: Lettura dettaglio pendenze da utente spid

* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/pendenze', <idA2A>, <idPendenza>
And headers spidHeaders
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| idA2A | idPendenza | httpStatus | risposta |
| idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| idA2A | idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | pendenza-get-dettaglio.json |
| idA2A | idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| idA2A | idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A | 403 | errore_auth.json |
| idA2A | idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A | 403 | errore_auth.json |
| idA2A | idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A | 403 | errore_auth.json |

Scenario: Ricerca pendenze da utente SPID

# idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A
# idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A
# idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A
# idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A
# idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A
# idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A
# idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2
# idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2
# idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2
# idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2
# idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2

Given url pagamentiBaseurl
And path '/pendenze'
And param dataDa = dataInizio
And param dataA = dataFine
And headers spidHeaders
When method get
Then status 200
And match response.risultati[0].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2)'
And match response.risultati[1].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2)'
And match response.risultati[2].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2)'
And match response.risultati[3].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2)'
And match response.risultati[4].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2)'
And match response.risultati[5].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2)'
And match response.risultati[6].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A)'
And match response.risultati[7].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A)'
And match response.risultati[8].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A)'
And match response.risultati[9].idPendenza == '#(""+ idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A)'
And match response.risultati[10].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A)'
And match response.risultati[11].idPendenza == '#(""+ idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A	)'
And match response == 
"""
{
	numRisultati: 12,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: '#[12]'
}
"""

Scenario Outline: Acquisizione ricevuta non pagato dalla propria posizione

Given url pagamentiBaseurl
And path '/pendenze', <idA2A>, <idPendenza>
And headers spidHeaders
When method get
Then status 200

Given url pagamentiBaseurl
And path 'ricevute', response.dominio.idDominio, getIuvFromNumeroAvviso(response.numeroAvviso)
And param esito = 'ESEGUITO'
And headers spidHeaders
When method get
Then status 404

Examples:
| idA2A | idPendenza | 
| idA2A2 | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 | 
| idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 | 
| idA2A | idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A | 
| idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A | 

Scenario Outline: Acquisizione ricevuta pagato dalla propria posizione

Given url pagamentiBaseurl
And path '/pendenze', <idA2A>, <idPendenza>
And headers spidHeaders
When method get
Then status 200

Given url pagamentiBaseurl
And path 'ricevute', response.dominio.idDominio, getIuvFromNumeroAvviso(response.numeroAvviso)
And param esito = 'ESEGUITO'
And headers spidHeaders
When method get
Then status 200
And match response.risultati[0].idRicevuta == '#notnull'
And match response == 
"""
{
	numRisultati: <numero>,
	numPagine: 1,
	risultatiPerPagina: <numero>,
	pagina: '##null',
	prossimiRisultati: '##null',
	risultati: '#[]'
}
"""

Given url pagamentiBaseurl
And path 'ricevute', response.risultati[0].dominio.idDominio, response.risultati[0].iuv, response.risultati[0].idRicevuta
And headers spidHeaders
And headers { 'Accept' : 'application/json' }
When method get
Then status 200
And match response.rt.json.datiPagamento.codiceEsitoPagamento == '0'


Examples:
| idA2A | idPendenza | httpStatus | numero |
| idA2A2 | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 | 200 | 1 |
| idA2A2 | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 | 200 | 1 |
| idA2A2 | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 | 200 | 1 |
| idA2A | idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A | 200 | 1 |
| idA2A | idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A | 200 | 1 |
| idA2A | idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A | 200 | 1 |

