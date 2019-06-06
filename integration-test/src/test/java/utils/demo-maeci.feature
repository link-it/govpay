Feature:

Background:
 
* callonce read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader_idA2A = getBasicAuthenticationHeader( { username: 'fastit', password: 'farnesina' } )
* def basicAutenticationHeader_gpadmin = getBasicAuthenticationHeader( { username: 'gpadmin', password: 'password' } )

Scenario Outline: Caricamento pendenze

* def pendenzaPut =
"""
{
   "idDominio": "80213330584",
   "idUnitaOperativa": "4500203",
   "causale": <causale>,
   "soggettoPagatore":{
      "tipo":"F",
      "identificativo":"MNZLSN99E05F205J",
      "anagrafica":"Alessandro Manzoni",
      "email":"innominato@yahoo.it"
   },
   "importo": <importo>,
   "dataValidita":"2020-12-30T00:00",
   "dataScadenza":"2020-12-30T00:00",
   "tassonomiaAvviso":"Servizi erogati da altri enti",
   "voci":[
      {
				"idVocePendenza": "1",
				"importo": <importo>,
				"descrizione": <causale>,
				"ibanAccredito": "SI56101000037773444",
				"tipoContabilita": "ALTRO",
				"codiceContabilita": "RinnovoPassaporto"
      }
   ],
   "datiAllegati": {
   		"cfpassaporto": "DRCGNN12A46A326K"
   }
}
"""

Given url 'https://lab.link.it/govpay/backend/api/backoffice/rs/basic/v1'
And path '/pendenze', 'FASTIT', <idPendenza>
And param stampaAvviso = false
And headers basicAutenticationHeader_gpadmin
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '80213330584', numeroAvviso: '#regex[0-9]{18}' }

Examples:
| importo | causale | idPendenza |
| 55.02 | "Rinnovo passaporto n. abc00002" | "DEMO_MNZLSN99E05F205J_0002" |
| 55.03 | "Rinnovo passaporto n. abc00003" | "DEMO_MNZLSN99E05F205J_0003" |
| 55.04 | "Rinnovo passaporto n. abc00004" | "DEMO_MNZLSN99E05F205J_0004" |
| 55.05 | "Rinnovo passaporto n. abc00005" | "DEMO_MNZLSN99E05F205J_0005" |
| 55.06 | "Rinnovo passaporto n. abc00006" | "DEMO_MNZLSN99E05F205J_0006" |
| 55.07 | "Rinnovo passaporto n. abc00007" | "DEMO_MNZLSN99E05F205J_0007" |
| 55.08 | "Rinnovo passaporto n. abc00008" | "DEMO_MNZLSN99E05F205J_0008" |
