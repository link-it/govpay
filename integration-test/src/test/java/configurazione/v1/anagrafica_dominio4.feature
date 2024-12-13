Feature: Configurazione

Background:

* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idDominio_4 = '12345678904'
* def ragioneSocialeDominio_4 = 'Ente Creditore Test 4'
* def ibanAccredito_4 = 'IT04L1234512345123456789014'
* def ibanAccredito_4Descrizione = 'IBAN Accredito 4'
* def ibanAccreditoPostale_4 = 'IT04L0760112345123456789014'
* def ibanAccreditoPostale_4Descrizione = 'IBAN Accredito Postale 4'
* def codEntrataSiope = 'SIOPE_IMU'


Scenario: configurazione anagrafica base

#### creazione intermediario

#### creazione stazione

#### creazione dominio
* def dominio4 = read('classpath:configurazione/v1/msg/dominio.json')

* set dominio4.ragioneSociale = ragioneSocialeDominio_4

Given url backofficeBaseurl
And path 'domini', idDominio_4 
And headers basicAutenticationHeader
And request dominio4
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione contiAccredito

Given url backofficeBaseurl
And path 'domini', idDominio_4, 'contiAccredito', ibanAccredito_4
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true, descrizione:'#(ibanAccredito_4Descrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_4, 'contiAccredito', ibanAccreditoPostale_4
And headers basicAutenticationHeader
And request {postale:true,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoPostale_4Descrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_4, 'contiAccredito', ibanAccreditoErrato
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
And path 'domini', idDominio_4, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_4)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_4, 'entrate', codSpontaneo
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_4)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_4, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_4)', ibanAppoggio: '#(ibanAccreditoPostale_4)', abilitato: true }
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
And path 'domini', idDominio_4, 'tipiPendenza', codEntrataSiope
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: true  }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_4, 'tipiPendenza', codSpontaneo
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: false }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_4, 'tipiPendenza', codEntrataSegreteria
And headers basicAutenticationHeader
And request { codificaIUV: '89',  pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201


Given url ndpsym_config_url 
And path 'domini', idDominio_4
And request 
"""
{
  "urlEC": "#(govpay_url +'/govpay/frontend/web/connector/ecsp/psp')",
  "auxDigit": 0,
  "versione": 1,
  "segregationCode": null,
  "ragioneSociale": "Ente Creditore Test 4",
  "idStazione": "11111111113_01",
  "idIntermediario": "11111111113"
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201


#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

