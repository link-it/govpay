Feature: Lettura Iban

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
* def backofficeBasicBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: Lettura Iban associati ad un dominio non esistente

* def idDominioNonCensito = '11221122331'

Given url backofficeBaseurl
And path 'domini', '11221122331', 'contiAccredito'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 


Scenario: Lettura di un Iban associato ad un dominio non esistente

* def idDominioNonCensito = '11221122331'
* def idIbanNonCensito = 'IT02L1235412345123456789012'

Given url backofficeBaseurl
And path 'domini', '11221122331', 'contiAccredito' , idIbanNonCensito
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Dominio "+idDominioNonCensito+" non censito in Anagrafica")' 

Scenario: Lettura di un iban inesistente associato ad un dominio

* def idIbanNonCensito = 'IT02L1235412345123456789012'

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito' , idIbanNonCensito
And headers gpAdminBasicAutenticationHeader
When method get
Then status 404
* match response == { categoria: 'OPERAZIONE', codice: '404000', descrizione: 'Risorsa non trovata', dettaglio: '#notnull' }
* match response.dettaglio contains '#("Iban di accredito "+idIbanNonCensito+" non censito in Anagrafica per il Dominio "+idDominio+"")' 


