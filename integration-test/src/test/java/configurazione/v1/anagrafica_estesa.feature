Feature: Configurazione

Background:

* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idDominio_2 = '12345678902'
* def ibanAccredito_2 = 'IT04L1234512345123456789012'
* def ibanAccreditoPostale_2 = 'IT04L0760112345123456789012'
* def codEntrataSiope = 'SIOPE_IMU'


Scenario: configurazione anagrafica base

#### creazione intermediario

#### creazione stazione

#### creazione dominio
* def dominio = read('classpath:configurazione/v1/msg/dominio.json')

Given url backofficeBaseurl
And path 'domini', idDominio_2 
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione contiAccredito

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'contiAccredito', ibanAccredito_2
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'contiAccredito', ibanAccreditoPostale_2
And headers basicAutenticationHeader
And request {postale:true,mybank:false,abilitato:true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'contiAccredito', ibanAccreditoErrato
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione tributi

Given url backofficeBaseurl
And path 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request {  descrizione: 'Imposta municipale aggiunta',  tipoContabilita: 'SIOPE',  codiceContabilita: '#(codEntrataSiope)' }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_2)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'entrate', codSpontaneo
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_2)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione tipiPendenza

Given url backofficeBaseurl
And path 'tipiPendenza', codEntrataSiope
And headers basicAutenticationHeader
And request {  descrizione: 'Imposta municipale aggiunta', codificaIUV: '012',  pagaTerzi: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'tipiPendenza', codEntrataSiope
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: true  }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', codSpontaneo
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: false }
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione applicazione

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

