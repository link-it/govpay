Feature: Lettura Entrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Lettura Entrate associate ad un dominio non esistente

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'domini', '11221122331', 'entrate'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 


Scenario: Lettura di una Entrata associata ad un dominio non esistente

* def idDominioNonCensito = '11221122331'
* def idEntrataNonCensita = 'BLOLLOT'

Given url backofficeBaseurl
And path 'domini', '11221122331', 'entrate' , idEntrataNonCensita
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 

Scenario: Lettura di una Entrata inesistente associato ad un dominio

* def idEntrataNonCensita = 'BLOLLOT'

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate' , idEntrataNonCensita
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Entrata "+idEntrataNonCensita+" non censita in Anagrafica per il Dominio "+idDominio+"")' 


