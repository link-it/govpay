Feature: Configurazione

Background:

* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idDominio_5 = '12345678905'
* def ragioneSocialeDominio_5 = 'Ente Creditore Test 5'
* def ibanAccredito_5 = 'IT04L1234512345123456789015'
* def ibanAccredito_5Descrizione = 'IBAN Accredito 5'
* def ibanAccreditoPostale_5 = 'IT04L0760112345123456789015'
* def ibanAccreditoPostale_5Descrizione = 'IBAN Accredito Postale 5'
* def codEntrataSiope = 'SIOPE_IMU'


Scenario: configurazione anagrafica base

#### creazione intermediario

#### creazione stazione

#### creazione dominio
* def dominio4 = read('classpath:configurazione/v1/msg/dominio.json')

* set dominio4.ragioneSociale = ragioneSocialeDominio_5

Given url backofficeBaseurl
And path 'domini', idDominio_5 
And headers basicAutenticationHeader
And request dominio4
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### creazione contiAccredito

Given url backofficeBaseurl
And path 'domini', idDominio_5, 'contiAccredito', ibanAccredito_5
And headers basicAutenticationHeader
And request {postale:false,mybank:false,abilitato:true, descrizione:'#(ibanAccredito_5Descrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_5, 'contiAccredito', ibanAccreditoPostale_5
And headers basicAutenticationHeader
And request {postale:true,mybank:false,abilitato:true, descrizione:'#(ibanAccreditoPostale_5Descrizione)'}
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_5, 'contiAccredito', ibanAccreditoErrato
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
And path 'domini', idDominio_5, 'entrate', codEntrataSiope
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_5)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_5, 'entrate', codSpontaneo
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_5)', abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_5, 'entrate', codEntrataSegreteria
And headers basicAutenticationHeader
And request { ibanAccredito: '#(ibanAccredito_5)', ibanAppoggio: '#(ibanAccreditoPostale_5)', abilitato: true }
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
And path 'domini', idDominio_5, 'tipiPendenza', codEntrataSiope
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: true  }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_5, 'tipiPendenza', codSpontaneo
And headers basicAutenticationHeader
And request { codificaIUV: null, pagaTerzi: false }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_5, 'tipiPendenza', codEntrataSegreteria
And headers basicAutenticationHeader
And request { codificaIUV: '89',  pagaTerzi: true, abilitato: true }
When method put
Then assert responseStatus == 200 || responseStatus == 201


Given url ndpsym_config_url 
And path 'domini', idDominio_5
And request 
"""
{
  "urlEC": "#(govpay_web_connector_url +'/ecsp/psp')",
  "auxDigit": 3,
  "versione": 1,
  "segregationCode": '00',
  "ragioneSociale": "Ente Creditore Test 5",
  "idStazione": "11111111113_01",
  "idIntermediario": "11111111113"
}
"""
When method put
Then assert responseStatus == 200 || responseStatus == 201


#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

