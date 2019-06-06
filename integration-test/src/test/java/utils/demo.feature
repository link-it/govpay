Feature:

Background:
 
* callonce read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader_idA2A = getBasicAuthenticationHeader( { username: 'demo', password: '123456' } )

Scenario Outline: Caricamento pendenze

* def pendenzaPut =
"""
{
   "idDominio": "01234567890",
   "causale":<causale>,
   "soggettoPagatore":{
      "tipo":"F",
      "identificativo":"DRCGNN12A46A326K",
      "anagrafica":"Giovanna D'Arco",
      "email":"laPulzelladOrleans@yahoo.fr"
   },
   "importo": <importo>,
   "dataValidita":"2019-12-30T00:00",
   "dataScadenza":"2020-12-30T00:00",
   "tassonomiaAvviso":"Servizi erogati da altri enti",
   "voci":[
      {
				"idVocePendenza": "1",
				"importo": <importo>,
				"descrizione": <causale>,
				"ibanAccredito": "IT00A0000012345000000012345",
				"tipoContabilita": "ALTRO",
				"codiceContabilita": "CodiceContabilita"
      }
   ]
}
"""

Given url 'https://lab.link.it/govpay/backend/api/pendenze/rs/basic/v1'
And path '/pendenze', 'DEMO', <idPendenza>
And headers basicAutenticationHeader_idA2A
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '01234567890', numeroAvviso: '#regex[0-9]{18}' }

Examples:
| importo | causale | idPendenza |
| 121.99 | 'Tassa Passo Carrabile n. abc00006' | 'DEMO_DRCGNN12A46A326K_0006' |
| 132.99 | 'Tassa Passo Carrabile n. abc00007' | 'DEMO_DRCGNN12A46A326K_0007' |
| 143.99 | 'Tassa Passo Carrabile n. abc00008' | 'DEMO_DRCGNN12A46A326K_0008' |
| 154.99 | 'Tassa Passo Carrabile n. abc00009' | 'DEMO_DRCGNN12A46A326K_0009' |