Feature: Gestione rendicontazione di altri intermediari

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:utils/nodo-genera-rendicontazioni.feature')
* callonce read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')
* def ragioneriaBaseurl = getGovPayApiBaseUrl({api: 'ragioneria', versione: 'v1', autenticazione: 'basic'})

Scenario: Acquisizione flusso di rendicontazione per EC registrati ma gestiti da altri intermediari

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"
* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')

* def ndpsym_rendicontazioni_url = ndpsym_url + '/pagopa/rs/dars/rendicontazioni/'

Given url ndpsym_rendicontazioni_url
And path 'generaEsterni', idDominio
When method get
Then assert responseStatus == 200
And def generaEsterni = response

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url ragioneriaBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	numRisultati: '##number',
	numPagine: '##number',
	risultatiPerPagina: '##number',
	pagina: '##number',
	prossimiRisultati: '##string',
	risultati: '#[]'
}
""" 
And match response.risultati[0].idFlusso == generaEsterni.response.rendicontazioni[0].identificativoFlusso

Given url ragioneriaBaseurl
And path 'flussiRendicontazione', generaEsterni.response.rendicontazioni[0].identificativoFlusso
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

Scenario: Acquisizione flusso di rendicontazione per EC non registrati

Given url ndpsym_rendicontazioni_url
And path 'generaEstraneo'
When method get
Then assert responseStatus == 200
And def generaEstranei = response

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url ragioneriaBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	numRisultati: '##number',
	numPagine: '##number',
	risultatiPerPagina: '##number',
	pagina: '##number',
	prossimiRisultati: '##string',
	risultati: '#[]'
}
""" 
And match response.risultati[0].idFlusso == generaEstranei.response.rendicontazioni[0].identificativoFlusso

Given url ragioneriaBaseurl
And path 'flussiRendicontazione', generaEstranei.response.rendicontazioni[0].identificativoFlusso
And headers idA2ABasicAutenticationHeader
When method get
Then status 200

Scenario: Acquisizione flusso di rendicontazione errati

* def tipoRicevuta = "R01"
* def riversamentoCumulativo = "true"
* call read('classpath:utils/workflow/modello3/v1/modello3-pagamento.feature')

* def ndpsym_rendicontazioni_url = ndpsym_url + '/pagopa/rs/dars/rendicontazioni/'

Given url ndpsym_rendicontazioni_url
And path 'generaErrate', idDominio
When method get
Then assert responseStatus == 200
And def generaErrate = response

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

Given url ragioneriaBaseurl
And path 'flussiRendicontazione'
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
	numRisultati: '##number',
	numPagine: '##number',
	risultatiPerPagina: '##number',
	pagina: '##number',
	prossimiRisultati: '##string',
	risultati: '#[]'
}
""" 
And match response.risultati[0].idFlusso == generaErrate.response.rendicontazioni[0].identificativoFlusso

Given url ragioneriaBaseurl
And path 'flussiRendicontazione', generaErrate.response.rendicontazioni[0].identificativoFlusso
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
