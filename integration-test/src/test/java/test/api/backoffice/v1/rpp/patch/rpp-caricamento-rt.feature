Feature: Aggiornamento RT

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')

* def idPendenza = getCurrentTimeMillis()
* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v1', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A, password: 'password' } )

Scenario: Update RT non pagata con non pagata

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt1 = response 
* def dataRptEnd1 = getDateTime()
* def idSession = responseRpt1.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R02'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')
* def dataRtEnd1 = getDateTime()

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'NON_ESEGUITO'

* def idDominioRT = response.rpp[0].rpt.dominio.identificativoDominio
* def iuvRT = response.rpp[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpRT = response.rpp[0].rpt.datiVersamento.codiceContestoPagamento

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
And headers {'Accept' : 'application/xml'}
When method get
Then status 200


# Eseguo la patch

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/rt",
      "value": null
   }
]
"""

* set response /RT/identificativoMessaggioRicevuta = idPendenza
* xmlstring rt = response
* set patchRequest[0].value = encodeBase64(rt)

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

# Controllo che lo stato non sia cambiato e la RT sia aggiornata

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'NON_ESEGUITO'
And match response.rpp[0].rt.identificativoMessaggioRicevuta == '#(idPendenza + "")'

Scenario: Update RT non pagata con RT malformata

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt1 = response 
* def dataRptEnd1 = getDateTime()
* def idSession = responseRpt1.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R02'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')
* def dataRtEnd1 = getDateTime()

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'NON_ESEGUITO'

* def idDominioRT = response.rpp[0].rpt.dominio.identificativoDominio
* def iuvRT = response.rpp[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpRT = response.rpp[0].rpt.datiVersamento.codiceContestoPagamento

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
And headers {'Accept' : 'application/xml'}
When method get
Then status 200

* copy rtOriginale = response
* set response /RT/versioneOggetto = <pippo>bar</pippo>
* xmlstring rt = response

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/rt",
      "value": null
   }
]
"""
* set patchRequest[0].value = encodeBase64(rt)

# controllo che l'operazione risulti fallita

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'RT non valida'
And match response.dettaglio contains 'versioneOggetto'

# controllo che lo stato del pagamento non sia cambiato e la RT risulti immutata

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'NON_ESEGUITO'

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
And headers {'Accept' : 'application/xml'}
When method get
Then status 200
And match response == rtOriginale

Scenario: Update RT non pagata con RT pagata

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt1 = response 
* def dataRptEnd1 = getDateTime()
* def idSession = responseRpt1.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R02'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')
* def dataRtEnd1 = getDateTime()

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'NON_ESEGUITO'

