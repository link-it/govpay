Feature: Accesso ai flussi di rendicontazione per idFlusso e dataOraFlusso

Background:

* callonce read('classpath:utils/api/v2/ragioneria/bunch-riconciliazioni-idFlussoNonUnivoco-v2.feature')

Scenario: Ricerca rendicontazioni da applicazione applicazione_dominio1.

* def applicazione = read('msg/applicazione_dominio1e2.json')
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v2', autenticazione: 'basic'})

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

#  risultatiPerPagina: 10
# pagina: 1
# ordinamento: data
# idDominio: 80002890301

Given url ragioneriaBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
And param pagina = 1
And param risultatiPerPagina = 10
And param ordinamento = 'data'
And param idDominio = idDominio
When method get
Then status 200
And match response ==
"""
{
	numRisultati: '#notnull',
	numPagine: '#notnull',
	risultatiPerPagina: '#notnull',
	pagina: '#notnull',
	prossimiRisultati: '#ignore',
	risultati: '#[]'
}
"""

# verifica ordinamento per field non previsto

Given url ragioneriaBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
And param pagina = 1
And param risultatiPerPagina = 10
And param ordinamento = 'iuv'
And param idDominio = idDominio
When method get
Then status 422

* match response == { categoria: 'RICHIESTA', codice: '100001', descrizione: 'Errore nella valorizzazione dei parametri della richiesta', dettaglio: '#notnull' }
* match response.dettaglio contains 'Il campo iuv non e\' valido per ordinare la ricerca in corso. Campi consentiti: [data, idFlusso]'


