Feature: Configurazione

Background:

* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idDominio_3 = '12345678903'
* def ragioneSocialeDominio_3 = 'Ente Creditore Test 3'
* def ibanAccredito_3 = 'IT04L1234512345123456789013'
* def ibanAccredito_3Descrizione = 'IBAN Accredito 3'
* def ibanAccreditoPostale_3 = 'IT04L0760112345123456789013'
* def ibanAccreditoPostale_3Descrizione = 'IBAN Accredito Postale 3'
* def codEntrataSiope = 'SIOPE_IMU'


Scenario: configurazione anagrafica base

#### creazione intermediario

#### creazione stazione

#### creazione dominio
* def dominio3 = read('classpath:configurazione/v1/msg/dominio.json')

* set dominio3.ragioneSociale = ragioneSocialeDominio_3

Given url backofficeBaseurl
And path 'domini', idDominio_3 
And headers basicAutenticationHeader
And request dominio3
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione contiAccredito

Given url backofficeBaseurl
And path 'domini', idDominio_3, 'contiAccredito', ibanAccredito_3
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true, descrizione:'#(ibanAccredito_3Descrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_3, 'contiAccredito', ibanAccreditoPostale_3
And headers basicAutenticationHeader
And request {postale:true,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoPostale_3Descrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_3, 'contiAccredito', ibanAccreditoErrato
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoErratoDescrizione)'}
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
And path 'domini', idDominio_3, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_3)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_3, 'entrate', codSpontaneo
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_3)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_3, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_3)', ibanAppoggio: '#(ibanAccreditoPostale_3)', abilitato: true }
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
And path 'domini', idDominio_3, 'tipiPendenza', codEntrataSiope
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: true  }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_3, 'tipiPendenza', codSpontaneo
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: false }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_3, 'tipiPendenza', codEntrataSegreteria
And headers basicAutenticationHeader
And request { codificaIUV: '89',  pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201


Given url ndpsym_config_url 
And path 'domini', idDominio_3
And request 
"""
{
  "urlEC": "#(govpay_web_connector_url +'/ecsp/psp')",
  "auxDigit": 0,
  "versione": 1,
  "segregationCode": null,
  "ragioneSociale": "Ente Creditore Test 3",
  "idStazione": "11111111113_01",
  "idIntermediario": "11111111113"
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201


#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