* def idDominioRT = response.rpp[0].rpt.dominio.identificativoDominio
* def iuvRT = response.rpp[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpRT = response.rpp[0].rpt.datiVersamento.codiceContestoPagamento

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
And headers {'Accept' : 'application/xml'}
When method get
Then status 200

* def newRt = response 
* remove newRt /RT/datiPagamento
* set newRt /RT = 
"""
<ns2:datiPagamento>
 <ns2:codiceEsitoPagamento>0</ns2:codiceEsitoPagamento>
 <ns2:importoTotalePagato>125.99</ns2:importoTotalePagato>
 <ns2:identificativoUnivocoVersamento>#(iuvRT)</ns2:identificativoUnivocoVersamento>
 <ns2:CodiceContestoPagamento>n/a</ns2:CodiceContestoPagamento>
 <ns2:datiSingoloPagamento>
    <ns2:singoloImportoPagato>109.99</ns2:singoloImportoPagato>
    <ns2:dataEsitoSingoloPagamento>2020-01-10+01:00</ns2:dataEsitoSingoloPagamento>
    <ns2:identificativoUnivocoRiscossione>#('idRisc-' + iuvRT)</ns2:identificativoUnivocoRiscossione>
    <ns2:causaleVersamento>#('/RFS/' + iuvRT + '/109.99/TXT/Diritti e segreteria')</ns2:causaleVersamento>
    <ns2:datiSpecificiRiscossione>9/SEGRETERIA</ns2:datiSpecificiRiscossione>
 </ns2:datiSingoloPagamento>
 <ns2:datiSingoloPagamento>
    <ns2:singoloImportoPagato>16.00</ns2:singoloImportoPagato>
    <ns2:dataEsitoSingoloPagamento>2020-01-10+01:00</ns2:dataEsitoSingoloPagamento>
    <ns2:identificativoUnivocoRiscossione>#('idRisc-' + iuvRT)</ns2:identificativoUnivocoRiscossione>
    <ns2:causaleVersamento>#('/RFS/' + iuvRT + '/16.00/TXT/Marca da bollo')</ns2:causaleVersamento>
    <ns2:datiSpecificiRiscossione>9/MBT</ns2:datiSpecificiRiscossione>
    <ns2:allegatoRicevuta>
       <ns2:tipoAllegatoRicevuta>BD</ns2:tipoAllegatoRicevuta>
       <ns2:testoAllegato>PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48bWFyY2FEYUJvbGxvIHhtbG5zPSJodHRwOi8vd3d3LmFnZW56aWFlbnRyYXRlLmdvdi5pdC8yMDE0L01hcmNhRGFCb2xsbyIgeG1sbnM6bnMyPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIj48UFNQPjxDb2RpY2VGaXNjYWxlPjk5OTk5OTk5OTk5PC9Db2RpY2VGaXNjYWxlPjxEZW5vbWluYXppb25lPkJhbmNvIGRpIFBvbnppIFMucC5BLjwvRGVub21pbmF6aW9uZT48L1BTUD48SVVCRD41MzcwNDUzNDA5NTAyPC9JVUJEPjxPcmFBY3F1aXN0bz4yMDIwLTAxLTEwVDE2OjEwOjAwKzAxMDA8L09yYUFjcXVpc3RvPjxJbXBvcnRvPjE2LjA8L0ltcG9ydG8+PFRpcG9Cb2xsbz4wMTwvVGlwb0JvbGxvPjxJbXByb250YURvY3VtZW50bz48RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjc2hhMjU2Ii8+PG5zMjpEaWdlc3RWYWx1ZT55NVplcVdyOEVlRngvNmhnSkxKZElOMEFZZzd4YkFXbEd5am96K3hpdHNnPTwvbnMyOkRpZ2VzdFZhbHVlPjwvSW1wcm9udGFEb2N1bWVudG8+PFNpZ25hdHVyZSB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+PFNpZ25lZEluZm8+PENhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy9UUi8yMDAxL1JFQy14bWwtYzE0bi0yMDAxMDMxNSIvPjxTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNyc2Etc2hhMjU2Ii8+PFJlZmVyZW5jZSBVUkk9IiI+PFRyYW5zZm9ybXM+PFRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNlbnZlbG9wZWQtc2lnbmF0dXJlIi8+PC9UcmFuc2Zvcm1zPjxEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGEyNTYiLz48RGlnZXN0VmFsdWU+aDRsQ011MldVdnRpNldQSUlvTnF4UG85UU1FTlJsTlJYK3VOaS9YQmIwQT08L0RpZ2VzdFZhbHVlPjwvUmVmZXJlbmNlPjwvU2lnbmVkSW5mbz48U2lnbmF0dXJlVmFsdWU+azNRaCtTQ2JPVjUxQ3VYdCtpVjJrQ3BrL01MR05uaW5aQ0dGZ3pNQWFOUTF2Y25qdnJON0VGTU5iQkROeHBJekxXK1RrQXM0eDQzRldCZkdVdzVhejA3c2diSElvazhwT2dVSndhKzV0elVuUXVndFhyVHo4QjZBZ01xNjgyUVp2Qm9zdFVibk1nTExPWUlFc25kY2c3REE1L1JlN2ROQjlGUXAzWUcxWUVDN3lHZXZTaGVyZElhVnBiazFCRmdyWHJZSm9Jcmp3SGlkb1Q5QkVmMk5wbnlpbURRUnhLWGlrZWNXNW1SMFBLWUlITUZTM1g1OGtwNWJadU9VcFlSZEcwUk5Kdy9KNHh5bC9qSDk2Qm42WVBacUk3cGMrbitBdXV2bkNMb21zRVByNVh1eEFIaFdxV2oxNHlLa0s5bE5yTjdrejA1c1dBaFhGdDZLVCtPeDB3PT08L1NpZ25hdHVyZVZhbHVlPjxLZXlJbmZvPjxYNTA5RGF0YT48WDUwOUNlcnRpZmljYXRlPk1JSUV0RENDQXB5Z0F3SUJBZ0lJVWJqNG9PTGdXVWt3RFFZSktvWklodmNOQVFFTEJRQXdhREVMTUFrR0ExVUVCaE1DU1ZReEhqQWNCZ05WQkFvTUZVRm5aVzU2YVdFZ1pHVnNiR1VnUlc1MGNtRjBaVEViTUJrR0ExVUVDd3dTVTJWeWRtbDZhU0JVWld4bGJXRjBhV05wTVJ3d0dnWURWUVFEREJORFFTQkNiMnhzYnlCVVpXeGxiV0YwYVdOdk1CNFhEVEUzTURFeU56RTJNamt5TTFvWERUSXpNREV5T0RFMk1qa3lNMW93WkRFTE1Ba0dBMVVFQmhNQ1NWUXhIakFjQmdOVkJBb01GVUZuWlc1NmFXRWdaR1ZzYkdVZ1JXNTBjbUYwWlRFTU1Bb0dBMVVFQ3d3RFVGTlFNU2N3SlFZRFZRUUREQjR3TWpNeE16Z3lNVEF3TnlCSlVDQkpUa1pQUTBGTlJWSkZJRk5EVUVFd2dnRWlNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0SUJEd0F3Z2dFS0FvSUJBUURuWDR5aDhiVnpoVGloU1VnbUJ1anlUT0tYaUVkM1hJQjZOeFN4V3F3RkJyTXVraGJUR0lrQnFDTENwZWZsUzZRbWM3QUFGRmx1VG1LTDNtSWJtREV6SzQyNmtLZkhxT2ZBdHB1b1hwMkFCNXhXUTBBR2ZlUmpjMWNSU0VKQVRlMjhkOTFuZlplSkxsR0JMcS9ESm1sU2c1UFd3ZG9OcnVlYlEwVDhYcnRvUFdTZUhSMUhKMU9DN1RLUm5tNjFSU0hZQVAyRmNySVFvNGN3RE1zZkFxSW5PbVQ1M1psbDhWZjN4TUZ1cEpZMHBBWWdMZmhzYXI3ZERpajBaOU1tTldxVG11UitXbmJSYUpYenNoZmVlV1N3bjA4RmliZFl6SjJnTk03Z09IMWZvV1c5RXhKTktKNjJLK2dycmpnWkRzK0xCL0F0ZHA1MUxuREVjQW5kU0V0ZkFnTUJBQUdqWmpCa01COEdBMVVkSXdRWU1CYUFGQ3BIc244aFRML0wzZ2lOMHZnNTZ1K3Yza0pFTUJJR0ExVWRJQVFMTUFrd0J3WUZLMHdkQVFrd0hRWURWUjBPQkJZRUZBSC9LQTFBTUhGejJTamlzQjM5eW41ODFrQjZNQTRHQTFVZER3RUIvd1FFQXdJR1FEQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FnRUFEbURqL0NVd2xPQUZHMnJrR1UwcUIyMXU0eTRPVlNyMXgwTlNxT2N0WWxTS2QxenBseDRocWxucDhDY0xCQm5yU0FERnNheUdoQXowNnRZSXp6UnZrQXFOOWt0bXlORHg1QkZwQk5oSjZSanZ2MnFxTWMweGVqZVdsbUtzSU1UUEhuRmdjZ3ZPZk9HK2RxUDBqWFJPUmtHOXp0SWVlUGFEdlBHWFpUS1ZGZkJBTWdVcU90NlRXOGgxaWtLdWdUTCtCV0pKcThhelp2OGE0M3pkRVcwemJBdzY3eFAwQXlUV2J5OVp4Yk91d1RUemlmTVJzdXNna2RMdHRCN1g0U0VQY1NUMVFoMmFnaXJpVDJ0T0cvYy9xRWx0d2Y5ampVZGtDZ1R2QjJtMW9jV0dtcm5ja083U2loK1R6elRyNnF5eHFaeFRWRWh0aGV1ZXMyZ0wxS1QwbTJYQ1FPYktZUGx5eU5wVjhvdk5KNVRDOGF0aUU1Vk1iVDZ3Y1F1SkFEZTZCaVlkRHpyVGtaY1EvSk1LRU95N2pUTGcxNXhOTm02SCtaVGJnMGtKV1o3ZkJnNHpnNzlxazltQzQwcGllWVFVMktNSW8yMFNDYUF1TTUxWWFkMStBanBGTElHUkp3S21ZRmhnQkd6NnpMWUhSQ09vUWd1aXdET1NsbWZkN1c5RkgzYUZuYjY5c055MGJsemE4U3R1YXZjOGlMNnNBTm5nQy9uczUvMU50d0ZJZzZqQkE0N2t1VHdDV2FVTkdSbENHRVJGcFQ2RXhNbE8wV3ovMStUTW55alZUMG5tdzI0TEtVc2IwWEthRk96bVk5TFUwVDNvcGFGVHFMTE1HM2dUZEp6R295L0FMQVNhcm5pWVhRS2VpVkdIUUdrNTFtWUVNYzE2eHRMRnJpWT08L1g1MDlDZXJ0aWZpY2F0ZT48WDUwOUNSTD5NSUlDNHpDQnpBSUJBVEFOQmdrcWhraUc5dzBCQVFzRkFEQm9NUXN3Q1FZRFZRUUdFd0pKVkRFZU1Cd0dBMVVFQ2d3VlFXZGxibnBwWVNCa1pXeHNaU0JGYm5SeVlYUmxNUnN3R1FZRFZRUUxEQkpUWlhKMmFYcHBJRlJsYkdWdFlYUnBZMmt4SERBYUJnTlZCQU1NRTBOQklFSnZiR3h2SUZSbGJHVnRZWFJwWTI4WERURTRNRFV3TnpBNU5UVXlNVm9YRFRFNE1EVXdPREE1TlRVeU1WcWdNREF1TUI4R0ExVWRJd1FZTUJhQUZDcEhzbjhoVEwvTDNnaU4wdmc1NnUrdjNrSkVNQXNHQTFVZEZBUUVBZ0lFbERBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQWdFQUhFSHhEdERWa0RiZFA5MDd5NitwcnhzcnpETHppZTZZb29sRmRydzZFZ0ZmaXUrWWFFelpCdEk0U0tVVXdQdnR1ZUlsaXhUamE3anplVlZmM2RhSnBJVmdGbWFtVUZvdWN4OTRsSU1xNTNvTkRhRHlZMFJ0YXdaVGhlMEdsQ1BSaThkTGpORzNWelBMYWNwMVhydTdObFVYTFhrVWlvemlCNGwyaHlKMm1ybm95a1BxdStOWndvTnlWUFNZZ0g5STAzS1B0WDltN1IwK3hzOWhKNE9tOUNDeTZSMjhZbFpvRlZ3VXNOaG1SUXNHN0hnQ1BuS3QrUStsTDdLa2J1Q3JIcXVnTHZWck9zSW9sVVlHRVR6TGF5TUU4eFJWcUlNZE1nVHdPWmhjV3lQYkpDNjZ0OUhKUHZ6NFd6TlUwTnNJQ08ya0FkZzlyMk9BZzAyQ29BaXJwbHROQkQ4b20xazhOOTdPdEdFbExYMmttS0ZNNCtHMnNMSm93RUo3UWR6bW8vZEEyeEhBRGttTy9YNld1R3FFcUJSRm9kZzBrOGhZbElzNHhoakpid2N4MWZvSXlaVEQxcEkzTnVaRVozc3lDQ1Z3bFJDVHhxN2oycms3WjRSamxHSzI1YkxuSS9WMGNxUnNMUlI3Y1h2QmUyTmF4RGh0RkJWSitVOU5qQmxMOTFmU2k1YjlncEt2SkJyRmpjVHRRRmNjeE5NZzZIM1N6YnR5MVJ0TnBhOVEzS2NBY2FKcXBvVlBGUEVpUjB4NTNJcnI2V3FqeWNUcXZKa2NRNEVLUWJtc2JHRXd2bk9OY1Z3VExYclpzR0Y4Y1hVUi84T3o0YjJEakNQTERHNStzUlVzcERZb1h2bDUrZDZyQlNFVndoK0FFQmtWTmdWUzJqZE5KU2wrTGprPTwvWDUwOUNSTD48L1g1MDlEYXRhPjwvS2V5SW5mbz48L1NpZ25hdHVyZT48L21hcmNhRGFCb2xsbz4=</ns2:testoAllegato>
    </ns2:allegatoRicevuta>
 </ns2:datiSingoloPagamento>
</ns2:datiPagamento>
"""
* xmlstring rt = newRt

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/rt",
      "value": null
   }
]
"""
* set patchRequest[0].value = encodeBase64(rt)

# controllo che l'operazione risulti fallita

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 200

# controllo che lo stato del pagamento non sia cambiato e la RT risulti immutata

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ESEGUITO'

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
And headers {'Accept' : 'application/xml'}
When method get
Then status 200
And match response == newRt

Scenario: Update RT pagata con pagata

* def dataRptStart = getDateTime()
* def idPendenza = getCurrentTimeMillis()
Given url pagamentiBaseurl
And headers basicAutenticationHeader
And path '/pagamenti'
And request read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita_bollo.json')
When method post
Then status 201

* def responseRpt1 = response 
* def dataRptEnd1 = getDateTime()
* def idSession = responseRpt1.idSession

Given url ndpsym_url + '/psp'
And path '/eseguiPagamento'
And param idSession = idSession
And param idDominio = idDominio
And param codice = 'R01'
And param riversamento = '0'
When method get

* call read('classpath:utils/pa-notifica-terminazione-byIdSession.feature')
* def dataRtEnd1 = getDateTime()

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ESEGUITO'

* def idDominioRT = response.rpp[0].rpt.dominio.identificativoDominio
* def iuvRT = response.rpp[0].rpt.datiVersamento.identificativoUnivocoVersamento
* def ccpRT = response.rpp[0].rpt.datiVersamento.codiceContestoPagamento

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200

* def oldRT_identificativoMessaggioRicevuta = response.identificativoMessaggioRicevuta

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT, 'rt'
And headers gpAdminBasicAutenticationHeader
And headers {'Accept' : 'application/xml'}
When method get
Then status 200

# Eseguo la patch

* def patchRequest = 
"""
[
   {
      "op":"REPLACE",
      "path":"/rt",
      "value": null
   }
]
"""

* set response /RT/identificativoMessaggioRicevuta = idPendenza
* xmlstring rt = response
* set patchRequest[0].value = encodeBase64(rt)

Given url backofficeBaseurl
And path '/rpp', idDominioRT, iuvRT, ccpRT
And headers basicAutenticationHeader
And request patchRequest
When method patch
Then assert responseStatus == 422
And match response == { categoria: 'RICHIESTA', codice: 'SEMANTICA', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'Aggiornamento di RT in pagamenti con esito PAGAMENTO_ESEGUITO non supportata.'

# Controllo che lo stato non sia cambiato e la RT sia invariata

Given url backofficeBaseurl
And path '/pagamenti', responseRpt1.id
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.stato == 'ESEGUITO'
And match response.rpp[0].rt.identificativoMessaggioRicevuta == oldRT_identificativoMessaggioRicevuta
