Feature: Ricerca Tipi Pendenza con filtro non associati

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def tipoPendenza = 
"""
{
  descrizione: "Tipo Pendenza No Dominio2",
  tipo: "dovuto",
  codificaIUV: "030",
  pagaTerzi: true,
  portaleBackoffice: null,
  portalePagamento: null,
  avvisaturaMail: null,
  avvisaturaAppIO: null,
  visualizzazione: null,
  tracciatoCsv: null
}
"""          
* def idTipoPendenza1 = 'SCDS_NO_DOMINIO_2'

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  portaleBackoffice: null,
  portalePagamento: null,
  avvisaturaMail: null,
  avvisaturaAppIO: null,
  visualizzazione: null,
  tracciatoCsv: null,
  abilitato: true
}
""" 

Scenario: Aggiunta di un tipoPendenza e associazione al dominio1, ricerca per nonAssociati dominio1 e dominio2

Given url backofficeBaseurl
And path 'tipiPendenza', idTipoPendenza1
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', idTipoPendenza1
And headers basicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

# ricerca non associati dominio1

Given url backofficeBaseurl
And path 'tipiPendenza'
And param descrizione = idTipoPendenza1
And param nonAssociati = idDominio
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response ==
"""
{
	numRisultati: 0,
	numPagine: 1,
	risultatiPerPagina: 25,
	pagina: 1,
	prossimiRisultati: '##null',
	risultati: []
}
""" 

# ricerca non associati dominio2

Given url backofficeBaseurl
And path 'tipiPendenza'
And param descrizione = idTipoPendenza1
And param nonAssociati = idDominio_2
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200
And match response.risultati[0].idTipoPendenza == idTipoPendenza1




