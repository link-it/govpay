Feature: Generazione rendicontazioni cumulative

Background:

# idflusso_dom1
# idflusso_dom1_uo
# idflusso_dom1_uo2
# idflusso_dom2_uo
# idflusso_dom2

# idpendenza_dom1
# idpendenza_dom1_uo
# idpendenza_dom1_uo2
# idpendenza_dom2_uo
# idpendenza_dom2

Scenario: Generazione rendicontazioni cumulative

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200
* def idflusso_dom1 = response.response.rendicontazioni[0].identificativoFlusso
* def annoRif_flusso_dom1 = response.response.rendicontazioni[0].anno
* def idpendenza_dom1 = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idUnitaOperativa = idUnitaOperativa
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200
* def idflusso_dom1_uo = response.response.rendicontazioni[0].identificativoFlusso
* def annoRif_flusso_dom1_uo = response.response.rendicontazioni[0].anno
* def idpendenza_dom1_uo = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idUnitaOperativa = idUnitaOperativa2
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200
* def idflusso_dom1_uo2 = response.response.rendicontazioni[0].identificativoFlusso
* def annoRif_flusso_dom1_uo2 = response.response.rendicontazioni[0].anno
* def idpendenza_dom1_uo2 = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idDominio = idDominio_2
* set pagamentoPost.pendenze[0].idUnitaOperativa = idUnitaOperativa
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento.feature')
* def idPendenzaUO2 = idPendenza

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio_2
When method get
Then assert responseStatus == 200
* def idflusso_dom2_uo = response.response.rendicontazioni[0].identificativoFlusso
* def annoRif_flusso_dom2_uo = response.response.rendicontazioni[0].anno
* def idpendenza_dom2_uo = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pagamentoPost = read('classpath:test/api/pagamento/v1/pagamenti/post/msg/pagamento-post_spontaneo_entratariferita.json')
* set pagamentoPost.pendenze[0].idDominio = idDominio_2
* set pagamentoPost.pendenze[0].voci[0].codEntrata = codEntrataSiope
* call read('classpath:utils/workflow/modello1/v1/modello1-pagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio_2
When method get
Then assert responseStatus == 200
* def idflusso_dom2 = response.response.rendicontazioni[0].identificativoFlusso
* def annoRif_flusso_dom2 = response.response.rendicontazioni[0].anno
* def idpendenza_dom2 = idPendenza


* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)



