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
* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataInizio = getDateTime()
* call sleep(1000)

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* call read('classpath:utils/pa-carica-avviso.feature')

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = cumulativo
* def idDominioPendenza = idDominio
* call read('classpath:utils/workflow/modellounico/v1/modellounico-solopagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200
* def idflusso_dom1 = response.response.rendicontazioni[0].identificativoFlusso
* def idpendenza_dom1 = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idUnitaOperativa = idUnitaOperativa
* call read('classpath:utils/pa-carica-avviso.feature')

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = cumulativo
* def idDominioPendenza = idDominio
* call read('classpath:utils/workflow/modellounico/v1/modellounico-solopagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200
* def idflusso_dom1_uo = response.response.rendicontazioni[0].identificativoFlusso
* def idpendenza_dom1_uo = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idUnitaOperativa = idUnitaOperativa2
* call read('classpath:utils/pa-carica-avviso.feature')

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = cumulativo
* def idDominioPendenza = idDominio
* call read('classpath:utils/workflow/modellounico/v1/modellounico-solopagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200
* def idflusso_dom1_uo2 = response.response.rendicontazioni[0].identificativoFlusso
* def idpendenza_dom1_uo2 = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.idUnitaOperativa = idUnitaOperativa
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* call read('classpath:utils/pa-carica-avviso.feature')

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = cumulativo
* def idDominioPendenza = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-solopagamento.feature')

* def idPendenzaUO2 = idPendenza

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio_2
When method get
Then assert responseStatus == 200
* def idflusso_dom2_uo = response.response.rendicontazioni[0].identificativoFlusso
* def idpendenza_dom2_uo = idPendenza

* def idPendenza = getCurrentTimeMillis()
* def tipoRicevuta = 'R01'
* def cumulativo = '1'
* def pendenzaPut = read('classpath:utils/workflow/modellounico/v1/msg/pendenza-put_monovoce_entratariferita.json')
* set pendenzaPut.idDominio = idDominio_2
* set pendenzaPut.voci[0].codEntrata = codEntrataSiope
* call read('classpath:utils/pa-carica-avviso.feature')

* def numeroAvviso = response.numeroAvviso
* def importo = pendenzaPut.importo
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)
* def ccp = numeroAvviso
* def idCart = getCurrentTimeMillis()
* def riversamentoCumulativo = cumulativo
* def idDominioPendenza = idDominio_2
* call read('classpath:utils/workflow/modellounico/v1/modellounico-solopagamento.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio_2
When method get
Then assert responseStatus == 200
* def idflusso_dom2 = response.response.rendicontazioni[0].identificativoFlusso
* def idpendenza_dom2 = idPendenza


* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataFine = getDateTime()
* call sleep(1000)



