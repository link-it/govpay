Feature: Lettura pendenza da backoffice

Background: 

* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario: 

Given url backofficeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
When method get
Then status 200
And match response ==
"""
{
   "causale":"#notnull",
   "soggettoPagatore":"#notnull",
   "importo":"#notnull",
   "numeroAvviso":"#ignore",
   "dataCaricamento":"#notnull",
   "dataValidita":"#ignore",
   "dataScadenza":"#ignore",
   "tassonomiaAvviso":"#ignore",
   "idA2A":"#notnull",
   "idPendenza":"#notnull",
   "dominio":"#notnull",
   "stato":"#notnull",
   "segnalazioni":"#ignore",
   "iuvAvviso":"#ignore",
   "dataUltimoAggiornamento":"#notnull",
   "dataPagamento":"#ignore",
   "importoPagato":"#ignore",
   "importoIncassato":"#ignore",
   "iuvPagamento":"#ignore",
   "anomalo":"#notnull",
   "verificato":"#notnull",
   "UUID":"#notnull",
   "voci":"#notnull",
   "rpp":"#ignore",
   "pagamenti":"#ignore"
}
"""
And match response.soggettoPagatore contains
"""
{
  "tipo":"#notnull",
  "identificativo":"#notnull",
	"anagrafica":"#notnull",
}
"""
And match each response.rpp ==
"""
{
   "stato":"#notnull",
   "bloccante":"#notnull",
   "rpt": "#notnull",
   "rt":"#ignore",
   "pendenza":"#ignore"
}
"""
And match each response.pagamenti contains
"""
{  
	"id":"#notnull",
  "nome":"#notnull",
  "dataRichiestaPagamento":"#notnull",
  "importo":"#notnull",
  "modello":"#notnull",
  "stato":"#notnull",
  "verificato":"#notnull",
}
"""
