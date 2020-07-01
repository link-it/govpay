Feature: Lettura documento

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: pwdA2A } )


Scenario: Lettura di un avviso pdf per un documento con rate

* def idDocumento = getCurrentTimeMillis()

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.documento.identificativo = idDocumento
* set pendenzaPut.documento.descrizione = "Prova documento"
* set pendenzaPut.documento.rata = 1

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.documento.identificativo = idDocumento
* set pendenzaPut.documento.descrizione = "Prova documento"
* set pendenzaPut.documento.rata = 2

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.documento.identificativo = idDocumento
* set pendenzaPut.documento.descrizione = "Prova documento"

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And param stampaAvviso = 'true'
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path '/documenti', response.idDominio, idDocumento, 'avvisi'
And headers basicAutenticationHeader
And header Accept = 'application/pdf'
When method get
Then status 200

Scenario: Lettura di un avviso pdf per un documento con soglie

* def idDocumento = getCurrentTimeMillis()

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.documento.identificativo = idDocumento
* set pendenzaPut.documento.descrizione = "Prova documento"
* set pendenzaPut.documento.soglia.giorni = 5
* set pendenzaPut.documento.soglia.tipo = 'ENTRO'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.documento.identificativo = idDocumento
* set pendenzaPut.documento.descrizione = "Prova documento"
* set pendenzaPut.documento.soglia.giorni = 30
* set pendenzaPut.documento.soglia.tipo = 'ENTRO'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def idPendenza = getCurrentTimeMillis()
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('../put/msg/pendenza-put_monovoce_riferimento.json')
* set pendenzaPut.documento.identificativo = idDocumento
* set pendenzaPut.documento.descrizione = "Prova documento"
* set pendenzaPut.documento.soglia.giorni = 30
* set pendenzaPut.documento.soglia.tipo = 'OLTRE'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers basicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Given url pendenzeBaseurl
And path 'documenti', response.idDominio, idDocumento, 'avvisi'
And headers basicAutenticationHeader
And header Accept = 'application/pdf'
When method get
Then status 200

