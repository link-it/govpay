Feature: Ricerca Tipi Pendenza con diversi filtri

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def tipoPendenza = 
"""
{
  descrizione: "Tipo Pendenza ABCD",
  tipo: "dovuto",
  codificaIUV: "030",
  pagaTerzi: true,
  form: null,
  trasformazione: null,
  validazione: null,
  visualizzazione: null,
  tracciatoCsv: null
}
"""          
* def idTipoPendenza1 = 'SCDS_XYZ'

Scenario: Aggiunta di un tipoPendenza e test che il filtro per descrizione filtri sia sul campo idTipoPendenza che sul campo descrizione.

Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza1
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'tipiPendenza'
And param descrizione = 'XYZ'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response.risultati[0].idTipoPendenza == idTipoPendenza1

Given url backofficeBaseurl
And path 'tipiPendenza'
And param descrizione = 'ABCD'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response.risultati[0].idTipoPendenza == idTipoPendenza1




