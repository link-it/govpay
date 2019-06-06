Feature: Pagamento attivato da psp rifiutato perche' gia eseguito

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('classpath:test/api/pendenza/pendenze/v1/put/msg/pendenza-put_monovoce_riferimento.json')
* call read('classpath:utils/pa-carica-avviso.feature')
* def esitoAttivaRPT = read('msg/attiva-response-ok.json')
* def numeroAvviso = response.numeroAvviso
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = pendenzaPut.importo

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.dati == esitoAttivaRPT

* call read('classpath:utils/pa-notifica-attivazione.feature')
* match response == read('msg/notifica-attivazione.json')

* call read('classpath:utils/pa-notifica-terminazione.feature')
* match response == read('msg/notifica-terminazione-eseguito.json')

Scenario: Verifica rifiutata perche' pagamento gia' eseguito

* call read('classpath:utils/psp-verifica-rpt.feature')
* match response.faultBean == 
"""
{
"faultCode":"PAA_PAGAMENTO_DUPLICATO",
"faultString":"Pagamento in attesa risulta concluso all'Ente Creditore.",
"id":"#(idDominio)",
"description":"#notnull",
"serial":null
}
"""
Scenario: Attivazione rifiutata perche' pagamento gia' eseguito

* call read('classpath:utils/psp-attiva-rpt.feature')
* match response.faultBean == 
"""
{
"faultCode":"PAA_PAGAMENTO_DUPLICATO",
"faultString":"Pagamento in attesa risulta concluso all'Ente Creditore.",
"id":"#(idDominio)",
"description":"#notnull",
"serial":null
}
"""