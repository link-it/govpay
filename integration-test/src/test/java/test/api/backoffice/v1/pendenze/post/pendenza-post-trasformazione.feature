Feature: Trasformazione pendenza

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

Given url backofficeBaseurl
And path 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request { descrizione: 'Rinnovo autorizzazione' , codificaIUV: null, tipo: 'dovuto', pagaTerzi: true}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* def tipoPendenzaDominio = 
"""
{
  codificaIUV: null,
  pagaTerzi: true,
  form: { 
  	tipo: "angular2-json-schema-form",
  	definizione: null
  },
  trasformazione: {
  	tipo: "freemarker",
  	definizione: null
  },
  validazione: null
}
"""          
* set tipoPendenzaDominio.form.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-form.json.payload'))
* set tipoPendenzaDominio.trasformazione.definizione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-freemarker.ftl'))
* set tipoPendenzaDominio.validazione = encodeBase64InputStream(read('msg/tipoPendenza-dovuta-validazione-form.json.payload'))

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request tipoPendenzaDominio
When method put
Then assert responseStatus == 200 || responseStatus == 201


Scenario: Pendenza da form validata e convertita con freemarker

* def idPendenza = getCurrentTimeMillis()

Given url backofficeBaseurl
And path 'pendenze', idDominio, tipoPendenzaRinnovo
And headers gpAdminBasicAutenticationHeader
And request 
"""
{
	idPendenza: "#(''+idPendenza)",
	soggettoPagatore: {
		"identificativo": "RSSMRA30A01H501I",
		"anagrafica": "Mario Rossi",
		"email": "mario.rossi@testmail.it"
	},
	importo: 10.0
}
"""
When method post
Then assert responseStatus == 201
